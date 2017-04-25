package com.ur.urcap.bachelor.security.impl;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.ui.annotation.Input;
import com.ur.urcap.api.ui.component.InputButton;
import com.ur.urcap.api.ui.component.InputEvent;
import com.ur.urcap.api.ui.component.InputTextField;

public class SecurityInstallationNodeContribution implements InstallationNodeContribution
{

    private static final String POPUPTITLE_KEY = "popuptitle";
    private static final String DEFAULT_VALUE = "Network security foo";
    private static final String PERMISSIONS = "Permissions";
    private static final String ADVSETTINGS = "Advances Settings";
    private static final String CHANGEPSW = "Change Password";
    private static final String SEEMORE = "See more";
    
    

    private DataModel model;

    public SecurityInstallationNodeContribution(DataModel model)
    {
        this.model = model;
    }
    
    @Input(id = PERMISSIONS)
    private InputButton permissionsButton;
    
    @Input(id = ADVSETTINGS)
    private InputButton advSettingsButton;
    
    @Input(id = CHANGEPSW)
    private InputButton changePswButton;
    
    @Input(id = SEEMORE)
    private InputButton seeMoreButton;
    
    @Input(id = POPUPTITLE_KEY)
    private InputTextField popupTitleField;
    
    @Input(id = PERMISSIONS)
    public void onPermissionsClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            
            
        }
    }
    
    @Input(id = ADVSETTINGS)
    public void onAdvSettingsClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            
            
        }
    }
    
    @Input(id = CHANGEPSW)
    public void onChangePswClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            
            
        }
    }

    @Input(id = POPUPTITLE_KEY)
    public void onMessageChange(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_CHANGE)
        {
            setPopupTitle(popupTitleField.getText());
        }
    }

    @Override
    public void openView()
    {
        popupTitleField.setText(getPopupTitle());
        permissionsButton.setText("Permissions");
        advSettingsButton.setText("Advanced Settings");
        changePswButton.setText("Change Password");
        seeMoreButton.setText("See more");
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

}
