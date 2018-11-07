package com.example.rohitandchandra.Due;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;




import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hotchemi.android.rate.AppRate;
import me.leolin.shortcutbadger.ShortcutBadger;
import spencerstudios.com.bungeelib.Bungee;

public class MainActivity extends AppCompatActivity {

    private TextView dayNumber;
    private TextView month;
    private TextView year;
    private TextView day;
    private RecyclerView displayBills;
    private ImageButton addBill;
    private int overdue;
    private TextView display;
    private ImageButton checkButton;
    private ImageButton editButton;
    private ImageButton deleteButton;



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
        display = findViewById(R.id.dueText);
        checkButton = findViewById(R.id.checkButton);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);

        display.setVisibility(View.VISIBLE);



        addBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, addActivity.class);
                startActivity(intent);
                Bungee.slideLeft(MainActivity.this);



            }
        });
        AppRate.with(this)
                .setTitle("Rate This App")
                .setMessage("If you enjoyed this app, would you mind taking a moment to rate it? It won't take more than a minute. Thanks for your support!")
                .setInstallDays(0)
                .setLaunchTimes(3)
                .setRemindInterval(7)
                .monitor();
        AppRate.showRateDialogIfMeetsConditions(this);


        mDBHandler = new MyDBHandler(this, null, null, 5);

        displayBills.removeAllViewsInLayout();

        initRecyclerView();

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        day.setText(dayOfTheWeek);
        String month1 = "";
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf1.format(new Date());
        switch (Calendar.getInstance().get(Calendar.MONTH)) {
            case 0: {
                month1 = "JAN";
                break;
            }
            case 1: {
                month1 = "FEB";
                break;
            }
            case 2: {
                month1 = "MAR";
                break;
            }
            case 3: {
                month1 = "APR";
                break;
            }
            case 4: {
                month1 = "MAY";
                break;
            }
            case 5: {
                month1 = "JUN";
                break;
            }
            case 6: {
                month1 = "JUL";
                break;
            }
            case 7: {
                month1 = "AUG";
                break;
            }
            case 8: {
                month1 = "SEP";
                break;
            }
            case 9: {
                month1 = "OCT";
                break;
            }
            case 10: {
                month1 = "NOV";
                break;
            }
            case 11: {
                month1 = "DEC";
                break;
            }

        }
        String year1 = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        year.setText(year1);
        month.setText(month1);
        dayNumber.setText(date.substring(0,2));


    }

    private void initRecyclerView() {

        ArrayList<String> mAmount;
        ArrayList<String> mCurrency;
        ArrayList<String> mDate;
        ArrayList<String> mInterval;
        ArrayList<String> mNotify;
        ArrayList<String> mType;
        ArrayList<String> mNames;
        ArrayList<String> mNotifyDays;
        ArrayList<String> mId;
        ArrayList<String> mSync;

        ArrayList<Date> ndate = new ArrayList<>();


        mId = mDBHandler.idArray();
        mNames = mDBHandler.namesArray();
        mDate = mDBHandler.dateArray();
        mCurrency = mDBHandler.currencyArray();
        mAmount = mDBHandler.amountArray();
        mInterval = mDBHandler.intervalArray();
        mNotify = mDBHandler.notifyArray();
        mType = mDBHandler.typeArray();
        mNotifyDays = mDBHandler.notifyDays();
        mSync = mDBHandler.syncArray();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");


        for(int i=0;i<mDate.size();i++)
        {
            try {
                Date date = sdf1.parse(mDate.get(i));
                long millis = date.getTime();
                if (System.currentTimeMillis()>millis)
                {
                    overdue++;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        Log.d("Overdue", Integer.toString(overdue));
        ShortcutBadger.applyCount(this, overdue);




        if (mNames.size() != 0) {
            display.setVisibility(View.INVISIBLE);

            //SORTING
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                for (int i = 0; i < mDate.size(); i++)
                    ndate.add(format.parse(mDate.get(i)));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            int min;
            for (int i = 0; i < mDate.size(); i++)
            {
                min = i;
                for(int j = i+1;j<mDate.size();j++) {
                    if (ndate.get(j).before(ndate.get(i))) {

                        min=j;
                    }
                }
                Date temp = ndate.get(i);
                ndate.set(i, ndate.get(min));
                ndate.set(min, temp);


                String namet = mNames.get(i);
                mNames.set(i, mNames.get(min));
                mNames.set(min, namet);


                String idt = mId.get(i);
                mId.set(i, mId.get(min));
                mId.set(min, idt);


                String currencyt = mCurrency.get(i);
                mCurrency.set(i, mCurrency.get(min));
                mCurrency.set(min, currencyt);


                String amount = mAmount.get(i);
                mAmount.set(i, mAmount.get(min));
                mAmount.set(min, amount);


                String interval  = mInterval.get(i);
                mInterval.set(i, mInterval.get(min));
                mInterval.set(min, interval);


                String notify  = mNotify.get(i);
                mNotify.set(i, mNotify.get(min));
                mNotify.set(min, notify);


                String type  = mType.get(i);
                mType.set(i, mType.get(min));
                mType.set(min, type);


                String notifydays  = mNotifyDays.get(i);
                mNotifyDays.set(i, mNotifyDays.get(min));
                mNotifyDays.set(min, notifydays);


                String sync  = mSync.get(i);
                mSync.set(i, mSync.get(min));
                mSync.set(min, sync);



            }

            for (int i = 0; i < mDate.size(); i++) {
                Date date = ndate.get(i);
                String dateTime = format.format(date);
                mDate.set(i,dateTime);
            }






            Log.d("rohit",ndate.toString() );
            Log.d("rohit",mNames.toString() );
            displayAdapter displayAdapter = new displayAdapter(this, mId, mNames, mDate, mCurrency, mAmount, mType, mNotify, mInterval, mNotifyDays, mSync, display, displayBills, editButton, deleteButton, checkButton);
            displayBills.setVisibility(View.VISIBLE);
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
            displayBills.setLayoutManager(staggeredGridLayoutManager);
            displayBills.setAdapter(displayAdapter);


        } else

        {

            display.setVisibility(View.VISIBLE);
            displayBills.removeAllViewsInLayout();
            displayBills.setAdapter(null);

        }
    }

}














