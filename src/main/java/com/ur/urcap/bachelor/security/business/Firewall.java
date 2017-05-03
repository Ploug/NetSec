/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.security.business;

import com.ur.urcap.bachelor.security.business.iptables.IPTable;
import com.ur.urcap.bachelor.security.business.shell.ShellCommunicator;
import com.ur.urcap.bachelor.security.exceptions.UnsuccessfulCommandException;
import com.ur.urcap.bachelor.security.services.Callback;
import com.ur.urcap.bachelor.security.services.ShellComService;
import com.ur.urcap.bachelor.security.services.ShellCommandResponse;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import net.sf.jIPtables.log.LogListener;
import net.sf.jIPtables.log.LogTracker;
import net.sf.jIPtables.log.Packet;

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
        System.out.println("Java library path: " + System.getProperty("java.library.path"));
        try
        {
            System.out.println("yoBLA!!asd");
            System.load("/usr/lib/jni/libjiptables_log.so");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        System.out.println("yo");
        LogTracker tracker = LogTracker.getInstance();
        tracker.addLogListener(new LogListener()
        {
            @Override
            public void onNewLog(Packet newPacket)
            {
                System.out.println(newPacket.toString());
            }
        });
        System.out.println("yooo");

        ipPattern = Pattern.compile(IPADDRESS_PATTERN);
        shellCom = new ShellCommunicator();

        // sudo apt-get install libnetfilter-log-dev libnetfilter-conntrack-dev
        //Allow established
        appendIpTablesRule("INPUT -j NFLOG --nflog-prefix START");
        
        appendIpTablesRule("INPUT -m conntrack --ctstate ESTABLISHED,RELATED -j ACCEPT");
        appendIpTablesRule("OUTPUT -m conntrack --ctstate ESTABLISHED -j ACCEPT");

        //Allow http and ssh tcp connections
        appendIpTablesRule("INPUT -p tcp --dport 22 -m conntrack --ctstate NEW,ESTABLISHED -j ACCEPT");
        appendIpTablesRule("INPUT -p tcp --dport 80 -m conntrack --ctstate NEW,ESTABLISHED -j ACCEPT");

        //Allow icmp.  (General conclusion is the only con is possible DDOS attack, while there is several pros, such as MTU discovery and pinging)
        appendIpTablesRule("INPUT -p icmp -j ACCEPT");

        //Allow Loopback Connections
        appendIpTablesRule("INPUT -i lo -j ACCEPT");
        appendIpTablesRule("OUTPUT -o lo -j ACCEPT");

        //Drop invalid packets
        appendIpTablesRule("INPUT -m conntrack --ctstate INVALID -j DROP");

        //SSH Brute force protection
        appendIpTablesRule("INPUT -p tcp --dport ssh -m conntrack --ctstate NEW -m recent --set");
        appendIpTablesRule("INPUT -p tcp --dport ssh -m conntrack --ctstate NEW -m recent --update --seconds 60 --hitcount 10 -j DROP  ");

        //Port scanning protection
        appendIpTablesRule("-N port-scanning");
        appendIpTablesRule("-A port-scanning -p tcp --tcp-flags SYN,ACK,FIN,RST RST -m limit --limit 1/s --limit-burst 2 -j RETURN ");
        appendIpTablesRule("-A port-scanning -j DROP");
        
        appendIpTablesRule("INPUT -j NFLOG --nflog-prefix INDROPPED");

        //
        try
        {
            // drop all incoming packets by default, accept forwarding and output packets by default
            shellCom.doCommand("iptables --policy INPUT DROP");
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

    public void startLogging(Callback cb)
    {
        boolean important = false;

        //iptables -I INPUT -m state --state NEW -j LOG --log-prefix "New Connection: "
        if (important)
        {
            cb.call();
        }

    }

    // Helper method
    private void deleteIpTablesRule(String rule)
    {
        try
        {
            shellCom.doCommand("iptables -D " + rule);

        }
        catch (UnsuccessfulCommandException ex)
        {
            Logger.getLogger(Firewall.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void insertIpTablesRule(String rule, int number)
    {
        int spaceIndex = rule.indexOf(" ");
        String s = rule.substring(0, spaceIndex + 1);
        String s2 = rule.substring(spaceIndex);
        String actualRule = s + number + s2;
        try
        {
            // Only do stuff if it isn't already done
            ShellCommandResponse response = shellCom.doCommand("iptables -C " + rule);
            if (response.getExitValue() != 0)
            {
                shellCom.doCommand("iptables -I " + actualRule);
            }

        }
        catch (UnsuccessfulCommandException ex)
        {
            Logger.getLogger(Firewall.class.getName()).log(Level.SEVERE, null, ex);
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
