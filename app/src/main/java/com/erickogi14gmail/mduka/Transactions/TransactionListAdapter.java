package com.erickogi14gmail.mduka.Transactions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erickogi14gmail.mduka.Db.TransactionsPojo;
import com.erickogi14gmail.mduka.R;

import java.util.ArrayList;

/**
 * Created by Eric on 9/6/2017.
 */

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<TransactionsPojo> modelList;

    public TransactionListAdapter(Context context, ArrayList<TransactionsPojo> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<TransactionsPojo> list) {
        modelList = list;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TransactionsPojo transactionsPojo = modelList.get(position);
        holder.textViewId.setText("Tr.No : " + String.valueOf(transactionsPojo.getTransaction_id()));
        holder.textViewItems.setText(transactionsPojo.getTransaction_quantity() + "  Items");
        holder.textViewDate.setText(transactionsPojo.getTransaction_date());
        holder.textViewPrice.setText("Ksh  " + transactionsPojo.getTransaction_total_sp());

    }

    @Override
    public int getItemCount() {
        try {
            return modelList.size();
        } catch (NullPointerException m) {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewId, textViewItems, textViewDate, textViewPrice;

        public MyViewHolder(View itemView) {
            super(itemView);
            textViewId = (TextView) itemView.findViewById(R.id.transaction_id);
            textViewItems = (TextView) itemView.findViewById(R.id.transaction_items);
            textViewDate = (TextView) itemView.findViewById(R.id.transaction_date);
            textViewPrice = (TextView) itemView.findViewById(R.id.transaction_total_price);
        }
    }
}
