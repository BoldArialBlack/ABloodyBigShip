package com.edu.scnu.anonymous.local;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.edu.scnu.anonymous.Config;
import com.edu.scnu.anonymous.tools.MD5Tool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ASUS on 2018/3/30.
 */

public class MyContacts {

    public static String getContactsJSONString(Context context) {
        Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        String phoneNum;
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;

        while (c.moveToNext()) {
            phoneNum = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            if (phoneNum.charAt(0) == '+' &&
                    phoneNum.charAt(1) == '8' &&
                    phoneNum.charAt(2) == '6') {
                phoneNum = phoneNum.substring(3);
            }

            Log.e("PHONE NUM", "getContactsJSONString: " + phoneNum );

             jsonObject = new JSONObject();
            try {
                jsonObject.put(Config.KEY_PHONE_MD5, MD5Tool.md5(phoneNum));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            jsonArray.put(jsonObject);
        }

        return jsonArray.toString();
    }
}
