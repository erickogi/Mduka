package com.erickogi14gmail.mduka.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Eric on 8/28/2017.
 */

public class DbOperations  {
    private DbClass dbHandler;
    Context context;

    public DbOperations(Context context) {
        dbHandler = new DbClass(context);
        this.context=context;
    }






    public ArrayList<StockItemsPojo> getAllItems(String search) {
        //Open connection to read only
        SQLiteDatabase db = dbHandler.getReadableDatabase();


        ArrayList<StockItemsPojo> data = new ArrayList<>();
        String QUERY = "SELECT * FROM  mduka_items_stock " ;


        Cursor cursor = db.rawQuery(QUERY, null);

        if (!cursor.isLast()) {

            while (cursor.moveToNext()) {
                StockItemsPojo pojo = new StockItemsPojo();
                pojo.setItem_id(cursor.getInt(0));
                pojo.setItem_name(cursor.getString(1));
                pojo.setItem_quantity(cursor.getString(2));
                pojo.setItem_unit_type(cursor.getString(3));
                pojo.setItem_buying_price(cursor.getString(4));
                pojo.setItem_selling_price(cursor.getString(5));
                pojo.setItem_category(cursor.getString(6));
                pojo.setItem_discount(cursor.getString(7));

                data.add(pojo);

            }
        }
        db.close();
        // looping through all rows and adding to list

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return data;


    }
    public ArrayList<StockItemsPojo> getAllByCategories(String search,String category){
        //Open connection to read only
        SQLiteDatabase db = dbHandler.getReadableDatabase();


        ArrayList<StockItemsPojo> data = new ArrayList<>();
        String QUERY = "SELECT * FROM  mduka_items_stock WHERE item_name LIKE '%"+search+"%' AND item_category = '"+category+"'" ;


        Cursor cursor = db.rawQuery(QUERY, null);

        if (!cursor.isLast()) {

            while (cursor.moveToNext()) {
                StockItemsPojo pojo = new StockItemsPojo();
                pojo.setItem_id(cursor.getInt(0));
                pojo.setItem_name(cursor.getString(1));
                pojo.setItem_quantity(cursor.getString(2));
                pojo.setItem_unit_type(cursor.getString(3));
                pojo.setItem_buying_price(cursor.getString(4));
                pojo.setItem_selling_price(cursor.getString(5));
                pojo.setItem_category(cursor.getString(6));
                pojo.setItem_discount(cursor.getString(7));

                data.add(pojo);

            }
        }
        db.close();
        // looping through all rows and adding to list

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return data;


    }


    public boolean insertItem(StockItemsPojo data) {
        boolean success = false;

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("item_id", data.getItem_id());
        values.put("item_name", data.getItem_name());


        values.put("item_quantity", data.getItem_quantity());
        values.put("item_unit_type", data.getItem_unit_type());
        values.put("item_buying_price", data.getItem_buying_price());
        values.put("item_selling_price", data.getItem_selling_price());
        values.put("item_category",data.getItem_category());
        values.put("item_discount",data.getItem_discount());





        // Inserting Row
        if (db.insert("mduka_items_stock", null, values) >= 1) {
            success = true;
        }
        db.close();

        //    }
        //             }).start();
        return success;


    }
}
