package com.task.vidhurvoora.neattodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

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

    public void onSaveItem(View view) {
        Intent editIntent = new Intent();
        int todoPosition = editTodoItem.itemPosition;
        String todoContent = etItem.getText().toString();
        TodoItem modifiedTodo = new TodoItem(todoContent,todoPosition);
        editIntent.putExtra("TodoItem",modifiedTodo);
        setResult(RESULT_OK,editIntent);
        this.finish();
    }
}
