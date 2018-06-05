package com.edu.scnu.hellonotes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * Created by ASUS on 2018/3/29.
 */

public class MyAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;

    private LinearLayout layout;

    public MyAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int i) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.view_img);
            holder.videoView = (ImageView) convertView.findViewById(R.id.view_video);
            holder.contentTv = (TextView) convertView.findViewById(R.id.text_content);
            holder.timeTv = (TextView) convertView.findViewById(R.id.text_time);
            cursor.moveToPosition(i);
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String url = cursor.getString(cursor.getColumnIndex("path"));
            holder.contentTv.setText(content);
            holder.timeTv.setText(time);
            holder.imageView.setImageBitmap(getImageThumbnail(url, 200, 200));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            cursor.moveToPosition(i);
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String url = cursor.getString(cursor.getColumnIndex("path"));
            holder.contentTv.setText(content);
            holder.timeTv.setText(time);
            holder.imageView.setImageBitmap(getImageThumbnail(url, 200, 200));
        }
        return convertView;

       /* LayoutInflater inflater = LayoutInflater.from(context);
        layout = (LinearLayout) inflater.inflate(R.layout.item, null);
        ImageView imageView = (ImageView) layout.findViewById(R.id.view_img);
        ImageView videoView = (ImageView) layout.findViewById(R.id.view_video);
        TextView contentTv = (TextView) layout.findViewById(R.id.text_content);
        TextView timeTv = (TextView) layout.findViewById(R.id.text_time);
        cursor.moveToPosition(i);
        String content = cursor.getString(cursor.getColumnIndex("content"));
        String time = cursor.getString(cursor.getColumnIndex("time"));
        contentTv.setText(content);
        timeTv.setText(time);

        return layout;*/
    }

    public Bitmap getImageThumbnail(String uri, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(uri, options);
        options.inJustDecodeBounds = false;
        int beWidth = options.outWidth / width;
        int beHeight = options.outHeight / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else  {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(uri, options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    private static class ViewHolder{
        ImageView imageView;
        ImageView videoView;
        TextView contentTv;
        TextView timeTv;
    }
}
