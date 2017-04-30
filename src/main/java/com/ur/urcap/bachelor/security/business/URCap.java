/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.security.business;

import java.util.ArrayList;
import java.util.List;

/**
 * Only used for GUI, actual permissions would have to be implemented from ground up with security manager.
 * @author frede
 */
public class URCap
{
    private final String name;
    private boolean networkPermission;
    private boolean robotControlPermission;
    private boolean shellPermission;
    
    
    public URCap(String inputName)
    {
        name = inputName;
        networkPermission = false;
        robotControlPermission = false;
        shellPermission = false;
    }
    
    public static List<URCap> createTestCaps()
    {
        List<URCap> testCaps = new ArrayList();
        URCap vncServer = new URCap("VNC Server");
        vncServer.setNetworkPermission(true);
        vncServer.setShellPermission(true);
        URCap robotMod = new URCap("Robot Hand Mod");
        robotMod.setRobotControlPermission(true);
        testCaps.add(vncServer);
        testCaps.add(robotMod);
        testCaps.add(new URCap("Unknown URCap"));
        
        
        return testCaps;
    }

    public boolean isNetworkingAllowed()
    {
        return networkPermission;
    }

    public void setNetworkPermission(boolean networkPermission)
    {
        this.networkPermission = networkPermission;
    }

    public boolean isRobotControlAllowed()
    {
        return robotControlPermission;
    }

    public void setRobotControlPermission(boolean robotControlPermission)
    {
        this.robotControlPermission = robotControlPermission;
    }

    public boolean isShellAllowed()
    {
        return shellPermission;
    }

    public void setShellPermission(boolean shellPermission)
    {
        this.shellPermission = shellPermission;
    }
    
    @Override
    public String toString()
    {
        return name;
    }
}
