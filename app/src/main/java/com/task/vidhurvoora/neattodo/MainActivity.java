package com.task.vidhurvoora.neattodo;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> items; //acts as the modal which feeds the data to the view
    private ArrayAdapter<String > itemsAdapter; //connects the model to the view
    private NeatTodoCursorAdapter todoCursorAdapter;
    private ListView lvItems; //view item
    private EditText etNewItem; //view item
    private TodoDatabaseHelper todoDbHelper;
    private Cursor todoCursor;
    private final int TODO_EDIT_REQUEST_CODE = 301;
    public Calendar currentCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todoDbHelper = TodoDatabaseHelper.getInstance(this);
        //associate the lvItems to the actual view using the id
        lvItems = (ListView)findViewById(R.id.lvItems);
        etNewItem = (EditText)findViewById(R.id.etNewItem);
        items = new ArrayList<>();
        //read the items from the file
        TodoDatabaseHelper helper = TodoDatabaseHelper.getInstance(this);
        todoCursor = helper.getTodoItemsCursor();
        todoCursorAdapter = new NeatTodoCursorAdapter(this,todoCursor);
        lvItems.setAdapter(todoCursorAdapter);
        //setup the long click listener to delete
        setupListViewLongClickListener();
        //setup the click listener to edit item
        setupListViewClickListener();
        currentCalendar = Calendar.getInstance();

    }

    public void onTimeSet()
    {
        String todoText = etNewItem.getText().toString();
        Date date = currentCalendar.getTime();
        int minute = currentCalendar.get(Calendar.MINUTE);
        //default to medium priority
        TodoItem item = new TodoItem(todoText,date,2);
        todoDbHelper.addTodo(item);
        reload();
        etNewItem.setText("");
    }
    //Handles the call back after the item has been edited.Fetches the changed item from the
    //extra.

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TODO_EDIT_REQUEST_CODE ) {
            reload();
            Toast.makeText(this,"Succesfully saved the edit!",Toast.LENGTH_LONG).show();
        }
    }
    //Sets up the Item Click handler when a item in the todolist is clicked.
    //launches the edit screen with the selected information
    private void setupListViewClickListener() {
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem item = todoDbHelper.getTodoItem(Long.toString(id));
                launchEditIntent(item);
            }
        });
    }
    //Sets up the Item long click handler to delete an item in the todolist
    private void setupListViewLongClickListener() {

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoDbHelper.deleteTodo(Long.toString(id));
                reload();
                return true;
            }
        });

    }

    public void reload() {
        Cursor selectCursor = TodoDatabaseHelper.getInstance(this).getTodoItemsCursor();
        todoCursorAdapter.changeCursor(selectCursor);
        todoCursor = selectCursor;
    }
    //Add item button click handler
    //Adds item to the list view and persists item by writing it to a file
    public void onAddItem(View view) {
        String todoText = etNewItem.getText().toString();

        //check if its length is 0, if so raise a toast
        if (todoText.length() == 0 ) {
            Toast.makeText(this,"Please enter atleast one character to create a Todo item",Toast.LENGTH_LONG).show();
            return;
        }

        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(),"time picker");

        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getFragmentManager(),"date picker");


//        DateTime dt = DateTime.now();
//        Date currentDate = dt.toLocalDate().toDate();
//        Calendar cal = Calendar.getInstance();
//
//        //cal.add(Calendar.HOUR_OF_DAY,2);
//        Date oneHourFromNow = cal.getTime();
//        TodoItem item = new TodoItem(todoText,oneHourFromNow);
//        todoDbHelper.addTodo(item);
//        reload();
//        etNewItem.setText("");
    }

    //Utility method to read todoitems from a file
    private  void readItems() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir,"todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        }
        catch (IOException e) {
            //Inform the user?
            Toast.makeText(this,"Some Exception occurred while displaying Todo items.Please try relaunching",Toast.LENGTH_LONG).show();
        }
    }

    //Utility method to write todoitems to a file
    private  void writeItems() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir,"todo.txt");
        try {
           FileUtils.writeLines(todoFile,items);
        }
        catch (IOException e) {
            //Inform the user?
            Toast.makeText(this,"Some Exception occurred while storing Todo items.Please try relaunching",Toast.LENGTH_LONG).show();
        }
    }

    //Launches the Edit Todoitem activity and sends the corresponding item as extra
    private  void launchEditIntent(TodoItem item) {
        Intent editIntent = new Intent(this,EditItemActivity.class);
        editIntent.putExtra("TodoItem",item);
        startActivityForResult(editIntent,TODO_EDIT_REQUEST_CODE);
    }




}
