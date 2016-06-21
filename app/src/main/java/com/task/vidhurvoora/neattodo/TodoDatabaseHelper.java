package com.task.vidhurvoora.neattodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vidhurvoora on 6/20/16.
 */
public class TodoDatabaseHelper extends SQLiteOpenHelper {

    //singleton instance
    private static TodoDatabaseHelper sInstance;

    // Database Info
    private static final String DATABASE_NAME = "todoDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    private static final String TABLE_TODO = "todoItem";

    // Post Table Column
    private static final String KEY_TODO_ID = "id";
    private static final String KEY_TODO_CONTENT = "content";
    private static final String KEY_TODO_POSITION = "position";

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
                KEY_TODO_CONTENT + " TEXT ," + // Define a foreign key
                KEY_TODO_POSITION + " INTEGER" +
                ")";

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

    //add todoitem
    public long addTodo(TodoItem todoItem) {
        SQLiteDatabase db = getWritableDatabase();
        long itemId = -1;
        //insert in transaction
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TODO_CONTENT,todoItem.itemContent);
            values.put(KEY_TODO_POSITION,todoItem.itemPosition);
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
            values.put(KEY_TODO_POSITION,todoItem.itemPosition);
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
    public void deleteTodo(TodoItem todoItem) {
        SQLiteDatabase db = getWritableDatabase();
        //delete in transaction
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TODO_CONTENT,todoItem.itemContent);
            values.put(KEY_TODO_POSITION,todoItem.itemPosition);
            db.delete(TABLE_TODO,KEY_TODO_ID + " = ?",new String[]{Long.toString(todoItem.itemId)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            //Exception occurred
        } finally {
            db.endTransaction();
        }
        return;
    }

    //fetch all todoitems
    public List<TodoItem> getAllTodoItems() {
        List<TodoItem> todoItems = new ArrayList<>();
        String TODO_SELECT_QUERY = String.format("SELECT * FROM %s",TABLE_TODO);
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(TODO_SELECT_QUERY,null);
        try {
            if ( cursor.moveToFirst()) {
                do {
                    String itemContent = cursor.getString(cursor.getColumnIndex(KEY_TODO_CONTENT));
                    int itemPosition = cursor.getInt(cursor.getColumnIndex(KEY_TODO_POSITION));
                    long itemId = cursor.getLong(cursor.getColumnIndex(KEY_TODO_ID));
                    TodoItem item = new TodoItem(itemContent,itemPosition,itemId);
                    todoItems.add(item);
                }while (cursor.moveToNext());
            }
        }catch (Exception e) {

        }finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return todoItems;
    }

}
