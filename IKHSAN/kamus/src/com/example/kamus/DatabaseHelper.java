package com.example.kamus;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dbkamus";
    private static final int DATABASE_VERSION = 1; // Naikkan ke 2 jika ingin reset

    public static final String TABLE_NAME = "kamus";
    public static final String COL_ID = "_id";
    public static final String COL_INGGRIS = "inggris";
    public static final String COL_INDONESIA = "indonesia";
    public static final String COL_JERMAN = "jerman";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Membuat tabel
    private void createTable(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_INGGRIS + " TEXT, " +
                COL_INDONESIA + " TEXT, " +
                COL_JERMAN + " TEXT);";
        db.execSQL(CREATE_TABLE);
    }

    // Mengisi data awal
    private void generateData(SQLiteDatabase db) {
        // Cek apakah data sudah ada
        android.database.Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        if (c.moveToFirst() && c.getInt(0) > 0) {
            c.close();
            return; // Data sudah ada, jangan insert ulang
        }
        c.close();

        ContentValues cv = new ContentValues();

        cv.put(COL_INGGRIS, "run");
        cv.put(COL_INDONESIA, "lari");
        cv.put(COL_JERMAN, "laufen");
        db.insert(TABLE_NAME, null, cv);

        cv.clear();
        cv.put(COL_INGGRIS, "walk");
        cv.put(COL_INDONESIA, "jalan");
        cv.put(COL_JERMAN, "gehen");
        db.insert(TABLE_NAME, null, cv);

        cv.clear();
        cv.put(COL_INGGRIS, "read");
        cv.put(COL_INDONESIA, "membaca");
        cv.put(COL_JERMAN, "lesen");
        db.insert(TABLE_NAME, null, cv);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
        generateData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Hanya drop jika benar-benar upgrade
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db); // Buat ulang tabel + isi data
    }
}