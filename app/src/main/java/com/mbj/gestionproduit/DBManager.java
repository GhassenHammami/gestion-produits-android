package com.mbj.gestionproduit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;

    private SQLiteDatabase database;

    public DBManager(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long add(String code, String label, double price, int qty) {

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COL_CODE, code);
        values.put(DatabaseHelper.COL_LABEL, label);
        values.put(DatabaseHelper.COL_PRICE, price);
        values.put(DatabaseHelper.COL_QTY, qty);

        return database.insert(DatabaseHelper.TABLE_PRODUCT, null, values);
    }

    public Cursor getAllProducts() {
        return database.query(DatabaseHelper.TABLE_PRODUCT, null, null, null, null, null, DatabaseHelper.COL_LABEL);
    }

    public int update(long id, String code, String libelle, double prix, int qte) {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COL_CODE, code);
        values.put(DatabaseHelper.COL_LABEL, libelle);
        values.put(DatabaseHelper.COL_PRICE, prix);
        values.put(DatabaseHelper.COL_QTY, qte);

        return database.update(DatabaseHelper.TABLE_PRODUCT, values, DatabaseHelper.COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void delete(long id) {
        database.delete(DatabaseHelper.TABLE_PRODUCT, DatabaseHelper.COL_ID + " = ?", new String[]{String.valueOf(id)});
    }


}
