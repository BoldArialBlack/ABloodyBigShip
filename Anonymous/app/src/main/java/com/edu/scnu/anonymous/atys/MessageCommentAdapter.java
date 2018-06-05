package com.edu.scnu.anonymous.atys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edu.scnu.anonymous.R;
import com.edu.scnu.anonymous.entities.Comment;
import com.edu.scnu.anonymous.entities.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2018/4/1.
 */

public class MessageCommentAdapter extends BaseAdapter {

    private Context context;
    private List<Comment> comments = new ArrayList<Comment>();

    public MessageCommentAdapter(Context context) {
        this.context = context;
    }

    @Override

    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int i) {
        return comments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public Context getContext() {
        return context;
    }

    public void addAll(List<Comment> data) {
        comments.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        comments.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_time_line_list_cell, null);
            convertView.setTag(new TimelineMessageAdapter.ListCell((TextView) convertView.findViewById(R.id.text_cell_label)));
        }

        TimelineMessageAdapter.ListCell lc = (TimelineMessageAdapter.ListCell) convertView.getTag();

        Comment comment = (Comment) getItem(i);

        lc.getCellLabelTxt().setText(comment.getContent());

        return convertView;
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
