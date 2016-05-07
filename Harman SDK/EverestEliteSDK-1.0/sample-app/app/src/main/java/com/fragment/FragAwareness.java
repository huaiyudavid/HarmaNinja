package com.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.activity.R;
import com.harman.everestelite.ANCAwarenessPreset;
import com.harman.everestelite.ANCCtrlListner;
import com.interfaces.OnHeadphoneconnectListener;
import com.storage.JBLprefrences;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragAwareness extends BaseFragment
        implements CompoundButton.OnCheckedChangeListener,
        View.OnClickListener, SeekBar.OnSeekBarChangeListener,
        ANCCtrlListner, OnHeadphoneconnectListener {
    @Bind(R.id.bletooth_conn)
    TextView bletoothConn;
    @Bind(R.id.bletooth_conn_stat)
    TextView bletoothConnStat;
    @Bind(R.id.progressBattery)
    ProgressBar progressBattery;
    @Bind(R.id.textBattery)
    TextView textBattery;
    @Bind(R.id.ANCSwitch)
    Switch ANCSwitch;
    @Bind(R.id.awarenessOff)
    TextView awarenessOff;
    @Bind(R.id.progressLPhone)
    SeekBar progressLPhone;
    @Bind(R.id.progressRPhone)
    SeekBar progressRPhone;
    @Bind(R.id.textLPhone)
    TextView textLPhone;
    @Bind(R.id.awareLow)
    TextView awareLow;
    @Bind(R.id.awareMedium)
    TextView awareMedium;
    @Bind(R.id.awareHigh)
    TextView awareHigh;
    @Bind(R.id.textRPhone)
    TextView textRPhone;

    public static final String TAG = FragAwareness.class.getSimpleName();
    public static String LEFT_PERSIST = "LEFT_PERSIST";
    public static String RIGHT_PERSIST = "RIGHT_PERSIST";
    public static String AWARENESS = "AWARENESS";
    private static String TOGGLE_PERSIST = "toggle";
    private int drawCase = -1, batteryLevel = 0, mLeftProgress, mRightProgress;
    private long uintVal;
    ANCAwarenessPreset awarenessPreset, lastsavedAwarenessState;
    boolean mChecked;
    int lPhoneVal, rPhoneVal;
//    ANControlManager ancCtrlMng;

    public FragAwareness() {
    }

    public void onResume(){
        super.onResume();
//        getAppActivity().headphoneConnListener = this;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ancCtrlMng = ANControlManager.getANCManager(getAppActivity());
        if (getAppActivity().isConnected) {
            Log.e("Home", "Not Connected");
        } else {
//            lightX = getAppActiviy().mLightX;
//            getAppActivity().headphCtrl = ((BaseActivity)getAppActivity()).getAppActivity().headphCtrl;
            Log.e("Home", "Connected");
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_awareness, container, false);
        ButterKnife.bind(this, view);
        ANCSwitch.setOnCheckedChangeListener(this);
        progressLPhone.setOnSeekBarChangeListener(this);
        progressRPhone.setOnSeekBarChangeListener(this);
        awareLow.setOnClickListener(this);
        awareMedium.setOnClickListener(this);
        awareHigh.setOnClickListener(this);
        awarenessOff.setOnClickListener(this);
        bletoothConn.setOnClickListener(this);
        return view;
    }


    private void readValues() {
        if (getAppActivity().headphCtrl == null)
            return;
        getAppActivity().headphCtrl.ancCtrl.getANCEnable();
        getAppActivity().headphCtrl.ancCtrl.getANCAwarenessPreset();
        getAppActivity().headphCtrl.ancCtrl.getBatteryLevel();
        getAppActivity().headphCtrl.eqCtrl.getCurrentPreset();

        getAppActivity().headphCtrl.commonCtrl.getConfigModelNumber();
        getAppActivity().headphCtrl.commonCtrl.getConfigProductName();
        getAppActivity().headphCtrl.commonCtrl.getProgrammableIndexButton();
        getAppActivity().headphCtrl.commonCtrl.getFirmwareVersion();
        getAppActivity().headphCtrl.commonCtrl.getEnableVoicePrompt();
        getAppActivity().headphCtrl.commonCtrl.getAutoOffFeature();

    }



    /**
     * Handle all changed when ANC changed to On
     */
    public void ANCOn() {
        lPhoneVal = JBLprefrences.getInt(LEFT_PERSIST, getAppActivity());
        rPhoneVal = JBLprefrences.getInt(LEFT_PERSIST, getAppActivity());
        progressLPhone.setProgress(lPhoneVal);
        progressRPhone.setProgress(rPhoneVal);
        progressLPhone.setEnabled(true);
        progressRPhone.setEnabled(true);
        awareLow.setEnabled(true);
        awareMedium.setEnabled(true);
        awareHigh.setEnabled(true);
        if(getAppActivity().headphCtrl != null) {
            getAppActivity().headphCtrl.ancCtrl.switchANC(true);
            getAppActivity().headphCtrl.ancCtrl.setLeftAwarenessPresetValue(lPhoneVal);
            getAppActivity().headphCtrl.ancCtrl.setLeftAwarenessPresetValue(rPhoneVal);
        }
    }

    /**
     * Handle all changed when ANC changed to off
     */
    public void ANCOff() {
        JBLprefrences.setInt(LEFT_PERSIST, lPhoneVal, getAppActivity());
        JBLprefrences.setInt(LEFT_PERSIST, rPhoneVal, getAppActivity());
        progressLPhone.setProgress(0);
        progressRPhone.setProgress(0);
        progressLPhone.setEnabled(false);
        progressRPhone.setEnabled(false);
        awareLow.setEnabled(false);
        awareMedium.setEnabled(false);
        awareHigh.setEnabled(false);
        awareLow.setAlpha(0.4f);
        awareMedium.setAlpha(0.4f);
        awareHigh.setAlpha(0.4f);
        if(getAppActivity().headphCtrl != null) {
            getAppActivity().headphCtrl.ancCtrl.switchANC(false);
            getAppActivity().headphCtrl.ancCtrl.setLeftAwarenessPresetValue(0);
            getAppActivity().headphCtrl.ancCtrl.setLeftAwarenessPresetValue(0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
//            noiseCancel.setTextColor(getResources().getColor(R.color.outerCircle));
//            control.setSwitchOff(false);
            ANCOff();
            mChecked = false;
            JBLprefrences.setBoolean(TOGGLE_PERSIST, mChecked, getAppActivity());
//            lightX.writeAppANCEnable(mChecked);

        } else {
//            isAnimationNeeded = true;
//            noiseCancel.setTextColor(Color.BLACK);
            ANCOn();
            mChecked = true;
            JBLprefrences.setBoolean(TOGGLE_PERSIST, mChecked, getAppActivity());
//            lightX.writeAppANCEnable(mChecked);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.awarenessOff:
                awarenessPreset = ANCAwarenessPreset.None;
                awarenessProcess(true);
                break;
            case R.id.awareLow:
                awarenessPreset = ANCAwarenessPreset.Low;

                awarenessProcess(true);
                break;
            case R.id.awareMedium:
                awarenessPreset = ANCAwarenessPreset.Medium;

                awarenessProcess(true);
                break;
            case R.id.awareHigh:
                awarenessPreset = ANCAwarenessPreset.High;

                awarenessProcess(true);
                break;
            default:break;
        }
    }
    
    void awarenessProcess(boolean action){
        lastsavedAwarenessState = awarenessPreset;
        switch (awarenessPreset) {

            case None:
                if (getAppActivity().headphCtrl != null) {
//                    lightX.writeAppANCAwarenessPreset(ANCAwarenessPreset.None);
                    if(action)
                        getAppActivity().headphCtrl.ancCtrl.setANCAwarenessPreset(ANCAwarenessPreset.None);
                    progressLPhone.setProgress(0);
                    progressRPhone.setProgress(0);
                    textLPhone.setText("0");
                    textRPhone.setText("0");
                    awareLow.setAlpha(0.4f);
                    awareMedium.setAlpha(0.4f);
                    awareHigh.setAlpha(0.4f);
                    JBLprefrences.setInt(LEFT_PERSIST, 0, getAppActivity());
                    JBLprefrences.setInt(RIGHT_PERSIST, 0, getAppActivity());
                    JBLprefrences.setInt(AWARENESS, 0, getAppActivity());
                }
                Log.d("Presets", "None sent");
                break;
            case Low:
                if (getAppActivity().headphCtrl != null) {
//                    lightX.writeAppANCAwarenessPreset(ANCAwarenessPreset.Low);
                    if(action)
                        getAppActivity().headphCtrl.ancCtrl.setANCAwarenessPreset(ANCAwarenessPreset.Low);
                    progressLPhone.setProgress(25);
                    progressRPhone.setProgress(25);
                    textLPhone.setText("25%");
                    textRPhone.setText("25%");
                    awareLow.setAlpha(1f);
                    awareMedium.setAlpha(0.4f);
                    awareHigh.setAlpha(0.4f);
                    JBLprefrences.setInt(LEFT_PERSIST, 25, getAppActivity());
                    JBLprefrences.setInt(RIGHT_PERSIST, 25, getAppActivity());
                    JBLprefrences.setInt(AWARENESS, 1, getAppActivity());
                }
                Log.d("Presets", "Low sent");
                break;
            case Medium:
                if (getAppActivity().headphCtrl != null) {
//                    lightX.writeAppANCAwarenessPreset(ANCAwarenessPreset.Medium);
                    if(action)
                        getAppActivity().headphCtrl.ancCtrl.setANCAwarenessPreset(ANCAwarenessPreset.Medium);
                    progressLPhone.setProgress(55);
                    progressRPhone.setProgress(55);
                    textLPhone.setText("55%");
                    textRPhone.setText("55%");
                    awareLow.setAlpha(0.4f);
                    awareMedium.setAlpha(1f);
                    awareHigh.setAlpha(0.4f);
                    JBLprefrences.setInt(LEFT_PERSIST, 55, getAppActivity());
                    JBLprefrences.setInt(RIGHT_PERSIST, 55, getAppActivity());
                    JBLprefrences.setInt(AWARENESS, 2, getAppActivity());
                }
                Log.d("Presets", "Medium sent");
                break;
            case High:
                if (getAppActivity().headphCtrl != null) {
//                    lightX.writeAppANCAwarenessPreset(ANCAwarenessPreset.High);
                    if(action)
                        getAppActivity().headphCtrl.ancCtrl.setANCAwarenessPreset(ANCAwarenessPreset.High);
                    progressLPhone.setProgress(100);
                    progressRPhone.setProgress(100);
                    textLPhone.setText("100%");
                    textRPhone.setText("100%");
                    awareLow.setAlpha(0.4f);
                    awareMedium.setAlpha(0.4f);
                    awareHigh.setAlpha(1f);
                    JBLprefrences.setInt(LEFT_PERSIST, 100, getAppActivity());
                    JBLprefrences.setInt(RIGHT_PERSIST, 100, getAppActivity());
                    JBLprefrences.setInt(AWARENESS, 3, getAppActivity());
                }
                Log.d("Presets", "High sent");
                break;
            case First:
                break;
            case Last:
                break;
            case NumPresets:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(getAppActivity().headphCtrl == null)
            return;
        if(seekBar == progressLPhone){
            lPhoneVal = progress;
            textLPhone.setText(lPhoneVal + "%");
//            ancCtrlMng.setLeftAwarenessPresetValue(lightX, lPhoneVal);
            getAppActivity().headphCtrl.ancCtrl.setLeftAwarenessPresetValue(lPhoneVal);
        }
        else if(seekBar == progressRPhone){
            rPhoneVal = progress;
            textRPhone.setText(rPhoneVal + "%");
//            ancCtrlMng.setLeftAwarenessPresetValue(lightX, rPhoneVal);
            getAppActivity().headphCtrl.ancCtrl.setRightAwarenessPresetValue(rPhoneVal);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void getANCSwitchStateReply(boolean onOff) {
        ANCSwitch.setChecked(onOff);
    }

    @Override
    public void getANCAwarenessPresetReply(ANCAwarenessPreset preset) {
        awarenessPreset = preset;
        awarenessProcess(false);
    }

    @Override
    public void getLeftANCValueReply(long leftVal) {
//        textLPhone.setText(leftVal + "%");
    }

    @Override
    public void getRightANCValueReply(long rightVal) {
//        textLPhone.setText(rightVal + "%");
    }

    @Override
    public void getBatteryLevelReply(long level) {
        if(level == 255) {
            progressBattery.setProgress(100);
            textBattery.setText("charging");
        }
        else {
            progressBattery.setProgress((int)level);
            textBattery.setText(String.valueOf(level) + " %");
        }
    }


    @Override
    public void onHeadPhoneState(boolean isConnect, String headphoneName) {
//        super.onHeadPhoneState(isConnect, headphoneName);
        if(!isConnect) {
            if(bletoothConnStat != null)
                bletoothConnStat.setText("not connected");

        } else {
            if(bletoothConnStat != null)
                bletoothConnStat.setText("connected to " + headphoneName);
            readValues();
            getAppActivity().headphCtrl.setCommonListner(getAppActivity().frgSetting);
            getAppActivity().headphCtrl.setAncListner(getAppActivity().frgAware);
            getAppActivity().headphCtrl.setEqListner(getAppActivity().frgEQ);
            getAppActivity().headphCtrl.setCaliListener(getAppActivity().frgCal);
        }
    }
}
