package com.edu.scnu.anonymous.atys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edu.scnu.anonymous.R;
import com.edu.scnu.anonymous.entities.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2018/3/31.
 */

public class TimelineMessageAdapter extends BaseAdapter {

    private Context context;
    private List<Message> data = new ArrayList<Message>();

    public TimelineMessageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Message getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_time_line_list_cell, null);
            convertView.setTag(new ListCell((TextView) convertView.findViewById(R.id.text_cell_label)));
        }

        ListCell lc = (ListCell) convertView.getTag();

        Message msg = getItem(i);

        lc.getCellLabelTxt().setText(msg.getMsg());

        return convertView;
    }

    public Context getContext() {
        return context;
    }

    public void addAll(List<Message> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public static class ListCell {
        public TextView cellLabelTxt;

        public ListCell(TextView cellLabelTxt) {
            this.cellLabelTxt = cellLabelTxt;
        }

        public TextView getCellLabelTxt() {
            return cellLabelTxt;
        }
    }
}
