package com.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.constants.DBKey;

import java.util.ArrayList;


public class JBLDatabase extends SQLiteOpenHelper implements DBKey {

    private final static String DATABASE_NAME = "JBL.db";
    private final static int version = 1;
    private final static String TAG = "JBL_" + JBLDatabase.class.getName();


    private SQLiteDatabase db;
    private String CREATE_TABLE = "create table " + JBLEQ + " (" +
            ID + " INTEGER ," +
            EQ_NAME + " TEXT PRIMARY KEY ," +
            HIGH1 + " TEXT ," +
            HIGH2 + " TEXT ," +
            HIGH3 + " TEXT ," +
            MEDIUM1 + " TEXT ," +
            MEDIUM2 + " TEXT ," +
            MEDIUM3 + " TEXT ," +
            MEDIUM4 + " TEXT ," +
            LOW1 + " TEXT ," +
            LOW2 + " TEXT ," +
            LOW3 + " TEXT " +
            ");";


    public static ArrayList<String> predefinepresets = new ArrayList<String> ();

    static {
        predefinepresets.add("Jazz");
        predefinepresets.add("Bass");
        predefinepresets.add("Vocal");
        predefinepresets.add("Off");
    }

    /**
     * <p>Constructor</p>
     *
     * @param context
     */
    public JBLDatabase(Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableEqSettings(db);
    }

    /**
     * <p>Creates table named JBLEQ </p>
     * @param db
     */
    public void createTableEqSettings(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
