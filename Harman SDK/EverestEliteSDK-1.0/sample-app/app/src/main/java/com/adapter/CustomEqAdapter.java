package com.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.activity.R;
import com.constants.JBLConstants;
import com.fragment.FragEQSetting;
import com.model.EQModel;
import com.storage.JBLprefrences;

import java.util.ArrayList;


public class CustomEqAdapter extends BaseAdapter {


    private static final int NUMBER_LAYOUTS = 2;
    public static String HEADER_TAG = "HEADER_TAG";
    public static int selectedPosition;
    public ArrayList<EQModel> eQNameList = new ArrayList<>();
    public ArrayList<Boolean> toggleDeleteList = new ArrayList<>();
    public ArrayList<Boolean> frameAnimation = new ArrayList<>();
    LayoutInflater inflater;
    FragEQSetting eqSettings;

    /**
     * Constructor
     *
     * @param eqSettings
     * @param eQNameList
     * @param inflater
     */
    public CustomEqAdapter(FragEQSetting eqSettings, ArrayList<EQModel> eQNameList, LayoutInflater inflater) {
        this.eQNameList = eQNameList;
        if (this.eQNameList.size() > 4)
            this.eQNameList.add(4, new EQModel());
        for (EQModel str : eQNameList) {
            frameAnimation.add(false);
            toggleDeleteList.add(false);
        }
        this.eqSettings = eqSettings;
        this.inflater = inflater;

    }

    public void refreshList(ArrayList<EQModel> eQNameList){
        this.eQNameList = eQNameList;
        if (this.eQNameList.size() > 4)
            this.eQNameList.add(4, new EQModel());
        for (EQModel str : eQNameList) {
            frameAnimation.add(false);
            toggleDeleteList.add(false);
        }
        frameAnimation.set(selectedPosition, true);
        notifyDataSetChanged();
    }

    /**
     * <p>Toogles the animation based on the name</p>
     *
     * @param cusomEQName
     */
    public void toggleAnimation(String cusomEQName) {

        if (cusomEQName == null) {
            cusomEQName = "Off";
        }
        for (EQModel model : eQNameList) {
            try {
                if (model.getEqName().equals(cusomEQName)) {
                    frameAnimation.set(eQNameList.indexOf(model), true);
                    selectedPosition = eQNameList.indexOf(model);
                    JBLprefrences.setInt("PRESETPOSITION", eQNameList.indexOf(model), eqSettings.getActivity());
                    //  Log.d("demo","---------ADAPTER SEL-----"+eQNameList.indexOf(model));
                } else {
                    frameAnimation.set(eQNameList.indexOf(model), false);
                }

            } catch (NullPointerException e) {
                // Will get exception for the custom header in the list.
            }

        }
        notifyDataSetChanged();
    }

    /**
     * @param eQNameList
     * @deprecated
     */
    public void seteQNameList(ArrayList<EQModel> eQNameList) {
        this.eQNameList.addAll(eQNameList);
    }

    @Override
    public int getCount() {
        return eQNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 4)
            return 1;
        return 0;

    }

    @Override
    public int getViewTypeCount() {
        return NUMBER_LAYOUTS;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        int layoutType = 0;

        if (position == 4)
            layoutType = getItemViewType(position);
        if (convertView == null) {
            vh = new ViewHolder();
            if (layoutType == 1) {
                convertView = inflater.inflate(R.layout.header_item, parent, false);
                convertView.setTag(null);
            } else {

                convertView = inflater.inflate(R.layout.eq_custom_list, parent, false);
                vh.txtView = (TextView) convertView.findViewById(R.id.eqCustomText);
                vh.image_option = (ImageView) convertView.findViewById(R.id.image_option);
                vh.animationImage = (ImageView) convertView.findViewById(R.id.animationImage);
                vh.txtDelete = (TextView) convertView.findViewById(R.id.txtDelete);
                convertView.setTag(vh);
            }//child.xml

        } else {
            vh = (ViewHolder) convertView.getTag();

        }


        if (layoutType == 0) {

            vh.animationImage.setVisibility(frameAnimation.get(position) ? View.VISIBLE : View.INVISIBLE);
            vh.image_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eqSettings.onClick(v);
                }
            });

            if (position == 0) {
                vh.image_option.setVisibility(View.GONE);
                vh.txtDelete.setVisibility(View.GONE);
            } else {
                vh.txtDelete.setVisibility(toggleDeleteList.get(position) ? View.VISIBLE : View.GONE);
            }
            vh.txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eqSettings.onClick(v);
                }
            });
            vh.txtView.setText(eQNameList.get(position).getEqName());
            if (frameAnimation.get(position) && vh.txtView.getText().length() >= JBLConstants.MAX_MARQUEE_LEN) {
                vh.txtView.setSelected(true);
                vh.txtView.setMarqueeRepeatLimit(-1);
            } else
                vh.txtView.setSelected(false);
            vh.image_option.setTag(position);
            vh.txtView.setTag(position);
            vh.txtDelete.setTag(position);
            return convertView;
        }
        return convertView;
    }

    private class ViewHolder {
        TextView txtView, txtDelete;
        ImageView image_option,animationImage;
    }
}
