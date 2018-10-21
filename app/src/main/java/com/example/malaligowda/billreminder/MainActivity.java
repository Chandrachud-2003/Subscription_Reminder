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
        mainBill.toggle();
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


        if (mDBHandler.namesArray().size() != 0) {


            bNames = mDBHandler.namesArray();
            bDate = mDBHandler.dateArray();
            bCurrency = mDBHandler.currencyArray();
            bAmount = mDBHandler.amountArray();
            bInterval = mDBHandler.intervalArray();
            bNotify = mDBHandler.notifyArray();
            bType = mDBHandler.typeArray();


            displayAdapter displayAdapter = new displayAdapter(this, bNames, bDate, bCurrency, bAmount, bType, bNotify);
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










