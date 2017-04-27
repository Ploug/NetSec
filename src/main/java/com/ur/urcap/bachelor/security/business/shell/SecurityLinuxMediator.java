/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.security.business.shell;

import com.ur.urcap.bachelor.security.exceptions.UnsuccessfulCommandException;
import com.ur.urcap.bachelor.security.services.ShellComService;
import com.ur.urcap.bachelor.security.services.ShellCommandResponse;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
    private String elevated;
    private ShellComService shellCom;
    private String dataPath;

    public SecurityLinuxMediator(String dataFolder)
    {
        dataPath += fs + "var" + fs + dataFolder + fs;
        shellCom = new ShellCommunicator();
        this.dataPath = dataPath;
        passwdPath = dataPath + "passwd";
        ShellCommandResponse response;
        try
        {
            response = shellCom.doCommand("whoami");
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

    public boolean isRootPassword(String password)
    {
        password = password.replace(" ", "\\ ");
        try
        {
            install("whois");

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
            ShellCommandResponse response = shellCom.doCommand(elevated + "dpkg -s " + program);
            return response.getOutput().contains("Status: install ok installed");
        }
        catch (UnsuccessfulCommandException ex)
        {
            Logger.getLogger(SecurityLinuxMediator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void install(String program) throws UnsuccessfulCommandException
    {
        if (isInstalled(program))
        {
            return;
        }
        shellCom.doCommand(elevated + "apt-get -y install " + program);
        if (isInstalled(program))
        {
            return;
        }
        commentSources(false);
        shellCom.doCommand(elevated + "apt-get update");
        shellCom.doCommand(elevated + "apt-get -y install " + program);
        commentSources(true);
        if (!isInstalled(program))
        {
            throw new UnsuccessfulCommandException(program + " was not installed for unknown reasons");
        }

    }

    public void setVNCPassword(String password) throws UnsuccessfulCommandException
    {

        File f = new File(passwdPath);
        shellCom.doCommand(elevated + "mkdir -p " + passwdPath.replace(fs + "passwd", ""));
        shellCom.doCommand(elevated + "touch " + passwdPath);
        ShellCommandResponse response = shellCom.doCommand(elevated + "x11vnc --storepasswd " + password + " " + passwdPath);

        if (!response.getOutput().contains("stored passwd in file"))
        {
            throw new UnsuccessfulCommandException("Password was not saved properly");
        }
    }

    public void startVNC(boolean shared, int port, boolean log, String dataPath) throws UnsuccessfulCommandException
    {
        /*
         -forever: keeps server running after you log out
         - nodpms: prevents power management saving and keeps display alive
         - noxdamage: prevents framebuffer issues and lets x11vnc run with screen tearing issues.
         - rfbport: 5900 is the default port for vnc
         - display:0 chooses which display to show.
         - bg : runs process in background
         - o: location to log the shit.
         - rfbauth: location password is stored.

         */

        shellCom.doCommand("x11vnc -R stop");
        String command = elevated + "" + fs + "usr" + fs + "bin" + fs + "x11vnc -forever -nodpms -noxdamage -bg -rfbauth " + passwdPath;
        command += shared ? " -shared" : "";
        command += " -rfbport " + port;
        if (log)
        {
            File f;
            int i = 0;
            do
            {
                f = new File(dataPath + "logFile_" + i + ".log");
                i++;
            }
            while (f.exists());

            shellCom.doCommand(elevated + "touch" + f.getAbsolutePath());

            String logPath = f.getAbsolutePath();
            command += " -o " + logPath;
        }
        shellCom.doCommand(command);
    }

    public void stopVNC() throws UnsuccessfulCommandException
    {
        ShellCommandResponse response = shellCom.doCommand("x11vnc -R stop");
        if (response.getExitValue() != 0)
        {
            throw new UnsuccessfulCommandException("Something went wrong when stoppping the server");
        }
    }

}
