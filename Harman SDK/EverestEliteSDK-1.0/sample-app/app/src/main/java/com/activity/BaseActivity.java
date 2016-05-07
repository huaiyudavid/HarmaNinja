/*
 * MainActivity.java
 * Smart Digital Headset
 *
 * Created by Tofeeq Ahmad on 6/27/15
 * Copyright (c) 2015 STC
 */
package com.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.constants.DataKey;
import com.control.InsertPredefinePreset;
import com.harman.everestelite.Bluetooth;
import com.harman.everestelite.BluetoothListener;
import com.harman.everestelite.HeadPhoneCtrl;
import com.interfaces.OnHeadphoneconnectListener;
import com.storage.JBLprefrences;
import com.util.AlertsDialog;
import com.util.FirmwareUtil;

//import com.harman.everestelite.Log;


public class BaseActivity extends AppCompatActivity implements
        BluetoothListener
//        LightX.Delegate
//        Log.Delegate
{

    public static final String JBL_HEADSET_MAC_ADDRESS = "com.jbl.headset.mac_address";
    public static final String JBL_HEADSET_NAME = "com.jbl.headset.name";

    public static String DEVICENAME = "DEVICENAME";
    //public LightX mLightX;
    public boolean isConnected = false;
    boolean isNeedtoShowDashboard;
    boolean disconnected;
    // Bluetooth Delegate
    public boolean donSendCallback = true;
    private Bluetooth mBluetooth;
    public BluetoothDevice mBluetoothDevice;
    public HeadPhoneCtrl headphCtrl;
    public OnHeadphoneconnectListener headphoneConnListener;
//    public AppLightXDelegate getAppLightXDelegate() {
//        return appLightXDelegate;
//    }

    // Members and methods to support hardware
    //private AppLightXDelegate appLightXDelegate;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {


            case Bluetooth.REQUEST_ENABLE_BT: {
                if (resultCode == RESULT_CANCELED) {
                    showExitDialog("Unable to enable Bluetooth.");
                } else {
                    mBluetooth.discoverBluetoothDevices();
                }
            }
            break;
        }
    }

//    ConnectionChangeReceiver connectionChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        LightX.sEnablePacketDumps = true;
        //Log.sLogDelegate = this;

        if (!JBLprefrences.getBoolean(DataKey.IsAllDefaultInserted, this)) {
            InsertPredefinePreset insertdefaultValueTask = new InsertPredefinePreset();
            insertdefaultValueTask.executeOnExecutor(InsertPredefinePreset.THREAD_POOL_EXECUTOR, this);
        }
        initializeOResetLibrary();

    }

    /**
     * <p>Initialize or reset library. Its used when app start or headphone timeout a command from app<p/>
     */
    private void initializeOResetLibrary() {
//        if (mLightX != null)
//            mLightX.close();
        if (mBluetooth != null)
            mBluetooth.close();
        mBluetoothDevice = null;
        try {
            Log.e("Selection screen", "LightXB as activity oncreate starts");
            mBluetooth = new Bluetooth(this, this, true);
            mBluetooth.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Members and methods to support hardware

    public synchronized void connect(BluetoothDevice bluetoothDevice) {
        try {
            if (mBluetooth == null || bluetoothDevice == null) {
                Log.d(TAG, "Connection and device both are null");
                return;
            }
            if (isConnected())
                return;
            if (bluetoothDevice != null && bluetoothDevice.getName().toUpperCase().contains("JBL Everest".toUpperCase()) && mBluetoothDevice != null && !bluetoothDevice.getAddress().equalsIgnoreCase(mBluetoothDevice.getAddress()))
                mBluetoothDevice = null; // New device find so initiate contact again.
            if (isConnectedLogically()) return;
            if (!shouldConnectToBluetoothDevice(bluetoothDevice)) return;
//        mBluetooth.cancelDiscovery();

            Log.e(TAG,"Pairing Failed " + bluetoothDevice.getName() + " " + bluetoothDevice.getBondState());
            if (bluetoothDevice.getBondState() != BluetoothDevice.BOND_BONDED && bluetoothDevice.getBondState() != BluetoothDevice.BOND_BONDING) {
                bluetoothDevice.createBond();
                mBluetooth.pair(bluetoothDevice);
                return;
            }

            try {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, bluetoothDevice.getName() + " connecting...");
                mBluetooth.connect(bluetoothDevice);
                mBluetoothDevice = bluetoothDevice;
            } catch (Exception e) {
                Log.e(TAG, mBluetooth.deviceName(bluetoothDevice) + " connect() failed: " + e.getLocalizedMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean mIsConnectedPhysically;

    // advisory
    public synchronized boolean isConnected() {
        return mIsConnectedPhysically;
    }

    public synchronized boolean isConnectedLogically() {
        return mBluetoothDevice != null;

    }

    // Utilities

    public String getMACAddressOfSavedJBLHeadset() {
        return JBLprefrences.getString(JBL_HEADSET_MAC_ADDRESS, this, null);
    }

    public String getNameOfSavedJBLHeadset() {
        return JBLprefrences.getString(JBL_HEADSET_NAME, this, null);
    }

    protected void saveJBLHeadsetInfo(String name, String macAddress) {
        JBLprefrences.setString(JBL_HEADSET_MAC_ADDRESS, macAddress, this);
        JBLprefrences.setString(JBL_HEADSET_NAME, name, this);    // advisory
    }

    // Members and methods to support hardware

    private synchronized boolean shouldConnectToBluetoothDevice(BluetoothDevice bluetoothDevice) {
        String deviceMACAddress;
        String deviceName;
        String macAddressOfSavedJBLHeadset;
        boolean result = false;

        if (bluetoothDevice != null) {
            deviceMACAddress = bluetoothDevice.getAddress().toUpperCase();
            deviceName = bluetoothDevice.getName();

            if ((macAddressOfSavedJBLHeadset = getMACAddressOfSavedJBLHeadset()) == null) {
                if (deviceName.toUpperCase().contains("JBL Everest".toUpperCase())) {
                    saveJBLHeadsetInfo(deviceName, deviceMACAddress);
                    result = true;
                }
            } else if (deviceMACAddress.equals(macAddressOfSavedJBLHeadset.toUpperCase())) {
                result = true;
            } else if (deviceName.toUpperCase().contains("JBL Everest".toUpperCase())) {
                saveJBLHeadsetInfo(deviceName, deviceMACAddress);
                result = true;
            }
        }

        return result;
    }

    public synchronized void disconnect() {
        if (isConnectedLogically()) {
            Log.d(TAG, "Closing logical connection to " + mBluetoothDevice.getName());
        }

        mIsConnectedPhysically = false;

        if (mBluetoothDevice != null) {
            try {
                if (mBluetooth != null)
                    mBluetooth.disconnect(mBluetoothDevice);
            } catch (Exception e) {
            }
        }

        mBluetoothDevice = null;
    }

    public void showExitDialog(String message) {
        AlertsDialog.bluetoothAlertFinish(null, message, this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        donSendCallback = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (android.os.Build.MANUFACTURER.toLowerCase().contains("samsun")) {
            donSendCallback = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        donSendCallback = true;
    }

    @Override
    public void bluetoothAdapterChangedState(Bluetooth bluetooth, int currentState, int previousState) {
        if (currentState != BluetoothAdapter.STATE_ON) {
            Log.e(TAG, "The Bluetooth adapter is not enabled, cannot communicate with LightX device");
            // Could ask the user if it's ok to call bluetooth.enableBluetoothAdapter() here, otherwise abort
        }
    }

    @Override
    public void bluetoothDeviceBondStateChanged(Bluetooth bluetooth, BluetoothDevice bluetoothDevice, int currentState, int previousState) {
        //no need connect here
        //connect(bluetoothDevice);

    }

    String TAG = BaseActivity.class.getSimpleName();

    @Override
    public void bluetoothDeviceConnected(Bluetooth bluetooth, final BluetoothDevice bluetoothDevice, BluetoothSocket bluetoothSocket) {
        Log.e(TAG, "Connected");

        synchronized (this) {
//            showCommunicationIssue = true;
            handler.removeCallbacks(runnable);
            handlerDelayToast.removeCallbacks(runnableToast);
            try {
                FirmwareUtil.JBL_RSRCversion = bluetoothDevice.getAddress();
            } catch (Exception e) {
                FirmwareUtil.JBL_RSRCversion = "JBL_RSRCversion";
            }
            JBLprefrences.setString(DEVICENAME, bluetoothDevice.getName(), BaseActivity.this);
            if (headphCtrl != null && headphCtrl.getSocket().equals(bluetoothSocket)) {
                Log.d(TAG, "bluetoothDeviceConnected() received for extant LightX/socket pair.  Ignoring.");
                headphCtrl.resetHeadPhoneCtrl(bluetoothSocket);
            } else {
                try {
                    headphCtrl.close();
                    headphCtrl = null;
                } catch (Exception e) {
                }
                headphCtrl = HeadPhoneCtrl.getInstance(this, bluetoothSocket);
            }
            mIsConnectedPhysically = true;
        }

        isConnected = true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (headphoneConnListener != null)
                    headphoneConnListener.onHeadPhoneState(true, bluetoothDevice.getName());
            }
        });
        resetTime = 10 * 1000;
    }

    @Override
    public void bluetoothDeviceDisconnected(Bluetooth bluetooth, BluetoothDevice bluetoothDevice) {
        synchronized (this) {
            mIsConnectedPhysically = false;
        }
        headphCtrl = null;//This line is important, or if bluetootch disconnect, there may be null pointer error.
        isConnected = false;
        disconnected = true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(headphoneConnListener != null)
                    headphoneConnListener.onHeadPhoneState(false, null);
            }
        });

        Log.e(TAG, "Dissconnected");
//        if (Calibration.getCalibration() != null) {
//            Calibration.getCalibration().finish();
//        }
        Log.e(TAG, "Resetdisconnect " + resetTime);
        if (resetTime == 10 * 1000)
            handlerDelayToast.postDelayed(runnableToast, 5 * 1000);
        --resetTime;
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, resetTime);
    }

    int resetTime = 10 * 1000;
    Handler handlerDelayToast = new Handler();
    Runnable runnableToast = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertsDialog.showToast(BaseActivity.this, getString(R.string.taking_longer_time));
                }
            });
        }
    };
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isConnected) {
                Log.e(TAG, "Resetdisconnect ");
                initializeOResetLibrary();

            }
        }
    };

    // LightX App Delegate
    @Override
    public void bluetoothDeviceDiscovered(Bluetooth bluetooth, BluetoothDevice bluetoothDevice) {
        try {
            if (bluetoothDevice != null && bluetoothDevice.getName().toUpperCase().contains("JBL Everest".toUpperCase()))
                connect(bluetoothDevice);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bluetoothDeviceFailedToConnect(Bluetooth bluetooth, BluetoothDevice bluetoothDevice, Exception e) {
        String name = bluetooth.deviceName(bluetoothDevice);
        Log.e(TAG, "ACL Events");
        Log.d(TAG, name + " failed to connect, waiting passively: " + e.getLocalizedMessage());
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * Destroy all single instance object;
         */
//        EQSettingManager eqSettingManager = EQSettingManager.getEQSettingManager(this);
//        if (eqSettingManager != null)
//            eqSettingManager = null;
//        try {
//            DeviceManager deviceManager = DeviceManager.getManager(MainActivity.getMainActivity());
//            if (deviceManager != null)
//                deviceManager = null;
//        } catch (Exception e) {
//            Log.e(BaseActivity.class.getSimpleName(), e.getMessage());
//        }
//
//        try {
//            ANControlManager anCcontrolManager = ANControlManager.getANCManager(this);
//            if (anCcontrolManager != null)
//                anCcontrolManager = null;
//        } catch (Exception e) {
//            Log.e(BaseActivity.class.getSimpleName(), e.getMessage());
//        }
        handler.removeCallbacks(runnable);
        Log.e("Selection screen", "LightXB destroy");
//        if (mLightX != null) mLightX.close();
        if (mBluetooth != null) mBluetooth.close();
        try {
            Process.killProcess(Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public void setAppLightXDelegate(AppLightXDelegate appLightXDelegate) {
//        this.appLightXDelegate = appLightXDelegate;
//    }

//    public LightX getLightX() {
//        return mLightX;
//    }
}

