package com.erickogi14gmail.mduka.Db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Eric on 8/28/2017.
 */

public class DbClass extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mduka.db";

    String createTableItems = "CREATE TABLE `mduka_items_stock` (" +
            "  `item_id` INTEGER PRIMARY KEY AUTOINCREMENT  ," +
            "  `item_name` varchar NOT NULL," +
            "  `item_quantity` varchar NOT NULL," +
            "  `item_unit_type` varchar NOT NULL," +
            "  `item_buying_price` varchar ," +
            "  `item_selling_price` varchar NOT NULL ," +
            "  `item_category` varchar ," +
            "  `item_discount` varchar  ," +
            "  `item_image` varchar  ," +
            "  `favorite` varchar  "

            + ")";

    String createTableCategory = "CREATE TABLE `mduka_items_category` (" +
            "  `item_id` INTEGER PRIMARY KEY AUTOINCREMENT  ," +

            "  `item_category` varchar "


            + ")";

    String createTableItemsCart = "CREATE TABLE `mduka_items_cart` (" +
            "  `item_id` INTEGER PRIMARY KEY  NOT NULL," +
            "  `item_name` varchar NOT NULL," +
            "  `item_unit_type` varchar NOT NULL," +
            "  `item_quantity` varchar NOT NULL," +
            "  `item_selling_price` varchar NOT NULL," +
            "  `item_total_selling_price` varchar NOT NULL," +
            "  `item_discount` varchar ," +

            "  `item_buying_price` varchar ," +
            "  `item_total_buying_price` varchar "


            + ")";


    String createTableTransactions = "CREATE TABLE `mduka_transactions` (" +
            "  `transaction_id` INTEGER PRIMARY KEY  AUTOINCREMENT," +
            "  `transaction_date` varchar NOT NULL," +
            "  `transaction_items` varchar NOT NULL," +
            "  `transaction_total_sp` varchar NOT NULL," +
            "  `transaction_total_bp` varchar NOT NULL," +
            "  `transaction_quantity` varchar NOT NULL," +
            "  `transaction_time` datetime NOT NULL"


            + ")";


    public DbClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableItems);
        db.execSQL(createTableItemsCart);
        db.execSQL(createTableCategory);
        db.execSQL(createTableTransactions);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + createTableItems);
        db.execSQL("DROP TABLE IF EXISTS " + createTableItemsCart);
        db.execSQL("DROP TABLE IF EXISTS " + createTableCategory);
        db.execSQL("DROP TABLE IF EXISTS " + createTableTransactions);


        onCreate(db);
    }

}
