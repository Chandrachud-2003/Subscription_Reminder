package com.example.malaligowda.billreminder;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toolbar;

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







    }


}


