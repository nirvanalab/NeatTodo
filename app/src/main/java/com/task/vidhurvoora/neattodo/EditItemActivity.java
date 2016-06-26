package com.task.vidhurvoora.neattodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditItemActivity extends AppCompatActivity {

    private EditText etItem;
    private TodoItem editTodoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etItem = (EditText) findViewById(R.id.etItem);
        TodoItem todoItem = (TodoItem) getIntent().getSerializableExtra("TodoItem");
        this.editTodoItem = todoItem;
        etItem.setText(todoItem.itemContent);
    }

    //Sends the edited content back to the Main Activity
    public void onSaveItem(View view) {
        String todoContent = etItem.getText().toString();

        //Check if the content length is equal to 0, if so raise a toast
        if ( todoContent.length() == 0 ) {
            Toast.makeText(this,"The Todoitem should have atleast one character",Toast.LENGTH_LONG).show();
            return;
        }

        this.editTodoItem.itemContent = todoContent;
        TodoDatabaseHelper.getInstance(this).updateTodo(this.editTodoItem);
        this.finish();
    }
}
