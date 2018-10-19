package com.example.malaligowda.billreminder;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Date;

public  class addActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private EditText titleView;
    private EditText amountView;
    private CalendarView mCalendarView;
    private Spinner currencySpinner;
    private Spinner ringtoneSpinner;
    private Spinner intervalSpinner;
    private CheckBox reminder;
    private TextView alarmTime;
    private ImageButton backbutton;
    private Button addbutton;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bill);

        titleView = findViewById(R.id.titleView);
        amountView = findViewById(R.id.amountView);
        mCalendarView = findViewById(R.id.calendarView);
        currencySpinner = findViewById(R.id.currencySpinner);
        ringtoneSpinner = findViewById(R.id.ringView);
        intervalSpinner = findViewById(R.id.intervalView);
        reminder = findViewById(R.id.checkBox);
        alarmTime = findViewById(R.id.alarmTimeView);
        backbutton = findViewById(R.id.backButton);
        addbutton = findViewById(R.id.addPaymentButton);
        dbHandler = new MyDBHandler(this,null,null,1);
        String[] intervalarraySpinner = new String[] {"Monthly", "Annually"};
        String[] currencyarraySpinner = new String[] {"AUD $","EUR €", "INR ₹","USD $","JPY	¥","ZAR R"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, intervalarraySpinner);
        intervalSpinner.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, currencyarraySpinner);
        currencySpinner.setAdapter(adapter2);
            mCalendarView.setDate(System.currentTimeMillis(),false,true);

        alarmTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragmant();
                timePicker.show(getSupportFragmentManager(), "Time Picker");
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(addActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        String selectedDate = year+"/"+month+"/"+dayOfMonth;

                        Bills bill = new Bills(titleView.getText().toString());
                        bill.set_date(selectedDate);
                        bill.setAmt(Integer.parseInt(amountView.getText().toString()));
                        bill.set_currency(currencySpinner.getSelectedItem().toString());
                        bill.set_interval(intervalSpinner.getSelectedItem().toString());
                        bill.set_name(titleView.getText().toString());
                        dbHandler.addBill(bill);

                    }
                });

            }

        });




    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        alarmTime.setText(hourOfDay+":"+minute);
    }
}
