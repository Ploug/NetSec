package com.ur.urcap.bachelor.security.front;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.ui.annotation.Div;
import com.ur.urcap.api.ui.annotation.Input;
import com.ur.urcap.api.ui.annotation.Label;
import com.ur.urcap.api.ui.annotation.Select;
import com.ur.urcap.api.ui.component.InputButton;
import com.ur.urcap.api.ui.component.InputEvent;
import com.ur.urcap.api.ui.component.DivComponent;
import com.ur.urcap.api.ui.component.InputTextField;
import com.ur.urcap.api.ui.component.LabelComponent;
import com.ur.urcap.api.ui.component.SelectDropDownList;
import com.ur.urcap.bachelor.security.business.Firewall;
import com.ur.urcap.bachelor.security.business.URCap;
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
    private static final String PERMISSION_LIST = "permissionList";
    private static final String PERMISSION_ROBOT_BUTTON = "Robotting";
    private static final String PERMISSION_NETWORK_BUTTON = "Networking";
    private static final String PERMISSION_SHELL_BUTTON = "Shelling";
    private static final String PERMISSION_UPDATE = "updatePerm";
    private boolean neededProgramsInstalled;

    private DataModel model;
    private SecurityLinuxMediator linMed;
    private Firewall firewall;

    public SecurityInstallationNodeContribution(DataModel model)
    {
        this.model = model;
    }

    @Input(id = PERMISSION_ROBOT_BUTTON)
    private InputButton permRobButton;

    @Input(id = PERMISSION_NETWORK_BUTTON)
    private InputButton permNetButton;

    @Input(id = PERMISSION_SHELL_BUTTON)
    private InputButton permShellButton;

    @Input(id = PERMISSION_UPDATE)
    private InputButton permUpdate;

    @Div(id = "permissionScreen")
    private DivComponent permissionScreen;

    @Div(id = "advancedScreen")
    private DivComponent advancedScreen;

    @Select(id = PERMISSION_LIST)
    private SelectDropDownList permissionList;

    @Div(id = "passwordScreen")
    private DivComponent passwordScreen;

    @Div(id = "mainScreen")
    private DivComponent mainScreen;

    @Label(id = "informationText")
    private LabelComponent informationText;

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

    private void updatePermButtons()
    {
        URCap selectedCap = (URCap) permissionList.getSelectedItem();
        permRobButton.setText(selectedCap.isRobotControlAllowed() ? "Allowed" : "Denied");
        permNetButton.setText(selectedCap.isNetworkingAllowed() ? "Allowed" : "Denied");
        permShellButton.setText(selectedCap.isShellAllowed() ? "Allowed" : "Denied");
    }

    @Input(id = PERMISSION_UPDATE)
    public void onPermissionUpdateClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            updatePermButtons();
        }
    }

    @Input(id = PERMISSIONS)
    public void onPermissionsClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            updatePermButtons();
            changeScreen(permissionScreen);
        }
    }

    @Input(id = PERMISSION_SHELL_BUTTON)
    public void onPermShellClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            URCap selectedCap = (URCap) permissionList.getSelectedItem();
            selectedCap.setShellPermission(!selectedCap.isShellAllowed());
            updatePermButtons();
        }
    }

    @Input(id = PERMISSION_NETWORK_BUTTON)
    public void onPermNetClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            URCap selectedCap = (URCap) permissionList.getSelectedItem();
            selectedCap.setNetworkPermission(!selectedCap.isNetworkingAllowed());
            updatePermButtons();
        }
    }

    @Input(id = PERMISSION_ROBOT_BUTTON)
    public void onPermRobClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            URCap selectedCap = (URCap) permissionList.getSelectedItem();
            selectedCap.setRobotControlPermission(!selectedCap.isRobotControlAllowed());
            updatePermButtons();
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
            if (neededProgramsInstalled)
            {
                changeScreen(passwordScreen);
            }

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
        Thread installMissing = new Thread(new Runnable() // Installing missing programs asynchronously
        {
            @Override
            public void run()
            {

                try
                {
                    synchronized (informationText)
                    {
                        informationText.setText("Installing missing programs...");
                    }

                    synchronized (linMed)
                    {
                        neededProgramsInstalled = linMed.installMissingPrograms();
                    }

                    synchronized (informationText)
                    {
                        informationText.setText("Missing programs were installed correctly!");
                    }
                    Thread.sleep(10000);

                    synchronized (informationText)
                    {
                        informationText.setText("");
                    }

                }
                catch (UnsuccessfulCommandException ex)
                {
                    synchronized (informationText)
                    {
                        informationText.setText("Could not install missing programs. Something wrong with the network or OS access?");
                    }
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(SecurityInstallationNodeContribution.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        if (!linMed.neededProgramsInstalled())
        {
            installMissing.start();

        }
        else
        {
            neededProgramsInstalled = true;
        }
        
        firewall = Firewall.getInstance();
        
        linMed = SecurityLinuxMediator.getInstance("securityGui");
        permissionsButton.setText("Permissions");
        advSettingsButton.setText("Firewall settings");
        changePswScreenButton.setText("Change Password");
        changePswButton.setText("Confirm change");
        seeMoreButton.setText("See more");
        exitButton.setText("Back");
        permUpdate.setText("Update");
        exitButton.setVisible(false);
        permissionScreen.setVisible(false);
        mainScreen.setVisible(true);
        advancedScreen.setVisible(false);
        passwordScreen.setVisible(false);
        permissionList.setItems(URCap.createTestCaps());
        informationText.setText("");

        

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
