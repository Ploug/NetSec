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
import com.ur.urcap.bachelor.security.services.ActivityListener;
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
    private static final String UP_NET = "upNet";
    private static final String DOWN_NET = "downNet";
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

    @Div(id = "networkActScreen")
    private DivComponent networkActScreen;

    @Select(id = PERMISSION_LIST)
    private SelectDropDownList permissionList;

    @Div(id = "passwordScreen")
    private DivComponent passwordScreen;

    @Div(id = "mainScreen")
    private DivComponent mainScreen;

    @Label(id = "informationText")
    private LabelComponent informationText;

    @Label(id = "upped")
    private LabelComponent uppedText;

    @Label(id = "act1")
    private LabelComponent activity1;
    @Label(id = "act2")
    private LabelComponent activity2;
    @Label(id = "act3")
    private LabelComponent activity3;
    @Label(id = "act4")
    private LabelComponent activity4;
    @Label(id = "act5")
    private LabelComponent activity5;
    @Label(id = "act6")
    private LabelComponent activity6;

    private final LabelComponent activities[] = new LabelComponent[6];

    @Label(id = "nact1")
    private LabelComponent nactivity1;
    @Label(id = "nact2")
    private LabelComponent nactivity2;
    @Label(id = "nact3")
    private LabelComponent nactivity3;
    @Label(id = "nact4")
    private LabelComponent nactivity4;
    @Label(id = "nact5")
    private LabelComponent nactivity5;
    @Label(id = "nact6")
    private LabelComponent nactivity6;
    @Label(id = "nact7")
    private LabelComponent nactivity7;
    @Label(id = "nact8")
    private LabelComponent nactivity8;
    @Label(id = "nact9")
    private LabelComponent nactivity9;
    @Label(id = "nact10")
    private LabelComponent nactivity10;
    @Label(id = "nact11")
    private LabelComponent nactivity11;
    @Label(id = "nact12")
    private LabelComponent nactivity12;
    @Label(id = "nact13")
    private LabelComponent nactivity13;
    @Label(id = "nact14")
    private LabelComponent nactivity14;
    @Label(id = "nact15")
    private LabelComponent nactivity15;
    @Label(id = "nact16")
    private LabelComponent nactivity16;
    @Label(id = "nact17")
    private LabelComponent nactivity17;
    @Label(id = "nact18")
    private LabelComponent nactivity18;
    @Label(id = "nact19")
    private LabelComponent nactivity19;
    @Label(id = "nact20")
    private LabelComponent nactivity20;
    @Label(id = "nact21")
    private LabelComponent nactivity21;
    @Label(id = "nact22")
    private LabelComponent nactivity22;

    private final LabelComponent nactivities[] = new LabelComponent[22];

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
    @Input(id = UP_NET)
    private InputButton upNetButton;
    @Input(id = DOWN_NET)
    private InputButton downNetButton;

    private int upped = 0;

    @Input(id = DOWN_NET)
    public void onDownNetClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            if (upped > 0)
            {
                upped--;
                updateActivity();
            }

        }
    }

    @Input(id = UP_NET)
    public void onUpNetClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            if (firewall.getAllActivity(0).length >= nactivities.length + upped)
            {
                upped++;
                updateActivity();
            }

        }
    }

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
        networkActScreen.setVisible(networkActScreen == screen);
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

    @Input(id = SEEMORE)
    public void onSeeMoreClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            changeScreen(networkActScreen);
        }
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

    private void updateActivity()
    {
        uppedText.setText(upped + "");
        String[] log = firewall.getInterestingActivity(activities.length);
        for (int i = 0; i < log.length; i++)
        {
            activities[i].setText(log[i]);
        }
        log = firewall.getAllActivity(nactivities.length + upped);
        for (int i = 0; i < log.length - upped; i++)
        {
            nactivities[i].setText(log[i]);
        }
    }

    @Override
    public void openView()
    {
        linMed = SecurityLinuxMediator.getInstance("securityGui");
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

        activities[0] = activity1;
        activities[1] = activity2;
        activities[2] = activity3;
        activities[3] = activity4;
        activities[4] = activity5;
        activities[5] = activity6;

        nactivities[0] = nactivity1;
        nactivities[1] = nactivity2;
        nactivities[2] = nactivity3;
        nactivities[3] = nactivity4;
        nactivities[4] = nactivity5;
        nactivities[5] = nactivity6;
        nactivities[6] = nactivity7;
        nactivities[7] = nactivity8;
        nactivities[8] = nactivity9;
        nactivities[9] = nactivity10;
        nactivities[10] = nactivity11;
        nactivities[11] = nactivity12;
        nactivities[12] = nactivity13;
        nactivities[13] = nactivity14;
        nactivities[14] = nactivity15;
        nactivities[15] = nactivity16;
        nactivities[16] = nactivity17;
        nactivities[17] = nactivity18;
        nactivities[18] = nactivity19;
        nactivities[19] = nactivity20;
        nactivities[20] = nactivity21;
        nactivities[21] = nactivity22;
        firewall.setActivityListener(new ActivityListener()
        {
            @Override
            public void activityUpdate()
            {
                updateActivity();

            }
        });
        for (LabelComponent activitie : activities)
        {
            activitie.setText("");
        }
        for (LabelComponent nactivitie : nactivities)
        {
            nactivitie.setText("");
        }
        updateActivity();
        uppedText.setText(upped + "");
        permissionsButton.setText("Permissions");
        advSettingsButton.setText("Firewall settings");
        changePswScreenButton.setText("Change Password");
        changePswButton.setText("Confirm change");
        seeMoreButton.setText("See more");
        exitButton.setText("Back");
        permUpdate.setText("Update");
        downNetButton.setText("v");
        upNetButton.setText("^");
        changeScreen(mainScreen);
        permissionList.setItems(URCap.createTestCaps());

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
