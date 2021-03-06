/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.security.business.shell;

import com.ur.urcap.bachelor.security.exceptions.UnsuccessfulCommandException;
import com.ur.urcap.bachelor.security.interfaces.ShellComService;
import com.ur.urcap.bachelor.security.interfaces.ShellCommandResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author frede
 */
public class ShellCommunicator implements ShellComService
{
    
    private String elevated = "";
    public ShellCommunicator()
    {
        ShellCommandResponse response;
        try
        {
            response = doCommand("whoami");
            if (response.getOutput().contains("root"))
            {
                elevated = "";
            }
            else
            {
                elevated = "sudo ";
            }
        }
        catch (UnsuccessfulCommandException ex)
        {
            Logger.getLogger(SecurityLinuxMediator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public ShellCommandResponse doCommand(String command, String[] answers) throws UnsuccessfulCommandException
    {
        command = elevated+command;
        String output = "";
        int exitValue = -1;
        Process p;
        try
        {
            p = Runtime.getRuntime().exec(command);
            PrintWriter pw = new PrintWriter(p.getOutputStream());
            for (String answer : answers)
            {
                pw.println(answer);
            }
            pw.flush();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            BufferedReader ir = new BufferedReader(
                    new InputStreamReader(p.getErrorStream()));

            while ((command = ir.readLine()) != null)
            {
                output += command + "\n";
            }
            while ((command = br.readLine()) != null)
            {
                output += command + "\n";
            }

            p.waitFor();
            exitValue = p.exitValue();
            p.destroy();
        }
        catch (IOException ex)
        {
            throw new UnsuccessfulCommandException(ex.getMessage());
        }
        catch (InterruptedException ex)
        {
            throw new UnsuccessfulCommandException(ex.getMessage());
        }
        return new ShellCommandResponse(output, exitValue);
    }

    @Override
    public ShellCommandResponse doCommand(String command) throws UnsuccessfulCommandException
    {
        command = elevated+command;
        String output = "";
        int exitValue = -1;
        Process p;
        try
        {
            p = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            BufferedReader ir = new BufferedReader(
                    new InputStreamReader(p.getErrorStream()));
            while ((command = ir.readLine()) != null)
            {
                output += command + "\n";
            }
            while ((command = br.readLine()) != null)
            {
                output += command + "\n";
            }

            p.waitFor();
            exitValue = p.exitValue();
            p.destroy();
        }
        catch (IOException ex)
        {
            throw new UnsuccessfulCommandException(ex.getMessage());
        }
        catch (InterruptedException ex)
        {
            throw new UnsuccessfulCommandException(ex.getMessage());
        }
        return new ShellCommandResponse(output, exitValue);
    }

    @Override
    public ShellCommandResponse runScript(String path) throws UnsuccessfulCommandException
    {
        /* Currently trouble with handling proper file pathing in linux.
        try
        {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(script);
            proc.waitFor();
            StringBuffer output = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                output.append(line + "\n");
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        } */
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
