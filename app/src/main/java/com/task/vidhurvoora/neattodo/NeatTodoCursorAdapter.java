package com.task.vidhurvoora.neattodo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by vidhurvoora on 6/22/16.
 */
public class NeatTodoCursorAdapter extends CursorAdapter {

    public NeatTodoCursorAdapter (Context context,Cursor cursor) {
        super(context,cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_neattodo,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvContent = (TextView) view.findViewById(R.id.tvContent);
        String itemContent = cursor.getString(cursor.getColumnIndexOrThrow("content"));
        tvContent.setText(itemContent);
    }
}
