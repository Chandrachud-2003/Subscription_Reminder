package com.example.malaligowda.billreminder;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public abstract class addActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private EditText titleView;
    private EditText amountView;
    private CalendarView mCalendarView;
    private Spinner currrenySpinner;
    private Spinner ringtoneSpinner;
    private Spinner intervalSpinner;
    private CheckBox reminder;
    private TextView alarmTime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        titleView = findViewById(R.id.titleView);
        amountView = findViewById(R.id.amountView);
        mCalendarView = findViewById(R.id.calendarView);
        currrenySpinner = findViewById(R.id.currencySpinner);
        ringtoneSpinner = findViewById(R.id.ringView);
        intervalSpinner = findViewById(R.id.intervalView);
        reminder = findViewById(R.id.checkBox);
        alarmTime = findViewById(R.id.alarmTimeView);


        alarmTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment timePicker = new TimePickerFragmant();
                timePicker.show(getSupportFragmentManager(), "Time Picker");

            }
        });






    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        alarmTime.setText(hourOfDay+":"+minute);
    }
}
