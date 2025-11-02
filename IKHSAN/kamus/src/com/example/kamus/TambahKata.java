package com.example.kamus;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TambahKata extends Activity {

    private SQLiteDatabase db = null;
    private EditText txtInggris, txtIndonesia, txtJerman;
    private DatabaseHelper dbHelper;

    // Kolom tabel
    public static final String INGGRIS = "inggris";
    public static final String INDONESIA = "indonesia";
    public static final String JERMAN = "jerman";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambahkata);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        txtInggris = (EditText) findViewById(R.id.txtInggris);
        txtIndonesia = (EditText) findViewById(R.id.txtIndonesia);
        txtJerman = (EditText) findViewById(R.id.txtJerman);
    }

    public void saveData(View view) {
        String inggris = txtInggris.getText().toString().trim();
        String indonesia = txtIndonesia.getText().toString().trim();
        String jerman = txtJerman.getText().toString().trim();

        if (inggris.length() == 0 || indonesia.length() == 0 || jerman.length() == 0) {
            Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues cv = new ContentValues();
        cv.put(INGGRIS, inggris);
        cv.put(INDONESIA, indonesia);
        cv.put(JERMAN, jerman);

        long result = db.insert("kamus", null, cv);

        if (result > 0) {
            Toast.makeText(this, "Kata berhasil disimpan!", Toast.LENGTH_LONG).show();
            clearForm();
            // Kembali ke daftar atau main
            finish();
        } else {
            Toast.makeText(this, "Gagal menyimpan kata!", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearForm() {
        txtInggris.setText("");
        txtIndonesia.setText("");
        txtJerman.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}