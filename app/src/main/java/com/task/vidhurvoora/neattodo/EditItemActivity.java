package com.task.vidhurvoora.neattodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class EditItemActivity extends AppCompatActivity {

    private EditText etItem;
    private DatePicker dpTodo;
    private TimePicker tpTodo;
    private TodoItem editTodoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etItem = (EditText) findViewById(R.id.etItem);
        dpTodo = (DatePicker)findViewById(R.id.dpTodo);
        tpTodo = (TimePicker)findViewById(R.id.tpTodo);
        TodoItem todoItem = (TodoItem) getIntent().getSerializableExtra("TodoItem");
        this.editTodoItem = todoItem;
        Date todoDate = todoItem.itemDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(todoDate);

        dpTodo.updateDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
        tpTodo.setCurrentHour(cal.get(Calendar.HOUR));
        tpTodo.setCurrentMinute(cal.get(Calendar.MINUTE));
        //tpTodo.setHour(cal.get(Calendar.HOUR));
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
        Calendar cal = Calendar.getInstance();
        cal.set(dpTodo.getYear(),dpTodo.getMonth(),dpTodo.getDayOfMonth(),tpTodo.getCurrentHour(),tpTodo.getCurrentMinute());
        Date todoDate = cal.getTime();
        this.editTodoItem.itemDate = todoDate;
        TodoDatabaseHelper.getInstance(this).updateTodo(this.editTodoItem);
        this.finish();
    }

    public void onCancel(View view) {
        this.finish();
    }
}
