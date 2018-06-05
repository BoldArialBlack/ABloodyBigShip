package com.edu.scnu.anonymous.atys;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.scnu.anonymous.Config;
import com.edu.scnu.anonymous.R;
import com.edu.scnu.anonymous.entities.Comment;
import com.edu.scnu.anonymous.entities.Message;
import com.edu.scnu.anonymous.net.GetComment;
import com.edu.scnu.anonymous.net.PubComment;
import com.edu.scnu.anonymous.tools.MD5Tool;

import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private String phone_md5, msg, msgId, token;

    private TextView messageTxt;
    private ListView msgList;
    private Button sendBtn;
    private EditText commentEdt;
    private MessageCommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        msgList = (ListView) findViewById(R.id.list_msg);
        adapter = new MessageCommentAdapter(this);
        msgList.setAdapter(adapter);

        messageTxt = (TextView) findViewById(R.id.text_message);

        Intent data = getIntent();
        phone_md5 = data.getStringExtra(Config.KEY_PHONE_MD5);
        msg = data.getStringExtra(Config.KEY_MSG);
        msgId = data.getStringExtra(Config.KEY_MSG_ID);
        token = data.getStringExtra(Config.KEY_TOKEN);

        messageTxt.setText(msg);

        getComments();

        sendBtn = (Button) findViewById(R.id.btn_send_comment);
        commentEdt = (EditText) findViewById(R.id.edit_comment);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(commentEdt.getText())) {
                    Toast.makeText(MessageActivity.this, R.string.comment_content_can_not_be_empty, Toast.LENGTH_LONG).show();
                    return;
                }

                final ProgressDialog pd = ProgressDialog.show(MessageActivity.this, getResources().getString(R.string.connecting), getResources().getString(R.string.connecting_to_server));
                new PubComment(MD5Tool.md5(Config.getCachedPhoneNum(MessageActivity.this)), token, commentEdt.getText().toString(), msgId, new PubComment.SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        pd.dismiss();
                        commentEdt.setText("");
                        getComments();
                    }
                }, new PubComment.FailCallback() {
                    @Override
                    public void onFail(int errorCode) {
                        pd.dismiss();
                        if (errorCode == Config.RESULT_STATUS_INVALID_TOKEN) {
                            startActivity(new Intent(MessageActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(MessageActivity.this, R.string.fail_to_pub_comment, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    private void getComments() {
        final ProgressDialog pd = ProgressDialog.show(MessageActivity.this, getResources().getString(R.string.connecting), getResources().getString(R.string.connecting_to_server));
        new GetComment(phone_md5, token, msgId, 1, 20, new GetComment.SuccessCallback() {
            @Override
            public void onSuccess(String msgId, int page, int perpage, List<Comment> comments) {
                Log.e("MESSAGE_ACTIVITY", "onSuccess(msgId, page, perpage, comments)" );
                pd.dismiss();

                adapter.clear();
                adapter.addAll(comments);
            }
        }, new GetComment.FailCallback() {
            @Override
            public void onFail(int errorCode) {
                if (errorCode == Config.RESULT_STATUS_INVALID_TOKEN) {
                    startActivity(new Intent(MessageActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(MessageActivity.this, getString(R.string.fail_to_get_comment), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
