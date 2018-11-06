package com.example.malaligowda.billreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

public class displayAdapter extends RecyclerView.Adapter<displayAdapter.ViewHolder>  {
    private ArrayList<String> mId;
    private ArrayList<String> mNames;
    private ArrayList<String> mDates;
    private ArrayList<String> mCurrency;
    private ArrayList<String> mAmount;
    private ArrayList<String> mType;
    private ArrayList<String> mNotify;
    private ArrayList<String> mInterval;
    MyDBHandler dbHandler;
    private Context mContext;
    private ArrayList<String> mNotifyDays;
    private ArrayList<String> msync;
    private TextView displayView;
    private RecyclerView mRecyclerView;
    private ImageButton check;
    private ImageButton delete;
    private ImageButton edit;








    public displayAdapter(Context context, ArrayList<String> id, ArrayList<String> names, ArrayList<String> dates, ArrayList<String> currency, ArrayList<String> amount, ArrayList<String> type, ArrayList<String> notify, ArrayList<String> interval, ArrayList<String> notifyDays, ArrayList<String> sync, TextView display, RecyclerView recyclerView, ImageButton editButton, ImageButton deleteButton, ImageButton checkButton) {


        mContext = context;
        mNames = names;
        mDates = dates;
        mCurrency = currency;
        mAmount = amount;
        mType = type;
        mNotify = notify;
        mInterval = interval;
        mNotifyDays = notifyDays;
        mId = id;
        msync = sync;
        displayView = display;
        mRecyclerView = recyclerView;
        delete = deleteButton;
        edit = editButton;
        check = checkButton;
    }

    @NonNull
    @Override
    public displayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_rows, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        displayView.setVisibility(View.INVISIBLE);
        RequestOptions requestOptions = new RequestOptions().placeholder(R.color.white);
        holder.nameDisplay.setText(mNames.get(position));
        holder.currencyDisplay.setText(mCurrency.get(position));
        holder.dateDisplay.setText(mDates.get(position));
        holder.amountDisplay.setText(mAmount.get(position));
        holder.typeView.setText(mType.get(position));
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");

        String selectedDate = sdf1.format(new java.sql.Date(System.currentTimeMillis()));

        try {
            Date date = sdf1.parse(mDates.get(position));
            long millis = date.getTime();
            if (System.currentTimeMillis()>millis)
            {
                if (selectedDate.equals(mDates.get(position)))
                {
                    holder.typeView.setText(mType.get(position)+" : Due Today");

                }
                else {

                    holder.typeView.setText(mType.get(position) + " : Overdue");
                }
                holder.dateDisplay.setTextColor(Color.RED);
                holder.amountDisplay.setTextColor(Color.RED);
                holder.nameDisplay.setTextColor(Color.RED);
                holder.currencyDisplay.setTextColor(Color.RED);
                holder.typeView.setTextColor(Color.RED);

                }
            else {
                holder.typeView.setText(mType.get(position));

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        dbHandler = new MyDBHandler(mContext,null,null,5);

        Glide.with(mContext)
                .load(R.drawable.edit)
                .apply(requestOptions)
                .into(holder.editButton);
        Glide.with(mContext)
                .load(R.drawable.check)
                .apply(requestOptions)
                .into(holder.checkButton);
        Glide.with(mContext)
                .load(R.drawable.delete)
                .apply(requestOptions)
                .into(holder.deleteButton);



        holder.mainbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.mainbutton.setClickable(false);








                Log.d("billReminder", holder.nameDisplay.getText().toString()+"  main clicked");
                if (holder.editButton.getVisibility()== View.INVISIBLE) {

                    YoYo.with(Techniques.Landing)
                            .duration(700)
                            .playOn(holder.deleteButton);
                    YoYo.with(Techniques.Landing)
                            .duration(700)
                            .playOn(holder.editButton);
                    YoYo.with(Techniques.Landing)
                            .duration(700)
                            .playOn(holder.checkButton);
                    holder.editButton.setVisibility(View.VISIBLE);
                    holder.checkButton.setVisibility(View.VISIBLE);
                    holder.deleteButton.setVisibility(View.VISIBLE);
                }
                else if (holder.editButton.getVisibility()== View.VISIBLE){
                    YoYo.with(Techniques.ZoomOut)
                            .duration(700)
                            .playOn(holder.deleteButton);
                    YoYo.with(Techniques.ZoomOut)
                            .duration(700)
                            .playOn(holder.editButton);
                    YoYo.with(Techniques.ZoomOut)
                            .duration(700)
                            .playOn(holder.checkButton);
                    Log.d("billReminder", "animation Played");

//                    holder.editButton.setVisibility(View.INVISIBLE);
//                    holder.checkButton.setVisibility(View.INVISIBLE);
//                    holder.deleteButton.setVisibility(View.INVISIBLE);

                }

            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("billReminder", holder.nameDisplay.getText().toString() + " delete clicked");


                if (mNames.size() != 1){
                    String remove = mId.get(position);
                    Log.d("del", ""+mId.get(position));
                dbHandler.deleteBill(remove);
                deleteitem(position);
            }
                else{
                    String remove = mId.get(0);
                    dbHandler.deleteBill(remove);
                    deleteitem(0);
                    YoYo.with(Techniques.BounceIn)
                            .duration(700)
                            .playOn(displayView);
                    displayView.setVisibility(View.VISIBLE);
                }
                Uri eventsUri;
                int osVersion = android.os.Build.VERSION.SDK_INT;
                if (osVersion <= 7) { //up-to Android 2.1
                    eventsUri = Uri.parse("content://calendar/events");
                } else { //8 is Android 2.2 (Froyo) (http://developer.android.com/reference/android/os/Build.VERSION_CODES.html)
                    eventsUri = Uri.parse("content://com.android.calendar/events");
                }

                ContentResolver resolver = mContext.getContentResolver();
                deleteEvent(resolver, eventsUri, 3, position);
                Toast.makeText(mContext, "Event Deleted", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, AlarmReciever.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,Integer.valueOf(mId.get(position)),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();

            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("billReminder", holder.nameDisplay.getText().toString()+" edit clicked");

                Uri eventsUri;
                int osVersion = android.os.Build.VERSION.SDK_INT;
                if (osVersion <= 7) { //up-to Android 2.1
                    eventsUri = Uri.parse("content://calendar/events");
                } else { //8 is Android 2.2 (Froyo) (http://developer.android.com/reference/android/os/Build.VERSION_CODES.html)
                    eventsUri = Uri.parse("content://com.android.calendar/events");
                }
                ContentResolver resolver = mContext.getContentResolver();
                deleteEvent(resolver, eventsUri, 3, position);
                Intent intent = new Intent(mContext, addActivity.class);
                intent.putExtra("id",mId.get(position));
                intent.putExtra("amount",mAmount.get(position));
                intent.putExtra("name",mNames.get(position));
                intent.putExtra("notify",mNotify.get(position));
                intent.putExtra("currency",mCurrency.get(position));
                intent.putExtra("interval",mInterval.get(position));
                intent.putExtra("dates",mDates.get(position));
                intent.putExtra("type",mType.get(position));
                mContext.startActivity(intent);


            }
        });
        holder.checkButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Log.d("bllReminder", holder.nameDisplay.getText().toString()+" check clicked");
                holder.nameDisplay.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.currencyDisplay.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.amountDisplay.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.editButton.setVisibility(View.GONE);
                holder.checkButton.setVisibility(View.GONE);
                holder.deleteButton.setVisibility(View.GONE);
                holder.mainbutton.setClickable(false);
                String newdate="";
                Uri eventsUri;
                int osVersion = android.os.Build.VERSION.SDK_INT;
                if (osVersion <= 7) { //up-to Android 2.1
                    eventsUri = Uri.parse("content://calendar/events");
                } else { //8 is Android 2.2 (Froyo) (http://developer.android.com/reference/android/os/Build.VERSION_CODES.html)
                    eventsUri = Uri.parse("content://com.android.calendar/events");
                }

                ContentResolver resolver = mContext.getContentResolver();
                deleteEvent(resolver, eventsUri, 3, position);
                Toast.makeText(mContext, "Bill Deleted", Toast.LENGTH_SHORT).show();
                if(mType.get(position).equals("Subscription"))
                {
                    String olddate = mDates.get(position);
                    if (mInterval.get(position).equals("Monthly")) {

                        if(olddate.substring(3,5).equals("01"))
                        {
                            if(Integer.valueOf(olddate.substring(0,2))>28)
                            newdate = "28/02/"+olddate.substring(6,10);
                        }
                        else if(olddate.substring(3,5).equals("03"))
                        {
                            if(Integer.valueOf(olddate.substring(0,2))>30)
                            newdate = "30/04/"+olddate.substring(6,10);
                        }
                        else if(olddate.substring(3,5).equals("05"))
                        {
                            if(Integer.valueOf(olddate.substring(0,2))>30)
                            newdate = "30/06/"+olddate.substring(6,10);
                        }
                        else if(olddate.substring(3,5).equals("08"))
                        {
                            if(Integer.valueOf(olddate.substring(0,2))>30)
                            newdate = "30/09/"+olddate.substring(6,10);
                        }

                        else
                        {

                        if (Integer.valueOf(olddate.substring(3, 5)) == 12)
                            newdate = olddate.substring(0, 2) + "/01/" + Integer.toString(Integer.valueOf(olddate.substring(6, 10)) + 1);
                        else
                            newdate = olddate.substring(0, 2) + "/" + Integer.toString(Integer.valueOf(olddate.substring(3, 5)) + 1) + "/" + olddate.substring(6, 10);

                      }
                        }
                        else
                        newdate = olddate.substring(0,6)+Integer.toString(Integer.valueOf(olddate.substring(6,10))+1);
                    dbHandler.updateDate(newdate,mId.get(position));
                    Intent intent = new Intent(mContext, AlarmReciever.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,Integer.valueOf(mId.get(position)),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
                    alarmManager.cancel(pendingIntent);
                    pendingIntent.cancel();
                    setNotification(mType.get(position), mNames.get(position),newdate,newdate, mAmount.get(position), Character.toString(mCurrency.get(position).charAt(4)),mType.get(position),mInterval.get(position),Integer.valueOf(mId.get(position)));


                    int l = Integer.valueOf(mNotifyDays.get(position));
                    if (l > 0) {
                        int day = Integer.valueOf(newdate.substring(0, 2));
                        int month = Integer.valueOf(newdate.substring(3, 5));
                        int year = Integer.valueOf(newdate.substring(6, 10));
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.DATE, day);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.YEAR, year);
                        calendar.add(Calendar.DATE, -l);
                        String date = Integer.toString(calendar.get(Calendar.DATE));
                        String mon = Integer.toString(calendar.get(Calendar.MONTH));
                        String newyear = Integer.toString(calendar.get(Calendar.YEAR));
                        String notifydate;
                        if (Integer.valueOf(mon) > 9){
                            if (Integer.valueOf(date) > 9)
                                notifydate = date + "/" + mon + "/" + newyear;
                            else
                                notifydate = "0"+date + "/" + mon + "/" + newyear;
                        }
                        else {

                            if (Integer.valueOf(date) > 9)
                                notifydate = date + "/0" + mon + "/" + newyear;
                            else
                                notifydate = "0" + date + "/0" + mon + "/" + newyear;
                        }
                        setNotification(mType.get(position), mNames.get(position),notifydate,newdate, mAmount.get(position), Character.toString(mCurrency.get(position).charAt(4)),mType.get(position),mInterval.get(position),Integer.valueOf(mId.get(position)));
                    }
                }
                else{
                    String remove = mId.get(position);
                    dbHandler.deleteBill(remove);
                }

                holder.view.setVisibility(View.VISIBLE);
                holder.layout.setClickable(false);



            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.editButton.setVisibility(View.INVISIBLE);
                holder.checkButton.setVisibility(View.INVISIBLE);
                holder.deleteButton.setVisibility(View.INVISIBLE);
                holder.mainbutton.setClickable(true);

            }
        });



    }

    @Override
    public int getItemCount() {
        if (mNames!=null) {
            return mNames.size();
        }
        else {
            return 0;
        }
    }
    public void deleteitem(int position)
    {
        mAmount.remove(position);
        mCurrency.remove(position);
        mNames.remove(position);
        mInterval.remove(position);
        mDates.remove(position);
        mNotify.remove(position);
        mType.remove(position);
        msync.remove(position);
        notifyItemRemoved(position);
    }


     class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameDisplay;
        TextView dateDisplay;
        TextView currencyDisplay;
        TextView amountDisplay;
        TextView typeView;
        ConstraintLayout layout;
        Button mainbutton;
        ImageButton deleteButton;
        ImageButton editButton;
        ImageButton checkButton;
        ImageView view;



        RecyclerView mRecyclerView;


         public ViewHolder(View itemView) {
             super(itemView);

             this.amountDisplay = itemView.findViewById(R.id.amountView);
             this.nameDisplay = itemView.findViewById(R.id.nameView);
             this.dateDisplay = itemView.findViewById(R.id.dateView);
             this.currencyDisplay = itemView.findViewById(R.id.currencyView);
             this.typeView = itemView.findViewById(R.id.typeView);
             this.layout = itemView.findViewById(R.id.parent_layout);
             this.deleteButton = itemView.findViewById(R.id.deleteButton);
             this.editButton = itemView.findViewById(R.id.editButton);
             this.checkButton = itemView.findViewById(R.id.checkButton);
             mRecyclerView = itemView.findViewById(R.id.displayView);
             this.layout = itemView.findViewById(R.id.parent_layout);
             this.mainbutton = itemView.findViewById(R.id.mainButton);
             this.deleteButton = itemView.findViewById(R.id.deleteButton);
             this.editButton = itemView.findViewById(R.id.editButton);
             this.checkButton = itemView.findViewById(R.id.checkButton);
             this.view = itemView.findViewById(R.id.imageView);

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
   // long eventId = cursor.getLong(cursor.getColumnIndex("_id"));
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
       Intent intent = new Intent(mContext, AlarmReciever.class);
       intent.putExtra("amount",amount);
       intent.putExtra("name",text);
       intent.putExtra("currency", currency);
       //   intent.putExtra("interval");
       intent.putExtra("dates",dateofbill);
       intent.putExtra("type",title);
       PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,id1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
       intent.putExtra("amount",amount);
       intent.putExtra("name",text);
       intent.putExtra("currency", currency);
       //   intent.putExtra("interval");
       intent.putExtra("dates",dateofbill);
       intent.putExtra("type",title);

       AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
       alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

   }

}
