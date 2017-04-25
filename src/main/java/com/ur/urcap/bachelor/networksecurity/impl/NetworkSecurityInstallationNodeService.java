package com.ur.urcap.bachelor.networksecurity.impl;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.InstallationNodeService;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.data.DataModel;
import java.io.InputStream;

public class NetworkSecurityInstallationNodeService implements InstallationNodeService
{

    public NetworkSecurityInstallationNodeService()
    {
      
    }

    @Override
    public InstallationNodeContribution createInstallationNode(URCapAPI api, DataModel model)
    {
        return new NetworkSecurityInstallationNodeContribution(model);
    }

    @Override
    public String getTitle()
    {
        return "Network Security";
    }

    @Override
    public InputStream getHTML()
    {
        InputStream is = this.getClass().getResourceAsStream("/com/ur/urcap/bachelor/networksecurity/impl/installation.html");
        return is;
    }
}
