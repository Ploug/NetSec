package com.ur.urcap.bachelor.security.front;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.ui.annotation.Input;
import com.ur.urcap.api.ui.component.InputButton;
import com.ur.urcap.api.ui.component.InputEvent;

public class SecurityInstallationNodeContribution implements InstallationNodeContribution
{

    private static final String DEFAULT_VALUE = "SecurityDefault";
    private static final String PERMISSIONS = "Permissions";
    private static final String ADVSETTINGS = "AdvancedSettings";
    private static final String CHANGEPSW = "ChangePsw";
    private static final String SEEMORE = "seeMore";

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

    @Override
    public void openView()
    {
        permissionsButton.setText("Permissions");
        advSettingsButton.setText("Advanced Settings");
        changePswButton.setText("Change Password");
        seeMoreButton.setText("See more");
    }

    @Override
    public void closeView()
    {
    }

    @Override
    public void generateScript(ScriptWriter writer)
    {

    }

}
