package com.edu.scnu.phonebook;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2018/3/28.
 */

public class GetNumber {

    public static List<PhoneInfo> lists = new ArrayList<PhoneInfo>();

    public static List<PhoneInfo> getNumber(Context context) {
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        String phoneName;
        String phoneNumber;
        while (cursor.moveToNext()) {
            phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            System.out.println(phoneNumber);
            Log.e("PhoneBook",phoneName + phoneNumber);

            PhoneInfo phoneInfo = new PhoneInfo(phoneName, phoneNumber);
            lists.add(phoneInfo);
        }
        return lists;
    }

}
