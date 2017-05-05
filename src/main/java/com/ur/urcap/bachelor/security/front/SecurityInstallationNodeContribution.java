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
import com.ur.urcap.bachelor.security.interfaces.ActivityListener;
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
    private static final String INPUT_ADV = "inputAdv";
    private static final String FORWARD_ADV = "forwardAdv";
    private static final String OUTPUT_ADV = "outputAdv";
    private static final String RULE_INPUT_FIELD = "ruleInput";
    private static final String APPEND_ADV = "appendAdv";
    private static final String INSERT_ADV = "insertAdv";
    private static final String DELETE_ADV = "deleteAdv";
    private static final String OPEN_PORT = "openPort";
    private static final String CLOSE_PORT = "closePort";

    private static final String UP_ADV = "upAdv";
    private static final String DOWN_ADV = "downAdv";

    private boolean neededProgramsInstalled;

    private DataModel model;
    private SecurityLinuxMediator linMed;
    private Firewall firewall;

    public SecurityInstallationNodeContribution(DataModel model)
    {
        this.model = model;
    }
    @Div(id = "advancedScreen")
    private DivComponent advancedScreen;
    @Label(id = "rule1")
    private LabelComponent ruleLabel1;
    @Label(id = "rule2")
    private LabelComponent ruleLabel2;
    @Label(id = "rule3")
    private LabelComponent ruleLabel3;
    @Label(id = "rule4")
    private LabelComponent ruleLabel4;
    @Label(id = "rule5")
    private LabelComponent ruleLabel5;
    @Label(id = "rule6")
    private LabelComponent ruleLabel6;
    @Label(id = "rule7")
    private LabelComponent ruleLabel7;
    @Label(id = "rule8")
    private LabelComponent ruleLabel8;
    @Label(id = "rule9")
    private LabelComponent ruleLabel9;
    @Label(id = "rule10")
    private LabelComponent ruleLabel10;
    @Label(id = "rule11")
    private LabelComponent ruleLabel11;
    @Label(id = "rule12")
    private LabelComponent ruleLabel12;
    @Label(id = "rule13")
    private LabelComponent ruleLabel13;
    @Label(id = "rule14")
    private LabelComponent ruleLabel14;
    @Label(id = "rule15")
    private LabelComponent ruleLabel15;
    @Label(id = "rule16")
    private LabelComponent ruleLabel16;
    @Label(id = "rule17")
    private LabelComponent ruleLabel17;
    @Label(id = "rule18")
    private LabelComponent ruleLabel18;
    @Label(id = "rule19")
    private LabelComponent ruleLabel19;
    @Label(id = "rule20")
    private LabelComponent ruleLabel20;
    @Label(id = "rule21")
    private LabelComponent ruleLabel21;
    @Label(id = "rule22")
    private LabelComponent ruleLabel22;
    private int upped;

    private final LabelComponent ruleLabels[] = new LabelComponent[22];

    @Input(id = UP_ADV)
    private InputButton upAdvButton;
    @Input(id = DOWN_ADV)
    private InputButton downAdvButton;

    @Input(id = INPUT_ADV)
    private InputButton inputAdvButton;
    @Input(id = FORWARD_ADV)
    private InputButton forwardAdvButton;
    @Input(id = OUTPUT_ADV)
    private InputButton outputAdvButton;
    @Input(id = RULE_INPUT_FIELD)
    private InputTextField ruleInputFeld;
    @Input(id = APPEND_ADV)
    private InputButton appendAdvButton;
    @Input(id = INSERT_ADV)
    private InputButton insertAdvButton;
    @Input(id = DELETE_ADV)
    private InputButton deleteAdvButton;
    @Input(id = OPEN_PORT)
    private InputButton openPortButton;
    @Input(id = CLOSE_PORT)
    private InputButton closePortButton;

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

    @Input(id = INPUT_ADV)
    public void onFnputAdvClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {

        }
    }

    @Input(id = FORWARD_ADV)
    public void onForwardAdvClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {

        }
    }

    @Input(id = OUTPUT_ADV)
    public void onOutputAdvClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {

        }
    }

    @Input(id = RULE_INPUT_FIELD)
    public void onRuleEnter(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_CHANGE)
        {

        }
    }

    @Input(id = APPEND_ADV)
    public void onAppendAdvClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {

        }
    }

    @Input(id = INSERT_ADV)
    public void onInsertAdvClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {

        }
    }

    @Input(id = DELETE_ADV)
    public void onDeleteAdvClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {

        }
    }

    @Input(id = OPEN_PORT)
    public void onOpenPortClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {

        }
    }

    @Input(id = CLOSE_PORT)
    public void onClosePortClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {

        }
    }

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
        networkActScreen.setVisible(networkActScreen == screen);
        passwordScreen.setVisible(passwordScreen == screen);
        advancedScreen.setVisible(advancedScreen == screen);
        exitButton.setVisible(mainScreen != screen);
        upped = 0;
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

    @Input(id = DOWN_ADV)
    public void onDownAdvClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            if (upped > 0)
            {
                upped--;
                updateRulesTable();
            }

        }
    }

    @Input(id = UP_ADV)
    public void onUpAdvClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            if (firewall.getAllActivity(0).length >= ruleLabels.length + upped)
            {
                upped++;
                updateRulesTable();
            }

        }
    }

    private void updateRulesTable()
    {
        /* String[] rules = firewall.getRules("INPUT");
        for (int i = 0; i < log.length - upped; i++)
        {
            nactivities[i].setText(log[i]);
        } */
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

        ruleLabels[0] = ruleLabel1;
        ruleLabels[1] = ruleLabel2;
        ruleLabels[2] = ruleLabel3;
        ruleLabels[3] = ruleLabel4;
        ruleLabels[4] = ruleLabel5;
        ruleLabels[5] = ruleLabel6;
        ruleLabels[6] = ruleLabel7;
        ruleLabels[7] = ruleLabel8;
        ruleLabels[8] = ruleLabel9;
        ruleLabels[9] = ruleLabel10;
        ruleLabels[10] = ruleLabel11;
        ruleLabels[11] = ruleLabel12;
        ruleLabels[12] = ruleLabel13;
        ruleLabels[13] = ruleLabel14;
        ruleLabels[14] = ruleLabel15;
        ruleLabels[15] = ruleLabel16;
        ruleLabels[16] = ruleLabel17;
        ruleLabels[17] = ruleLabel18;
        ruleLabels[18] = ruleLabel19;
        ruleLabels[19] = ruleLabel20;
        ruleLabels[20] = ruleLabel21;
        ruleLabels[21] = ruleLabel22;
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
        for (LabelComponent nactivity : nactivities)
        {
            nactivity.setText("");
        }
        for (LabelComponent ruleLabel : ruleLabels)
        {
            ruleLabel.setText("");
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
        downNetButton.setText("Down");
        upNetButton.setText("Up");
        downAdvButton.setText("Down");
        upAdvButton.setText("Up");
        inputAdvButton.setText("INPUT");
        forwardAdvButton.setText("FORWARD");
        outputAdvButton.setText("OUTPUT");

        appendAdvButton.setText("Append rule");
        insertAdvButton.setText("Insert rule");

        deleteAdvButton.setText("Delete rule");
        openPortButton.setText("Open port");
        closePortButton.setText("Close port");

        informationText.setText("");
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
