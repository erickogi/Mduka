package com.erickogi14gmail.mduka.Sell;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.erickogi14gmail.mduka.Db.StockItemsPojo;
import com.erickogi14gmail.mduka.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Eric on 8/29/2017.
 */

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<StockItemsPojo> modelList;
    private cartItemClickListener cartItemClickListener;

    public CartItemsAdapter(Context context, ArrayList<StockItemsPojo> modelList, cartItemClickListener cartItemClickListener) {
        this.context = context;
        this.modelList = modelList;
        this.cartItemClickListener = cartItemClickListener;
    }

    @Override
    public CartItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new MyViewHolder(itemView, cartItemClickListener);
    }

    @Override
    public void onBindViewHolder(CartItemsAdapter.MyViewHolder holder, int position) {
        StockItemsPojo stockItemsPojo = modelList.get(position);
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

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtItemName, txtItemPrice, txtItemTotalPrice;
        Button btnRemove, btnChange;
        private WeakReference<cartItemClickListener> listenerWeakReference;


        public MyViewHolder(View itemView, cartItemClickListener cartItemClickListener) {
            super(itemView);
            listenerWeakReference = new WeakReference<cartItemClickListener>(cartItemClickListener);

            txtItemName = (TextView) itemView.findViewById(R.id.item_name);
            txtItemTotalPrice = (TextView) itemView.findViewById(R.id.item_total_price);
            txtItemPrice = (TextView) itemView.findViewById(R.id.item_price);

            btnChange = (Button) itemView.findViewById(R.id.btn_change);
            btnRemove = (Button) itemView.findViewById(R.id.btn_remove);

            btnRemove.setOnClickListener(this);
            btnChange.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_remove:
                    listenerWeakReference.get().onBtnRemoveClicked(getAdapterPosition());

                    break;
                case R.id.btn_change:
                    listenerWeakReference.get().onBtnChangeClicked(getAdapterPosition());


                    break;
            }
        }
    }
}
