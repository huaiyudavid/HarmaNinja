package com.storage;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;


public class JBLprefrences {
    /**
     * <p> Saves string to the preference with provided key and value</p>
     *
     * @param key
     * @param value
     * @param context
     */
    public static void setString(String key, String value, Context context) {
        try {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).commit();
        } catch (Exception e) {
            Log.e("JBLprefrences", "Failed");
        }
    }

    /**
     * <p> Saves boolean to the preference with provided key and value</p>
     *
     * @param key
     * @param value
     * @param context
     */
    public static void setBoolean(String key, boolean value, Context context) {
        try {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key, value).commit();
        } catch (Exception e) {
            Log.e("JBLprefrences", "Failed");
        }
    }

    /**
     * <p> Saves int to the preference with provided key and value</p>
     *
     * @param key
     * @param value
     * @param context
     */
    public static void setInt(String key, int value, Context context) {
        try {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key, value).commit();
        } catch (Exception e) {
            Log.e("JBLprefrences", "Failed");
        }
    }

    /**
     * <p> Returns int for provided key</p>
     *
     * @param key
     * @param context
     * @return int
     */
    public static int getInt(String key, Context context) {
        try {
            return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, 0);
        } catch (Exception e) {
            Log.e("JBLprefrences", "Failed");
        }
        return 0;
    }

    /**
     * <p> Returns booelan for provided key</p>
     *
     * @param key
     * @param context
     * @return boolean
     */
    public static boolean getBoolean(String key, Context context) {
        try {
            return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, false);
        } catch (Exception e) {
            Log.e("JBLprefrences", "Failed");
        }
        return false;
    }

    /**
     * <p> Returns String for provided key</p>
     *
     * @param key
     * @param context
     * @param defaultValue
     * @return String
     */
    public static String getString(String key, Context context, String defaultValue) {
        try {
            return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultValue);
        } catch (Exception e) {
            Log.e("JBLprefrences", "Failed");
        }
        return defaultValue;
    }

}
