/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.security.business.iptables;

import java.util.List;

/**
 *
 * @author frede
 */
public class IPTable {
    
    private String name;
    private List<IPTableRule> rules;
    
    
    
    public List<IPTableRule> getRules()
    {
        
        return rules;
    }
    
}
