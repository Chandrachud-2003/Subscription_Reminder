package com.example.malaligowda.billreminder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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
    private Context mContext;


    public displayAdapter(Context context,ArrayList<String> names, ArrayList<String> dates, ArrayList<String> currency, ArrayList<String> amount, ArrayList<String> type, ArrayList<String> notify) {

        mContext = context;
        mNames = names;
        mDates = dates;
        mCurrency = currency;
        mAmount = amount;
        mType = type;
        mNotify = notify;
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
        Glide.with(mContext)
                .load(R.drawable.edit)
                .apply(requestOptions)
                .into(holder.editButton);

        Glide.with(mContext)
                .load(R.drawable.check_mark)
                .apply(requestOptions)
                .into(holder.checkButton);
        Glide.with(mContext)
                .load(R.drawable.delete_icon)
                .apply(requestOptions)
                .into(holder.deleteButton);

        holder.mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.checkButton.setVisibility(View.VISIBLE);
                holder.editButton.setVisibility(View.VISIBLE);
                holder.deleteButton.setVisibility(View.VISIBLE);

                holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String billName = mNames.get(position);
                        MyDBHandler dbHandler=new MyDBHandler(mContext, null,null, 1);
                        dbHandler.deleteBill(billName);






                    }
                });

                holder.editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final int REQ_CODE_UPDATE=101;
                        String title = mNames.get(position);
                        String date = mDates.get(position);
                        String currency = mCurrency.get(position);
                        String type = mType.get(position);
                        String notify = mNotify.get(position);
                        String amount = mAmount.get(position);
                        Intent intent = new Intent(mContext, addActivity.class);
                        intent.putExtra("NAMES", title);
                        intent.putExtra("DATE", date);
                        intent.putExtra("CURRENCY", currency);
                        intent.putExtra("TYPE", type);
                        intent.putExtra("NOTIFY", notify);
                        intent.putExtra("AMOUNT", amount );
                        mContext.startActivity(intent);

                    }
                });

                holder.mainButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        holder.checkButton.setVisibility(View.INVISIBLE);
                        holder.editButton.setVisibility(View.INVISIBLE);
                        holder.deleteButton.setVisibility(View.INVISIBLE);

                    }
                });



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
        ImageButton editButton;
        ImageButton deleteButton;
        ImageButton checkButton;
        Button mainButton;
        RecyclerView mRecyclerView;


         public ViewHolder(View itemView) {
             super(itemView);

             this.amountDisplay = itemView.findViewById(R.id.amountView);
             this.nameDisplay = itemView.findViewById(R.id.nameView);
             this.dateDisplay = itemView.findViewById(R.id.dateView);
             this.currencyDisplay = itemView.findViewById(R.id.currencyView);
             this.editButton = itemView.findViewById(R.id.editIcon);
             this.deleteButton = itemView.findViewById(R.id.deleteButton);
             this.checkButton = itemView.findViewById(R.id.check);
             this.mainButton = itemView.findViewById(R.id.RecyclerButton);
             mRecyclerView = itemView.findViewById(R.id.displayView);

         }
     }
}
