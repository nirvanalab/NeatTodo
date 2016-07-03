package com.task.vidhurvoora.neattodo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


public class EditItemActivity extends AppCompatActivity implements OnItemSelectedListener {

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

        //setup the spinner
        Spinner spinner = (Spinner) findViewById(R.id.priority_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, R.layout.spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setSelection((int)todoItem.priority);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               updateBackgroundColor((long)position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        this.updateBackgroundColor(todoItem.priority);
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
        Spinner spinner = (Spinner) findViewById(R.id.priority_spinner);
        this.editTodoItem.priority = spinner.getSelectedItemPosition();
        TodoDatabaseHelper.getInstance(this).updateTodo(this.editTodoItem);
        this.finish();
    }

    private void updateBackgroundColor(long priority) {
        if (priority == 2 ) {
            dpTodo.getRootView().setBackgroundColor(this.getResources().getColor(R.color.lowPriority));

        }
        else  if ( priority == 1 ) {
            dpTodo.getRootView().setBackgroundColor(this.getResources().getColor(R.color.mediumPriority));
        }
        else if ( priority == 0) {
            dpTodo.getRootView().setBackgroundColor(this.getResources().getColor(R.color.highPriority));
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        if (pos == 0 ) {
            dpTodo.getRootView().setBackgroundColor(Color.parseColor("#FFFF00"));
        }
        else  if (pos == 1 ) {
            dpTodo.getRootView().setBackgroundColor(Color.parseColor("#ff7400"));
        }
        else if ( pos == 2) {
            dpTodo.getRootView().setBackgroundColor(Color.parseColor("##ff1a00"));
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
    public void onCancel(View view) {
        this.finish();
    }
}
