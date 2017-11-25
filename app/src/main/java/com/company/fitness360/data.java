package com.company.fitness360;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.HashSet;

public class data extends AppCompatActivity {
    int noteId;
    ImageView imageView;
    View view;
    static final int REQUEST_TAKE_PHOTO = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#000000"));
//        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        ConstraintLayout v = (ConstraintLayout)findViewById(R.id.ly);
        v.setBackgroundResource(R.drawable.grey);
        EditText editText = (EditText) findViewById(R.id.editText);
        imageView = (ImageView) findViewById(R.id.imageView);
        view = findViewById(R.id.imageButton);
        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);

        if (noteId != -1) {
            if(memo.cam.get(noteId).compareTo("")==0) {
                view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dispatchTakePictureIntent();
                        view.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                if (memo.cam.get(noteId).compareTo("") != 0) {
                                    setPic1();
                                }
                            }
                        });
                    }
                });
            }
            if(memo.cam.get(noteId).compareTo("")!=0){
                setPic1();}
            editText.setText(memo.ds.get(noteId));
            if(memo.notes.get(noteId).compareTo("START ADDING HERE!")==0) {
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());
                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss - EEEE, dd/MM/yyyy");
                String formattedDate = df.format(c.getTime());
                memo.notes.set(noteId, formattedDate);
                memo.arrayAdapter.notifyDataSetChanged();
            }
            arToSQL(1);

        } else {
            memo.cam.add(0,"");
            noteId = 0;
            if(memo.cam.get(noteId).compareTo("")==0) {
                view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dispatchTakePictureIntent();
                        view.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                if(memo.cam.get(noteId).compareTo("")!=0){
                                    setPic1();}
                                else{dispatchTakePictureIntent();}
                            }
                        });
                    }
                });
            }
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss - EEEE, dd/MM/yyyy");
            String formattedDate = df.format(c.getTime());
            memo.notes.add(0, formattedDate);
            memo.ds.add(0, "");
            memo.arrayAdapter.notifyDataSetChanged();
            arToSQL(2);
        }
        //SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.memo", Context.MODE_PRIVATE);
        // HashSet<String> set1 = new HashSet(memo.ds);
        // sharedPreferences.edit().putStringSet("ds", set1).apply();
        // HashSet<String> set = new HashSet(memo.notes);
        //  sharedPreferences.edit().putStringSet("notes", set).apply();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                memo.ds.set(noteId, String.valueOf(charSequence));
                // SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.memo", Context.MODE_PRIVATE);
                // HashSet<String> set1 = new HashSet(memo.ds);
                //sharedPreferences.edit().putStringSet("ds", set1).apply();

            }

            @Override
            public void afterTextChanged(Editable editable) {

                arToSQL(1);
            }
        });
    }

    public void arToSQL(int chk) {
        SQLiteDatabase eventsDB = this.openOrCreateDatabase("info", MODE_PRIVATE, null);
        // eventsDB.execSQL("DELETE FROM info");

        // for (int i = 0; i < memo.ds.size(); i++) {//if(memo.ds.get(i).compareTo("'")!=0&&memo.ds.get(i).compareTo("''")!=0&&memo.ds.get(i).compareTo("'''")!=0)
        if(chk==2) {
            eventsDB.execSQL("DROP TABLE info");
            eventsDB.execSQL("CREATE TABLE IF NOT EXISTS info (text VARCHAR, date VARCHAR, cam VARCHAR, id INTEGER PRIMARY KEY)");
            for (int i = 0; i < memo.ds.size(); i++) {
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
        }
        else{
            String s1 = memo.ds.get(noteId);
            ArrayList <Character> cr = new ArrayList<>();
            for(int i1=0;i1<s1.length();i1++) {
                char c = s1.charAt(i1);
                cr.add(c);
                if (c == '\'')
                    cr.add('\'');
            }
            char[] cs = new char[cr.size()];
            for(int x = 0; x < cs.length; x++){
                cs[x] = cr.get(x);
            }
            String output = new String(cs);
            eventsDB.execSQL("UPDATE info " +
                    "SET text = '" + output + "', date = '" + memo.notes.get(noteId) + "', cam = '" + memo.cam.get(noteId) + "' " +
                    "WHERE id = '" + (noteId+1)+"'");}

        eventsDB.close();}

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.load);
                imageView.setImageBitmap(bMap);
                memo.cam.set(noteId,photoFile.getAbsolutePath());
                arToSQL(1);
            }
        }
    }
    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        String imageFileName = "JPEG_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,".jpg",storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }
    private void setPic1() {
        mCurrentPhotoPath = memo.cam.get(noteId);
        imageView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int trW =imageView.getMeasuredWidth();
        int trH =imageView.getMeasuredHeight();
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = Math.min(photoW/trW, photoH/trH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        try {
            ExifInterface exif = new ExifInterface(mCurrentPhotoPath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            }
            else if (orientation == 3) {
                matrix.postRotate(180);
            }
            else if (orientation == 8) {
                matrix.postRotate(270);
            }
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        catch (Exception e) {
        }
        imageView.setImageBitmap(bitmap);
    }

}
