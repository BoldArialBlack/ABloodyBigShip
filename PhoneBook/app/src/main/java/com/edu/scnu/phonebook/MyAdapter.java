package com.edu.scnu.phonebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by ASUS on 2018/3/28.
 */

public class MyAdapter extends BaseAdapter {

    private List<PhoneInfo> lists;
    private Context context;
    private LinearLayout layout;

    public MyAdapter(List<PhoneInfo> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
/*        LayoutInflater inflater = LayoutInflater.from(context);
        layout = (LinearLayout) inflater.inflate(R.layout.item_phone, null);
        TextView nameTxt = (TextView) layout.findViewById(R.id.text_name);
        TextView numberTxt = (TextView) layout.findViewById(R.id.text_number);
        nameTxt.setText(lists.get(i).getName());
        numberTxt.setText(lists.get(i).getNumber());
        return layout;*/
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_phone, null);
            holder = new ViewHolder();
            holder.nameTv = (TextView) convertView.findViewById(R.id.text_name);
            holder.numberTv = (TextView) convertView.findViewById(R.id.text_number);
            holder.nameTv.setText(lists.get(i).getName());
            holder.numberTv.setText(lists.get(i).getNumber());
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.nameTv.setText(lists.get(i).getName());
            holder.numberTv.setText(lists.get(i).getNumber());
        }
        return convertView;
    }
    private static class ViewHolder {
        TextView nameTv;
        TextView numberTv;
    }

}
