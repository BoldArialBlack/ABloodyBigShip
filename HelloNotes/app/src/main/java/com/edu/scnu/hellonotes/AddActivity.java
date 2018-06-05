package com.edu.scnu.hellonotes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ASUS on 2018/3/29.
 */

public class AddActivity extends Activity implements View.OnClickListener{

    private String val;

    private Button saveBtn, cancelBtn;
    private EditText edit;
    private ImageView imgView;
    private VideoView videoView;

    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;

    private File phoneFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        val = getIntent().getStringExtra("flag");

        saveBtn = (Button) findViewById(R.id.btn_save);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        edit = (EditText) findViewById(R.id.add_edit);
        imgView = (ImageView) findViewById(R.id.add_img);
        videoView = (VideoView) findViewById(R.id.add_video);
        saveBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        notesDB = new NotesDB(this);
        dbWriter = notesDB.getWritableDatabase();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        initView();
    }

    public void initView() {
       if (val.equals("1")) {
           imgView.setVisibility(View.GONE);
           videoView.setVisibility(View.GONE);
       }
       if (val.equals("2")) {
           imgView.setVisibility(View.VISIBLE);
           videoView.setVisibility(View.GONE);
           Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
           phoneFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()
                   + "/" + getTime() + ".jpg");
           intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(phoneFile));
           startActivityForResult(intent, 1);
       }
        if (val.equals("2")) {
            imgView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                addDB();
                finish();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Bitmap bitmap = BitmapFactory.decodeFile(phoneFile.getAbsolutePath());
            imgView.setImageBitmap(bitmap);
        }
    }

    public void addDB() {
        ContentValues cv = new ContentValues();
        cv.put(NotesDB.CONTENT, edit.getText().toString());
        cv.put(NotesDB.TIME, getTime());
        cv.put(NotesDB.PATH, phoneFile + "");
        dbWriter.insert(NotesDB.TABLE_NAME, null, cv);
    }

    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }

}
