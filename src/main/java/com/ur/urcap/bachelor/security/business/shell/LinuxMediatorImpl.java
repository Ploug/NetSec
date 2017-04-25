/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.security.business.shell;

import com.ur.urcap.bachelor.security.interfaces.LinuxMediator;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author frede
 */
public class LinuxMediatorImpl implements LinuxMediator
{

    private String ls = System.getProperty("line.separator");
    private String fs = System.getProperty("file.separator");

    public void setPassword(String password) throws UnsuccessfulCommandException
    {

       
    }


    public String getIP()
    {
        ShellCommandResponse response = doCommand("ip addr show eth0");
        String s = response.getOutput();
        int startIndex = s.indexOf("inet");
        return response.getOutput().substring(s.indexOf("inet") + 5, s.indexOf("/", startIndex));
    }
    
    
    private ShellCommandResponse doCommand(String s)
    {
        String output = "";
        int exitValue = -1;
        Process p;
        try
        {
            System.out.println("\ncommand:" + s + "\n");
            p = Runtime.getRuntime().exec(s);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            BufferedReader ir = new BufferedReader(
                    new InputStreamReader(p.getErrorStream()));
            while ((s = ir.readLine()) != null)
            {
                output += s + "\n";
            }
            while ((s = br.readLine()) != null)
            {
                output += s + "\n";
            }

            p.waitFor();
            exitValue = p.exitValue();
            p.destroy();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ShellCommandResponse(output, exitValue);
    }

}
