package com.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.R;


public class AlertsDialog {

    public static Toast toast = null;

    /**
     * <p>Displays alert with the message</p>
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        if (AlertsDialog.toast != null) {
            AlertsDialog.toast.cancel();
        }
        AlertsDialog.toast = new Toast(context);
        AlertsDialog.toast.setDuration(Toast.LENGTH_SHORT);
        View view = LayoutInflater.from(context).inflate(R.layout.toast, null);
        TextView txtView = (TextView) view.findViewById(R.id.title);
        txtView.setText(message);
        AlertsDialog.toast.setView(view);
        AlertsDialog.toast.show();
    }

    /**
     * <p>Displays alert with title,message and OK button</p>
     *
     * @param title
     * @param message
     * @param context
     */
    public static void showSimpleDialogWithOKButton(String title, String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }

        builder.setPositiveButton(context.getString(R.string.OK), null);
        builder.create().show();
    }

    /**
     * <p>Displays alert with title,message and OK button</p>
     *
     * @param title
     * @param message
     * @param context
     */
    public static void showSimpleDialogWithOKButtonWithBack(String title, String message, final AppCompatActivity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
            builder.setIcon(R.mipmap.ic_launcher);
        }

        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }

        builder.setPositiveButton(context.getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.onBackPressed();
            }
        });
        builder.create().show();
    }

    /**
     * <p>Displays alert with title,message and OK button</p>
     *
     * @param title
     * @param message
     * @param context
     */
    public static void showSimpleDialogWithOKButtonWithExit(String title, String message, final AppCompatActivity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
            builder.setIcon(R.mipmap.ic_launcher);
        }

        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }

        builder.setPositiveButton(context.getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                context.finish();
            }
        });
        builder.setNegativeButton(context.getString(R.string.cancel), null);
        builder.create().show();
    }

    /**
     * @param title
     * @param message
     * @param context
     * @deprecated
     */
    public static void bluetoothAlert(String title, String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (TextUtils.isEmpty(title)) {
            builder.setTitle(title);
            builder.setIcon(R.mipmap.ic_launcher);
        }

        if (TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }

        builder.setPositiveButton("Settings", null);
    }

    /**
     * <p>Displays alert with EXIT button</p>
     *
     * @param title
     * @param message
     * @param context
     */

    public static void bluetoothAlertFinish(String title, String message, final Activity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (TextUtils.isEmpty(title)) {
            builder.setTitle(title);
            builder.setIcon(R.mipmap.ic_launcher);
        }

        if (TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }

        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.finish();
            }
        });
    }
}
