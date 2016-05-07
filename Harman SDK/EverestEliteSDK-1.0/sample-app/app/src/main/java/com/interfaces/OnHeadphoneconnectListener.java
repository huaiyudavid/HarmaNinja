package com.interfaces;


public interface OnHeadphoneconnectListener {

    /**
     *
     * @param isConnect
     * @param headphoneName pass null if isConnect=false
     */
    public void onHeadPhoneState(boolean isConnect, String headphoneName);
}
