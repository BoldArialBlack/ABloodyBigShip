package com.edu.scnu.anonymous.atys;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.edu.scnu.anonymous.Config;
import com.edu.scnu.anonymous.R;
import com.edu.scnu.anonymous.entities.Message;
import com.edu.scnu.anonymous.local.MyContacts;
import com.edu.scnu.anonymous.net.Timeline;
import com.edu.scnu.anonymous.net.UploadContacts;
import com.edu.scnu.anonymous.tools.MD5Tool;

import org.json.JSONArray;

import java.util.List;

public class TimeLineActivity extends AppCompatActivity implements ListView.OnItemClickListener{


    private String phone_md5, token, phone_num;
    private ListView listView;
    private TimelineMessageAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        adapter = new TimelineMessageAdapter(this);
        listView = (ListView) findViewById(R.id.list_timeline);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        phone_num = getIntent().getStringExtra(Config.KEY_PHONE_NUM);
        token = getIntent().getStringExtra(Config.KEY_TOKEN);
        phone_md5 = MD5Tool.md5(phone_num);


        final ProgressDialog pd = ProgressDialog.show(this, getResources().getString(R.string.connecting), getResources().getString(R.string.connecting_to_server));

        UploadContacts uploadContacts = new UploadContacts(phone_md5, token, MyContacts.getContactsJSONString(this), new UploadContacts.SuccessCallback() {
            @Override
            public void onSuccess() {
                loadMessage();

                pd.dismiss();
            }
        }, new UploadContacts.FailCallback() {
            @Override
            public void onFail(int errorCode) {
                pd.dismiss();
                if (errorCode == Config.RESULT_STATUS_INVALID_TOKEN) {
                    startActivity(new Intent(TimeLineActivity.this, LoginActivity.class));
                    finish();
                } else {
                    loadMessage();
                }
            }
        });
    }

    private void loadMessage() {
        Log.e("TIMELINE", "loadMessage>>>>>>>");

        final ProgressDialog pd = ProgressDialog.show(this, getResources().getString(R.string.connecting), getResources().getString(R.string.connecting_to_server));

        new Timeline(phone_md5, token, 1, 20, new Timeline.SuccessCallback() {

            @Override
            public void onSuccess(int page, int perpage, List<Message> timeline) {
                pd.dismiss();
                adapter.clear();
                adapter.addAll(timeline);
                Toast.makeText(TimeLineActivity.this, R.string.success_to_load_timeline_data, Toast.LENGTH_LONG).show();

            }
        }, new Timeline.FailCallback() {
            @Override
            public void onFail(int errorCode) {
                pd.dismiss();

                if (errorCode == Config.RESULT_STATUS_INVALID_TOKEN) {
                    startActivity(new Intent(TimeLineActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(TimeLineActivity.this, R.string.fail_to_load_timeline_data, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return super.onCreateOptionsMenu(menu);
//        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_show_publish_activity:
                Intent intent = new Intent(TimeLineActivity.this, PublishActivity.class);
                intent.putExtra(Config.KEY_PHONE_MD5, phone_md5);
                intent.putExtra(Config.KEY_TOKEN, token);
                startActivityForResult(intent, 0);
                break;
            default:
                break;
        }
//        return super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Config.ACTIVITY_RESULT_NEED_REFRESH:
                loadMessage();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Message msg = adapter.getItem(i);
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra(Config.KEY_MSG, msg.getMsg());
        intent.putExtra(Config.KEY_MSG_ID, msg.getMsgId());
        intent.putExtra(Config.KEY_PHONE_MD5, msg.getPhone_md5());
        intent.putExtra(Config.KEY_TOKEN, token);
        startActivity(intent);
    }
}
