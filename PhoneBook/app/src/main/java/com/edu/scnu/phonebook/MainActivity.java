package com.edu.scnu.phonebook;

import android.Manifest;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewOutlineProvider;
import android.widget.ListView;
import android.widget.Toast;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getNumber();
        listView = (ListView) findViewById(R.id.lv);
        adapter = new MyAdapter(GetNumber.lists, this);
        listView.setAdapter(adapter);
    }

    @NeedsPermission(Manifest.permission.READ_CONTACTS)
    void getNumber() {
        GetNumber.getNumber(this);
    }

    @OnShowRationale(Manifest.permission.READ_CONTACTS)
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("是否允许获取通讯录权限？")
                .setPositiveButton("允许", (dialog, button) -> request.proceed())
                .setNegativeButton("拒绝", (dialog, button) -> request.cancel())
                .show();
    }

    @OnPermissionDenied(Manifest.permission.READ_CONTACTS)
    void showDeniedForCamera() {
        Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.READ_CONTACTS)
    void showNeverAskForCamera() {
        Toast.makeText(this,"Permission neverAsk", Toast.LENGTH_SHORT).show();
    }
}
