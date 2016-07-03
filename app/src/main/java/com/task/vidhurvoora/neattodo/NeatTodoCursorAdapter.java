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
        TextView tvDateTime = (TextView)view.findViewById(R.id.tvDateTime);
        String itemContent = cursor.getString(cursor.getColumnIndexOrThrow("content"));
        String itemDate = cursor.getString(cursor.getColumnIndexOrThrow("date"));
        Long priority = cursor.getLong(cursor.getColumnIndexOrThrow("priority"));
        if ( priority == 2 ) {
            view.setBackgroundColor(context.getResources().getColor(R.color.lowPriority));
        }
        else  if ( priority == 1 ) {
            view.setBackgroundColor(context.getResources().getColor(R.color.mediumPriority));
        }
        else if ( priority == 0) {
            view.setBackgroundColor(context.getResources().getColor(R.color.highPriority));
        }
        tvContent.setText(itemContent);
        tvDateTime.setText(itemDate);
    }
}
