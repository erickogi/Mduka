package com.erickogi14gmail.mduka.Db;

import java.io.Serializable;

/**
 * Created by Eric on 9/6/2017.
 */

public class TransactionsPojo implements Serializable {
    private int transaction_id;
    private String transaction_date;
    private String transaction_items;
    private String transaction_total_sp;
    private String transaction_total_bp;
    private String transaction_quantity;
    private String transaction_time;
    private String transaction_cash_in;

    public TransactionsPojo() {
    }

    public TransactionsPojo(int transaction_id, String transaction_date, String transaction_items, String transaction_total_sp,
                            String transaction_total_bp, String transaction_quantity, String transaction_time) {
        this.transaction_id = transaction_id;
        this.transaction_date = transaction_date;
        this.transaction_items = transaction_items;
        this.transaction_total_sp = transaction_total_sp;
        this.transaction_total_bp = transaction_total_bp;
        this.transaction_quantity = transaction_quantity;
        this.transaction_time = transaction_time;

    }

    public String getTransaction_cash_in() {
        return transaction_cash_in;
    }

    public void setTransaction_cash_in(String transaction_cash_in) {
        this.transaction_cash_in = transaction_cash_in;
    }

    public String getTransaction_time() {
        return transaction_time;
    }

    public void setTransaction_time(String transaction_time) {
        this.transaction_time = transaction_time;
    }

    public String getTransaction_total_bp() {
        return transaction_total_bp;
    }

    public void setTransaction_total_bp(String transaction_total_bp) {
        this.transaction_total_bp = transaction_total_bp;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getTransaction_items() {
        return transaction_items;
    }

    public void setTransaction_items(String transaction_items) {
        this.transaction_items = transaction_items;
    }

    public String getTransaction_total_sp() {
        return transaction_total_sp;
    }

    public void setTransaction_total_sp(String transaction_total_sp) {
        this.transaction_total_sp = transaction_total_sp;
    }

    public String getTransaction_quantity() {
        return transaction_quantity;
    }

    public void setTransaction_quantity(String transaction_quantity) {
        this.transaction_quantity = transaction_quantity;
    }
}
