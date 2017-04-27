package com.ur.urcap.bachelor.security.front;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.ui.annotation.Div;
import com.ur.urcap.api.ui.annotation.Input;
import com.ur.urcap.api.ui.annotation.Label;
import com.ur.urcap.api.ui.component.InputButton;
import com.ur.urcap.api.ui.component.InputEvent;
import com.ur.urcap.api.ui.component.DivComponent;
import com.ur.urcap.api.ui.component.InputTextField;
import com.ur.urcap.api.ui.component.LabelComponent;
import com.ur.urcap.bachelor.security.business.shell.SecurityLinuxMediator;
import com.ur.urcap.bachelor.security.exceptions.UnsuccessfulCommandException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SecurityInstallationNodeContribution implements InstallationNodeContribution
{

    private static final int RECOMMENDED_PASSWORD_LENGTH = 8;
    private static final String DEFAULT_VALUE = "SecurityDefault";
    private static final String PERMISSIONS = "permissions";
    private static final String ADVSETTINGS = "advancedSettings";
    private static final String CHANGEPSW = "changePswScreen";
    private static final String CONFIRMPSW = "confirmPsw";
    private static final String NEWPSW = "newPsw";
    private static final String OLDPSW = "oldPsw";
    private static final String CHANGEPSWBUT = "changePswButton";
    private static final String SEEMORE = "seeMore";
    private static final String EXITB = "exit";

    private DataModel model;
    private SecurityLinuxMediator linMed;

    public SecurityInstallationNodeContribution(DataModel model)
    {
        this.model = model;
    }

    @Div(id = "permissionScreen")
    private DivComponent permissionScreen;

    @Div(id = "advancedScreen")
    private DivComponent advancedScreen;

    @Div(id = "passwordScreen")
    private DivComponent passwordScreen;

    @Div(id = "mainScreen")
    private DivComponent mainScreen;

    @Div(id = "permissionsConfig")
    private DivComponent permissionsConfig;

    @Label(id = "confirmPswError")
    private LabelComponent confirmPswError;
    @Label(id = "oldPswError")
    private LabelComponent oldPswError;
    @Label(id = "newPswError")
    private LabelComponent newPswError;

    @Label(id = "changePswButtonMsg")
    private LabelComponent passwordScreenMessage;

    @Input(id = PERMISSIONS)
    private InputButton permissionsButton;

    @Input(id = ADVSETTINGS)
    private InputButton advSettingsButton;

    @Input(id = CHANGEPSWBUT)
    private InputButton changePswButton;

    @Input(id = CONFIRMPSW)
    private InputTextField confirmPswField;
    @Input(id = NEWPSW)
    private InputTextField newPswField;
    @Input(id = OLDPSW)
    private InputTextField oldPswField;

    @Input(id = CHANGEPSW)
    private InputButton changePswScreenButton;
    @Input(id = SEEMORE)
    private InputButton seeMoreButton;
    @Input(id = EXITB)
    private InputButton exitButton;

    @Input(id = EXITB)
    public void onExitClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            changeScreen(mainScreen);
        }
    }

    private void changeScreen(DivComponent screen)
    {
        permissionScreen.setVisible(permissionScreen == screen);
        mainScreen.setVisible(mainScreen == screen);
        advancedScreen.setVisible(advancedScreen == screen);
        passwordScreen.setVisible(passwordScreen == screen);
        exitButton.setVisible(mainScreen != screen);
        clearLabels();
    }

    @Input(id = PERMISSIONS)
    public void onPermissionsClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            changeScreen(permissionScreen);
        }
    }

    @Input(id = ADVSETTINGS)
    public void onAdvSettingsClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            changeScreen(advancedScreen);
        }
    }

    @Input(id = CHANGEPSW)
    public void onChangePswScreenClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            changeScreen(passwordScreen);
        }
    }

    @Input(id = CHANGEPSWBUT)
    public void onChangePswClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            changePassword();

        }
    }

    @Input(id = NEWPSW)
    public void onNewEnter(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_CHANGE)
        {
            String newPsw = newPswField.getText();
            clearLabels();
            if (linMed.isRootPassword(newPsw))
            {
                newPswError.setText("Same as current password.");
                return;
            }
            if (newPsw.length() < RECOMMENDED_PASSWORD_LENGTH)
            {
                newPswError.setText("Longer than " + (RECOMMENDED_PASSWORD_LENGTH - 1) + " characters is recommended");
            }
        }
    }

    @Input(id = OLDPSW)
    public void onOldEnter(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_CHANGE)
        {
            String oldPsw = oldPswField.getText();
            clearLabels();
            if (!linMed.isRootPassword(oldPsw))
            {
                oldPswError.setText("Password is incorrect");
            }
        }
    }

    @Input(id = CONFIRMPSW)
    public void onConfirmEnter(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_CHANGE)
        {
            changePassword();
        }
    }

    private void changePassword()
    {
        String confirmedPsw = confirmPswField.getText();
        String newPsw = newPswField.getText();
        String oldPsw = oldPswField.getText();

        System.out.println(confirmedPsw);
        System.out.println(newPsw);
        boolean errors = false;
        clearLabels();
        if (newPsw.length() < RECOMMENDED_PASSWORD_LENGTH)
        {
            newPswError.setText("Longer than " + (RECOMMENDED_PASSWORD_LENGTH - 1) + " characters is recommended");
        }
        if (!linMed.isRootPassword(oldPsw))
        {
            oldPswError.setText("Password is incorrect");
            errors = true;
        }
        if (linMed.isRootPassword(newPsw))
        {
            newPswError.setText("Same as current password.");
            errors = true;
        }
        if (!confirmedPsw.equals(newPsw))
        {
            confirmPswError.setText("The two passwords do not match");
            errors = true;
        }

        if (errors)
        {
            return;
        }
        try
        {
            linMed.changePassword(newPsw);
            passwordScreenMessage.setText("System password changed!");
        }
        catch (UnsuccessfulCommandException ex)
        {
            oldPswError.setText("Password is not correct");
        }
        confirmPswError.setText("");
        oldPswError.setText("");

    }

    private void clearLabels()
    {
        confirmPswError.setText("");
        oldPswError.setText("");
        newPswError.setText("");
        passwordScreenMessage.setText("");
    }

    @Override
    public void openView()
    {

        linMed = new SecurityLinuxMediator("securityGui");
        permissionsButton.setText("Permissions");
        advSettingsButton.setText("Advanced Settings");
        changePswScreenButton.setText("Change Password");
        changePswButton.setText("Confirm change");
        seeMoreButton.setText("See more");
        exitButton.setText("Back");
        exitButton.setVisible(false);
        permissionScreen.setVisible(false);
        mainScreen.setVisible(true);
        advancedScreen.setVisible(false);
        passwordScreen.setVisible(false);
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
