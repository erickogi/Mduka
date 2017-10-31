package com.erickogi14gmail.mduka.Transactions;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.erickogi14gmail.mduka.Db.DbOperations;
import com.erickogi14gmail.mduka.Db.StockItemsPojo;
import com.erickogi14gmail.mduka.Db.TransactionsPojo;
import com.erickogi14gmail.mduka.R;
import com.erickogi14gmail.mduka.Sell.Receipt;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

public class Transaction_details extends AppCompatActivity {
    ArrayList<StockItemsPojo> p;
    File myFile = null;
    String fle;
    ProgressDialog progressDialog;
    private ArrayList<TransactionsPojo> transactionsPojos;
    private int position;
    private RecyclerView recyclerView;
    private TransactionItemsAdapter transactionItemsAdapter;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private TextView textViewDate, textViewTotal;
    private DbOperations dbOperations;
    private Dialog dialog;
    private Button reproduce, reverse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbOperations = new DbOperations(getApplicationContext());
        reproduce = (Button) findViewById(R.id.btn_reproduce);
        reverse = (Button) findViewById(R.id.btn_reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reverseTransaction();
            }
        });
        reproduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print();
            }
        });


        textViewDate = (TextView) findViewById(R.id.txt_date);
        textViewTotal = (TextView) findViewById(R.id.txt_total);

        Intent intent = getIntent();
        position = intent.getIntExtra("dataPosition", 0);
        transactionsPojos = (ArrayList<TransactionsPojo>) intent.getSerializableExtra("data");
        getSupportActionBar().setTitle("Transaction No : " + String.valueOf(transactionsPojos.get(position).getTransaction_id()));
        String items = transactionsPojos.get(position).getTransaction_items();

        textViewDate.setText(transactionsPojos.get(position).getTransaction_date());
        textViewTotal.setText("Total : " + transactionsPojos.get(position).getTransaction_total_sp());

        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<StockItemsPojo>>() {

        }.getType();
        p = gson.fromJson(items, collectionType);

        //ArrayList<StockItemsPojo> l=new ArrayList<>();
        // for(int a=0;a<p.size();a++){
        //     S
        // }


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        transactionItemsAdapter = new TransactionItemsAdapter(getApplicationContext(), p);
        transactionItemsAdapter.notifyDataSetChanged();
        // if (isListView) {
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        // } else {
        //   mStaggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // }
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(transactionItemsAdapter);



    }

    private void reverseTransaction() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        int siz = p.size();
                        for (int a = 0; a < siz; a++) {

                            int item_id = p.get(a).getItem_id();
                            double quantityInStock = Double.valueOf(dbOperations.getItemQuantity(item_id));
                            double quantityTransacted = Double.valueOf(p.get(a).getItem_quantity());

                            if (dbOperations.updateItemQuantity(item_id, String.valueOf(quantityInStock + quantityTransacted))) {
                                //p.remove(a);
                                //transactionItemsAdapter.updateList(p);
                            }
                            // transactionItemsAdapter.updateList(p);
                        }
                        if (dbOperations.deleteTransaction(transactionsPojos.get(position).getTransaction_id())) {
                            transactionItemsAdapter.updateList(p);
                            p.clear();
                            //transactionItemsAdapter.updateList(tr);
                        }
                        transactionItemsAdapter.updateList(p);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();

                        break;
                }
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(Transaction_details.this);
        builder.setMessage("Are you certain you want to reverse this transaction ? ").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();


    }

    private void print() {
        Receipt r = new Receipt(getApplicationContext());

        dialog = new Dialog(Transaction_details.this);
        dialog.setTitle("Select Action");
        dialog.setContentView(R.layout.dialog_receipt_action);
        TextView view = (TextView) dialog.findViewById(R.id.txt_view);
        TextView share = (TextView) dialog.findViewById(R.id.txt_share);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(Transaction_details.this);
                progressDialog.setMessage("Generating Receipt ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();

                Back b = new Back(1, p, Double.valueOf(transactionsPojos.get(position).getTransaction_total_sp()), Double.valueOf(transactionsPojos.get(position).getTransaction_total_sp()), "My Shop\n0714406984\nerickogi14@gmail.com", "Eric Kogi", "9173");
                b.execute();


                // viewPdf(myFile);
                // dialog.dismiss();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(Transaction_details.this);
                progressDialog.setMessage("Generating Receipt ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();
                Back b = new Back(0, p, Double.valueOf(transactionsPojos.get(position).getTransaction_total_sp()), Double.valueOf(transactionsPojos.get(position).getTransaction_total_sp()), "My Shop\n0714406984\nerickogi14@gmail.com", "Eric Kogi", "9173");
                b.execute();
                // shareReciept(myFile);
                // dialog.dismiss();
            }
        });
        //viewPdf(myFile);
        dialog.show();
    }

    private void viewPdf(File myFile) {
        dialog.dismiss();
        progressDialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void shareReciept(File f) {
        dialog.dismiss();
        progressDialog.dismiss();
        Uri uri = Uri.fromFile(f);
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(share);
    }


    private class Back extends AsyncTask<String, Void, File> {
        /**
         * Runs on the UI thread after {@link #publishProgress} is invoked.
         * The specified values are the values passed to {@link #publishProgress}.
         *
         * @param values The values indicating progress.
         * @see #publishProgress
         * @see #doInBackground
         */
        private int wht;
        private ArrayList<StockItemsPojo> s;
        private double itemsInCart;
        private double paidAmount;
        private String shopDetails;
        private String serverName;
        private String recNo;

        public Back(int wht, ArrayList<StockItemsPojo> s, double itemsInCart, double paidAmount, String shopDetails, String serverName, String recNo) {
            this.wht = wht;
            this.s = s;
            this.itemsInCart = itemsInCart;
            this.paidAmount = paidAmount;
            this.shopDetails = shopDetails;
            this.serverName = serverName;
            this.recNo = recNo;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(File s) {
            super.onPostExecute(s);
            if (wht == 0) {
                shareReciept(s);
            } else if (wht == 1) {
                viewPdf(s);
            }


        }

        @Override
        protected File doInBackground(String... params) {
            Receipt r = new Receipt(getApplicationContext());
            File myFilea = null;
            try {

                myFilea = r.re(s, itemsInCart,
                        paidAmount, shopDetails, serverName, recNo);
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            return myFilea;
        }
    }

}
