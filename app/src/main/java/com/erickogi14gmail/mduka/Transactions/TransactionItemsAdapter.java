package com.erickogi14gmail.mduka.Transactions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erickogi14gmail.mduka.Db.StockItemsPojo;
import com.erickogi14gmail.mduka.R;

import java.util.ArrayList;

/**
 * Created by Eric on 8/29/2017.
 */

public class TransactionItemsAdapter extends RecyclerView.Adapter<TransactionItemsAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<StockItemsPojo> modelList;

    public TransactionItemsAdapter(Context context, ArrayList<StockItemsPojo> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @Override
    public TransactionItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TransactionItemsAdapter.MyViewHolder holder, int position) {
        StockItemsPojo stockItemsPojo = modelList.get(position);
        holder.linearLayout.setVisibility(View.GONE);
        holder.txtItemName.setText(stockItemsPojo.getItem_name() + " x " + stockItemsPojo.getItem_quantity() + "  " + stockItemsPojo.getItem_unit_type());
        double Qty = Double.valueOf(stockItemsPojo.getItem_quantity()) * Double.valueOf(stockItemsPojo.getItem_selling_price());
        holder.txtItemTotalPrice.setText("" + String.valueOf(Qty) + " Ksh");
        holder.txtItemPrice.setText("@ : " + stockItemsPojo.getItem_selling_price() + "  Ksh");
    }

    @Override
    public int getItemCount() {
        try {
            return modelList.size();
        } catch (NullPointerException m) {
            return 0;
        }
    }

    public void updateList(ArrayList<StockItemsPojo> list) {
        modelList = list;
        notifyDataSetChanged();
    }

    public void updateItemItem(int position, StockItemsPojo stockItemsPojo) {
        modelList.get(position).setItem_quantity(stockItemsPojo.getItem_quantity());
        notifyItemChanged(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtItemName, txtItemPrice, txtItemTotalPrice;
        LinearLayout linearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.layout_buttons);
            txtItemName = (TextView) itemView.findViewById(R.id.item_name);
            txtItemTotalPrice = (TextView) itemView.findViewById(R.id.item_total_price);
            txtItemPrice = (TextView) itemView.findViewById(R.id.item_price);
        }
    }
}
