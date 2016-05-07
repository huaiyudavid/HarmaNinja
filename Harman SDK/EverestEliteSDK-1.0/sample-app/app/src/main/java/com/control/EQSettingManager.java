package com.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.constants.DBKey;
import com.model.EQModel;
import com.storage.JBLDatabase;
import com.storage.JBLprefrences;

import java.util.ArrayList;


public class EQSettingManager implements DBKey {

    public final static String EQKey = "EQKey";
    public final static String EQKeyNAME = "EQKeyName";
    private static final String TAG = EQSettingManager.class.getSimpleName();

    public enum OperationStatus {
        INSERTED, UPDATED, FAILED, EXISTED, DELETED;
    }

    public enum PresetType {
        OFF(0), VOCAL(1), BASS(2), JAZZ(3), DEFAULT(4);

        PresetType(int type) {
            this.type = type;
        }

        int type;
    }

    private static EQSettingManager eqSettingManager;
    private Context context;

    private EQSettingManager(Context context) {
        this.context = context;
        loadDefaultEQSettingAndApplyIt();
    }

    /**
     * Don't pass context object as null.
     *
     * @param context
     * @return
     */
    public static EQSettingManager getEQSettingManager(Context context) {
        if (eqSettingManager == null)
            eqSettingManager = new EQSettingManager(context);
        return eqSettingManager;
    }

    private int currentEQ;

    /**
     * Retrieves current EQ setting from preferences.
     */
    public void loadDefaultEQSettingAndApplyIt() {
        currentEQ = JBLprefrences.getInt(EQSettingManager.EQKey, context);
    }

    /**
     * Insert custom EQ setting to Database.
     *
     * @param eqModel
     * @return Status
     */
    public OperationStatus addCustomEQ(EQModel eqModel) {
        OperationStatus operationStatus = OperationStatus.FAILED;
        SQLiteDatabase db = null;
        try {
            db = new JBLDatabase(context).getWritableDatabase();
//            Cursor cursor = db.query(JBLDatabase.JBLEQ, null, JBLDatabase.EQ_NAME + "=?", new String[]{eqModel.getEqName()}, null, null, null);
//            if (cursor != null && cursor.getCount() == 1) {
//                operationStatus = OperationStatus.EXISTED;
//                long i = db.update(JBLDatabase.JBLEQ, getContentValueFromModel(eqModel), JBLDatabase.EQ_NAME + "=?", new String[]{eqModel.getEqName()});
//                if (i != -1)
//                    operationStatus = OperationStatus.UPDATED;
//            } else {
            long i = db.insert(JBLDatabase.JBLEQ, null, getContentValueFromModel(eqModel));
            if (i != -1)
                operationStatus = OperationStatus.INSERTED;
            else
                operationStatus = OperationStatus.FAILED;
//            }
        } catch (Exception e) {
            Log.e(EQSettingManager.TAG, e.getMessage());
        } finally {
            if (db != null)
                db.close();
        }
        return operationStatus;
    }

    /**
     * Update Custom EQ setting in database.
     *
     * @param eqModel
     * @param updateEqName String as base name to Update.
     * @return Status.
     */
    public OperationStatus updateCustomEQ(EQModel eqModel, String updateEqName) {
        OperationStatus operationStatus = OperationStatus.FAILED;
//        operationStatus = OperationStatus.EXISTED;
        SQLiteDatabase db = null;
        try {
            db = new JBLDatabase(context).getWritableDatabase();
            long i = db.update(JBLDatabase.JBLEQ, getContentValueFromModel(eqModel), JBLDatabase.EQ_NAME + "=?", new String[]{updateEqName});
            if (i != -1)
                operationStatus = OperationStatus.UPDATED;
        } catch (Exception e) {
            Log.e(EQSettingManager.TAG, e.getMessage());
        } finally {
            if (db != null)
                db.close();
        }
        return operationStatus;
    }

    /**
     * Delete custom EQ setting from database.
     *
     * @param eqName String as base name to delete.
     * @return Status
     */
    public OperationStatus deleteEQ(String eqName) {
        loadDefaultEQSettingAndApplyIt();
        OperationStatus operationStatus = OperationStatus.FAILED;
        SQLiteDatabase db = null;
        try {
            db = new JBLDatabase(context).getWritableDatabase();
            long i = db.delete(JBLDatabase.JBLEQ, JBLDatabase.EQ_NAME + "=?", new String[]{eqName});
            if (i != -1) {
                if (currentEQ > 3)
                    changeDefaultSetting(PresetType.OFF, null);
                operationStatus = OperationStatus.DELETED;
            }
        } catch (Exception e) {
            Log.e(EQSettingManager.TAG, e.getMessage());
        } finally {
            if (db != null)
                db.close();
        }
        return operationStatus;
    }

    /**
     * Change Default EQ Setting.
     *
     * @param eqType  Predefined EQ setting.
     * @param eqModel
     */
    public void changeDefaultSetting(PresetType eqType, EQModel eqModel) {

        switch (eqType) {

            case OFF:
                eqModel = loadPredefineEQ(PresetType.OFF);
                break;
            case VOCAL:
                eqModel = loadPredefineEQ(PresetType.VOCAL);
                break;
            case BASS:
                eqModel = loadPredefineEQ(PresetType.BASS);
                break;
            case JAZZ:
                eqModel = loadPredefineEQ(PresetType.JAZZ);
                break;
            case DEFAULT:
                break;
        }

        //TODO apply new eq to libray, it will be implemented when chipset will support custom EQ
        JBLprefrences.setString(EQSettingManager.EQKey, eqType.name(), context);
    }

    /**
     * Load predefined EQ setting.
     *
     * @param eqType Setting type
     * @return EQModel
     */
    public EQModel loadPredefineEQ(PresetType eqType) {

        switch (eqType) {

            case OFF:
                return new EQModel(PresetType.OFF);
            case VOCAL:
                return new EQModel(PresetType.VOCAL);
            case BASS:
                return new EQModel(PresetType.BASS);
            case JAZZ:
                return new EQModel(PresetType.JAZZ);
            default:
                return new EQModel(PresetType.OFF);
        }
    }

    /**
     * convert EQModel to ContentValues
     *
     * @param eqModel
     * @return ContentValues
     */
    private ContentValues getContentValueFromModel(EQModel eqModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(JBLDatabase.EQ_NAME, eqModel.getEqName());
        contentValues.put(JBLDatabase.HIGH1, eqModel.getHigh1());
        contentValues.put(JBLDatabase.HIGH2, eqModel.getHigh2());
        contentValues.put(JBLDatabase.HIGH3, eqModel.getHigh3());
        contentValues.put(JBLDatabase.MEDIUM1, eqModel.getMedium1());
        contentValues.put(JBLDatabase.MEDIUM2, eqModel.getMedium2());
        contentValues.put(JBLDatabase.MEDIUM3, eqModel.getMedium3());
        contentValues.put(JBLDatabase.MEDIUM4, eqModel.getMedium4());
        contentValues.put(JBLDatabase.LOW1, eqModel.getLow1());
        contentValues.put(JBLDatabase.LOW2, eqModel.getLow2());
        contentValues.put(JBLDatabase.LOW3, eqModel.getLow3());
        return contentValues;
    }

    /**
     * Returns complete EQ setting list from database.
     *
     * @return EQModel ArrayList
     */
    public ArrayList<EQModel> getCompleteEQList() {
        ArrayList<EQModel> eQList = new ArrayList<>();
        String selectQuery = "SELECT * from JBLEQ";
        SQLiteDatabase db = null;
        try {
            db = new JBLDatabase(context).getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    EQModel eqModel = new EQModel();
                    eqModel.setEqName(cursor.getString(cursor.getColumnIndex(EQ_NAME)));
                    eqModel.setHigh1(getInt(cursor.getString(cursor.getColumnIndex(HIGH1))));
                    eqModel.setHigh2(getInt(cursor.getString(cursor.getColumnIndex(HIGH2))));
                    eqModel.setHigh3(getInt(cursor.getString(cursor.getColumnIndex(HIGH3))));
                    eqModel.setMedium1(getInt(cursor.getString(cursor.getColumnIndex(MEDIUM1))));
                    eqModel.setMedium2(getInt(cursor.getString(cursor.getColumnIndex(MEDIUM2))));
                    eqModel.setMedium3(getInt(cursor.getString(cursor.getColumnIndex(MEDIUM3))));
                    eqModel.setMedium4(getInt(cursor.getString(cursor.getColumnIndex(MEDIUM4))));
                    eqModel.setLow1(getInt(cursor.getString(cursor.getColumnIndex(LOW1))));
                    eqModel.setLow2(getInt(cursor.getString(cursor.getColumnIndex(LOW2))));
                    eqModel.setLow3(getInt(cursor.getString(cursor.getColumnIndex(LOW3))));

                    eQList.add(eqModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(EQSettingManager.TAG, e.getMessage());
        } finally {
            if (db != null)
                db.close();
        }
        return eQList;
    }

    /**
     * Returns EQ Model based on provided name
     *
     * @param name String as base name.
     * @return EQModel
     */
    public EQModel getEQModelByName(String name) {
        EQModel eqModel = null;

        String selectQuery = "SELECT * from JBLEQ where " + EQ_NAME + "='" + name + "'";
        SQLiteDatabase db = null;
        try {
            db = new JBLDatabase(context).getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    eqModel = new EQModel();
                    eqModel.setEqName(cursor.getString(cursor.getColumnIndex(EQ_NAME)));
                    eqModel.setHigh1(getInt(cursor.getString(cursor.getColumnIndex(HIGH1))));
                    eqModel.setHigh2(getInt(cursor.getString(cursor.getColumnIndex(HIGH2))));
                    eqModel.setHigh3(getInt(cursor.getString(cursor.getColumnIndex(HIGH3))));
                    eqModel.setMedium1(getInt(cursor.getString(cursor.getColumnIndex(MEDIUM1))));
                    eqModel.setMedium2(getInt(cursor.getString(cursor.getColumnIndex(MEDIUM2))));
                    eqModel.setMedium3(getInt(cursor.getString(cursor.getColumnIndex(MEDIUM3))));
                    eqModel.setMedium4(getInt(cursor.getString(cursor.getColumnIndex(MEDIUM4))));
                    eqModel.setLow1(getInt(cursor.getString(cursor.getColumnIndex(LOW1))));
                    eqModel.setLow2(getInt(cursor.getString(cursor.getColumnIndex(LOW2))));
                    eqModel.setLow3(getInt(cursor.getString(cursor.getColumnIndex(LOW3))));

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(EQSettingManager.TAG, e.getMessage());
        } finally {
            if (db != null)
                db.close();
        }
        return eqModel;
    }

    /**
     * Returns complete list based on where clause constraint.
     *
     * @param where String
     * @return EQModel ArrayList
     */
    public ArrayList<EQModel> getCompleteEQList(String where) {
        ArrayList<EQModel> eQList = new ArrayList<>();
        String selectQuery = "SELECT * from JBLEQ where " + EQ_NAME + " like '%" + where + "%'";
        SQLiteDatabase db = null;
        try {
            db = new JBLDatabase(context).getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    EQModel eqModel = new EQModel();
                    eqModel.setEqName(cursor.getString(cursor.getColumnIndex(EQ_NAME)));
                    eqModel.setHigh1(getInt(cursor.getString(cursor.getColumnIndex(HIGH1))));
                    eqModel.setHigh2(getInt(cursor.getString(cursor.getColumnIndex(HIGH2))));
                    eqModel.setHigh3(getInt(cursor.getString(cursor.getColumnIndex(HIGH3))));
                    eqModel.setMedium1(getInt(cursor.getString(cursor.getColumnIndex(MEDIUM1))));
                    eqModel.setMedium2(getInt(cursor.getString(cursor.getColumnIndex(MEDIUM2))));
                    eqModel.setMedium3(getInt(cursor.getString(cursor.getColumnIndex(MEDIUM3))));
                    eqModel.setMedium4(getInt(cursor.getString(cursor.getColumnIndex(MEDIUM4))));
                    eqModel.setLow1(getInt(cursor.getString(cursor.getColumnIndex(LOW1))));
                    eqModel.setLow2(getInt(cursor.getString(cursor.getColumnIndex(LOW2))));
                    eqModel.setLow3(getInt(cursor.getString(cursor.getColumnIndex(LOW3))));

                    eQList.add(eqModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(EQSettingManager.TAG, e.getMessage());
        } finally {
            if (db != null)
                db.close();
        }
        return eQList;
    }

    /**
     * Returns single EQ model data according to provided EQ name.
     *
     * @param name
     * @return EQModel
     */
    public EQModel getSingleEQModel(String name) {
        EQModel eqModel = null;
        String selectQuery = "SELECT * from JBLEQ where " + EQ_NAME + " = " + name;
        SQLiteDatabase db = null;
        try {
            db = new JBLDatabase(context).getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                eqModel = new EQModel();
                do {
                    eqModel.setEqName(cursor.getString(cursor.getColumnIndex(EQ_NAME)));
                    eqModel.setHigh1(getInt(cursor.getString(cursor.getColumnIndex(HIGH1))));
                    eqModel.setHigh2(getInt(cursor.getString(cursor.getColumnIndex(HIGH2))));
                    eqModel.setHigh3(getInt(cursor.getString(cursor.getColumnIndex(HIGH3))));
                    eqModel.setMedium1(getInt(cursor.getString(cursor.getColumnIndex(MEDIUM1))));
                    eqModel.setMedium2(getInt(cursor.getString(cursor.getColumnIndex(MEDIUM2))));
                    eqModel.setMedium3(getInt(cursor.getString(cursor.getColumnIndex(MEDIUM3))));
                    eqModel.setMedium4(getInt(cursor.getString(cursor.getColumnIndex(MEDIUM4))));
                    eqModel.setLow1(getInt(cursor.getString(cursor.getColumnIndex(LOW1))));
                    eqModel.setLow2(getInt(cursor.getString(cursor.getColumnIndex(LOW2))));
                    eqModel.setLow3(getInt(cursor.getString(cursor.getColumnIndex(LOW3))));

                } while (cursor.moveToNext());
                return eqModel;
            }
        } catch (Exception e) {
            Log.e(EQSettingManager.TAG, e.getMessage());
        } finally {
            if (db != null)
                db.close();
        }
        return eqModel;
    }

    /**
     * Returns integer parsed from String.
     *
     * @param num String parameter
     * @return int
     */
    private int getInt(String num) {
        try {
            return Integer.parseInt(num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }



    public int mGraphicEQLimitNumBands;

    /**
     * Get EQ band from EQModel
     *
     * @param model
     * @return int array
     */
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
}
