/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.security.business;

import com.ur.urcap.bachelor.security.business.shell.ShellCommunicator;
import com.ur.urcap.bachelor.security.exceptions.UnsuccessfulCommandException;
import com.ur.urcap.bachelor.security.services.ShellComService;
import com.ur.urcap.bachelor.security.services.ShellCommandResponse;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author frede
 */
public class Firewall
{

    private ArrayList<IPTable> tables;
    private ShellComService shellCom;
    private final String folderName = "Firewall";
    private static Pattern ipPattern;

    private static final String IPADDRESS_PATTERN
            = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    private static Firewall instance = null;

    public static Firewall getInstance()
    {
        if (instance == null)
        {
            instance = new Firewall();
        }
        return instance;
    }

    // Setup standard firewall settings
    private Firewall()
    {

        ipPattern = Pattern.compile(IPADDRESS_PATTERN);
        shellCom = new ShellCommunicator();

        //Allow established
        appendIpTablesRule("INPUT -m conntrack --ctstate ESTABLISHED,RELATED -j ACCEPT");
        appendIpTablesRule("OUTPUT -m conntrack --ctstate ESTABLISHED -j ACCEPT");

        //Allow http and ssh tcp connections
        appendIpTablesRule("INPUT -p tcp --dport 22 -m conntrack --ctstate NEW,ESTABLISHED -j ACCEPT");
        appendIpTablesRule("INPUT -p tcp --dport 80 -m conntrack --ctstate NEW,ESTABLISHED -j ACCEPT");

        //Allow Loopback Connections
        appendIpTablesRule("INPUT -i lo -j ACCEPT");
        appendIpTablesRule("OUTPUT -o lo -j ACCEPT");

        //Drop invalid packets
        appendIpTablesRule("INPUT -m conntrack --ctstate INVALID -j DROP");
        try
        {
            // drop all incoming packets by default, accept forwarding and output packets by default
            shellCom.doCommand("iptables --policy INPUT REJECT");
            shellCom.doCommand("iptables --policy FORWARD ACCEPT");
            shellCom.doCommand("iptables --policy OUTPUT ACCEPT");
        }
        catch (UnsuccessfulCommandException ex)
        {
            Logger.getLogger(Firewall.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * For now it is all protocols.
     *
     * @param port the port number to accept
     */
    public void acceptPort(int port) throws UnsuccessfulCommandException
    {
        if (port > 0 && port < 65536)
        {
            appendIpTablesRule("INPUT -p tcp --dport " + port + " -m conntrack --ctstate NEW,ESTABLISHED -j ACCEPT");
        }
        else
        {
            throw new UnsuccessfulCommandException("Port is not correct range");
        }

    }

    /**
     * For now it is all protocols.
     *
     * @param port the port number to deny
     */
    public void denyPort(int port) throws UnsuccessfulCommandException
    {
        if (port > 0 && port < 65535)
        {
            try
            {
                shellCom.doCommand("iptables -D INPUT -p tcp --dport " + port + " -m conntrack --ctstate NEW,ESTABLISHED -j ACCEPT");
            }
            catch (UnsuccessfulCommandException ex)
            {
                Logger.getLogger(Firewall.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        else
        {
            throw new UnsuccessfulCommandException("Port is not correct range");
        }
    }

    public void blockIP(String IP) throws UnsuccessfulCommandException
    {

        if (ipPattern.matcher(IP).matches())
        {
            appendIpTablesRule("INPUT -s " + IP + " -j DROP");
        }
        else
        {
            throw new UnsuccessfulCommandException("IP address is not correct format");
        }

    }

    // Helper method
    private void appendIpTablesRule(String rule)
    {
        try
        {
            // Only do stuff if it isn't already done
            ShellCommandResponse response = shellCom.doCommand("iptables -C " + rule);
            if (response.getExitValue() != 0)
            {
                shellCom.doCommand("iptables -A " + rule);
            }

        }
        catch (UnsuccessfulCommandException ex)
        {
            Logger.getLogger(Firewall.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void persist()
    {
        try
        {
            shellCom.doCommand("invoke-rc.d iptables-persistent save");
        }
        catch (UnsuccessfulCommandException ex)
        {
            Logger.getLogger(Firewall.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
