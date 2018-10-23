package com.example.malaligowda.billreminder;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        day.setText(dayOfTheWeek);
        String month1="";
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        String datee = sdf1.format(new Date());
        switch (Calendar.getInstance().get(Calendar.MONTH)) {
            case 0:{ month1 = "JAN";
                break;}
            case 1:{ month1 = "FEB";
                break;}
            case 2:{ month1 = "MAR";
                break;}
            case 3:{ month1 = "APR";
                break;}
            case 5:{ month1 = "JUN";
                break;}
            case 6:{ month1 = "JUL";
                break;}
            case 7:{ month1 = "AUG";
                break;}
            case 8:{ month1 = "SEP";
                break;}
            case 9:{ month1 = "OCT";
                break;}
            case 10:{ month1 = "NOV";
                break;}
            case 11:{ month1 = "DEC";
                break;}
            case 4:{ month1 = "MAYY";
                break;}
        }
        String year1 = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        year.setText(year1);
        month.setText(month1);
        dayNumber.setText(datee);


    }

    private void initRecyclerView() {

        ArrayList<String> mAmount;
        ArrayList<String> mCurrency;
        ArrayList<String> mDate;
        ArrayList<String> mInterval;
        ArrayList<String> mNotify;
        ArrayList<String> mType;
        ArrayList<String> mNames;







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

    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof displayAdapter.ViewHolder) {
            int deleteIndex = viewHolder.getAdapterPosition();


        }

    }
}















