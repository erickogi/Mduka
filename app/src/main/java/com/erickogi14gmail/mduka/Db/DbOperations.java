package com.erickogi14gmail.mduka.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Eric on 8/28/2017.
 */

public class DbOperations {
    Context context;
    private DbClass dbHandler;

    public DbOperations(Context context) {
        dbHandler = new DbClass(context);
        this.context = context;
    }


    public ArrayList<StockItemsPojo> getAllItems(String search) {
        //Open connection to read only
        SQLiteDatabase db = dbHandler.getReadableDatabase();


        ArrayList<StockItemsPojo> data = new ArrayList<>();
        String QUERY = null;
        ///if(!search.equals("")) {
        //     QUERY= "SELECT * FROM  mduka_items_stock WHERE item_quantity > 0 AND item_category LIKE '%" + search + "%'  AND item_name LIKE '%" + search + "%'";

        // }else {
        QUERY = "SELECT * FROM  mduka_items_stock WHERE item_quantity > 0  AND item_name LIKE '%" + search + "%'";

        // }

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
                pojo.setItem_image(cursor.getString(8));

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

    public ArrayList<StockItemsPojo> getAllItemFavorites(String search) {
        //Open connection to read only
        SQLiteDatabase db = dbHandler.getReadableDatabase();


        ArrayList<StockItemsPojo> data = new ArrayList<>();
        String QUERY = null;
        ///if(!search.equals("")) {
        //     QUERY= "SELECT * FROM  mduka_items_stock WHERE item_quantity > 0 AND item_category LIKE '%" + search + "%'  AND item_name LIKE '%" + search + "%'";

        // }else {
        QUERY = "SELECT * FROM  mduka_items_stock WHERE item_quantity > 0  AND item_name LIKE '%" + search + "%' AND favorite = 1";

        // }

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
                pojo.setItem_image(cursor.getString(8));

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

    public ArrayList<StockItemsPojo> getAllItems1(String cat, String search) {
        //Open connection to read only
        SQLiteDatabase db = dbHandler.getReadableDatabase();


        ArrayList<StockItemsPojo> data = new ArrayList<>();
        String QUERY = "SELECT * FROM  mduka_items_stock WHERE item_category LIKE '%" + cat + "%'  AND item_name LIKE '%" + search + "%'";


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
                pojo.setItem_image(cursor.getString(8));
                pojo.setFav(cursor.getString(9));

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


    public ArrayList<StockItemsPojo> getAllByCategories(String search, String category) {
        //Open connection to read only
        SQLiteDatabase db = dbHandler.getReadableDatabase();


        ArrayList<StockItemsPojo> data = new ArrayList<>();
        String QUERY = "SELECT * FROM  mduka_items_stock WHERE item_name LIKE '%" + search + "%' AND item_category = '" + category + "'";


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

    public String getItemQuantity(int id) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String quantity = "";
        String QUERY = "SELECT * FROM  mduka_items_stock WHERE item_id ='" + id + "'";
        Cursor cursor = db.rawQuery(QUERY, null);
        if (!cursor.isLast()) {

            while (cursor.moveToNext()) {

                quantity = cursor.getString(2);

            }
        }
        db.close();
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return quantity;


    }


    public boolean insertItem(StockItemsPojo data) {
        boolean success = false;

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();

        // values.put("item_id", data.getItem_id());
        values.put("item_name", data.getItem_name());


        values.put("item_quantity", data.getItem_quantity());
        values.put("item_unit_type", data.getItem_unit_type());
        values.put("item_buying_price", data.getItem_buying_price());
        values.put("item_selling_price", data.getItem_selling_price());
        values.put("item_category", data.getItem_category());
        values.put("item_discount", data.getItem_discount());
        values.put("item_image", data.getItem_image());


        if (db.insert("mduka_items_stock", null, values) >= 1) {
            success = true;
        }
        db.close();


        return success;


    }

    public boolean deleteItem(int rowId) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        return db.delete("mduka_items_stock", "item_id" + "= '" + rowId + "' ", null) > 0;
    }

    public boolean updateItemQuantity(int id, String chan) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("item_quantity", chan);

            return db.update("mduka_items_stock", cv, "item_id ='" + id + "'", null) > 0;
        } catch (Exception m) {
            m.printStackTrace();
            Log.d("mjk", m.toString());
            return false;
        }
    }

    public boolean updateItemFav(int id, String chan) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("favorite", chan);

            return db.update("mduka_items_stock", cv, "item_id ='" + id + "'", null) > 0;
        } catch (Exception m) {
            m.printStackTrace();
            Log.d("mjk", m.toString());
            return false;
        }
    }

    public boolean updateItem(StockItemsPojo data) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("item_name", data.getItem_name());
        cv.put("item_quantity", data.getItem_quantity());
        cv.put("item_unit_type", data.getItem_unit_type());
        cv.put("item_buying_price", data.getItem_buying_price());
        cv.put("item_selling_price", data.getItem_selling_price());
        cv.put("item_category", data.getItem_category());
        cv.put("item_discount", data.getItem_discount());
        cv.put("item_image", data.getItem_image());

        return db.update("mduka_items_stock", cv, "item_id ='" + data.getItem_id() + "'", null) > 0;

    }

    public double getItemsCount() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        return (double) DatabaseUtils.longForQuery(db, "SELECT Count(*)  FROM mduka_items_stock ", null);

    }


    public boolean insertItemToCart(StockItemsPojo data) {
        boolean success = false;

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("item_id", data.getItem_id());
        values.put("item_name", data.getItem_name());


        values.put("item_quantity", "1");
        values.put("item_unit_type", data.getItem_unit_type());

        values.put("item_selling_price", data.getItem_selling_price());


        values.put("item_total_selling_price", data.getItem_selling_price());

        values.put("item_buying_price", data.getItem_buying_price());

        values.put("item_total_buying_price", data.getItem_buying_price());

        values.put("item_discount", data.getItem_discount());


        if (db.insert("mduka_items_cart", null, values) >= 1) {
            success = true;
        }
        db.close();


        return success;


    }

    public double[] checkIfItemIsInCart(int item_id) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        double[] results = new double[2];

        boolean isThere = false;
        results[0] = 0.0;
        results[1] = 0.0;

        String QUERY = "SELECT * FROM mduka_items_cart  WHERE item_id = '" + item_id + "' ";


        Cursor cursor = db.rawQuery(QUERY, null);

        if (!cursor.isLast()) {

            if (cursor.moveToNext()) {
                isThere = true;
                results[0] = 1.0;
                results[1] = Double.valueOf(cursor.getString(3));
            }
        }
        db.close();


        return results;
    }

    public boolean updateQuantityOfItemInCart(int item_id, String newQuantity, String total_price, String total_bp) {

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("item_quantity", newQuantity);
        cv.put("item_total_selling_price", total_price);
        cv.put("item_total_buying_price", total_bp);


        return db.update("mduka_items_cart", cv, "item_id ='" + item_id + "'", null) > 0;


    }

    public int getNoOfItemsInCart() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        return (int) DatabaseUtils.longForQuery(db, "SELECT Count(*)  FROM mduka_items_cart ", null);
    }

    public boolean clearCart() {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        return db.delete("mduka_items_cart", null, null) > 0;
    }

    public boolean deleteCartItem(int rowId) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        return db.delete("mduka_items_cart", "item_id" + "= '" + rowId + "' ", null) > 0;
    }

    public ArrayList<StockItemsPojo> getAllItemsInCart() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();


        ArrayList<StockItemsPojo> data = new ArrayList<>();
        String QUERY = "SELECT * FROM  mduka_items_cart";


        Cursor cursor = db.rawQuery(QUERY, null);

        if (!cursor.isLast()) {


            while (cursor.moveToNext()) {
                StockItemsPojo pojo = new StockItemsPojo();
                pojo.setItem_id(cursor.getInt(0));
                pojo.setItem_name(cursor.getString(1));
                pojo.setItem_quantity(cursor.getString(3));
                pojo.setItem_unit_type(cursor.getString(2));

                pojo.setItem_selling_price(cursor.getString(4));
                pojo.setItem_category("");
                pojo.setCart_item_total_price(cursor.getString(5));
                pojo.setItem_discount(cursor.getString(6));

                pojo.setItem_buying_price(cursor.getString(7));
                pojo.setItem_total_buying_price(cursor.getString(8));

                //pojo.setItem_total_buying_price(cursor.getString(8));
                //pojo.setItem_total_buying_price(cursor.getString(8));


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

    public double sumOfTotalPricesOfItemsInCart() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        return (double) DatabaseUtils.longForQuery(db, "SELECT SUM(item_total_selling_price)  FROM mduka_items_cart ", null);
    }

    public double sumOfTotalBpPricesOfItemsInCart() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        return (double) DatabaseUtils.longForQuery(db, "SELECT SUM(item_total_buying_price)  FROM mduka_items_cart ", null);
    }


    public boolean insertCategory(String ca) {
        boolean success = false;

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();

        // values.put("item_id", data.getItem_id());

        values.put("item_category", ca);


        if (db.insert("mduka_items_category", null, values) >= 1) {
            success = true;
        }
        db.close();


        return success;


    }

    public ArrayList<StockItemsPojo> getAllCategories() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();


        ArrayList<StockItemsPojo> data = new ArrayList<>();
        String QUERY = "SELECT * FROM  mduka_items_category";


        Cursor cursor = db.rawQuery(QUERY, null);

        if (!cursor.isLast()) {


            while (cursor.moveToNext()) {
                StockItemsPojo pojo = new StockItemsPojo();
                pojo.setItem_id(cursor.getInt(0));

                pojo.setItem_category(cursor.getString(1));


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

    public ArrayList<String> getAllCategories1() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();


        ArrayList<String> data = new ArrayList<>();
        String QUERY = "SELECT * FROM  mduka_items_category";


        Cursor cursor = db.rawQuery(QUERY, null);

        if (!cursor.isLast()) {


            while (cursor.moveToNext()) {
                //StockItemsPojo pojo = new StockItemsPojo();
                //pojo.setItem_id(cursor.getInt(0));

                //pojo.setItem_category(cursor.getString(1));


                data.add(cursor.getString(1));

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

    public boolean deleteCategory(int rowId) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        return db.delete("mduka_items_category", "item_id" + "= '" + rowId + "' ", null) > 0;
    }


    public boolean insertTransaction(TransactionsPojo data) {
        boolean success = false;

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        // values.put("item_id", data.getItem_id());
        values.put("transaction_date", data.getTransaction_date());


        values.put("transaction_items", data.getTransaction_items());
        values.put("transaction_total_sp", data.getTransaction_total_sp());
        values.put("transaction_total_bp", data.getTransaction_total_bp());
        values.put("transaction_quantity", data.getTransaction_quantity());


        if (db.insert("mduka_transactions", null, values) >= 1) {
            success = true;
        }
        db.close();


        return success;
    }

    public ArrayList<TransactionsPojo> getAllTransactions(String trans_id, String date) {
        //Open connection to read only
        SQLiteDatabase db = dbHandler.getReadableDatabase();


        ArrayList<TransactionsPojo> data = new ArrayList<>();
        String QUERY = "SELECT * FROM  mduka_transactions  WHERE transaction_id LIKE '%" + trans_id + "%' AND transaction_date LIKE '%" + date + "%' ORDER BY transaction_id DESC ";


        Cursor cursor = db.rawQuery(QUERY, null);

        if (!cursor.isLast()) {

            while (cursor.moveToNext()) {
                TransactionsPojo pojo = new TransactionsPojo();
                pojo.setTransaction_id(cursor.getInt(0));
                pojo.setTransaction_date(cursor.getString(1));
                pojo.setTransaction_items(cursor.getString(2));
                pojo.setTransaction_total_sp(cursor.getString(3));
                pojo.setTransaction_total_bp(cursor.getString(4));
                pojo.setTransaction_quantity(cursor.getString(5));

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

    public double[] getSumsOfTransactions(String trans_id, String date) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        double result[] = new double[3];
//        "  `transaction_total_sp` varchar NOT NULL," +
//                "  `transaction_total_bp` varchar NOT NULL," +
//                "  `transaction_quantity` varchar NOT NULL"
        result[0] = (double) DatabaseUtils.longForQuery(db, "SELECT SUM(transaction_total_bp) FROM  mduka_transactions  WHERE transaction_id LIKE '%" + trans_id + "%' AND transaction_date LIKE '%" + date + "%' ", null);
        result[1] = (double) DatabaseUtils.longForQuery(db, "SELECT SUM(transaction_total_sp) FROM  mduka_transactions  WHERE transaction_id LIKE '%" + trans_id + "%' AND transaction_date LIKE '%" + date + "%' ", null);
        result[2] = (double) DatabaseUtils.longForQuery(db, "SELECT SUM(transaction_quantity) FROM  mduka_transactions  WHERE transaction_id LIKE '%" + trans_id + "%' AND transaction_date LIKE '%" + date + "%' ", null);
        db.close();
        return result;
    }

}
