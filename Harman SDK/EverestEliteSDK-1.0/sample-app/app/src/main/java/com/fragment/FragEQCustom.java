package com.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.activity.MainActivity;
import com.activity.R;
import com.control.EQSettingManager;
import com.harman.everestelite.GraphicEQPreset;
import com.model.EQModel;
import com.storage.JBLprefrences;
import com.util.AlertsDialog;

import java.util.ArrayList;

/**
 * Created by root on 16-3-25.
 */
public class FragEQCustom extends BaseFragment implements View.OnClickListener{
    public static final String TAG = FragEQCustom.class.getSimpleName();
    public static int maxLimit = 10, minLimit = -10;
    TextView eQText, deletebtn, saveBtn;
    EditText settingName;
    String[] presets = {"Bass", "Vocal", "Jazz"};
    SeekBar seekBars[] = new SeekBar[10];
    EQModel eqModel;
    boolean updatOnly = false;
    String udateKey = "";
    boolean preset;
    Boolean deleteButton = false;
    String orgSettingVal;
    boolean isCreatingNew;
    String oldValueEQ = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_eq, container, false);
        settingName = (EditText) view.findViewById(R.id.setting_name);
        saveBtn = (Button)view.findViewById(R.id.save_name);
        saveBtn.setOnClickListener(this);
//        eQText = (TextView) getMainActivity().findViewById(R.id.eQText);
//        eQText.setOnClickListener(this);
        settingName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Character c = ' ';
                String name = "";
//                if (s.length() > start) {
//                    c = s.charAt(start);
//                    Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(c);
//                    name = unicodeBlock.toString();
//                    if (isEmoticans(name)) {
//                        Editable editable = (Editable) s;
//                        editable.delete(s.length() - count, s.length());
//
//                    }
//                }

                Log.d("demo", "character is  " + c + " name is  = " + name.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });
        deletebtn = (TextView) view.findViewById(R.id.deletebtn);
        deletebtn.setOnClickListener(this);
        seekBars[0] = (SeekBar) view.findViewById(R.id.seekbar1);
        seekBars[1] = (SeekBar) view.findViewById(R.id.seekbar2);
        seekBars[2] = (SeekBar) view.findViewById(R.id.seekbar3);
        seekBars[3] = (SeekBar) view.findViewById(R.id.seekbar4);
        seekBars[4] = (SeekBar) view.findViewById(R.id.seekbar5);
        seekBars[5] = (SeekBar) view.findViewById(R.id.seekbar6);
        seekBars[6] = (SeekBar) view.findViewById(R.id.seekbar7);
        seekBars[7] = (SeekBar) view.findViewById(R.id.seekbar8);
        seekBars[8] = (SeekBar) view.findViewById(R.id.seekbar9);
        seekBars[9] = (SeekBar) view.findViewById(R.id.seekbar10);
        if (eqModel != null) {
            seekBars[0].setProgress(getProgress(eqModel.getHigh1()));
            seekBars[1].setProgress(getProgress(eqModel.getHigh2()));
            seekBars[2].setProgress(getProgress(eqModel.getHigh3()));
            seekBars[3].setProgress(getProgress(eqModel.getMedium1()));
            seekBars[4].setProgress(getProgress(eqModel.getMedium2()));
            seekBars[5].setProgress(getProgress(eqModel.getMedium3()));
            seekBars[6].setProgress(getProgress(eqModel.getMedium4()));
            seekBars[7].setProgress(getProgress(eqModel.getLow1()));
            seekBars[8].setProgress(getProgress(eqModel.getLow2()));
            seekBars[9].setProgress(getProgress(eqModel.getLow3()));
            settingName.setText(eqModel.getEqName());
        }
        if (deleteButton) {
            deletebtn.setVisibility(View.VISIBLE);
        }
        if(isCreatingNew || deleteButton)
            saveBtn.setVisibility(View.VISIBLE);
        /* Checking for preset inputs
        * */
        if (preset == true) {
            settingName.setText(eqModel.getEqName());
        }
        try {
            for (int i = 0; i < 10; i++) {
                seekBars[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (eqModel == null)
                            eqModel = new EQModel();
                        progress = progress - 50;
                        if (progress > 0) {
                            int steps = 50 / maxLimit;
                            progress = progress / steps;
                        } else if (progress < 0) {
                            int steps = 50 / Math.abs(minLimit);
                            progress = progress / steps;
                        } else
                            progress = 0;
                        switch (seekBar.getId()) {
                            case R.id.seekbar1:
                                eqModel.setHigh1(progress);
                                break;
                            case R.id.seekbar2:
                                eqModel.setHigh2(progress);
                                break;
                            case R.id.seekbar3:
                                eqModel.setHigh3(progress);
                                break;
                            case R.id.seekbar4:
                                eqModel.setMedium1(progress);
                                break;
                            case R.id.seekbar5:
                                eqModel.setMedium2(progress);
                                break;
                            case R.id.seekbar6:
                                eqModel.setMedium3(progress);
                                break;
                            case R.id.seekbar7:
                                eqModel.setMedium4(progress);
                                break;
                            case R.id.seekbar8:
                                eqModel.setLow1(progress);
                                break;
                            case R.id.seekbar9:
                                eqModel.setLow2(progress);
                                break;
                            case R.id.seekbar10:
                                eqModel.setLow3(progress);
                                break;
                            default:
                                break;
                        }


                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        applyPresetEffect();
                    }
                });
            }
        } catch (Exception e) {
            Log.d("MyEquilizer", "Exception  : " + e.getMessage());
        }

        orgSettingVal = settingName.getText().toString();
        return view;
    }

    public void onResume(){
        super.onResume();
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        if (args != null)
            eqModel = (EQModel) args.getSerializable(EQModel.class.getSimpleName());
        updatOnly = args.getBoolean("Update_only");
        udateKey = args.getString("update_key");

        if (args.containsKey("showButton"))
            deleteButton = args.getBoolean("showButton");
        if (args.containsKey("preset"))
            preset = args.getBoolean("preset");
        if (args.containsKey("isCreatingNew"))
            isCreatingNew = args.getBoolean("isCreatingNew");

        if (eqModel == null) {
            eqModel = new EQModel();
        }
        oldValueEQ = eqModel.getEqName();

    }

    public void applyPresetEffect() {
        if (getAppActivity().headphCtrl != null) {
//            EQSettingManager eqSettingManager = EQSettingManager.getEQSettingManager(getAppActiviy());
//            eqSettingManager.applyPresetsWithBand(GraphicEQPreset.User, eqSettingManager.getBandFromEQModel(eqModel), lightX);
            getAppActivity().headphCtrl.eqCtrl.applyPresetWithBand(GraphicEQPreset.User, getBandFromEQModel(eqModel));
        }
    }

    public int[] getBandFromEQModel(EQModel model) {
        int[] array = new int[10];
        array[0] = model.getHigh1();
        array[1] = model.getHigh2();
        array[2] = model.getHigh3();
        array[3] = model.getMedium1();
        array[4] = model.getMedium2();
        array[5] = model.getMedium3();
        array[6] = model.getMedium4();
        array[7] = model.getLow1();
        array[8] = model.getLow2();
        array[9] = model.getLow3();
        return array;
    }

    public int getProgress(int value) {
        int steps = 50 / Math.abs(minLimit);
        int progress;
        if (value < 0) {
            progress = 50 + steps * value;
        } else {
            progress = 50 + steps * value;
        }
        return progress;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_name:
                if (preset) { /**** WHEN PRESET IS ON****/
                    // Case when preset is true and seting name hasn't changed
                    if (orgSettingVal.equals(settingName.getText().toString()) || settingName.getText().toString().length() == 0) {
                        // Find name for JAZZ BASS VOCAL

                        if (settingName.getText().toString().trim().length() == 0) {
                            eqModel.setEqName(getNewEqName(getActivity().getResources().getString(R.string.text_customEq)));
                        } else {
                            eqModel.setEqName(getNewEqName(settingName.getText().toString()));
                        }

                        popResult(EQSettingManager.getEQSettingManager(getMainActivity()).addCustomEQ(eqModel));

                    } else { // Case when preset is true and seting name has changed
                        // Check if new name is already present create or update

                        eqModel.setEqName(settingName.getText().toString());
                        if (createOrUpdateEQ(settingName.getText().toString())) {
                            popResult(EQSettingManager.OperationStatus.FAILED);
                        } else {
                            popResult(EQSettingManager.getEQSettingManager(getMainActivity()).addCustomEQ(eqModel));
                        }
                    }

                } else {/*****WHEN PRESET IS OFF****/
                    // Case when preset is false and setting name is empty i.e user is creating Cusotm EQ first time.

                    if (settingName.getText().toString().trim().length() == 0) {
                        //Find name for CUSTOM EQ
                        if (isCreatingNew) {
                            eqModel.setEqName(getNewEqName(getActivity().getResources().getString(R.string.text_customEq)));
                            popResult(EQSettingManager.getEQSettingManager(getMainActivity()).addCustomEQ(eqModel));
                        } else {
                            eqModel.setEqName(settingName.getText().toString().trim());
                            popResult(EQSettingManager.getEQSettingManager(getMainActivity()).updateCustomEQ(eqModel, orgSettingVal));
                        }
                    } else {
                        // Case when preset is false and setting name is not empty i.e user is creating/upgading  Cusotm EQ .
                        //Check if new name is already present create or update
                        eqModel.setEqName(settingName.getText().toString());

                        if (isCreatingNew) {
                            if (createOrUpdateEQ(eqModel.getEqName())) {
                                popResult(EQSettingManager.OperationStatus.FAILED);
                            } else {
                                popResult(EQSettingManager.getEQSettingManager(getMainActivity()).addCustomEQ(eqModel));
                            }
                        } else {
                            popResult(EQSettingManager.getEQSettingManager(getMainActivity()).updateCustomEQ(eqModel, orgSettingVal));
                        }
                    }
                }
                break;
            case R.id.deletebtn:
                String eqName = eqModel.getEqName();
                if (eqName.equals(JBLprefrences.getString(EQSettingManager.EQKeyNAME, getActivity(), null))) {
                    JBLprefrences.setString(EQSettingManager.EQKeyNAME, "Off", getActivity());
                    if(getAppActivity().headphCtrl != null){
                        getAppActivity().headphCtrl.eqCtrl.applyPresetWithoutBand(GraphicEQPreset.Off);
                    }
                }
                EQSettingManager.OperationStatus operationStatus = EQSettingManager.getEQSettingManager(getMainActivity()).deleteEQ(eqName);
                if (operationStatus == EQSettingManager.OperationStatus.DELETED) {
                    getAppActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                    getAppActivity().frgEQ.refreshEQList();
                } else {
                    AlertsDialog.showSimpleDialogWithOKButton(null, getString(R.string.deletefail), getMainActivity());
                }
                break;
            default:
                break;
        }
    }

    public String getNewEqName(String name) {

        ArrayList<EQModel> nameList = EQSettingManager.getEQSettingManager(getMainActivity()).getCompleteEQList(name);

        if (nameList.size() == 0 && name.equals(getActivity().getResources().getString(R.string.text_customEq))) {
            return getActivity().getResources().getString(R.string.text_customEq);
        }

        EQModel testModel = new EQModel();


        for (int i = 0; i <= nameList.size() - 1; i++) {

            if (i == 0 && name.equals(getActivity().getResources().getString(R.string.text_customEq))) {
                testModel.setEqName(getActivity().getResources().getString(R.string.text_customEq));
                if (!nameList.contains(testModel)) {
                    break;
                }
            }

            testModel.setEqName(name + (i + 1));
            if (!nameList.contains(testModel)) {
                break;
            }
        }
        return testModel.getEqName();
    }

    /**
     * @param name Name of Custom EQ
     * @return Returns boolean if TRUE means new Custom needs to be created in DB. For FALSE just update it.
     * @author harish
     */
    public boolean createOrUpdateEQ(String name) {
        boolean isPresent = false;
        EQModel eqModel = EQSettingManager.getEQSettingManager(getMainActivity()).getEQModelByName(name);

        if (eqModel != null) {
            isPresent = true;
        }

        return isPresent;
    }

    /**
     * @param dbOperationId Simply display the result.
     * @author harish
     */
    public boolean popResult(EQSettingManager.OperationStatus dbOperationId) {
        boolean result = true;
        if (dbOperationId == EQSettingManager.OperationStatus.INSERTED) {
            getAppActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            getAppActivity().frgEQ.refreshEQList();
        }
        else if (dbOperationId == EQSettingManager.OperationStatus.UPDATED) {
            if (JBLprefrences.getString(EQSettingManager.EQKeyNAME, getActivity(), "Off").equals(oldValueEQ)) {
                JBLprefrences.setString(EQSettingManager.EQKeyNAME, settingName.getText().toString(), getActivity());
            }
            getAppActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            getAppActivity().frgEQ.refreshEQList();
        } else {
            result = false;
            AlertsDialog.showSimpleDialogWithOKButton(null, getString(R.string.deletefail), getMainActivity());
        }
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(settingName.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        } catch (Exception e) {
            e.getMessage();
        }
        return result;

    }
}
