package com.util;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;


public class FirmwareUtil {
    public static String JBL_RSRCversion = "JBL_RSRCversion";
    public static AtomicBoolean isUpdatingFirmWare = new AtomicBoolean();

    static {
        isUpdatingFirmWare.set(false);
    }

    /**
     * @param rawResourceId
     * @param resources
     * @return
     * @deprecated
     */
    public static byte[] readRawResource(int rawResourceId, Resources resources) {
        byte[] buffer;
        InputStream inputStream;
        int read;
        ByteArrayOutputStream outputStream;

        buffer = new byte[4096];
        inputStream = resources.openRawResource(rawResourceId);
        outputStream = new ByteArrayOutputStream();

        try {
            for (; ; ) {
                if ((read = inputStream.read(buffer)) < 0) break;

                outputStream.write(buffer, 0, read);
            }

            return outputStream.toByteArray();
        } catch (IOException e) {
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
            }
        }

        return null;
    }

    /**
     * <p>Read data from Input stream</p>
     *
     * @param inputStream
     * @return
     */
    public static byte[] readInputStream(InputStream inputStream) {
        byte[] buffer;
        int read;
        ByteArrayOutputStream outputStream;

        buffer = new byte[4096];
        outputStream = new ByteArrayOutputStream();
        try {
            for (; ; ) {
                if ((read = inputStream.read(buffer)) < 0) break;

                outputStream.write(buffer, 0, read);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * <p>Checks whether firmware update is available by comparing the liveVersion with currentVersion</p>
     *
     * @param liveVersion
     * @param currentVersion
     * @return True is update is available else FALSE
     */
    public static boolean isUpdateAvailable(String liveVersion, String currentVersion) {
        try {
            if (Integer.parseInt(String.valueOf(liveVersion.charAt(0))) > Integer
                    .parseInt(String.valueOf(currentVersion.charAt(0)))) {
                return true;
            } else if (Integer.parseInt(String.valueOf(liveVersion.charAt(0))) == Integer
                    .parseInt(String.valueOf(currentVersion.charAt(0)))) {
                // Checking for second index
                if (Integer.parseInt(String.valueOf(liveVersion.charAt(2))) > Integer
                        .parseInt(String.valueOf(currentVersion.charAt(2)))) {
                    return true;
                } else if (Integer.parseInt(String.valueOf(liveVersion
                        .charAt(2))) == Integer.parseInt(String
                        .valueOf(currentVersion.charAt(2)))) {
                    // Checking for third Index
                    if (Integer.parseInt(String.valueOf(liveVersion.charAt(4))) > Integer
                            .parseInt(String.valueOf(currentVersion.charAt(4)))) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }

            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * <p>Checks if internet conneciton is available.</p>
     *
     * @param context
     * @return TRUE is available else FALSE
     */
    public static boolean isConnectionAvailable(Context context) {

        ConnectivityManager connMgr = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    /**
     * @return
     * @deprecated
     */
    public static String getURL() {
        return "";
    }
}
