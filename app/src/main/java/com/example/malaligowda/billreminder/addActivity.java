package com.example.malaligowda.billreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

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
    private View divider;
    String edit="";
    int id;
    SharedPreferences savedid;
    SharedPreferences.Editor editor;
    private EditText notifyDays;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bill);

        titleView = findViewById(R.id.titleView);

        amountView = findViewById(R.id.amount);
        notifyDays = findViewById(R.id.notificationDays);

        mCalendarView = findViewById(R.id.calendarView);
        currencySpinner = findViewById(R.id.currencySpinner);
        intervalSpinner = findViewById(R.id.intervalView);
        reminder = findViewById(R.id.checkBox);
        backbutton = findViewById(R.id.backButton);
        addbutton = findViewById(R.id.addPaymentButton);
        dbHandler = new MyDBHandler(this,null,null,1);
        divider = findViewById(R.id.divider7);
        billButton = findViewById(R.id.billButton);
        subscriptionButton = findViewById(R.id.subscriptionButton);

        intervalSpinner.setVisibility(View.INVISIBLE);
        divider.setVisibility(View.INVISIBLE);
        billButton.toggle();
        String[] intervalarraySpinner = new String[] {"Monthly", "Annually"};
        String[] currencyarraySpinner = new String[] {"AUD $","EUR €", "INR ₹","USD $","JPY	¥","ZAR R"};
        currencySpinner.setSelection(2);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, intervalarraySpinner);
        intervalSpinner.setAdapter(adapter1);
        savedid = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
        editor = savedid.edit();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, currencyarraySpinner);
        currencySpinner.setAdapter(adapter2);
        id=savedid.getInt("id",0);
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        selectedDate = sdf.format(new Date(mCalendarView.getDate()));
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                long milliTime = calendar.getTimeInMillis();
                mCalendarView.setDate(milliTime, true, true);
                final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                selectedDate = sdf.format(new Date(mCalendarView.getDate()));
                Log.d("billReminder",selectedDate);




            }
        });

//
        getIncomingIntent();
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

                        if (!titleView.getText().toString().equals("") && !amountView.getText().toString().equals("")&&!notifyDays.getText().toString().equals("")) {


                            Bills bill = new Bills(titleView.getText().toString());
                            bill.set_day(selectedDate);
                            id++;
                            bill.set_id(id);

                             bill.setAmt(amountView.getText().toString());
                            bill.set_currency(currencySpinner.getSelectedItem().toString());
                            bill.set_notifyDays(notifyDays.getText().toString());

                            bill.set_name(titleView.getText().toString());
                            if (billButton.isChecked()) {
                                bill.set_type("Bill");

                            }
                            if (subscriptionButton.isChecked()) {
                                bill.set_type("Subscription");


                            }
                            bill.set_interval(intervalSpinner.getSelectedItem().toString());
                            if (reminder.isChecked()) {
                                bill.set_notify("true");
                            } else {
                                bill.set_notify("false");

                            }

                            dbHandler.addBill(bill);
                            if (dbHandler.addBill(bill)) {
                                if(edit=="")
                                Toast.makeText(getBaseContext(), "Bill added", Toast.LENGTH_SHORT).show();
                                else {
                                    dbHandler.deleteBill(edit);
                                    Toast.makeText(getBaseContext(), "Bill changed", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getBaseContext(), "Something wasn't right", Toast.LENGTH_SHORT).show();

                            }

                            Intent intent = new Intent(addActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {

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
                divider.setVisibility(View.VISIBLE);

            }
        });
        billButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subscriptionButton.setChecked(false);
                billButton.setChecked(true);
                intervalSpinner.setVisibility(View.INVISIBLE);
                divider.setVisibility(View.INVISIBLE);


            }
        });



//
        //


    }
    private void getIncomingIntent(){


        if(getIntent().hasExtra("amount") && getIntent().hasExtra("name")){
            Log.d("intent", "getIncomingIntent: found intent extras.");

            String editamount = getIntent().getStringExtra("amount");
            String editname = getIntent().getStringExtra("name");
            String editcurrency = getIntent().getStringExtra("currency");
            String editnotify = getIntent().getStringExtra("notify");
            String editinterval = getIntent().getStringExtra("interval");
            String editdate = getIntent().getStringExtra("date");
            String edittype = getIntent().getStringExtra("type");
            String editid = getIntent().getStringExtra("id");

            titleView.setText(editname);
            amountView.setText(editamount);
            ArrayList<String> currency = new ArrayList<String>(Arrays.asList("AUD $","EUR €", "INR ₹","USD $","JPY	¥","ZAR R"));
            currencySpinner.setSelection(currency.indexOf(editcurrency));
            ArrayList<String> interval = new ArrayList<String>(Arrays.asList("Monthly", "Annually"));
            intervalSpinner.setSelection(interval.indexOf(editinterval));
            Log.d("intent", "type-"+edittype+"r");
            if (edittype.equals("Bill")) {
                billButton.setChecked(true);
                subscriptionButton.setChecked(false);
                intervalSpinner.setVisibility(View.INVISIBLE);


            }
            else if( edittype.equals("Subscription")) {
                billButton.setChecked(false);
                subscriptionButton.setChecked(true);
                intervalSpinner.setVisibility(View.VISIBLE);
                divider.setVisibility(View.VISIBLE);

            }
            subscriptionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    subscriptionButton.setChecked(true);
                    billButton.setChecked(false);
                    intervalSpinner.setVisibility(View.VISIBLE);
                    divider.setVisibility(View.VISIBLE);

                }
            });
            billButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    subscriptionButton.setChecked(false);
                    billButton.setChecked(true);
                    intervalSpinner.setVisibility(View.INVISIBLE);
                    divider.setVisibility(View.INVISIBLE);


                }
            });
            Log.d("intent", "notify-"+editnotify+"r");
            if(editnotify.equals("true"))
                reminder.setChecked(true);
            else
                reminder.setChecked(false);

            edit=editid;

        }
    }
    @Override
    protected void onStop(){
        super.onStop();
        editor.putInt("id",id);
        editor.commit();

    }


}
