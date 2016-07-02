package com.task.vidhurvoora.neattodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by vidhurvoora on 6/20/16.
 */
public class TodoDatabaseHelper extends SQLiteOpenHelper {

    //singleton instance
    private static TodoDatabaseHelper sInstance;

    // Database Info
    private static final String DATABASE_NAME = "todoDatabase";
    private static final int DATABASE_VERSION = 3;

    // Table Name
    private static final String TABLE_TODO = "todoItem";

    // Post Table Column
    private static final String KEY_TODO_ID = "id";
    private static final String KEY_TODO_CONTENT = "content";
    private static final String KEY_TODO_DATE = "date";

    //Singleton instance
    public static synchronized TodoDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TodoDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private TodoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // These is where we need to write create table statements.
    // This is called when database is created.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO +
                "(" +
                KEY_TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + // Define a primary key
                KEY_TODO_DATE + " DATE, "+
                KEY_TODO_CONTENT + " TEXT "+ ")";

        db.execSQL(CREATE_TODO_TABLE);
    }

    // This method is called when database is upgraded like
    // modifying the table structure,
    // adding constraints to database, etc
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        // SQL for upgrading the tables
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
            onCreate(db);
        }
    }


    //CRUD operations

    private String getFormattedDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        String dateStr = sdf.format(date);
        return dateStr;
    }
    //add todoitem
    public long addTodo(TodoItem todoItem) {
        SQLiteDatabase db = getWritableDatabase();
        long itemId = -1;
        //insert in transaction
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TODO_CONTENT,todoItem.itemContent);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
            String date = sdf.format(todoItem.itemDate);
            values.put(KEY_TODO_DATE,date);
            itemId = db.insertOrThrow(TABLE_TODO,null,values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            //Exception occurred
        } finally {
            db.endTransaction();
        }
        return itemId;
    }

    //update todoitem
    public long updateTodo(TodoItem todoItem) {
        SQLiteDatabase db = getWritableDatabase();
        long itemId = todoItem.itemId;
        //update in transaction
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TODO_CONTENT,todoItem.itemContent);
            String dateStr = getFormattedDate(todoItem.itemDate);
            values.put(KEY_TODO_DATE,dateStr);
            int rows = db.update(TABLE_TODO,values,KEY_TODO_ID + " = ?",new String[]{Long.toString(todoItem.itemId)});
            if ( rows == 1) {
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
           //Exception occurred
        } finally {
            db.endTransaction();
        }
        return itemId;
    }

    //delete todoitem
    public void deleteTodo(String  todoId) {
        SQLiteDatabase db = getWritableDatabase();
        //delete in transaction
        db.beginTransaction();
        try {
            db.delete(TABLE_TODO,KEY_TODO_ID + " = ?",new String[]{todoId});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            //Exception occurred
        } finally {
            db.endTransaction();
        }
        return;
    }

    //fetch all todoitems
//    public ArrayList<TodoItem> getAllTodoItems() {
//        ArrayList<TodoItem> todoItems = new ArrayList<TodoItem>();
//        String TODO_SELECT_QUERY = String.format("SELECT * FROM %s",TABLE_TODO);
//        SQLiteDatabase db = getWritableDatabase();
//        Cursor cursor = db.rawQuery(TODO_SELECT_QUERY,null);
//        try {
//            if ( cursor.moveToFirst()) {
//                do {
//                    String itemContent = cursor.getString(cursor.getColumnIndex(KEY_TODO_CONTENT));
//                    long itemId = cursor.getLong(cursor.getColumnIndex(KEY_TODO_ID));
//                    TodoItem item = new TodoItem(itemContent,itemId);
//                    todoItems.add(item);
//                }while (cursor.moveToNext());
//            }
//        }catch (Exception e) {
//
//        }finally {
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//        }
//        return todoItems;
//    }

    public TodoItem getTodoItem(String id) {
        String TODO_SELECT_QUERY = String.format("SELECT * FROM %s WHERE %s = %s",TABLE_TODO,KEY_TODO_ID,id);
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(TODO_SELECT_QUERY,null);
        TodoItem item = null;
        try {
            if ( cursor.moveToFirst()) {

                    String itemContent = cursor.getString(cursor.getColumnIndex(KEY_TODO_CONTENT));
                    long itemId = cursor.getLong(cursor.getColumnIndex(KEY_TODO_ID));
                    String itemDateStr = cursor.getString(cursor.getColumnIndex(KEY_TODO_DATE));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                format.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                Date date = format.parse(itemDateStr);
                item = new TodoItem(itemContent,itemId,date);
            }
        }catch (Exception e) {

        }finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return item;
    }

    public Cursor getTodoItemsCursor(){
        String TODO_SELECT_QUERY = String.format("SELECT id _id,* FROM %s",TABLE_TODO);
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(TODO_SELECT_QUERY,null);
        return cursor;
    }
}
