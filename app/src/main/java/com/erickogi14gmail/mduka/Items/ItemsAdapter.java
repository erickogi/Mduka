package com.erickogi14gmail.mduka.Items;

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

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by Eric on 8/28/2017.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<StockItemsPojo> modelList;

    public ItemsAdapter(Context context, ArrayList<StockItemsPojo> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @Override
    public ItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_s_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    public Bitmap getThumbnail(String filename) {
        Bitmap thumnail = null;
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
    public void onBindViewHolder(ItemsAdapter.MyViewHolder holder, int position) {
        StockItemsPojo stockItemsPojo = modelList.get(position);
        holder.txtItemName.setText(stockItemsPojo.getItem_name());
        holder.txtItemQuantity.setText("Qty : " + stockItemsPojo.getItem_quantity() + "  " + stockItemsPojo.getItem_unit_type());
        holder.txtItemPrice.setText("Sp @ : " + stockItemsPojo.getItem_selling_price() + "  Kshs");
        holder.txtItemBuyingPrice.setText("Bp @ : " + stockItemsPojo.getItem_buying_price() + "  Kshs");
        holder.txtDiscount.setText("Discount : " + stockItemsPojo.getItem_discount() + "  %");
        holder.txtCategory.setText("Cat.. : " + stockItemsPojo.getItem_category());
        holder.txtProfit.setText("Profit : " + String.valueOf(Double.valueOf(stockItemsPojo.getItem_selling_price()) - Double.valueOf(stockItemsPojo.getItem_buying_price())));
        try {
            if (stockItemsPojo.getFav().equals("1")) {
                holder.imgFav.setImageResource(android.R.drawable.star_on);
            } else {
                holder.imgFav.setImageResource(android.R.drawable.star_off);
            }
        } catch (NullPointerException m) {
            holder.imgFav.setImageResource(android.R.drawable.star_off);
        }
        // Picasso.with(context).load(R.drawable.sky).centerCrop().fit().into(holder.imgItemImage);
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
        modelList.get(position).setFav(stockItemsPojo.getFav());
        notifyItemChanged(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtItemName, txtItemQuantity, txtItemPrice, txtItemBuyingPrice, txtDiscount, txtProfit, txtCategory;
        ImageView imgItemImage, imgFav;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtItemName = (TextView) itemView.findViewById(R.id.item_name);
            txtItemQuantity = (TextView) itemView.findViewById(R.id.item_quantity);
            txtItemPrice = (TextView) itemView.findViewById(R.id.item_price);

            txtItemBuyingPrice = (TextView) itemView.findViewById(R.id.item_buying_price);
            txtDiscount = (TextView) itemView.findViewById(R.id.item_discount);
            txtProfit = (TextView) itemView.findViewById(R.id.item_profit);

            txtCategory = (TextView) itemView.findViewById(R.id.item_category);
            imgItemImage = (ImageView) itemView.findViewById(R.id.item_image);
            imgFav = (ImageView) itemView.findViewById(R.id.img_fav);
        }
    }
}
