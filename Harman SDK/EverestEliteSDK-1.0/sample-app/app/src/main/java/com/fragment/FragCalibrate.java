package com.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activity.R;
import com.harman.everestelite.CalibrateListener;

/**
 * Created by root on 16-3-25.
 */
public class FragCalibrate extends BaseFragment implements View.OnClickListener,
        CalibrateListener
{
    public static final String TAG = FragCalibrate.class.getSimpleName();
    TextView txtConnectMessage, txthelp, txtChangeMessage, txtCalibrating, txtExtraHelp;
    ProgressBar progressBar;
    int timing = 10 * 1000;
    private View informationLayout;
    boolean isCalibrationComplete;
    boolean isCalibrationStarted;
    int retryWaitforCalibration = 0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.calibrationglobal, container, false);
        txtConnectMessage = (TextView) view.findViewById(R.id.txtConnectMessage);
        txtConnectMessage.setText(Html.fromHtml(getResources().getString(R.string.personalizemessgae)));

        txthelp = (TextView) view.findViewById(R.id.txthelp);
        txtChangeMessage = (TextView) view.findViewById(R.id.txtChangeMessage);
        txtCalibrating = (TextView) view.findViewById(R.id.txtCalibrating);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        txtExtraHelp = (TextView) view.findViewById(R.id.txtExtraHelp);
        informationLayout = view.findViewById(R.id.informationLayout);
        informationLayout.setOnClickListener(this);
//        getActivity().findViewById(R.id.txtCancel).setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onResume(){
        super.onResume();
//        getAppActivity().headphoneConnListener = this;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.informationLayout)
            prepareCalibration();
        informationLayout.setOnClickListener(null);
    }


    String getStrings(int n) {
        return getActivity().getString(n);
    }

    public void prepareCalibration() {
        txtCalibrating.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        txtChangeMessage.setText("");
        startCalibration();
    }

    public void startCalibration() {
        retryWaitforCalibration = 0;
        isCalibrationStarted = true;
        isCalibrationComplete = false;
        if(getAppActivity().headphCtrl != null)
            getAppActivity().headphCtrl.calCtrl.startCalibration();
        handler.postDelayed(runnable, 5000);
    }

    @Override
    public void calibrationCompleteReply(boolean success) {
        if(success)
            setIsCalibrationComplete(success);
        else
            calibrationFailed();
    }
    /**
     * Calibration start screen will be visible.
     */
    public void calibrationComplete() {
        txtConnectMessage.setVisibility(View.INVISIBLE);
//        txtChangeMessage.setBackgroundResource(R.drawable.ic_donetick);
        progressBar.setVisibility(View.INVISIBLE);
        txthelp.setVisibility(View.VISIBLE);
        txtExtraHelp.setVisibility(View.INVISIBLE);
        txtCalibrating.setVisibility(View.INVISIBLE);
        txtChangeMessage.setText("OK");
        txthelp.setText(Html.fromHtml(getStrings(R.string.autoComplete)));

        txthelp.postDelayed(new Runnable() {
            @Override
            public void run() {
//                getActivity().finish();
                txtConnectMessage.setVisibility(View.VISIBLE);
                txtConnectMessage.setText(R.string.personalizemessgae);
                txthelp.setText(R.string.help);
                txtChangeMessage.setText(R.string.start);
                informationLayout.setOnClickListener(FragCalibrate.this);
            }
        }, 2000);
    }

    /**
     * Calibration failed screen will be visible.
     */
    public void calibrationFailed() {
        txtCalibrating.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        txtChangeMessage.setText(getStrings(R.string.start));
        txtCalibrating.setText(getStrings(R.string.failcalibration));
//        txtCalibrating.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_alert), null, null, null);
        txtCalibrating.setCompoundDrawablePadding(5);
//        getActivity().findViewById(R.id.txtCancel).setVisibility(View.VISIBLE);
        informationLayout.setOnClickListener(this);
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isCalibrationComplete) {
                handler.removeCallbacks(runnable);
                calibrationComplete();
            } else if (retryWaitforCalibration <= 1) {
                //** Wait another few second until calibration complete **//
                ++retryWaitforCalibration;
                if(getAppActivity().headphCtrl != null)
                    getAppActivity().headphCtrl.calCtrl.getCalibrationStatus();
                handler.postDelayed(runnable, 5000);
            } else {
                // Calibration failed
                calibrationFailed();
            }
        }
    };


    Handler handler = new Handler();
    public void setIsCalibrationComplete(boolean isCalibrationComplete) {
        this.isCalibrationComplete = isCalibrationComplete;
        handler.removeCallbacks(runnable);
        if (isCalibrationComplete) {
            calibrationComplete();
        } else {
            calibrationFailed();
        }
    }
}
