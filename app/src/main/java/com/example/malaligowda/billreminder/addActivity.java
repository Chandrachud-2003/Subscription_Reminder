package com.example.malaligowda.billreminder;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
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

public class addActivity extends AppCompatActivity {
    private TextView addheading;
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
    private CheckBox syncBox;
    MyDBHandler dbHandler;
    String selectedDate;
    private View divider;
    String edit = "";
    int id;
    SharedPreferences savedid;
    SharedPreferences.Editor editor;
    private EditText notifyDays;
    private AlarmManager mAlarmManager;
    NotificationCompat.Builder notification;
    private static  final int UNIQUE_ID=456;
    private static final String CHANNEL_ID="myId";

    final int REQUEST_CODE = 123;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bill);

        titleView = findViewById(R.id.titleView);
        addheading = findViewById(R.id.addHeading);

        amountView = findViewById(R.id.amount);
        notifyDays = findViewById(R.id.notificationDays);

        mCalendarView = findViewById(R.id.calendarView);
        currencySpinner = findViewById(R.id.currencySpinner);
        intervalSpinner = findViewById(R.id.intervalView);
        reminder = findViewById(R.id.checkBox);
        backbutton = findViewById(R.id.backButton);
        addbutton = findViewById(R.id.addPaymentButton);
        dbHandler = new MyDBHandler(this, null, null, 1);
        divider = findViewById(R.id.divider7);
        billButton = findViewById(R.id.billButton);
        subscriptionButton = findViewById(R.id.subscriptionButton);
        syncBox = findViewById(R.id.syncBox);
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        notification = new NotificationCompat.Builder(this, CHANNEL_ID);
        notification.setAutoCancel(true);

        intervalSpinner.setVisibility(View.INVISIBLE);
        divider.setVisibility(View.INVISIBLE);
        billButton.toggle();
        String[] intervalarraySpinner = new String[]{"Monthly", "Annually"};
        String[] currencyarraySpinner = new String[]{"AUD $", "EUR €", "INR ₹", "USD $", "JPY	¥", "ZAR R"};
        currencySpinner.setSelection(2);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, intervalarraySpinner);
        intervalSpinner.setAdapter(adapter1);
        savedid = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
        editor = savedid.edit();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, currencyarraySpinner);
        currencySpinner.setAdapter(adapter2);
        id = savedid.getInt("id", 0);
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
                Log.d("billReminder", selectedDate);


            }
        });


        getIncomingIntent();
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(addActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
        addbutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                if (!titleView.getText().toString().equals("") && !amountView.getText().toString().equals("") && !notifyDays.getText().toString().equals("")) {


                    Bills bill = new Bills(titleView.getText().toString());
                    bill.set_day(selectedDate);
                    id++;
                    bill.set_id(id);

                    bill.setAmt(amountView.getText().toString());
                    bill.set_currency(currencySpinner.getSelectedItem().toString());
                    bill.set_notifyDays(notifyDays.getText().toString());
                    String type="";

                    bill.set_name(titleView.getText().toString());
                    if (billButton.isChecked()) {
                        bill.set_type("Bill");
                        type = "Bill";


                    }
                    if (subscriptionButton.isChecked()) {
                        bill.set_type("Subscription");
                        type = "Subscription";


                    }
                    bill.set_interval(intervalSpinner.getSelectedItem().toString());
                    if (reminder.isChecked()) {
                        bill.set_notify("true");
                        setNotification(type, titleView.getText().toString(), selectedDate, amountView.getText().toString(), currencySpinner.getSelectedItem().toString().charAt(4));

                        String n = notifyDays.getText().toString();
                        int l = Integer.valueOf(n);
                        if(l>0) {
                            int day = Integer.valueOf(selectedDate.substring(0, 2));
                            int month = Integer.valueOf(selectedDate.substring(3, 5));
                            int year = Integer.valueOf(selectedDate.substring(6, 10));

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.DATE, day);
                            calendar.set(Calendar.MONTH, month);
                            calendar.set(Calendar.YEAR, year);
                            calendar.add(Calendar.DATE, -l);
                            String date = Integer.toString(calendar.get(Calendar.DATE));
                            String mon = Integer.toString(calendar.get(Calendar.MONTH));
                            String newyear = Integer.toString(calendar.get(Calendar.YEAR));
                            String newdate;
                            if(Integer.valueOf(mon)>9)
                                newdate = date+"/"+mon+"/"+newyear;
                            else
                                newdate = date+"/0"+mon+"/"+newyear;

                            Log.d("check", "onClick: "+newdate);
                            setNotification(type, titleView.getText().toString(), newdate, amountView.getText().toString(), currencySpinner.getSelectedItem().toString().charAt(4));
                        }
                    } else {
                        bill.set_notify("false");

                    }
                    if (syncBox.isChecked()) {
                        bill.set_sync("true");

                    } else {
                        bill.set_sync("false");
                    }

                    dbHandler.addBill(bill);
                    if (dbHandler.addBill(bill)) {
                        if (edit == "") {
                            Toast.makeText(getBaseContext(), "Bill added", Toast.LENGTH_SHORT).show();

                            if (syncBox.isChecked()) {
                                addEvent();



                            }
                            Log.d("billReminder", "Bill added");


                        } else {
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

    private void getIncomingIntent() {


        if (getIntent().hasExtra("amount") && getIntent().hasExtra("name")) {
            Log.d("intent", "getIncomingIntent: found intent extras.");

            String editamount = getIntent().getStringExtra("amount");
            String editname = getIntent().getStringExtra("name");
            String editcurrency = getIntent().getStringExtra("currency");
            String editnotify = getIntent().getStringExtra("notify");
            String editinterval = getIntent().getStringExtra("interval");
            String editdate = getIntent().getStringExtra("date");
            String edittype = getIntent().getStringExtra("type");
            String editid = getIntent().getStringExtra("id");
            addheading.setText("EDIT PAYMENT");
            addbutton.setText("SAVE");
            titleView.setText(editname);
            amountView.setText(editamount);
            ArrayList<String> currency = new ArrayList<String>(Arrays.asList("AUD $", "EUR €", "INR ₹", "USD $", "JPY	¥", "ZAR R"));
            currencySpinner.setSelection(currency.indexOf(editcurrency));
            ArrayList<String> interval = new ArrayList<String>(Arrays.asList("Monthly", "Annually"));
            intervalSpinner.setSelection(interval.indexOf(editinterval));
            Log.d("intent", "type-" + edittype + "r");
            if (edittype.equals("Bill")) {
                billButton.setChecked(true);
                subscriptionButton.setChecked(false);
                intervalSpinner.setVisibility(View.INVISIBLE);


            } else if (edittype.equals("Subscription")) {
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
            Log.d("intent", "notify-" + editnotify + "r");
            if (editnotify.equals("true"))
                reminder.setChecked(true);
            else
                reminder.setChecked(false);

            edit = editid;

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        editor.putInt("id", id);
        editor.commit();

    }

    private void registerNotification() {


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.d("Clima", "onRequestPermissionResult(): Permission granted");
                addEvent();

            } else {

                Log.d("Clima", "Permission denied =( ");

            }

        }
    }

    private void addEvent() {
        ContentResolver cr = getBaseContext().getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Events.DESCRIPTION, titleView.getText().toString()+" due on "+amountView.getText().toString());
        if (billButton.isChecked()) {
            cv.put(CalendarContract.Events.TITLE, "Bill Payment");
        } else if (subscriptionButton.isChecked()) {
            cv.put(CalendarContract.Events.TITLE, "Subscription Payment");
        }

        cv.put(CalendarContract.Events.CALENDAR_ID, 1);
        cv.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
        cv.put(CalendarContract.Events.DTSTART, Calendar.getInstance().getTimeInMillis());
        cv.put(CalendarContract.Events.DTEND, Calendar.getInstance().getTimeInMillis() + 60 * 60 * 1000);
        cv.put(CalendarContract.Events.ALL_DAY, true);



        if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(addActivity.this, new String[]{Manifest.permission.WRITE_CALENDAR}, REQUEST_CODE);
            return;

        }
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, cv);
        Toast.makeText(getBaseContext(), "Event Successfully added", Toast.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setNotification(String title, String text, String date, String amount, Character currency){



        int day = Integer.valueOf(date.substring(0,2));
        int month = Integer.valueOf(date.substring(3,5));
        int year = Integer.valueOf(date.substring(6,10));
        Log.d("notification", ""+day+""+month+""+year);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE,day);
        calendar.set(Calendar.MONTH,month-1);
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,1);
        Log.d("no1", calendar.getTime().toString());
        Intent intent = new Intent(this, AlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),10101,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        intent.putExtra("amount",amount);
        intent.putExtra("name",text);
        intent.putExtra("currency",currency);
     //   intent.putExtra("interval");
        intent.putExtra("dates",date);
        intent.putExtra("type",title);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);


    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_ID;
            String description = "notification channel for billReminder app";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
