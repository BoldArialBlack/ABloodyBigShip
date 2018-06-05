package com.edu.scnu.hellonotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button textBtn, imgBtn, videoBtn;
    private ListView listView;
    private Intent intent;

    private MyAdapter adapter;
    private NotesDB notesDB;
    private SQLiteDatabase dbReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    public void initView() {
        listView = (ListView) findViewById(R.id.list);
        textBtn = (Button) findViewById(R.id.btn_text);
        imgBtn = (Button) findViewById(R.id.btn_img);
        videoBtn = (Button) findViewById(R.id.btn_video);
        textBtn.setOnClickListener(this);
        imgBtn.setOnClickListener(this);
        videoBtn.setOnClickListener(this);

        notesDB = new NotesDB(this);
        dbReader = notesDB.getReadableDatabase();
    }

    public void selectDB() {
        Cursor cursor = dbReader.query(NotesDB.TABLE_NAME, null, null, null, null, null, null);
        adapter = new MyAdapter(this, cursor);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDB();
    }

    @Override
    public void onClick(View view) {
        intent = new Intent(this, AddActivity.class);
        switch (view.getId()) {
            case R.id.btn_text:
                intent.putExtra("flag","1");
                startActivity(intent);
                break;
            case R.id.btn_img:
                intent.putExtra("flag","2");
                startActivity(intent);
                break;
            case R.id.btn_video:
                intent.putExtra("flag","3");
                startActivity(intent);
                break;
        }
    }
}
