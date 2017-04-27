/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.security.business;

import com.ur.urcap.bachelor.security.business.shell.SecurityLinuxMediator;
import java.util.ArrayList;

/**
 *
 * @author frede
 */
public class Firewall
{

    private ArrayList<IPTable> tables;
    private SecurityLinuxMediator linMed;
    private final String folderName = "Firewall";

    public Firewall()
    {
        linMed = new SecurityLinuxMediator(folderName);
        tables = new ArrayList();
    }

    public void update()
    {
        /*
        for (IPTable table : tables)
        {
            LinuxMediator.doCommand(table.toString());
        }
        linMed.doCommand("sudo iptables -A INPUT -p tcp --dport ssh -j ACCEPT");
        linMed.doCommand("sudo iptables -A INPUT -p tcp --dport 80 -j ACCEPT");
        linMed.doCommand("sudo iptables -A INPUT -j DROP");
        */
    }

}
