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
import com.edu.scnu.anonymous.net.Publish;

import org.w3c.dom.Text;

public class PublishActivity extends AppCompatActivity {


    private String phone_md5, token;

    private Button publishBtn;
    private EditText msgContentEdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        Intent data = getIntent();
        phone_md5 = data.getStringExtra(Config.KEY_PHONE_MD5);
        token = data.getStringExtra(Config.KEY_TOKEN);

        msgContentEdt = (EditText) findViewById(R.id.edit_msg_content);
        publishBtn = (Button) findViewById(R.id.btn_publish);
        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(msgContentEdt.getText())) {
                    Toast.makeText(PublishActivity.this, R.string.message_content_can_not_be_empty, Toast.LENGTH_SHORT).show();
                    return;
                }

                final ProgressDialog pd = ProgressDialog.show(PublishActivity.this, getResources().getString(R.string.connecting), getResources().getString(R.string.connecting_to_server));
                new Publish(phone_md5, token, msgContentEdt.getText().toString(), new Publish.SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        pd.dismiss();
                        setResult(Config.ACTIVITY_RESULT_NEED_REFRESH);
                        Toast.makeText(PublishActivity.this, getString(R.string.success_to_publish), Toast.LENGTH_LONG).show();
                        finish();
                    }
                }, new Publish.FailCallback() {
                    @Override
                    public void onFail(int errorCode) {
                        pd.dismiss();
                        if (errorCode == Config.RESULT_STATUS_INVALID_TOKEN) {
                            startActivity(new Intent(PublishActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(PublishActivity.this, getString(R.string.fail_to_publish), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
