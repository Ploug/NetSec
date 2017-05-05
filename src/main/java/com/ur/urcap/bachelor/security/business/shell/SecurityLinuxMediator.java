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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author frede
 */
public class SecurityLinuxMediator
{

    // echo -e "hello\nhello" | someCommand and hello will be automatically written twice
    // echo "hello" | someCommand and hello will be automatically written once.
    private String ls = System.getProperty("line.separator");
    private String fs = System.getProperty("file.separator");
    private String passwdPath;
    private final ShellComService shellCom;
    private String dataPath;

    private static SecurityLinuxMediator instance = null;


    public static SecurityLinuxMediator getInstance(String dataFolder)
    {
        if (instance == null)
        {
            instance = new SecurityLinuxMediator(dataFolder);
        }
        return instance;
    }

    public static final String[] NEEDED_PROGRAMS =
    {
        "whois", "iptables-persistent"
    };

    private SecurityLinuxMediator(String dataFolder)
    {
        dataPath = fs + "var" + fs + dataFolder + fs;
        shellCom = new ShellCommunicator();
        passwdPath = dataPath + "passwd";
        

    }

    public boolean isRootPassword(String password)

    {
        password = password.replace(" ", "\\ ");
        try
        {

            ShellCommandResponse response = shellCom.doCommand("cat /etc/shadow");
            String s = response.getOutput();
            String target = "root:$6$";
            int start = s.indexOf(target) + target.length();
            int end = s.indexOf("$", start);

            int startHash = end + 1;
            int endHash = s.indexOf(":", start);
            String salt = s.substring(start, end);
            String hash = s.substring(startHash, endHash);
            response = shellCom.doCommand("mkpasswd -m sha-512 " + password + " " + salt);
            String returnedHash = response.getOutput().replace("$6$" + salt + "$", "");
            returnedHash = returnedHash.replace(ls, "");
            return hash.equals(returnedHash);
        }
        catch (UnsuccessfulCommandException ex)
        {
            Logger.getLogger(SecurityLinuxMediator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void changePassword(String password) throws UnsuccessfulCommandException
    {

        String[] answers =
        {
            password, password
        };
        ShellCommandResponse response = shellCom.doCommand("passwd", answers);

        if (!response.getOutput().contains("password updated successfully"))
        {
            throw new UnsuccessfulCommandException("Password was not changed");
        }

    }

    private void commentSources(boolean value)
    {
        if (value)
        {
            findReplaceInFile("/etc/apt/sources.list", "deb ", "#deb ");
            findReplaceInFile("/etc/apt/sources.list", "deb-", "#deb-");
        }
        else
        {
            findReplaceInFile("/etc/apt/sources.list", "#deb", "deb");
        }
    }

    private void findReplaceInFile(String path, String find, String replace)
    {

        try
        {
            File f1 = new File(path);
            FileReader fr = new FileReader(f1);
            BufferedReader br = new BufferedReader(fr);
            String line;
            List<String> lines = new ArrayList<String>();
            while ((line = br.readLine()) != null)
            {
                line = line.replaceAll(find, replace);
                lines.add(line + ls);
            }
            fr.close();
            br.close();

            FileWriter fw = new FileWriter(f1);
            BufferedWriter out = new BufferedWriter(fw);
            for (String s : lines)
            {
                out.write(s);
            }
            out.flush();
            out.close();
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(SecurityLinuxMediator.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(SecurityLinuxMediator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getIP()
    {
        ShellCommandResponse response;
        try
        {
            response = shellCom.doCommand("ip addr show");
            String s = response.getOutput();
            String firstEth = s.substring(s.indexOf("eth"));
            int startIndex = firstEth.indexOf("inet");
            return firstEth.substring(startIndex + 5, firstEth.indexOf("/", startIndex));
        }
        catch (UnsuccessfulCommandException ex)
        {
            Logger.getLogger(SecurityLinuxMediator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public boolean isInstalled(String program)
    {
        try
        {
            ShellCommandResponse response = shellCom.doCommand("dpkg -s " + program);
            return response.getOutput().contains("Status: install ok installed");
        }
        catch (UnsuccessfulCommandException ex)
        {
            Logger.getLogger(SecurityLinuxMediator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean installMissingPrograms() throws UnsuccessfulCommandException
    {
        boolean installedAnything = false;
        for (String neededProgram : NEEDED_PROGRAMS)
        {
            installedAnything = install(neededProgram) ? true : installedAnything;
        }
        return installedAnything;
    }

    public boolean neededProgramsInstalled()
    {
        for (String neededProgram : NEEDED_PROGRAMS)
        {
            if (!isInstalled(neededProgram))
            {
                return false;
            }
        }
        return true;
    }

    public boolean install(String program) throws UnsuccessfulCommandException
    {
        if (isInstalled(program))
        {
            return false;
        }
        shellCom.doCommand("apt-get -y install " + program);
        if (isInstalled(program))
        {
            return true;
        }
        commentSources(false);
        shellCom.doCommand("apt-get update");
        shellCom.doCommand("apt-get -y install " + program);
        commentSources(true);
        if (!isInstalled(program))
        {
            throw new UnsuccessfulCommandException(program + " was not installed for unknown reasons");
        }
        return true;

    }

}
