package com.edu.scnu.anonymous;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.edu.scnu.anonymous.atys.LoginActivity;
import com.edu.scnu.anonymous.atys.TimeLineActivity;
import com.edu.scnu.anonymous.local.MyContacts;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.e("MYCONTACTS", MyContacts.getContactsJSONString(this));


        String token = Config.getCachedToken(this);
        String phone_num = Config.getCachedPhoneNum(this);

//        startActivity(new Intent(this, LoginActivity.class));

        if (token != null && phone_num != null) {
            Intent intent = new Intent(this, TimeLineActivity.class);
            intent.putExtra(Config.KEY_TOKEN, token);
            intent.putExtra(Config.KEY_PHONE_NUM, phone_num);
            startActivity(intent);
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
