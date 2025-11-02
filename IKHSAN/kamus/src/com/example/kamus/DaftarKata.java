package com.example.kamus;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DaftarKata extends ListActivity {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor kamusCursor;
    private SimpleCursorAdapter adapter;

    private static final int EDIT_ID = Menu.FIRST + 1;
    private static final int DELETE_ID = Menu.FIRST + 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftarkata);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        registerForContextMenu(getListView());
        isiDataListView();
    }

    private void isiDataListView() {
        kamusCursor = db.query("kamus",
                new String[]{"_id", "inggris", "indonesia", "jerman"},
                null, null, null, null, "inggris ASC");

        String[] from = {"inggris", "indonesia", "jerman"};
        int[] to = {R.id.inggris, R.id.indonesia, R.id.jerman};

        adapter = new CustomCursorAdapter(this, R.layout.row, kamusCursor, from, to);
        setListAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, EDIT_ID, 0, "Edit Kata");
        menu.add(0, DELETE_ID, 1, "Hapus Kata");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final long id = info.id;

        switch (item.getItemId()) {
            case EDIT_ID:
                editKata(id);
                return true;
            case DELETE_ID:
                hapusKata(id);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    // ====================== EDIT KATA ======================
    private void editKata(final long id) {
        Cursor c = db.query("kamus", new String[]{"inggris", "indonesia", "jerman"},
                "_id=?", new String[]{String.valueOf(id)}, null, null, null);

        if (c.moveToFirst()) {
            final String inggris = c.getString(0);
            final String indonesia = c.getString(1);
            final String jerman = c.getString(2);

            LayoutInflater inflater = LayoutInflater.from(this);
            View dialogView = inflater.inflate(R.layout.edit, null);

            final EditText edInggris = (EditText) dialogView.findViewById(R.id.inggris);
            final EditText edIndonesia = (EditText) dialogView.findViewById(R.id.indonesia);
            final EditText edJerman = (EditText) dialogView.findViewById(R.id.jerman);

            edInggris.setText(inggris);
            edIndonesia.setText(indonesia);
            edJerman.setText(jerman);

            new AlertDialog.Builder(this)
                    .setTitle("Edit Kata")
                    .setView(dialogView)
                    .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ContentValues cv = new ContentValues();
                            cv.put("inggris", edInggris.getText().toString().trim());
                            cv.put("indonesia", edIndonesia.getText().toString().trim());
                            cv.put("jerman", edJerman.getText().toString().trim());

                            db.update("kamus", cv, "_id=?", new String[]{String.valueOf(id)});
                            isiDataListView();
                            Toast.makeText(DaftarKata.this, "Kata berhasil diupdate!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        }
        c.close();
    }

    // ====================== HAPUS KATA ======================
    private void hapusKata(final long id) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Kata")
                .setMessage("Yakin ingin menghapus kata ini?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.delete("kamus", "_id=?", new String[]{String.valueOf(id)});
                        isiDataListView();
                        Toast.makeText(DaftarKata.this, "Kata berhasil dihapus!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (kamusCursor != null && !kamusCursor.isClosed()) kamusCursor.close();
        if (db != null && db.isOpen()) db.close();
    }

    // ====================== CUSTOM ADAPTER (API 8) ======================
    public class CustomCursorAdapter extends SimpleCursorAdapter {
        private LayoutInflater inflater;

        public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
            super(context, layout, c, from, to);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return inflater.inflate(R.layout.row, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tvInggris = (TextView) view.findViewById(R.id.inggris);
            TextView tvIndonesia = (TextView) view.findViewById(R.id.indonesia);
            TextView tvJerman = (TextView) view.findViewById(R.id.jerman);

            tvInggris.setText(cursor.getString(cursor.getColumnIndexOrThrow("inggris")));
            tvIndonesia.setText(cursor.getString(cursor.getColumnIndexOrThrow("indonesia")));
            tvJerman.setText(cursor.getString(cursor.getColumnIndexOrThrow("jerman")));
        }
    }
}