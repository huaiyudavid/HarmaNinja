package com.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.activity.MainActivity;


public class BaseFragment extends Fragment
//        implements OnHeadphoneconnectListener
{


    public static final String RECEIVEPUSH = "RECEIVEPUSH";
//    HeadPhoneCtrl headphCtrl;
    public boolean bluetoothConnect = false;
    public String headPhoneName;
//    LightX lightX;
//    TextView eqText, txtPlain;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getAppActivity().isConnected) {
            Log.e("Home", "Not Connected");
        } else {
            Log.e("Home", "Connected");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        eqText = getAppActiviy().eQText;
//        txtPlain = getAppActiviy().txtPlain;
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    public MainActivity getAppActivity() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity == null)
            throw new NullPointerException();
        else
            return (MainActivity) getActivity();
    }

//    @Override
//    public void onHeadPhoneState(boolean isConnect, String headphoneName) {
//        bluetoothConnect = isConnect;
//        headPhoneName = headphoneName;
//        if(isConnect)
//        {
//            if(getAppActivity().headphCtrl != null) {
//                getAppActivity().headphCtrl.getBootImageType();
//                getAppActivity().headphCtrl.setCommonListner(getAppActivity().frgSetting);
//                getAppActivity().headphCtrl.setAncListner(getAppActivity().frgAware);
//                getAppActivity().headphCtrl.setEqListner(getAppActivity().frgEQ);
//
//            }
//        }
//        else
//            ;
//    }
}
