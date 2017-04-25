/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.security.interfaces;

import com.ur.urcap.bachelor.security.business.shell.UnsuccessfulCommandException;


/**
 *
 * @author frede
 */
public interface LinuxMediator
{

    void setPassword(String password) throws UnsuccessfulCommandException;
    String getIP();
}
