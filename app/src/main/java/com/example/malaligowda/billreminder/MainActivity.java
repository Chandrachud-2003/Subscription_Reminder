package com.example.malaligowda.billreminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView timeView;
    private TextView dayNumber;
    private TextView month;
    private TextView year;
    private TextView day;
    private RecyclerView displayBills;
    private ImageButton addBill;


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

        addBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, addActivity.class);
                startActivity(intent);

            }
        });



        mDBHandler = new MyDBHandler(this, null, null, 1);

        displayBills.removeAllViewsInLayout();
        initRecyclerView();


    }

    private void initRecyclerView() {
        ArrayList<String> mNames;
        ArrayList<String> mAmount;
        ArrayList<String> mCurrency;
        ArrayList<String> mDate;
        ArrayList<String> mInterval;
        ArrayList<String> mNotify;
        ArrayList<String> mType;








        mNames = mDBHandler.namesArray();
            mDate = mDBHandler.dateArray();
            mCurrency = mDBHandler.currencyArray();
            mAmount = mDBHandler.amountArray();
            mInterval = mDBHandler.intervalArray();
            mNotify = mDBHandler.notifyArray();
            mType = mDBHandler.typeArray();
        if (mNames.size()!=0)
        {


            displayAdapter displayAdapter = new displayAdapter(this, mNames, mDate, mCurrency, mAmount, mType, mNotify, mInterval);
            displayBills.setVisibility(View.VISIBLE);
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
            displayBills.setLayoutManager(staggeredGridLayoutManager);
            displayBills.setAdapter(displayAdapter);

        }


     else

    {

        Toast.makeText(getBaseContext(), "No Reminders Set", Toast.LENGTH_SHORT).show();
        displayBills.removeAllViewsInLayout();
        displayBills.setAdapter(null);

    }
}
    }










