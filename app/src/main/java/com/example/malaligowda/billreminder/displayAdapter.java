package com.example.malaligowda.billreminder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class displayAdapter extends RecyclerView.Adapter<displayAdapter.ViewHolder> {

    private ArrayList<String> mNames;
    private ArrayList<String> mDates;
    private ArrayList<String> mCurrency;
    private ArrayList<String> mAmount;
    private ArrayList<String> mType;
    private ArrayList<String> mNotify;
    private ArrayList<String> mInterval;
    private Context mContext;


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

        holder.nameDisplay.setText(mNames.get(position));
        holder.currencyDisplay.setText(mCurrency.get(position));
        holder.dateDisplay.setText(mDates.get(position));
        holder.amountDisplay.setText(mAmount.get(position));
        holder.typeView.setText(mType.get(position));


        holder.mainbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mNames.get(position), Toast.LENGTH_SHORT).show();

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


     class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameDisplay;
        TextView dateDisplay;
        TextView currencyDisplay;
        TextView amountDisplay;
        TextView typeView;
        ConstraintLayout layout;
        Button mainbutton;


        RecyclerView mRecyclerView;


         public ViewHolder(View itemView) {
             super(itemView);

             this.amountDisplay = itemView.findViewById(R.id.amountView);
             this.nameDisplay = itemView.findViewById(R.id.nameView);
             this.dateDisplay = itemView.findViewById(R.id.dateView);
             this.currencyDisplay = itemView.findViewById(R.id.currencyView);
             this.typeView = itemView.findViewById(R.id.typeView);
             this.layout = itemView.findViewById(R.id.parent_layout);
             this.mainbutton = itemView.findViewById(R.id.RecyclerButton);
             mRecyclerView = itemView.findViewById(R.id.displayView);

         }
     }
}
