package com.example.kamus;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends Activity {

    private SQLiteDatabase db = null;
    private Cursor kamusCursor = null;

    private EditText txtInggris;
    private EditText txtIndonesia;
    private EditText txtJerman;

    private DatabaseHelper datakamus = null;

    public static final String TABLE_NAME = "kamus";
    public static final String COL_ID = "_id";
    public static final String COL_INGGRIS = "inggris";
    public static final String COL_INDONESIA = "indonesia";
    public static final String COL_JERMAN = "jerman";
    
    public void bukaDaftarKata(View view) {
        startActivity(new Intent(this, DaftarKata.class));
    }
    public void bukaTambahKata(View view) {
        startActivity(new Intent(this, TambahKata.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // pastikan ini memanggil layout di atas

        datakamus = new DatabaseHelper(this);
        db = datakamus.getReadableDatabase();


        txtInggris = (EditText) findViewById(R.id.txtInggris);
        txtIndonesia = (EditText) findViewById(R.id.txtIndonesia);
        txtJerman = (EditText) findViewById(R.id.txtJerman);

    }

    public void getTerjemahan(View view) {
        String englishWord = txtInggris.getText().toString().trim();
        String bhsIndonesia = "";
        String bhsJerman = "";

        if (englishWord.length() == 0) {
            Toast.makeText(this, "Masukkan kata bahasa Inggris terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }


        kamusCursor = db.rawQuery(
                "SELECT " + COL_INDONESIA + ", " + COL_JERMAN +
                        " FROM " + TABLE_NAME +
                        " WHERE " + COL_INGGRIS + " = ?",
                new String[]{englishWord}
        );

        if (kamusCursor.moveToFirst()) {
            bhsIndonesia = kamusCursor.getString(0);
            bhsJerman = kamusCursor.getString(1);
        } else {
            Toast.makeText(this, "Terjemahan tidak ditemukan", Toast.LENGTH_SHORT).show();
        }

        txtIndonesia.setText(bhsIndonesia);
        txtJerman.setText(bhsJerman);

        kamusCursor.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (kamusCursor != null && !kamusCursor.isClosed()) kamusCursor.close();
            if (db != null && db.isOpen()) db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
