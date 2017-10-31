package com.erickogi14gmail.mduka.Sell;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.erickogi14gmail.mduka.Db.StockItemsPojo;
import com.erickogi14gmail.mduka.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by Eric on 8/28/2017.
 */

public class SellListAdapter extends RecyclerView.Adapter<SellListAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<StockItemsPojo> modelList;

    public SellListAdapter(Context context, ArrayList<StockItemsPojo> modelList) {
        this.context = context;
        this.modelList = modelList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sell_item, parent, false);
        return new MyViewHolder(itemView);
    }

    public Bitmap getThumbnail(String filename) {
        Bitmap thumnail = BitmapFactory.decodeResource(context.getResources(), R.drawable.mobisell);
        try {
            File filepath = context.getFileStreamPath(filename);
            FileInputStream fi = new FileInputStream(filepath);
            thumnail = BitmapFactory.decodeStream(fi);

        } catch (Exception m) {
            m.printStackTrace();
        }
        return thumnail;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        StockItemsPojo stockItemsPojo = modelList.get(position);
        holder.txtItemName.setText(stockItemsPojo.getItem_name());
        holder.txtItemQuantity.setText("Qty : " + stockItemsPojo.getItem_quantity() + "  " + stockItemsPojo.getItem_unit_type());
        holder.txtItemPrice.setText("@ : " + stockItemsPojo.getItem_selling_price() + "  Kshs");
        //Picasso.with(context).load(R.drawable.sky).centerCrop().fit().into(holder.imgItemImage);
        if (Double.valueOf(stockItemsPojo.getItem_quantity()) < 220) {
            Picasso.with(context).load(R.drawable.ic_stop_red_24dp).into(holder.imgQuantity);
            holder.imgQuantity.setImageResource(R.drawable.ic_stop_red_24dp);
        } else if (Double.valueOf(stockItemsPojo.getItem_quantity()) > 220 && Double.valueOf(stockItemsPojo.getItem_quantity()) < 240) {
            Picasso.with(context).load(R.drawable.ic_stop_yellow_24dp).into(holder.imgQuantity);
            holder.imgQuantity.setImageResource(R.drawable.ic_stop_yellow_24dp);
        } else {
            Picasso.with(context).load(R.drawable.ic_stop_green_24dp).into(holder.imgQuantity);
            holder.imgQuantity.setImageResource(R.drawable.ic_stop_green_24dp);
        }
        try {
            holder.imgItemImage.setImageBitmap(getThumbnail(stockItemsPojo.getItem_image()));
            //imagePath=stockItemsPojos.get(position).getItem_image();
            //img.setImageBitmap(getThumbnail(imagePath));

        } catch (Exception m) {

            holder.imgItemImage.setImageResource(R.drawable.ic_dashboard_black_24dp);
        }
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
        TextView txtItemName, txtItemQuantity, txtItemPrice;
        ImageView imgItemImage;
        ImageView imgQuantity;

        private MyViewHolder(View itemView) {
            super(itemView);
            txtItemName = (TextView) itemView.findViewById(R.id.item_name);
            txtItemQuantity = (TextView) itemView.findViewById(R.id.item_quantity);
            txtItemPrice = (TextView) itemView.findViewById(R.id.item_price);
            imgItemImage = (ImageView) itemView.findViewById(R.id.item_image);
            imgQuantity = (ImageView) itemView.findViewById(R.id.imgQuantity);

        }
    }
}
