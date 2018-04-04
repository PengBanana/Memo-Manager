package com.example.hp.memomanagerapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class newMemoActivity extends AppCompatActivity {
    public EditText et_date, et_time, et_title, et_priority, et_note;
    public Spinner et_status, et_intervals, et_category;
    //date picker variables
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    SimpleDateFormat tf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date;
    TimePickerDialog.OnTimeSetListener time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_memo);
        setnewMemoUI();
        setActionListers();
    }

    private void setActionListers() {

        Button btn_submit = (Button) findViewById(R.id.btn_Submit);
        Button btn_cancel = (Button) findViewById(R.id.btn_Cancel);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                String title=et_title.getText().toString();
                String category=et_category.getSelectedItem().toString();
                String deadline=et_date.getText().toString();
                String priorityLevel=et_priority.getText().toString();
                String notificationIntervals=et_intervals.getSelectedItem().toString();
                String notificationTime=et_time.getText().toString();
                String status=et_status.getSelectedItem().toString();
                String note=et_note.getText().toString();
                //Memo newItem = new Memo(title , category,deadline, priorityLevel, notificationIntervals, notificationTime, status, note);
                //MySQLiteHelper db = new MySQLiteHelper(view.getContext());
                //db.addMemo(newItem);
                returnIntent.putExtra(Memo.TITLE_CODE, title);
                returnIntent.putExtra(Memo.CATEGORY_CODE, category);
                returnIntent.putExtra(Memo.DEADLINE_CODE, deadline);
                returnIntent.putExtra(Memo.PRIORITYLEVEL_CODE, priorityLevel);
                returnIntent.putExtra(Memo.NOTIFICATIONINTERVALS_CODE, notificationIntervals);
                returnIntent.putExtra(Memo.NOTIFICATIONTIME_CODE, notificationTime);
                returnIntent.putExtra(Memo.STATUS_CODE, status);
                returnIntent.putExtra(Memo.NOTE_CODE, note);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void setnewMemoUI() {
        et_title = (EditText) findViewById(R.id.pt_Title);
        et_priority = (EditText) findViewById(R.id.pt_priority);
        et_note = (EditText) findViewById(R.id.pt_note);
        et_status =(Spinner) findViewById(R.id.sr_status);
        et_category =(Spinner) findViewById(R.id.sr_category);
        et_intervals =(Spinner) findViewById(R.id.sr_intervals);
        //Time picker
        long currentdate = System.currentTimeMillis();
        String dateString = sdf.format(currentdate);
        //Date Picker
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }

        };
        time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                myCalendar.set(Calendar.HOUR_OF_DAY, i);
                myCalendar.set(Calendar.MINUTE, i1);
                updateTime();
            }
        };
        et_date = (EditText) findViewById(R.id.pt_date);
        et_time = (EditText) findViewById(R.id.pt_time);

        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(view.getContext(), time, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false).show();
            }
        });

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insert code here
                new DatePickerDialog(view.getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        if(getIntent().hasExtra("action")){
            Bundle extras = getIntent().getExtras();
            int action=extras.getInt("action");
            if(action==2){
                int itemId=extras.getInt(Memo.ID_CODE);
                MySQLiteHelper db = new MySQLiteHelper(this);
                Memo itemEdited=db.getMemo(itemId);
                et_title.setText(itemEdited.getTitle());
                et_date.setText(itemEdited.getDeadline());
                et_priority.setText(itemEdited.getPriorityLevel());
                et_time.setText(itemEdited.getNotificationTime());
                et_note.setText(itemEdited.getNote());
                String statusText=itemEdited.getStatus();
                int statusNum=0;
                if(statusText.equalsIgnoreCase("active")){
                    statusNum=0;
                }
                else if(statusText.equalsIgnoreCase("complete")){
                    statusNum=1;
                }
                else if(statusText.equalsIgnoreCase("overdue")){
                    statusNum=2;
                }
                et_status.setSelection(statusNum);

                String intervalsText=itemEdited.getNotificationIntervals();
                int intervalsNum=0;
                if(intervalsText.equalsIgnoreCase("once")){
                    intervalsNum=0;
                }
                else if(intervalsText.equalsIgnoreCase("daily")){
                    intervalsNum=1;
                }
                else if(intervalsText.equalsIgnoreCase("weekly")){
                    intervalsNum=2;
                }
                et_intervals.setSelection(intervalsNum);

                String categoryText=itemEdited.getCategory();
                int num=0;
                if(categoryText.equalsIgnoreCase("academic")){
                    num=1;
                }
                else if(categoryText.equalsIgnoreCase("work")){
                    num=2;
                }
                else if(categoryText.equalsIgnoreCase("personal")){
                    num=3;
                }
                else if(categoryText.equalsIgnoreCase("others")){
                    num=4;
                }
                et_category.setSelection(num);
            }
        }
    }

    private void updateTime() {
        et_time.setText(tf.format(myCalendar.getTime()));
    }

    private void updateDate() {
        et_date.setText(sdf.format(myCalendar.getTime()));
    }
    //end of date picker
}
