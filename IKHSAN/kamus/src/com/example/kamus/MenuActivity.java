package com.example.kamus;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MenuActivity extends ListActivity {

    String[] menuUtama = new String[]{
            "Terjemah Kata",
            "Tambah Kata",
            "Daftar Kata",
            "Keluar Aplikasi"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set adapter untuk ListView
        setListAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                menuUtama
        ));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String pilihan = menuUtama[position];
        tampilkanPilihan(pilihan);
    }

    private void tampilkanPilihan(String pilihan) {
        Intent i = null;

        try {
            if (pilihan.equals("Terjemah Kata")) {
                i = new Intent(this, MainActivity.class);
            } else if (pilihan.equals("Tambah Kata")) {
                i = new Intent(this, TambahKata.class);
            } else if (pilihan.equals("Daftar Kata")) {
                i = new Intent(this, DaftarKata.class);
            } else if (pilihan.equals("Keluar Aplikasi")) {
                finish();
                return;
            }

            if (i != null) {
                startActivity(i);
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}