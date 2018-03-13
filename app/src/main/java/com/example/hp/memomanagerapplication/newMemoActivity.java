package com.example.hp.memomanagerapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class newMemoActivity extends AppCompatActivity {
    public EditText et_date, et_time;

    //date picker variables
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    SimpleDateFormat tf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date;
    TimePickerDialog.OnTimeSetListener time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_memo);

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

    }

    private void updateTime() {
        et_time.setText(tf.format(myCalendar.getTime()));
    }

    private void updateDate() {
        et_date.setText(sdf.format(myCalendar.getTime()));
    }
    //end of date picker
}
