package com.mbj.gestionproduit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "products.sqlite";
    public static final int DB_VERSION = 1;

    public static final String TABLE_PRODUCT = "product";
    public static final String COL_ID = "_id";
    public static final String COL_CODE = "code";
    public static final String COL_LABEL = "label";
    public static final String COL_PRICE = "price";
    public static final String COL_QTY = "qty";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_PRODUCT + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_CODE + " TEXT NOT NULL UNIQUE, " +
                    COL_LABEL + " TEXT NOT NULL, " +
                    COL_PRICE + " REAL NOT NULL, " +
                    COL_QTY + " INTEGER NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        onCreate(db);
    }
}

