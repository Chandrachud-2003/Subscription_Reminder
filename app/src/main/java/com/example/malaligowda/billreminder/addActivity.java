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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public  class addActivity extends AppCompatActivity {

    private EditText titleView;
    private EditText amountView;
    private CalendarView mCalendarView;
    private Spinner currencySpinner;
    private RadioButton billButton;
    private RadioButton subscriptionButton;
    private Spinner intervalSpinner;
    private CheckBox reminder;
    private ImageButton backbutton;
    private Button addbutton;
    MyDBHandler dbHandler;
    String selectedDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bill);

        titleView = findViewById(R.id.titleView);
        amountView = findViewById(R.id.amountView);
        mCalendarView = findViewById(R.id.calendarView);
        currencySpinner = findViewById(R.id.currencySpinner);
        intervalSpinner = findViewById(R.id.intervalView);
        reminder = findViewById(R.id.checkBox);
        backbutton = findViewById(R.id.backButton);
        addbutton = findViewById(R.id.addPaymentButton);
        dbHandler = new MyDBHandler(this,null,null,1);
        billButton = findViewById(R.id.billButton);
        subscriptionButton = findViewById(R.id.subscriptionButton);

        billButton.toggle();
        String[] intervalarraySpinner = new String[] {"Monthly", "Annually"};
        String[] currencyarraySpinner = new String[] {"AUD $","EUR €", "INR ₹","USD $","JPY	¥","ZAR R"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, intervalarraySpinner);
        intervalSpinner.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, currencyarraySpinner);
        currencySpinner.setAdapter(adapter2);
            mCalendarView.setDate(System.currentTimeMillis(),false,true);


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

                         selectedDate = year + "/" + (month+1) + "/" + dayOfMonth;

                    }
                });
                        if (titleView.getText().toString() != "" && amountView.getText().toString()!="")
                        {

                            Bills bill = new Bills(titleView.getText().toString());
                            bill.set_date(selectedDate);
                            bill.setAmt(amountView.getText().toString());
                            bill.set_currency(currencySpinner.getSelectedItem().toString());

                            bill.set_name(titleView.getText().toString());
                            String type = "";
                            if (billButton.isChecked()) {
                                type = "bill";
                                bill.set_interval("null");

                            }
                            if (subscriptionButton.isChecked()) {
                                type = "subscription";
                                bill.set_interval(intervalSpinner.getSelectedItem().toString());

                            }
                            bill.set_type(type);
                            if (reminder.isChecked())
                            {
                                bill.set_notify(true);
                            }
                            else {
                                bill.set_notify(false);

                            }

                            dbHandler.addBill(bill);
                            if (dbHandler.addBill(bill))
                            {
                                Toast.makeText(getBaseContext(), "Bill added", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getBaseContext(), "Something wasn't right", Toast.LENGTH_SHORT).show();

                            }

                            Intent intent = new Intent(addActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {

                            Toast.makeText(getBaseContext(), "Incomplete fields found!", Toast.LENGTH_LONG).show();

                        }





            }

        });

        subscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subscriptionButton.setChecked(true);
                billButton.setChecked(false);
                intervalSpinner.setVisibility(View.VISIBLE);

            }
        });
        billButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subscriptionButton.setChecked(false);
                billButton.setChecked(true);
                intervalSpinner.setVisibility(View.INVISIBLE);


            }
        });





    }


}
