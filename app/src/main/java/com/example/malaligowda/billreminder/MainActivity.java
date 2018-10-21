package com.example.malaligowda.billreminder;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

     private TextView timeView;
     private TextView dayNumber;
     private TextView month;
     private TextView year;
     private TextView day;
     private RecyclerView displayBills;
     private ImageButton addBill;
     private RadioButton mainBill;
     private RadioButton mainSubscription;

     private MyDBHandler mDBHandler;
    private static final int NUM_COLUMNS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dayNumber = findViewById(R.id.dayNumberText);
        month = findViewById(R.id.monthText);
        year = findViewById(R.id.yearText);
        day = findViewById(R.id.dayText);
        displayBills = findViewById(R.id.displayView);
        addBill = findViewById(R.id.addPaymentButton);
        mainBill = findViewById(R.id.mainBillButton);
        mainSubscription = findViewById(R.id.mainSubscriptionButton);
        addBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, addActivity.class);
                startActivity(intent);

            }
        });

        mainBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainBill.setChecked(true);
                mainSubscription.setChecked(false);
                initRecyclerView();
            }
        });

        mainSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainBill.setChecked(false);
                mainSubscription.setChecked(true);
                initRecyclerView();
            }
        });

        mDBHandler = new MyDBHandler(this, null, null, 1);

        displayBills.removeAllViewsInLayout();
        initRecyclerView();







    }

    private void initRecyclerView() {
        ArrayList<String> bNames = new ArrayList<>();
        ArrayList<String> bAmount = new ArrayList<>();
        ArrayList<String> bCurrency = new ArrayList<>();
        ArrayList<String> bDate = new ArrayList<>();
        ArrayList<String> bInterval = new ArrayList<>();
        ArrayList<String> bNotify = new ArrayList<>();
        ArrayList<String> bType = new ArrayList<>();

        ArrayList<String> sNames = new ArrayList<>();
        ArrayList<String> sAmount = new ArrayList<>();
        ArrayList<String> sCurrency = new ArrayList<>();
        ArrayList<String> sDate = new ArrayList<>();
        ArrayList<String> sInterval = new ArrayList<>();
        ArrayList<String> sNotify = new ArrayList<>();
        ArrayList<String> sType = new ArrayList<>();




        if (mDBHandler.namesArray().size() != 0){

        for (int i = 0; i < mDBHandler.namesArray().size(); i++) {
            if (mDBHandler.typeArray().get(i).equals("bill")) {
                bNames.add(mDBHandler.namesArray().get(i));
                bAmount.add(mDBHandler.amountArray().get(i));
                bCurrency.add(mDBHandler.currencyArray().get(i));
                bDate.add(mDBHandler.dateArray().get(i));
                bInterval.add(mDBHandler.intervalArray().get(i));
                bNotify.add(mDBHandler.notifyArray().get(i));


            } else if (mDBHandler.typeArray().get(i).equals("subscription")) {
                sNames.add(mDBHandler.namesArray().get(i));
                sAmount.add(mDBHandler.amountArray().get(i));
                sCurrency.add(mDBHandler.currencyArray().get(i));
                sDate.add(mDBHandler.dateArray().get(i));
                sInterval.add(mDBHandler.intervalArray().get(i));
                sNotify.add(mDBHandler.notifyArray().get(i));
            }

        }



            if (mainBill.isChecked()) {


                displayAdapter displayAdapter = new displayAdapter(this, bNames, bDate, bCurrency, bAmount, bType, bNotify );
                displayBills.setVisibility(View.VISIBLE);
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
                displayBills.setLayoutManager(staggeredGridLayoutManager);
                displayBills.setAdapter(displayAdapter);
            } else if (mainSubscription.isChecked()) {
                displayAdapter displayAdapter = new displayAdapter(this, sNames, sDate, sCurrency, sAmount, sType, sNotify);
                displayBills.setVisibility(View.VISIBLE);
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
                displayBills.setLayoutManager(staggeredGridLayoutManager);
                displayBills.setAdapter(displayAdapter);
            }


        }
        else {

            Toast.makeText(getBaseContext(), "No Reminders Set", Toast.LENGTH_SHORT).show();
            displayBills.removeAllViewsInLayout();
        }
    }


    }






