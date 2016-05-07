package com.control;

import android.app.Activity;
import android.os.AsyncTask;

import com.constants.DataKey;
import com.harman.everestelite.Log;
import com.model.EQModel;
import com.storage.JBLprefrences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class InsertPredefinePreset extends AsyncTask<Activity, Void, Void> implements DataKey {


    private static final String TAG = InsertPredefinePreset.class.getSimpleName();

    @Override
    protected Void doInBackground(Activity... params) {
        try {
            boolean isInserted = false;
            String jsonPresets = loadJSONFromAsset(params[0]);
            JSONObject jsonObject = new JSONObject(jsonPresets);
            JSONArray array = jsonObject.getJSONArray("defaults");
            for (int i = 0; i < array.length(); i++) {
                EQSettingManager.OperationStatus status = EQSettingManager.getEQSettingManager(params[0]).addCustomEQ(getModel(array.getJSONObject(i)));
                if (status == EQSettingManager.OperationStatus.INSERTED)
                    isInserted = true;
                else
                    isInserted = false;
                Log.d(InsertPredefinePreset.TAG, String.valueOf(isInserted));
            }
            JBLprefrences.setBoolean(IsAllDefaultInserted, isInserted, params[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public EQModel getModel(JSONObject jsonObject) throws JSONException {
        EQModel eqModel = new EQModel();
        eqModel.setEqName(jsonObject.getString(EQ_NAME));
        jsonObject = jsonObject.getJSONObject("settings");
        eqModel.setHigh1(getFrequency(jsonObject.getString(HIGH1)));
        eqModel.setHigh2(getFrequency(jsonObject.getString(HIGH2)));
        eqModel.setHigh3(getFrequency(jsonObject.getString(HIGH3)));

        eqModel.setMedium1(getFrequency(jsonObject.getString(MEDIUM1)));
        eqModel.setMedium2(getFrequency(jsonObject.getString(MEDIUM2)));
        eqModel.setMedium3(getFrequency(jsonObject.getString(MEDIUM3)));
        eqModel.setMedium4(getFrequency(jsonObject.getString(MEDIUM4)));

        eqModel.setLow1(getFrequency(jsonObject.getString(LOW1)));
        eqModel.setLow2(getFrequency(jsonObject.getString(LOW2)));
        eqModel.setLow3(getFrequency(jsonObject.getString(LOW3)));
        return eqModel;

    }


    public int getFrequency(String str) {
        int i = 0;
        try {
            i = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    public String loadJSONFromAsset(Activity activity) {
        String json = null;
        try {
            InputStream is = activity.getAssets().open("eqDefaults.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
