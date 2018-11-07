package com.example.malaligowda.billreminder;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import spencerstudios.com.bungeelib.Bungee;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

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
    private EditText days;
    private int minutes;
    private int seconds;
    private String interval;
    private int SPC = 1;
    private TextView displayDue;
    private String editId;

    private long milliTime;
    private
    MyDBHandler dbHandler;
    String selectedDate;
    private View divider;
    private View divider2;
    String edit = "";
    int id;
    SharedPreferences savedid;
    SharedPreferences.Editor editor;
    private AlarmManager mAlarmManager;
    NotificationCompat.Builder notification;
    private static final int UNIQUE_ID = 456;
    private static final String CHANNEL_ID = "my_channel";

    final int REQUEST_CODE = 123;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bill);

        titleView = findViewById(R.id.titleView);
        addheading = findViewById(R.id.addHeading);

        amountView = findViewById(R.id.amount);

        mCalendarView = findViewById(R.id.calendarView);
        currencySpinner = findViewById(R.id.currencySpinner);
        intervalSpinner = findViewById(R.id.intervalView);
        reminder = findViewById(R.id.checkBox);
        backbutton = findViewById(R.id.backButton);
        addbutton = findViewById(R.id.addPaymentButton);
        days = findViewById(R.id.notificationDays);
        dbHandler = new MyDBHandler(this, null, null, 1);
        divider = findViewById(R.id.divider7);
        billButton = findViewById(R.id.billButton);
        subscriptionButton = findViewById(R.id.subscriptionButton);
        syncBox = findViewById(R.id.syncBox);
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        notification = new NotificationCompat.Builder(this, CHANNEL_ID);
        notification.setAutoCancel(true);
        displayDue = findViewById(R.id.dueText);
        createNotificationChannel();
        checkPermissions(REQUEST_CODE, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);
        intervalSpinner.setVisibility(View.INVISIBLE);
        divider.setVisibility(View.INVISIBLE);
        divider2 = findViewById(R.id.divider2);
        billButton.toggle();
        String[] intervalarraySpinner = new String[]{"Monthly", "Yearly"};
        String[] currencyarraySpinner = new String[]{"USD $","INR ₹", "EUR €","AUD $", "JPY	¥", "ZAR R"};
        currencySpinner.setSelection(2);
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
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
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);
                int seconds = calendar.get(Calendar.SECOND);

                milliTime = calendar.getTimeInMillis()-((hour*3600000)+(minute*60000)+(seconds*1000));

                Log.d("time", hour+":"+minute+":"+seconds);


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
                Bungee.slideRight(addActivity.this);

            }
        });


        addbutton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                if ((ActivityCompat.checkSelfPermission(addActivity.this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(addActivity.this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) && (!titleView.getText().toString().equals("") && !amountView.getText().toString().equals("") && !days.getText().toString().equals("") && days.getVisibility() == View.VISIBLE) || (!titleView.getText().toString().equals("") && !amountView.getText().toString().equals("") && days.getVisibility() == View.INVISIBLE)) {


                    Calendar calendar = Calendar.getInstance();


                    Bills bill = new Bills(titleView.getText().toString());

                    bill.set_day(selectedDate);
                    if (dbHandler.idArray().size()==0)
                    {
                        bill.set_id(0);
                    }
                    else {
                        id++;
                        bill.set_id(id);
                    }

                    bill.setAmt(amountView.getText().toString());
                    bill.set_currency(currencySpinner.getSelectedItem().toString());
                    bill.set_notifyDays(days.getText().toString());
                    String type = "";

                    bill.set_name(titleView.getText().toString());
                    if (billButton.isChecked()) {
                        bill.set_type("Bill");
                        type = "Bill";
                        bill.set_interval("");
                        interval = "";


                    }
                    if (subscriptionButton.isChecked()) {
                        bill.set_type("Subscription");
                        type = "Subscription";
                        bill.set_interval(intervalSpinner.getSelectedItem().toString());
                        interval = intervalSpinner.getSelectedItem().toString().toUpperCase();


                    }

                    if (reminder.isChecked()) {
                        bill.set_notify("true");
                        bill.set_notifyDays(days.getText().toString());

                        setNotification(type, titleView.getText().toString(), selectedDate, selectedDate, amountView.getText().toString(), Character.toString(currencySpinner.getSelectedItem().toString().charAt(4)), type, intervalSpinner.getSelectedItem().toString(), id);

                        String n = days.getText().toString();
                        int l = Integer.valueOf(n);
                        if (l > 0) {
                            int day = Integer.valueOf(selectedDate.substring(0, 2));
                            int month = Integer.valueOf(selectedDate.substring(3, 5));
                            int year = Integer.valueOf(selectedDate.substring(6, 10));

                            calendar.set(Calendar.DATE, day);
                            calendar.set(Calendar.MONTH, month);
                            calendar.set(Calendar.YEAR, year);
                            calendar.add(Calendar.DATE, -l);
                            String date = Integer.toString(calendar.get(Calendar.DATE));
                            String mon = Integer.toString(calendar.get(Calendar.MONTH));
                            String newyear = Integer.toString(calendar.get(Calendar.YEAR));
                            String newdate;
                            if (Integer.valueOf(mon) > 9) {
                                if (Integer.valueOf(date) > 9)
                                    newdate = date + "/" + mon + "/" + newyear;
                                else
                                    newdate = "0" + date + "/" + mon + "/" + newyear;
                            } else {

                                if (Integer.valueOf(date) > 9)
                                    newdate = date + "/0" + mon + "/" + newyear;
                                else
                                    newdate = "0" + date + "/0" + mon + "/" + newyear;
                            }
                            Log.d("check", "onClick: " + newdate);
                            setNotification(type, titleView.getText().toString(), newdate, selectedDate, amountView.getText().toString(), Character.toString(currencySpinner.getSelectedItem().toString().charAt(4)), type, intervalSpinner.getSelectedItem().toString(), id);
                        }

                    } else {
                        bill.set_notify("false");
                        bill.set_notifyDays("");

                    }
                    if (syncBox.isChecked()) {
                        bill.set_sync("true");
                        addEvent(milliTime, interval);


                    } else {
                        bill.set_sync("false");
                    }

                    dbHandler.addBill(bill);
                    if (dbHandler.addBill(bill)) {
                        if (edit.equals("")) {
                            Toast.makeText(getBaseContext(), "Bill added", Toast.LENGTH_SHORT).show();




                            Log.d("billReminder", "Bill added");


                        } else {
                            dbHandler.deleteBill(edit);
                           if (dbHandler.syncArray().get(Integer.parseInt(edit)).equals("true")) {
                                Uri eventsUri;
                                int osVersion = android.os.Build.VERSION.SDK_INT;
                                if (osVersion <= 7) { //up-to Android 2.1
                                    eventsUri = Uri.parse("content://calendar/events");
                                } else { //8 is Android 2.2 (Froyo) (http://developer.android.com/reference/android/os/Build.VERSION_CODES.html)
                                    eventsUri = Uri.parse("content://com.android.calendar/events");
                                }

                                ContentResolver resolver = getBaseContext().getContentResolver();
                                deleteEvent(resolver, eventsUri, 3, Integer.parseInt(edit));
//
                            }

                            addEvent(milliTime, interval);
                            Toast.makeText(getBaseContext(), "Bill changed", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(addActivity.this, AlarmReciever.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(addActivity.this, Integer.valueOf(edit), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmManager = (AlarmManager) addActivity.this.getSystemService(ALARM_SERVICE);
                            alarmManager.cancel(pendingIntent);
                            pendingIntent.cancel();
                        }

                    } else {
                        Toast.makeText(getBaseContext(), "Something wasn't right", Toast.LENGTH_SHORT).show();

                    }


                    Intent intent = new Intent(addActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {


                    if (titleView.getText().toString().equals("")) {
                        YoYo.with(Techniques.Shake)
                                .duration(900)
                                .playOn(titleView);
                    }

                    if (amountView.getText().toString().equals("")) {
                        YoYo.with(Techniques.Shake)
                                .duration(900)
                                .playOn(amountView);
                    }
                    if (days.getVisibility() == View.VISIBLE) {
                        if (days.getText().toString().equals("")) {
                            YoYo.with(Techniques.Shake)
                                    .duration(900)
                                    .playOn(days);
                        }
                    }


                 /*   if (ActivityCompat.checkSelfPermission(addActivity.this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(addActivity.this, new String[]{Manifest.permission.READ_CALENDAR}, REQUEST_CODE);
                        if (ActivityCompat.checkSelfPermission(addActivity.this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED)
                            Toast.makeText(getBaseContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                    if (ActivityCompat.checkSelfPermission(addActivity.this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(addActivity.this, new String[]{Manifest.permission.WRITE_CALENDAR}, REQUEST_CODE);}
                 */


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

        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (reminder.isChecked()) {
                    days.setVisibility(View.VISIBLE);
                    divider2.setVisibility(View.VISIBLE);
                } else if (!reminder.isChecked()) {
                    days.setVisibility(View.INVISIBLE);
                    divider2.setVisibility(View.INVISIBLE);

                }

            }
        });


//
        //


    }

    private void getIncomingIntent() {


        if (getIntent().hasExtra("amount") && getIntent().hasExtra("name")) {
            Log.d("intent", "getIncomingIntent: found intent extras.");

            backbutton.setVisibility(View.INVISIBLE);
            String editamount = getIntent().getStringExtra("amount");
             editId = getIntent().getStringExtra("id");
            String editname = getIntent().getStringExtra("name");
            String editcurrency = getIntent().getStringExtra("currency");
            String editnotify = getIntent().getStringExtra("notify");
            String editsync = getIntent().getStringExtra("sync");
            final String editinterval = getIntent().getStringExtra("interval");
            String editdate = getIntent().getStringExtra("dates");
            Log.d("check", editdate);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            try {
                java.util.Date date = sdf1.parse(editdate);
                 long millis = date.getTime();
                mCalendarView.setDate(millis);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String editnotifydays = getIntent().getStringExtra("notifydays");
            days.setText(editnotifydays);
            String edittype = getIntent().getStringExtra("type");
            String editid = getIntent().getStringExtra("id");
            if (editnotify.equals("false")) {
                divider2.setVisibility(View.INVISIBLE);
                days.setVisibility(View.INVISIBLE);
            } else if (editnotify.equals("true")) {
                divider2.setVisibility(View.VISIBLE);
                days.setVisibility(View.VISIBLE);
            }

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
            if (editsync.equals("true"))
                syncBox.setChecked(true);
            else
                syncBox.setChecked(false);


            edit = editid;


        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        editor.putInt("id", id);
        editor.commit();

    }


     /*public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                            @NonNull int[] grantResults) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);

         if (requestCode == REQUEST_CODE) {

             if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                 Log.d("Clima", "onRequestPermissionResult(): Permission granted");
                 addEvent(milliTime, interval);

             } else {

                 Log.d("Clima", "Permission denied =( ");

             }

         }
     }*/

    private void addEvent(long dateTime, String interval) {
        ContentResolver cr = getBaseContext().getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Events.DESCRIPTION, titleView.getText().toString() + " due on " + amountView.getText().toString());
        if (billButton.isChecked()) {
            cv.put(CalendarContract.Events.TITLE, "Bill Payment");
        } else if (subscriptionButton.isChecked()) {
            cv.put(CalendarContract.Events.TITLE, "Subscription Payment");
        }

        cv.put(CalendarContract.Events.CALENDAR_ID, 3);
        cv.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());


        if (!interval.equals("")) {
            cv.put(CalendarContract.Events.RRULE, "FREQ=" + interval.toUpperCase());
        }

        cv.put(CalendarContract.Events.DTSTART, dateTime);

        cv.put(CalendarContract.Events.DURATION, "P1D");
        cv.put(CalendarContract.Events.DESCRIPTION, titleView.getText().toString());








      /*  if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(addActivity.this, new String[]{Manifest.permission.WRITE_CALENDAR}, REQUEST_CODE);
            return;

        }
      */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, cv);
            return;
        }
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, cv);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setNotification(String title, String text, String dateofnotificaiton,String dateofbill, String amount, String currency,String type,String Interval,int id1){



        int day = Integer.valueOf(dateofnotificaiton.substring(0,2));
        int month = Integer.valueOf(dateofnotificaiton.substring(3,5));
        int year = Integer.valueOf(dateofnotificaiton.substring(6,10));
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
        intent.putExtra("amount",amount);
        intent.putExtra("name",text);
        intent.putExtra("currency", currency);
        //   intent.putExtra("interval");
        intent.putExtra("dates",dateofbill);
        intent.putExtra("type",title);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),id1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        intent.putExtra("amount",amount);
        intent.putExtra("name",text);
        intent.putExtra("currency", currency);
     //   intent.putExtra("interval");
        intent.putExtra("dates",dateofbill);
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
            Log.d("billReminder","Channel created");
        }
    }
    private void deleteEvent(ContentResolver resolver, Uri eventsUri, int calendarId, int postion) {
        Cursor cursor;
        if (android.os.Build.VERSION.SDK_INT <= 7) { //up-to Android 2.1
            cursor = resolver.query(eventsUri, new String[]{ "_id" }, "Calendars._id=" + calendarId, null, null);
        } else { //8 is Android 2.2 (Froyo) (http://developer.android.com/reference/android/os/Build.VERSION_CODES.html)
            cursor = resolver.query(eventsUri, new String[]{ "_id" }, "calendar_id=" + calendarId, null, null);
        }
        cursor.moveToPosition(postion);
        long eventId = cursor.getLong(cursor.getColumnIndex("_id"));
        resolver.delete(ContentUris.withAppendedId(eventsUri, eventId), null, null);

        cursor.close();
    }

    private void checkPermissions(int callbackId, String... permissionsId) {
        boolean permissions = true;
        for (String p : permissionsId) {
            permissions = permissions && ContextCompat.checkSelfPermission(this, p) == PERMISSION_GRANTED;
            syncBox.setChecked(true);

        }

        if (!permissions)
            ActivityCompat.requestPermissions(this, permissionsId, callbackId);
    }



}
