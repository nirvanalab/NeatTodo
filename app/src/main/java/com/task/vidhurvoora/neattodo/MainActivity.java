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

    ArrayList<String> items;
    ArrayAdapter<String > itemsAdapter;
    ListView lvItems;
    EditText etNewItem;

    private final int TODO_EDIT_REQUEST_CODE = 301;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView)findViewById(R.id.lvItems);
        etNewItem = (EditText)findViewById(R.id.etNewItem);
        items = new ArrayList<>();
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewLongClickListener();
        setupListViewClickListener();

    }

    private void setupListViewClickListener() {
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String content = items.get(position);
                launchEditIntent(content,position);
            }
        });
    }
    private
    void setupListViewLongClickListener() {

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

    private  void readItems() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir,"todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        }
        catch (IOException e) {
            //Inform the user?
            System.out.println(" Read Exception Occurred");
        }
    }

    private  void writeItems() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir,"todo.txt");
        try {
           FileUtils.writeLines(todoFile,items);
        }
        catch (IOException e) {
            //Inform the user?
            System.out.println(" Write Exception Occurred");
        }
    }

    private  void launchEditIntent(String content,int pos) {
        Intent editIntent = new Intent(this,EditItemActivity.class);
        TodoItem item = new TodoItem(content,pos);
        editIntent.putExtra("TodoItem",item);
        startActivityForResult(editIntent,TODO_EDIT_REQUEST_CODE);
    }

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

    public void onAddItem(View view) {
        String todoText = etNewItem.getText().toString();
        itemsAdapter.add(todoText);
        etNewItem.setText("");
        writeItems();
    }
}
