package com.fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.activity.R;
import com.harman.everestelite.CommonListner;
import com.harman.everestelite.Log;
import com.storage.JBLprefrences;

/**
 * Created by root on 16-3-24.
 */
public class FragSetting extends BaseFragment implements View.OnClickListener,
        CommonListner, RadioGroup.OnCheckedChangeListener
{
    public FragSetting() {
    }

    public static final String TAG = FragSetting.class.getSimpleName();
    public static final String MODEL = "MODEL";
    public static final String PRODUCT = "PRODUCT";
    TextView updateDevice, smartButton, txtVersion, txtName, appVersion;
    Switch autOff, voicePrompt;
    RadioGroup radioGrp;
    RadioButton rdoNoise, rdoAmbient, rdoCustom;
    String devicename = "", devicenametemp;
    int major = 0, minor = 0, revision = 0;
    private boolean boolValue;
    String version;
    int versionCode = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readBasicInformation();
        devicename = JBLprefrences.getString(FragSetting.MODEL, getAppActivity(), getString(R.string.deviceName));
        try {
            if (devicename.length() >= 25) {
                devicenametemp = devicename.substring(0, 22) + "...";
            } else
                devicenametemp = devicename;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            version = getAppActivity().getPackageManager().getPackageInfo(getAppActivity().getPackageName(), 0).versionName;
            versionCode = getAppActivity().getPackageManager().getPackageInfo(getAppActivity().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_setting,
                container, false);
        updateDevice = (TextView) view.findViewById(R.id.text_updateDevice);
//        txtVersion = (TextView) view.findViewById(R.id.text_install_update);
        radioGrp = (RadioGroup)view.findViewById(R.id.radioGrp);
        rdoNoise = (RadioButton)view.findViewById(R.id.rdoNsCancel);
        rdoAmbient = (RadioButton)view.findViewById(R.id.rdoAmbAwr);
        rdoCustom = (RadioButton)view.findViewById(R.id.rdoCustom);
        smartButton = (TextView) view.findViewById(R.id.text_smartButton);
        appVersion = (TextView) view.findViewById(R.id.appVersion);

        autOff = (Switch) view.findViewById(R.id.toggleautoOff);
        txtName = (TextView) view.findViewById(R.id.deviceName);
        txtName.setTextSize(30.0f);
        txtName.setText(devicename);
        txtVersion = (TextView) view.findViewById(R.id.deviceId);
        txtVersion.setTextSize(20.0f);
        autOff.setOnClickListener(this);
        voicePrompt = (Switch) view.findViewById(R.id.toggleVoicePrompt);

        radioGrp.setOnCheckedChangeListener(this);
        voicePrompt.setOnClickListener(this);
        updateDevice.setOnClickListener(this);

        voicePrompt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getActionMasked() == MotionEvent.ACTION_MOVE;
            }
        });

        autOff.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getActionMasked() == MotionEvent.ACTION_MOVE;
            }
        });


        smartButton.setOnClickListener(this);
        txtVersion.setText(major + "." + minor + "." + revision);
        readBasicInformation();
        appVersion.setText(version + " (" + versionCode + ")");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        getAppActivity().headphoneConnListener = this;
//        if(getAppActivity().headphCtrl != null)
//            getAppActivity().headphCtrl.getConfigModelNumber();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /**
         * safely remove handler
         */
        try {
            autoOFfToggle.removeCallbacks(autoOffToggleRunnable);
            autoOffToggleRunnable = null;
            autoOffToggleRunnable = null;
        } catch (Exception e) {

        }

        try {
            enableVoicePrompt.removeCallbacks(enableVoicePromptRunnable);
            enableVoicePrompt = null;
            enableVoicePromptRunnable = null;
        } catch (Exception e) {

        }
    }

    /**
     * Stop user to abuse app and send all command only after 1.2 seconds
     */

    Handler autoOFfToggle = new Handler();
    Runnable autoOffToggleRunnable = new Runnable() {
        @Override
        public void run() {
            if (getAppActivity().headphCtrl != null)
                getAppActivity().headphCtrl.commonCtrl.setAutoOffFeature(autOff.isChecked());
            Log.d("AutoOffFeature", autOff.isChecked() + " sent");
        }
    };

    Handler enableVoicePrompt = new Handler();
    Runnable enableVoicePromptRunnable = new Runnable() {
        @Override
        public void run() {
            if (getAppActivity().headphCtrl != null)
                getAppActivity().headphCtrl.commonCtrl.setEnableVoicePrompt(voicePrompt.isChecked());
            Log.d("VoicePrompt", voicePrompt.isChecked() + " sent");
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_updateDevice:
//                if (FirmwareUtil.isUpdateAvailable("0.5.0", txtVersion.getText().toString())) {
//                    AlertsDialog.showSimpleDialogWithOKButton("A new update is available for your headphone.", "Please connect your Everest Headphone to a computer and go to" +
//                            " www.jbl.com/app to complete the upgrade.", getAppActivity());
//                }
                break;
            case R.id.text_smartButton:

                break;
            case R.id.toggleautoOff:
                autoOFfToggle.removeCallbacks(autoOffToggleRunnable);
                autoOFfToggle.postDelayed(autoOffToggleRunnable, 1000);
                break;
            case R.id.toggleVoicePrompt:
                enableVoicePrompt.removeCallbacks(enableVoicePromptRunnable);
                enableVoicePrompt.postDelayed(enableVoicePromptRunnable, 1000);
                break;
        }

    }

    @Override
    public void getProgrammableIndexButtonReply(boolean noiseOrAmbient){
        if(noiseOrAmbient){
            rdoNoise.setChecked(true);
            rdoAmbient.setChecked(false);
        }
        else{
            rdoNoise.setChecked(false);
            rdoAmbient.setChecked(true);
        }
    }

    @Override
    public void getConfigModelNumberReply(String modelNum){
        JBLprefrences.setString(FragSetting.MODEL, modelNum, getAppActivity());
        devicename = modelNum;
        txtName.setText(devicename);
    }

    @Override
    public void getConfigProductNameReply(String prodName){
        JBLprefrences.setString(FragSetting.PRODUCT, prodName, getAppActivity());
    }

    @Override
    public void getAutoOffFeatureReply(boolean autoOff){
        autOff.setChecked(autoOff);
    }

    @Override
    public void getEnableVoicePromptReply(boolean prompt){
        voicePrompt.setChecked(prompt);
    }

    @Override
    public void getFirmwareVersionReply(int version, int minor, int major){
        this.major = major;
        this.minor = minor;
        revision = version;
        txtVersion.setText(major + "." + minor + "." + revision);
    }

    @Override
    public void waitCommandReplyElapsedTime(int elapaseMs) {

    }

    @Override
    public void headPhoneError(Exception exception) {

    }

    @Override
    public void setAutoOffFeatureReply(boolean success) {

    }

    @Override
    public void setEnableVoicePromptReply(boolean success) {

    }

    /**
     * Reads settings from headphone.
     */
    public void readBasicInformation() {
        if (getAppActivity().headphCtrl == null)
            return;
        getAppActivity().headphCtrl.commonCtrl.getFirmwareVersion();
        getAppActivity().headphCtrl.commonCtrl.getEnableVoicePrompt();
        getAppActivity().headphCtrl.commonCtrl.getAutoOffFeature();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (getAppActivity().headphCtrl == null)
            return;
        if(checkedId == R.id.rdoNsCancel){
            getAppActivity().headphCtrl.commonCtrl.setProgrammableIndexButton(true);
        }
        else if (checkedId == R.id.rdoAmbAwr){
            getAppActivity().headphCtrl.commonCtrl.setProgrammableIndexButton(false);
        }
//        else if(checkedId == R.id.rdoCustom){
//            getAppActivity().headphCtrl.commonCtrl.setTapCommit();
//        }
    }
}
