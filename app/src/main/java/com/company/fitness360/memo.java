package com.company.fitness360;

import android.app.AlertDialog;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.File;
import java.util.ArrayList;

public class memo extends AppCompatActivity {
    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    static ArrayList<String> ds = new ArrayList<>();
    static ArrayList<String> cam = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), data.class);

                startActivity(intent);
            }
        });
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(memo.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete all notes?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        notes.clear();
                                        ds.clear();
                                        for(int j=0;j<cam.size();j++) {
                                            fileDl(j);
                                        }
                                        cam.clear();
                                        memo.arrayAdapter.notifyDataSetChanged();
                                        arToSQL();
                                    }
                                }
                        )
                        .setNegativeButton("No", null)
                        .show();

            }
        });

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setBackgroundResource(R.drawable.grey);

        // SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.memo", Context.MODE_PRIVATE);
        //  HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);
        //  HashSet<String> set1 = (HashSet<String>) sharedPreferences.getStringSet("ds", null);
        SQLiteDatabase eventsDB = this.openOrCreateDatabase("info", MODE_PRIVATE, null);
        //eventsDB.execSQL("DROP TABLE info");
        eventsDB.execSQL("CREATE TABLE IF NOT EXISTS info (text VARCHAR, date VARCHAR, cam VARCHAR, id INTEGER PRIMARY KEY)");

        //Log.i("test", set.toString());

        // if (set == null||set.isEmpty()) {
        if (!loadSQL()) {
            notes.add(0, "START ADDING HERE!");
            ds.add(0, "");
            cam.add(0,"");
            arToSQL();
        }
        //  } else {
        //  notes = new ArrayList(set);
        //  ds = new ArrayList(set1);


        arrayAdapter =new ArrayAdapter(this,R.layout.list, notes);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick (AdapterView < ? > adapterView, View view,int i, long l){

                Intent intent = new Intent(getApplicationContext(), data.class);
                intent.putExtra("noteId", i);
                startActivity(intent);
            }

        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick (AdapterView < ? > adapterView, View view,int i, long l){

                final int itemToDelete = i;

                new AlertDialog.Builder(memo.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        notes.remove(itemToDelete);
                                        ds.remove(itemToDelete);
                                        fileDl(itemToDelete);
                                        cam.remove(itemToDelete);
                                        arrayAdapter.notifyDataSetChanged();
                                        arToSQL();
                                        //SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.memo", Context.MODE_PRIVATE);

                                        // HashSet<String> set = new HashSet(memo.notes);

                                        //  sharedPreferences.edit().putStringSet("notes", set).apply();

                                    }
                                }
                        )
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }

        });
    }


    private boolean loadSQL(){
        SQLiteDatabase eventsDB = this.openOrCreateDatabase("info", MODE_PRIVATE, null);
        Cursor c = eventsDB.rawQuery("SELECT * FROM info", null);
        int nameIndex = c.getColumnIndex("text");
        int dateIndex = c.getColumnIndex("date");
        int camIndex = c.getColumnIndex("cam");
        int i =0;
        c.moveToFirst();
        if (c.getCount()>0) {
            ds.add(c.getString(nameIndex));
            notes.add(c.getString(dateIndex));
            cam.add(c.getString(camIndex));
            i++;
            while (c.moveToNext()) {
                i++;
                ds.add(c.getString(nameIndex));
                notes.add(c.getString(dateIndex));
                cam.add(c.getString(camIndex));
            }
        }
        c.close();
        eventsDB.close();
        if(i>0) return true;
        else return false;
    }

    private void arToSQL() {
        SQLiteDatabase eventsDB = this.openOrCreateDatabase("info", MODE_PRIVATE, null);
        eventsDB.execSQL("DROP TABLE info");
        eventsDB.execSQL("CREATE TABLE IF NOT EXISTS info (text VARCHAR, date VARCHAR, cam VARCHAR, id INTEGER PRIMARY KEY)");
        //eventsDB.execSQL("DELETE FROM info");
        for (int i = 0; i < ds.size(); i++) {
            if (memo.ds.get(i).compareTo("") == 0) {
                eventsDB.execSQL("INSERT INTO info (text, date, cam) VALUES ('" + memo.ds.get(i) + "','" + memo.notes.get(i) + "','" + memo.cam.get(i) + "')");
            } else {
                String s1 = memo.ds.get(i);
                ArrayList<Character> cr = new ArrayList<>();
                for (int i1 = 0; i1 < s1.length(); i1++) {
                    char c = s1.charAt(i1);
                    cr.add(c);
                    if (c == '\'')
                        cr.add('\'');
                }
                char[] cs = new char[cr.size()];
                for (int x = 0; x < cs.length; x++) {
                    cs[x] = cr.get(x);
                }
                String output = new String(cs);
                eventsDB.execSQL("INSERT INTO info (text, date, cam) VALUES ('" + output + "','" + memo.notes.get(i) + "','" + memo.cam.get(i) + "')");
            }
        }
        // eventsDB.execSQL("INSERT INTO info (text, date, cam) VALUES ('" + ds.get(i) + "','" + notes.get(i) + "','" + cam.get(i) + "')");
        eventsDB.close();
    }
    private void fileDl(int d){
        String dest = cam.get(d);
        Log.i("test", dest);
        File file = new File(dest);
        boolean deleted = file.delete();
    }

    @Override
    public void onBackPressed() {
        notes.clear();
        ds.clear();
        cam.clear();
        Intent goBack = new Intent(memo.this, StartPage.class);
        startActivity(goBack);
    }
}
