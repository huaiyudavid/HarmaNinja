package com.util;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.activity.R;

public class SettingHomeUtils {
    /**
     * <p>Utility method to adjust the UI on AwarenesHome screen</p>
     *
     * @param imageView
     * @param activity
     * @param outerLayout
     * @param batteryTxt
     * @param mbar
     * @param transparentShape
     */
    public static void setDimensionsIfDeviceSmal(ImageView imageView, Activity activity, FrameLayout outerLayout, TextView batteryTxt, SeekBar mbar, ImageView transparentShape) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        if (dm.widthPixels <= 720) {
//            imageView.setImageResource(R.drawable.ic_headphonew_low);
//
//            int height = (int) activity.getResources().getDimension(R.dimen.circleheight);
//            LinearLayout.LayoutParams layoutParamsOuter = (LinearLayout.LayoutParams) outerLayout.getLayoutParams();
//            layoutParamsOuter.width = height - dm.heightPixels / 40;
//            layoutParamsOuter.height = height - dm.heightPixels / 40;
//            outerLayout.setLayoutParams(layoutParamsOuter);
//            FrameLayout.LayoutParams seeklayoutParams = (FrameLayout.LayoutParams) mbar.getLayoutParams();
//            int marginBottom = (int) activity.getResources().getDimension(R.dimen.sbMarginBottom) + 5;
//            int marginLeft = (int) activity.getResources().getDimension(R.dimen.sbMarginLeft) - 10;
//            int marginRight = (int) activity.getResources().getDimension(R.dimen.sbMarginRight) - 10;
//            seeklayoutParams.setMargins(marginLeft, 0, marginRight, marginBottom);
//            mbar.setLayoutParams(seeklayoutParams);
//            GradientDrawable shapeDrawable = (GradientDrawable) transparentShape.getBackground();
//            int strokewidth = (int) activity.getResources().getDimension(R.dimen.transparentStrokeWidth);
//            shapeDrawable.setStroke(strokewidth, Color.WHITE);
//            batteryTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, batteryTxt.getTextSize() - 3);
//            transparentShape.setBackground(shapeDrawable);
        } else if (dm.heightPixels < 2560 && dm.heightPixels > 2400) {
            imageView.setImageResource(R.drawable.ic_headphonew_low);

            int height = (int) activity.getResources().getDimension(R.dimen.circleheight);
            LinearLayout.LayoutParams layoutParamsOuter = (LinearLayout.LayoutParams) outerLayout.getLayoutParams();
            layoutParamsOuter.width = height - 50;
            layoutParamsOuter.height = height - 50;
            outerLayout.setLayoutParams(layoutParamsOuter);
            FrameLayout.LayoutParams seeklayoutParams = (FrameLayout.LayoutParams) mbar.getLayoutParams();
            int marginBottom = (int) activity.getResources().getDimension(R.dimen.sbMarginBottom) - 10;
            int marginLeft = (int) activity.getResources().getDimension(R.dimen.sbMarginLeft) - 10;
            int marginRight = (int) activity.getResources().getDimension(R.dimen.sbMarginRight) - 10;
            seeklayoutParams.setMargins(marginLeft, 0, marginRight, marginBottom);
            mbar.setLayoutParams(seeklayoutParams);
            GradientDrawable shapeDrawable = (GradientDrawable) transparentShape.getBackground();
            int strokewidth = (int) activity.getResources().getDimension(R.dimen.transparentStrokeWidth) + 5;
            shapeDrawable.setStroke(strokewidth, Color.WHITE);
            transparentShape.setBackground(shapeDrawable);
        }
    }
}
