package com.example.tracker.Utilities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracker.Activities.DetailedView;
import com.example.tracker.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private static final String TAG = "MyAdapter";
    private Context context;
    private List<DataClass> dataList;
    private onTransactionCLick onTransactionCLick;

    public MyAdapter(Context context, List<DataClass> dataList, onTransactionCLick onTransactionCLick) {
        this.context = context;
        this.dataList = dataList;
        this.onTransactionCLick = onTransactionCLick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataClass data = dataList.get(position);
        int pos = position;

        // Log data for debugging
        Log.d(TAG, "Binding data at position: " + position + " with title: " + data.getTitle()
                + ", amount: " + data.getAmount()
                + ", category: " + data.getCategory()
                + ", method: " + data.getPaymentMethod());

        holder.recTitle.setText(data.getTitle());
        holder.recAmount.setText("$" + data.getAmount());
        holder.recCategory.setText(data.getCategory());
        holder.recMethod.setText(data.getPaymentMethod());

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTransactionCLick.onTransactionItemClick(pos);

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void updateList(List<DataClass> newList) {
        dataList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView recTitle, recAmount, recCategory, recMethod;
        CardView recCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recCard = itemView.findViewById(R.id.recCard);
            recTitle = itemView.findViewById(R.id.recTitle);
            recAmount = itemView.findViewById(R.id.recAmount);
            recCategory = itemView.findViewById(R.id.recCategory);
            recMethod = itemView.findViewById(R.id.recMethod);
        }

    }

}
