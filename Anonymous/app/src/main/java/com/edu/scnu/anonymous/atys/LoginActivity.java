package com.edu.scnu.anonymous.atys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.edu.scnu.anonymous.Config;
import com.edu.scnu.anonymous.R;
import com.edu.scnu.anonymous.net.GetCode;
import com.edu.scnu.anonymous.net.Login;
import com.edu.scnu.anonymous.tools.MD5Tool;

public class LoginActivity extends AppCompatActivity {

    private Button getCodeBtn, loginBtn;
    private EditText phoneEdit, codeEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneEdit = (EditText) findViewById(R.id.edit_phone);
        getCodeBtn = (Button) findViewById(R.id.btn_get_code);
        getCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(phoneEdit.getText())) {
                    Toast.makeText(LoginActivity.this, getString(R.string.phone_num_can_not_be_empty), Toast.LENGTH_LONG).show();
                    return;
                }

                final ProgressDialog pd = ProgressDialog.show(LoginActivity.this, getString(R.string.connecting), getString(R.string.connecting_to_server));

                GetCode getCode = new GetCode(phoneEdit.getText().toString(), new GetCode.SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        pd.dismiss();
                        Toast.makeText(LoginActivity.this, R.string.success_to_get_code, Toast.LENGTH_LONG).show();
                    }
                }, new GetCode.FailCallback() {
                    @Override
                    public void onFail() {
                        pd.dismiss();
                        Toast.makeText(LoginActivity.this, R.string.fail_to_get_code, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        codeEdit = (EditText) findViewById(R.id.edit_code);
        loginBtn = (Button) findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(phoneEdit.getText())) {
                    Toast.makeText(LoginActivity.this, getString(R.string.phone_num_can_not_be_empty), Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(codeEdit.getText())) {
                    Toast.makeText(LoginActivity.this, R.string.code_can_not_be_empty, Toast.LENGTH_LONG).show();
                    return;
                }

                final ProgressDialog pd = ProgressDialog.show(LoginActivity.this, getString(R.string.connecting), getString(R.string.connecting_to_server));

                new Login(MD5Tool.md5(phoneEdit.getText().toString()), codeEdit.getText().toString(), new Login.SuccessCallback() {
                    @Override
                    public void onSuccess(String token) {

                        pd.dismiss();

                        Toast.makeText(LoginActivity.this, R.string.success_to_login, Toast.LENGTH_LONG).show();

                        Config.cacheToken(LoginActivity.this, token);
                        Config.cachePhoneNum(LoginActivity.this, phoneEdit.getText().toString());

                        Intent intent = new Intent(LoginActivity.this, TimeLineActivity.class);
                        intent.putExtra(Config.KEY_TOKEN, token);
                        intent.putExtra(Config.KEY_PHONE_NUM, phoneEdit.getText().toString());
                        startActivity(intent);

                        finish();
                    }
                }, new Login.FailCallback() {
                    @Override
                    public void onFail() {
                        pd.dismiss();

                        Toast.makeText(LoginActivity.this, R.string.fail_to_login, Toast.LENGTH_LONG).show();
                        return;
                    }
                });
            }
        });
    }
}
