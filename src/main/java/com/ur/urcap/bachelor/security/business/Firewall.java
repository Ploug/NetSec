/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.security.business;

import com.ur.urcap.bachelor.security.business.shell.ShellCommunicator;
import com.ur.urcap.bachelor.security.exceptions.UnsuccessfulCommandException;
import com.ur.urcap.bachelor.security.interfaces.ShellComService;
import com.ur.urcap.bachelor.security.interfaces.ShellCommandResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import net.sf.jIPtables.log.LogListener;
import net.sf.jIPtables.log.LogTracker;
import net.sf.jIPtables.log.Packet;
import com.ur.urcap.bachelor.security.interfaces.ActivityListener;
import java.io.IOException;
import net.sf.jIPtables.rules.Chain;
import net.sf.jIPtables.rules.IPTables;
import net.sf.jIPtables.rules.Rule;
import net.sf.jIPtables.rules.RuleSet;

/**
 *
 * @author frede
 */
public class Firewall
{

    private ShellComService shellCom;
    private final String folderName = "Firewall";
    private final String fs = System.getProperty("file.separator");
    private final String libPath = fs + "usr" + fs + "lib" + fs + "jni" + fs;
    private Pattern ipPattern;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    private final List<LogActivity> interestingActivity = new ArrayList();
    private List<LogActivity> allActivity = new ArrayList();

    private ActivityListener activityListener;

    private int count = 0;
    private final String IPADDRESS_PATTERN
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

    private final LogListener logActivity = new LogListener()
    {
        @Override
        public void onNewLog(Packet newPacket)
        {
            LogActivity logActivity = new LogActivity(sdf.format(new Date()));

            /*if (newPacket.getSourceAddress().toString().contains("localhost"))   should do this in a real scenario
            {
                return;
            } */
            String packetInString = newPacket.toString();
            boolean interesting = false;
            if (newPacket.getPrefix().equals("INDROPPED"))
            {
                int startIndex = packetInString.indexOf("dport=") + "dport=".length();
                String port = packetInString.substring(startIndex, packetInString.indexOf(",", startIndex));
                logActivity.message = "Packet from " + newPacket.getSourceAddress() + " to port: " + port + " was dropped.";
                interesting = true;
            }
            else if (newPacket.getPrefix().equals("SSHATTEMPT"))
            {
                logActivity.message = newPacket.getSourceAddress() + " is messaging the SSH port";
                interesting = true;
            }
            else if (newPacket.getPrefix().equals("HTTPATTEMPT"))
            {
                logActivity.message = newPacket.getSourceAddress() + " is messaging the HTTP port";
                interesting = true;
            }
            else if (newPacket.getPrefix().equals("PORTSCAN"))
            {
                logActivity.message = "Port scanning attempt from " + newPacket.getSourceAddress() + " denied.";
                interesting = true;
            }
            else if (newPacket.getPrefix().equals("PORTSCAN"))
            {
                logActivity.message = "Port scanning attempt from " + newPacket.getSourceAddress() + " denied.";
                interesting = true;
            }
            else if (newPacket.getPrefix().equals("SSHBRUTE"))
            {
                logActivity.message = "SSH bruteforce denied from " + newPacket.getSourceAddress() + ", 60 seconds pause.";
                interesting = true;
            }

            if (interesting && !interestingActivity.contains(logActivity))
            {
                interestingActivity.add(logActivity);
            }
            allActivity.add(logActivity);
            if (activityListener != null)
            {
                activityListener.activityUpdate();
            }

        }

    };

    // Setup standard firewall settings
    private Firewall()
    {
        System.load(libPath + "libjiptables_log.so");
        System.load(libPath + "libjiptables_conntrack.so");

        LogTracker tracker = LogTracker.getInstance();
        tracker.addLogListener(logActivity);

        ipPattern = Pattern.compile(IPADDRESS_PATTERN);
        shellCom = new ShellCommunicator();

        setDefaultRules();

    }

    public String[] getAllActivity()
    {

        String[] retVal;
        retVal = new String[allActivity.size()];

        for (int i = 0; i < retVal.length; i++)
        {
            retVal[i] = allActivity.get(i).toString();
        }

        return retVal;

    }

    /**
     *
     * @param amount amount of activities to show. -1 gives all possible
     * @return
     */
    public String[] getInterestingActivity(int amount)
    {
        String[] retVal;
        if (amount > interestingActivity.size() || amount < 1)
        {
            retVal = new String[interestingActivity.size()];
        }
        else
        {
            retVal = new String[amount];
        }

        for (int i = 0; i < retVal.length; i++)
        {
            retVal[i] = interestingActivity.get(interestingActivity.size() - retVal.length + i).toString(); // Getting "amount" latest
        }

        return retVal;

    }

    private void setDefaultRules()
    {
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

        //Allow localhost 
        appendIpTablesRule("INPUT -s localhost -j ACCEPT");
        //Allow established 
        appendIpTablesRule("INPUT -m conntrack --ctstate ESTABLISHED,RELATED -j ACCEPT");
        appendIpTablesRule("OUTPUT -m conntrack --ctstate ESTABLISHED -j ACCEPT");

        //SSH Brute force protection
        appendIpTablesRule("INPUT -p tcp --dport ssh -m conntrack --ctstate NEW -m recent --set");
        appendIpTablesRule("INPUT -p tcp --dport ssh -m conntrack --ctstate NEW -m recent --update --seconds 60 --hitcount 10 -j NFLOG --nflog-prefix SSHBRUTE"); // Logging ssh brute attempts
        appendIpTablesRule("INPUT -p tcp --dport ssh -m conntrack --ctstate NEW -m recent --update --seconds 60 --hitcount 10 -j DROP  ");

        //Allow http and ssh tcp connections
        appendIpTablesRule("INPUT -p tcp --dport 22 -m conntrack --ctstate NEW,ESTABLISHED -j NFLOG --nflog-prefix SSHATTEMPT");
        appendIpTablesRule("INPUT -p tcp --dport 22 -m conntrack --ctstate NEW,ESTABLISHED -j ACCEPT");

        appendIpTablesRule("INPUT -p tcp --dport 80 -m conntrack --ctstate NEW,ESTABLISHED -j NFLOG --nflog-prefix HTTPATTEMPT");
        appendIpTablesRule("INPUT -p tcp --dport 80 -m conntrack --ctstate NEW,ESTABLISHED -j ACCEPT");

        //Allow icmp.  (General conclusion is the only con is possible DDOS attack, while there is several pros, such as MTU discovery and pinging)
        appendIpTablesRule("INPUT -p icmp -j ACCEPT");

        //Allow Loopback Connections
        appendIpTablesRule("INPUT -i lo -j ACCEPT");
        appendIpTablesRule("OUTPUT -o lo -j ACCEPT");

        //Drop invalid packets
        appendIpTablesRule("INPUT -m conntrack --ctstate INVALID -j DROP");

        // logging
        appendIpTablesRule("INPUT -j NFLOG --nflog-prefix INDROPPED");

    }

    /**
     * For now it is all protocols.
     *
     * @param port the port number to accept
     */
    public void openPort(int port) throws UnsuccessfulCommandException
    {
        if (port > 0 && port < 65536)
        {
            appendIpTablesRule("INPUT -p tcp --dport " + port + " -m conntrack --ctstate NEW,ESTABLISHED -j ACCEPT");
            appendIpTablesRule("INPUT -p udp --dport " + port + " -m conntrack --ctstate NEW,ESTABLISHED -j ACCEPT");
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
    public void closePort(int port) throws UnsuccessfulCommandException
    {
        try
        {
            List<Rule> inputRules = IPTables.getCurrentRules().getTable(RuleSet.TableType.FILTER).getChain("INPUT").getRules();

            int deletedCount = 1;
            for (int i = 0; i < inputRules.size(); i++)
            {
                Rule rule = inputRules.get(i);
                if (rule.getOption("--dport").equals(port + ""))
                {
                    shellCom.doCommand("iptables -D INPUT " + (i + deletedCount--)); // Dont break because udp and tcp both can contain --dport
                }
            }

        }
        catch (IOException ex)
        {
            Logger.getLogger(Firewall.class.getName()).log(Level.SEVERE, null, ex);
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

    public void setActivityListener(ActivityListener cb)
    {
        activityListener = cb;

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

    public void deleteRule(ChainType activeTab, int ruleNum) throws UnsuccessfulCommandException
    {

        ShellCommandResponse response = shellCom.doCommand("iptables -D " + activeTab.name() + " " + ruleNum);
        if (response.getExitValue() != 0)
        {
            throw new UnsuccessfulCommandException("Rule number does not exist");
        }

    }

    public void insertRule(String text, ChainType activeTab, int number) throws UnsuccessfulCommandException
    {

        String rule = activeTab.name() + " " + text;
        int spaceIndex = rule.indexOf(" ");
        String s = rule.substring(0, spaceIndex + 1);
        String s2 = rule.substring(spaceIndex);
        String actualRule = s + number + s2;

        // Only do stuff if it isn't already done
        ShellCommandResponse response = shellCom.doCommand("iptables -C " + rule);
        if (response.getExitValue() != 0)
        {
            shellCom.doCommand("iptables -I " + actualRule);
        }
        else
        {
            throw new UnsuccessfulCommandException("Rule is already in the chain somewhere");
        }
    }

    public enum ChainType
    {
        INPUT, FORWARD, OUTPUT
    };

    public String[] getRules(ChainType type)
    {

        String[] retval = null;

        try
        {
            List<Rule> selected = IPTables.getCurrentRules().getTable(RuleSet.TableType.FILTER).getChain(type.name()).getRules();

            retval = new String[selected.size()];
            for (int i = 0; i < retval.length; i++)
            {
                retval[i] = selected.get(i).toString().replace("Rule", ((i + 1) + ":")); // Getting "amount" latest
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(Firewall.class.getName()).log(Level.SEVERE, null, ex);
        }

        return retval;
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
