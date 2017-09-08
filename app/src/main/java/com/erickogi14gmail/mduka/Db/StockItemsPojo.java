package com.erickogi14gmail.mduka.Db;

import java.io.Serializable;

/**
 * Created by Eric on 8/28/2017.
 */


public class StockItemsPojo implements Serializable {
    private int item_id;
    private String item_name;
    private String item_quantity;
    private String item_unit_type;
    private String item_buying_price;
    private String item_selling_price;
    private String item_category;
    private String item_discount;
    private String cart_item_total_price;
    private String item_image;


    private String item_total_buying_price;

    private String fav;

    public StockItemsPojo() {
    }


    public StockItemsPojo(int item_id, String item_category) {
        this.item_id = item_id;
        this.item_category = item_category;
    }

    //    public StockItemsPojo(int item_id, String item_name, String item_quantity, String item_unit_type, String item_buying_price, String item_selling_price, String item_category, String item_discount, String cart_item_total_price) {
//        this.item_id = item_id;
//        this.item_name = item_name;
//        this.item_quantity = item_quantity;
//        this.item_unit_type = item_unit_type;
//        this.item_buying_price = item_buying_price;
//        this.item_selling_price = item_selling_price;
//        this.item_category = item_category;
//        this.item_discount = item_discount;
//        this.cart_item_total_price = cart_item_total_price;
//    }















    public StockItemsPojo(int item_id, String item_name, String item_quantity, String item_unit_type, String item_selling_price, String item_category) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_quantity = item_quantity;
        this.item_unit_type = item_unit_type;
        this.item_selling_price = item_selling_price;
        this.item_category = item_category;
    }

    //for cart
    public StockItemsPojo(int item_id, String item_name, String item_quantity, String item_unit_type, String item_buying_price, String item_selling_price, String item_category, String item_discount) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_quantity = item_quantity;
        this.item_unit_type = item_unit_type;
        this.item_buying_price = item_buying_price;
        this.item_selling_price = item_selling_price;
        this.item_category = item_category;
        this.item_discount = item_discount;
    }

    ///for sell
    public StockItemsPojo(int item_id, String item_name, String item_quantity, String item_unit_type, String item_buying_price, String item_selling_price, String item_category, String item_discount, String item_image) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_quantity = item_quantity;
        this.item_unit_type = item_unit_type;
        this.item_buying_price = item_buying_price;
        this.item_selling_price = item_selling_price;
        this.item_category = item_category;
        this.item_discount = item_discount;
        this.item_image = item_image;
    }

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }

    public String getItem_total_buying_price() {
        return item_total_buying_price;
    }

    public void setItem_total_buying_price(String item_total_buying_price) {
        this.item_total_buying_price = item_total_buying_price;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getItem_unit_type() {
        return item_unit_type;
    }

    public void setItem_unit_type(String item_unit_type) {
        this.item_unit_type = item_unit_type;
    }

    public String getItem_selling_price() {
        return item_selling_price;
    }

    public void setItem_selling_price(String item_selling_price) {
        this.item_selling_price = item_selling_price;
    }

    public String getItem_category() {
        return item_category;
    }

    public void setItem_category(String item_category) {
        this.item_category = item_category;
    }

    public String getItem_buying_price() {
        return item_buying_price;
    }

    public void setItem_buying_price(String item_buying_price) {
        this.item_buying_price = item_buying_price;
    }

    public String getItem_discount() {
        return item_discount;
    }

    public void setItem_discount(String item_discount) {
        this.item_discount = item_discount;
    }

    public String getCart_item_total_price() {
        return cart_item_total_price;
    }

    public void setCart_item_total_price(String cart_item_total_price) {
        this.cart_item_total_price = cart_item_total_price;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }
}
