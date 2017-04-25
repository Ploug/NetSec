/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.networksecurity.impl;

import java.util.ArrayList;

/**
 *
 * @author frede
 */
public class Firewall {

    ArrayList<IPTable> tables;

    public Firewall() {
        tables = new ArrayList();
    }

    public void update() {
//        for (IPTable table : tables) {
//            LinuxMediator.doCommand(table.toString());
//        }
            LinuxMediator.doCommand("sudo iptables -A INPUT -p tcp --dport ssh -j ACCEPT");
            LinuxMediator.doCommand("sudo iptables -A INPUT -p tcp --dport 80 -j ACCEPT");
            LinuxMediator.doCommand("sudo iptables -A INPUT -j DROP");
    }

}
