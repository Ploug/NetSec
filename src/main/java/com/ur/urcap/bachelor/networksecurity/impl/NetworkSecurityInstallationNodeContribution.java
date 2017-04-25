package com.ur.urcap.bachelor.networksecurity.impl;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.ui.annotation.Input;
import com.ur.urcap.api.ui.component.InputEvent;
import com.ur.urcap.api.ui.component.InputTextField;

public class NetworkSecurityInstallationNodeContribution implements InstallationNodeContribution
{

    private static final String POPUPTITLE_KEY = "popuptitle";
    private static final String IPTABLEBUTTON_KEY = "iptablebutton";
    private static final String DEFAULT_VALUE = "Network security foo";

    private DataModel model;
    private Firewall firewall;

    public NetworkSecurityInstallationNodeContribution(DataModel model)
    {
        this.model = model;
        firewall = new Firewall();
    }
   

    @Input(id = POPUPTITLE_KEY)
    public void onMessageChange(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_CHANGE)
        {
            setPopupTitle(popupTitleField.getText());
        }
    }
    @Input(id = POPUPTITLE_KEY)
    private InputTextField popupTitleField;

    @Input(id = IPTABLEBUTTON_KEY)
    public void onClick(InputEvent event)
    {
        System.out.println("woopti");
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            firewall.update();
        }
    }

    @Override
    public void openView()
    {
        popupTitleField.setText(getPopupTitle());
    }

    @Override
    public void closeView()
    {
    }

    public boolean isDefined()
    {
        return !getPopupTitle().isEmpty();
    }

    @Override
    public void generateScript(ScriptWriter writer)
    {
        // Store the popup title in a global variable so it is globally available to all HelloWorld program nodes.
        writer.assign("network_security_popup_title", "\"" + getPopupTitle() + "\"");
    }

     public String getPopupTitle()
    {
        return model.get(POPUPTITLE_KEY, DEFAULT_VALUE);
    }

    private void setPopupTitle(String message)
    {
        model.set(POPUPTITLE_KEY, message);
    }
    
    public String iptablebutton()
    {
        return model.get(IPTABLEBUTTON_KEY, DEFAULT_VALUE);
    }

    private void iptablebutton(String message)
    {
        model.set(IPTABLEBUTTON_KEY, message);
    }

}
