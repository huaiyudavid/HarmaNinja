package com.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.activity.MainActivity;
import com.activity.R;
import com.adapter.CustomEqAdapter;
import com.control.EQSettingManager;
import com.harman.everestelite.EQSettingListner;
import com.harman.everestelite.GraphicEQPreset;
import com.model.EQModel;
import com.storage.JBLDatabase;
import com.storage.JBLprefrences;
import com.util.AlertsDialog;
import com.util.Utility;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragEQSetting extends BaseFragment
        implements View.OnClickListener, AdapterView.OnItemClickListener,
        EQSettingListner {
    public static final String TAG = FragAwareness.class.getSimpleName();
    private final int kSizeofUInt32 = 4;
    ListView swpLv;
    EQSettingManager eqSettingManager;
    CustomEqAdapter customEqAdapter;
    int mGraphicEQLimitNumBands, mGraphicEQLimitNumSettings;
    boolean edit_view;
    MainActivity mainActivity;
    ArrayList<EQModel> eQNameList;
    int mPosition;

    public FragEQSetting() {
    }

    public void onResume(){
        super.onResume();
    }

    public void refreshEQList(){
//        eQNameList = EQSettingManager.getEQSettingManager(getContext()).getCompleteEQList();
//        customEqAdapter.refreshList(eQNameList);
        eQNameList = EQSettingManager.getEQSettingManager(getContext()).getCompleteEQList();
        customEqAdapter = new CustomEqAdapter(this, eQNameList, LayoutInflater.from(getActivity()));
        swpLv.setAdapter(customEqAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            edit_view = savedInstanceState.getBoolean("edit_true");
        eqSettingManager = EQSettingManager.getEQSettingManager(this.getContext());
        mainActivity = getAppActivity();
//        lightX = getAppActiviy().mLightX;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_eqsetting, container, false);
        swpLv = (ListView) view.findViewById(R.id.swipeLv);
        //setListViewOperation(swipeMenuListView);
        eQNameList = EQSettingManager.getEQSettingManager(getContext()).getCompleteEQList();
        customEqAdapter = new CustomEqAdapter(this, eQNameList, LayoutInflater.from(getActivity()));
        //Utility.setSwipeListViewListener(swipeMenuListView, customEqAdapter);
        swpLv.setOnItemClickListener(this);
        swpLv.setAdapter(customEqAdapter);

        view.findViewById(R.id.text_create_new).setOnClickListener(this);
        view.findViewById(R.id.image_option).setOnClickListener(this);
        return view;
    }



    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch(v.getId()){
            case R.id.text_create_new:
                bundle.putBoolean("showButton", false);
                bundle.putBoolean("isCreatingNew", true);
                mainActivity.frgEQCstm = new FragEQCustom();
                mainActivity.frgEQCstm.setArguments(bundle);
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, mainActivity.frgEQCstm)
                        .show(getAppActivity().frgEQCstm)
                        .commit();
//            try {
//                getAppActiviy().applyFragment(CustomEqFragment.TAG, bundle);
//            } catch (Exception e) {
//                e.getMessage();
//            }
                break;
            case R.id.eqCustomText:
            case R.id.image_option:

                // Pass selected EQ setting value to CustomEqFragment

                try {
                    int i = Integer.parseInt(v.getTag().toString());
                    EQModel model = eQNameList.get(i);

                    bundle.putSerializable(EQModel.class.getSimpleName(), model);
                    if (JBLDatabase.predefinepresets.contains(model.getEqName())) {
                        bundle.putBoolean("showButton", false);
                        bundle.putBoolean("preset", JBLDatabase.predefinepresets.contains(model.getEqName()));
                    } else {
                        bundle.putBoolean("showButton", true);
                        bundle.putBoolean("Update_only", true);
                        bundle.putString("update_key", model.getEqName());
                    }
                    mainActivity.frgEQCstm = new FragEQCustom();
                    mainActivity.frgEQCstm.setArguments(bundle);
                    mainActivity.getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, mainActivity.frgEQCstm)
                            .show(getAppActivity().frgEQCstm)
                            .commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.txtDelete:

                // Delete Custom EQ Setting

                try {
                    int i = Integer.parseInt(v.getTag().toString());
                    if (eQNameList.get(i).getEqName().equals(JBLprefrences.getString(EQSettingManager.EQKeyNAME, getActivity(), null))) {
                        JBLprefrences.setString(EQSettingManager.EQKeyNAME, "Off", getActivity());
//                        eqSettingManager.applyPresetWithoutBand(GraphicEQPreset.Off, lightX);
                    }
                    EQSettingManager.OperationStatus operationStatus = EQSettingManager.getEQSettingManager(mainActivity).deleteEQ(eQNameList.get(i).getEqName());
                    if (operationStatus == EQSettingManager.OperationStatus.DELETED) {
                        customEqAdapter.toggleDeleteList.remove(i);
                        customEqAdapter.eQNameList.remove(i);
                        customEqAdapter.notifyDataSetChanged();
                        customEqAdapter.toggleAnimation(JBLprefrences.getString(EQSettingManager.EQKeyNAME, getActivity(), null));
                        Utility.setListViewHeightBasedOnChildren(swpLv);
                    } else {
                        AlertsDialog.showToast(mainActivity, getString(R.string.faildeleting));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:

                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            if (position < 0 || position == 4)
                return;
            if (id != -1) {
                Log.d("Clicked", String.valueOf(position));
                mPosition = position;
                JBLprefrences.setString(EQSettingManager.EQKeyNAME, eQNameList.get(position).getEqName(), getActivity());
                eqPresetHandler.removeCallbacks(runnable);
                eqPresetHandler.postDelayed(runnable, 300);
                try {
                    customEqAdapter.toggleAnimation(JBLprefrences.getString(EQSettingManager.EQKeyNAME, getActivity(), null));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        Handler eqPresetHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                switch (mPosition) {
                    case 0:
                        JBLprefrences.setString(EQSettingManager.EQKeyNAME, eQNameList.get(mPosition).getEqName(), getActivity());
//                        eqSettingManager.applyPresetsWithBand(GraphicEQPreset.Off, eqSettingManager.getBandFromEQModel(eQNameList.get(mPosition)), lightX);
                        if(getAppActivity().headphCtrl != null)
                            getAppActivity().headphCtrl.eqCtrl.applyPresetWithBand(GraphicEQPreset.Off, eqSettingManager.getBandFromEQModel(eQNameList.get(mPosition)));
                        break;
                    case 1:
                        JBLprefrences.setString(EQSettingManager.EQKeyNAME, eQNameList.get(mPosition).getEqName(), getActivity());
//                        eqSettingManager.applyPresetsWithBand(GraphicEQPreset.Jazz, eqSettingManager.getBandFromEQModel(eQNameList.get(mPosition)), lightX);
                        if(getAppActivity().headphCtrl != null)
                            getAppActivity().headphCtrl.eqCtrl.applyPresetWithBand(GraphicEQPreset.Jazz, eqSettingManager.getBandFromEQModel(eQNameList.get(mPosition)));
                        break;
                    case 2:
                        JBLprefrences.setString(EQSettingManager.EQKeyNAME, eQNameList.get(mPosition).getEqName(), getActivity());
//                        eqSettingManager.applyPresetsWithBand(GraphicEQPreset.Vocal, eqSettingManager.getBandFromEQModel(eQNameList.get(mPosition)), lightX);
                        if(getAppActivity().headphCtrl != null)
                            getAppActivity().headphCtrl.eqCtrl.applyPresetWithBand(GraphicEQPreset.Vocal, eqSettingManager.getBandFromEQModel(eQNameList.get(mPosition)));
                        break;
                    case 3:
                        JBLprefrences.setString(EQSettingManager.EQKeyNAME, eQNameList.get(mPosition).getEqName(), getActivity());
//                        eqSettingManager.applyPresetsWithBand(GraphicEQPreset.Bass, eqSettingManager.getBandFromEQModel(eQNameList.get(mPosition)), lightX);
                        if(getAppActivity().headphCtrl != null)
                            getAppActivity().headphCtrl.eqCtrl.applyPresetWithBand(GraphicEQPreset.Bass, eqSettingManager.getBandFromEQModel(eQNameList.get(mPosition)));
                        break;
                    default:
                        JBLprefrences.setString(EQSettingManager.EQKeyNAME, eQNameList.get(mPosition).getEqName(), getActivity());
//                        eqSettingManager.applyPresetsWithBand(GraphicEQPreset.User, eqSettingManager.getBandFromEQModel(eQNameList.get(mPosition)), lightX);
                        if(getAppActivity().headphCtrl != null)
                            getAppActivity().headphCtrl.eqCtrl.applyPresetWithBand(GraphicEQPreset.User, eqSettingManager.getBandFromEQModel(eQNameList.get(mPosition)));
                        break;
                }
                Log.d("Applied", String.valueOf(mPosition));
            }
        };

    @Override
    public void getCurrentEQPresetReply(String eqName, int index) {
        JBLprefrences.setString(EQSettingManager.EQKeyNAME, eqName, getAppActivity());
        onItemClick(null, null, index, index);
    }

    @Override
    public void getEQSettingParamReply(int preset, int numOfBand, long values[]) {

    }

    @Override
    public void getEQMinMaxParam(int limitNumBands, int limitNumSettings, int limitMin, int limitMax) {
        mGraphicEQLimitNumBands = limitNumBands;
        mGraphicEQLimitNumSettings = limitNumSettings;
        FragEQCustom.maxLimit = limitMax;
        FragEQCustom.minLimit = limitMin;
    }
}
