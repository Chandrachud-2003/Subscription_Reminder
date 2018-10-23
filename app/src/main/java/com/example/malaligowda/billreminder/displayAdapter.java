package com.example.malaligowda.billreminder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

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




//
    public displayAdapter(Context context,ArrayList<String> names, ArrayList<String> dates, ArrayList<String> currency, ArrayList<String> amount, ArrayList<String> type, ArrayList<String> notify, ArrayList<String> interval) {

        mContext = context;
        mNames = names;
        mDates = dates;
        mCurrency = currency;
        mAmount = amount;
        mType = type;
        mNotify = notify;
        mInterval = interval;
    }

    @NonNull
    @Override
    public displayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_rows, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        RequestOptions requestOptions = new RequestOptions().placeholder(R.color.white);
        holder.nameDisplay.setText(mNames.get(position));
        holder.currencyDisplay.setText(mCurrency.get(position));
        holder.dateDisplay.setText(mDates.get(position));
        holder.amountDisplay.setText(mAmount.get(position));
        holder.typeView.setText(mType.get(position));
        dbHandler = new MyDBHandler(mContext,null,null,1);

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

                Log.d("illReminder", holder.nameDisplay.getText().toString()+"  main clicked");
                if (holder.editButton.getVisibility()== View.INVISIBLE) {
                    holder.editButton.setVisibility(View.VISIBLE);
                    holder.checkButton.setVisibility(View.VISIBLE);
                    holder.deleteButton.setVisibility(View.VISIBLE);
                }
                else {
                    holder.editButton.setVisibility(View.INVISIBLE);
                    holder.checkButton.setVisibility(View.INVISIBLE);
                    holder.deleteButton.setVisibility(View.INVISIBLE);
                }
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("billReminder", holder.nameDisplay.getText().toString() + " delete clicked");


                if (mNames.size() != 1){
                    String remove = mNames.get(position);
                dbHandler.deleteBill(remove);
                deleteitem(position);
            }
                else{
                    String remove = mNames.get(0);
                    dbHandler.deleteBill(remove);
                    deleteitem(0);
                }


            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("illReminder", holder.nameDisplay.getText().toString()+" edit clicked");

                Intent intent = new Intent(mContext, addActivity.class);
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
            @Override
            public void onClick(View v) {
                Log.d("illReminder", holder.nameDisplay.getText().toString()+" check clicked");
                holder.nameDisplay.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.currencyDisplay.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.amountDisplay.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.editButton.setVisibility(View.GONE);
                holder.checkButton.setVisibility(View.GONE);
                holder.deleteButton.setVisibility(View.GONE);
                holder.mainbutton.setClickable(false);
                String remove = mNames.get(position);
                dbHandler.deleteBill(remove);
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
}
