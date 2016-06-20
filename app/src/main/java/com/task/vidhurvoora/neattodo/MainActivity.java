package com.task.vidhurvoora.neattodo;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> items; //acts as the modal which feeds the data to the view
    private ArrayAdapter<String > itemsAdapter; //connects the model to the view
    private ListView lvItems; //view item
    private EditText etNewItem; //view item

    private final int TODO_EDIT_REQUEST_CODE = 301;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //associate the lvItems to the actual view using the id
        lvItems = (ListView)findViewById(R.id.lvItems);
        etNewItem = (EditText)findViewById(R.id.etNewItem);
        items = new ArrayList<>();
        //read the items from the file
        readItems();
        //set the adapter to link between the view and the items arraylist
        itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        lvItems.setAdapter(itemsAdapter);
        //setup the long click listener to delete
        setupListViewLongClickListener();
        //setup the click listener to edit item
        setupListViewClickListener();

    }

    //Handles the call back after the item has been edited.Fetches the changed item from the
    //extra.

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TODO_EDIT_REQUEST_CODE && resultCode == RESULT_OK ) {
            TodoItem item = (TodoItem)data.getSerializableExtra("TodoItem");
            items.set(item.itemPosition,item.itemContent);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
            Toast.makeText(this,"Succesfully saved the edit!",Toast.LENGTH_LONG).show();
        }
    }
    //Sets up the Item Click handler when a item in the todolist is clicked.
    //launches the edit screen with the selected information
    private void setupListViewClickListener() {
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String content = items.get(position);
                launchEditIntent(content,position);
            }
        });
    }
    //Sets up the Item long click handler to delete an item in the todolist
    private void setupListViewLongClickListener() {

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

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

        itemsAdapter.add(todoText);
        etNewItem.setText("");
        writeItems();
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
    private  void launchEditIntent(String content,int pos) {
        Intent editIntent = new Intent(this,EditItemActivity.class);
        TodoItem item = new TodoItem(content,pos);
        editIntent.putExtra("TodoItem",item);
        startActivityForResult(editIntent,TODO_EDIT_REQUEST_CODE);
    }




}
