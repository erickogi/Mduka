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
    public DbClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    String createTableItems="CREATE TABLE `mduka_items_stock` (" +
            "  `id` int(8) PRIMARY KEY  NOT NULL  ," +
            "  `item_name` varchar NOT NULL," +
            "  `item_quantity` varchar NOT NULL," +
            "  `item_unit_type` varchar NOT NULL," +
            "  `item_buying_price` varchar ," +
            "  `item_selling_price` varchar NOT NULL ,"+
            "  `item_category` varchar ,"+
            "  `item_discount` varchar  "
            + ")";

    String createTableBibleBookMarks="CREATE TABLE `bible_bookMarks` (" +
            "  `id` int(8)  NOT NULL," +
            "  `b` int(11) NOT NULL," +
            "  `c` int(11) NOT NULL," +
            "  `v` int(11) NOT NULL," +
            "  `t` text NOT NULL"

            + ")";
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableItems);
        //db.execSQL(createTableBibleBookMarks);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + createTableItems);
       // db.execSQL("DROP TABLE IF EXISTS " + createTableBibleBookMarks);



        onCreate(db);
    }
}
