package com.erickogi14gmail.mduka.Transactions;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.erickogi14gmail.mduka.Sell.CartItemsAdapter;
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
    private ArrayList<TransactionsPojo> transactionsPojos;
    private int position;
    private RecyclerView recyclerView;
    private CartItemsAdapter cartItemsAdapter;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private TextView textViewDate, textViewTotal;
    private DbOperations dbOperations;
    private Dialog dialog;
    private Button reproduce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbOperations = new DbOperations(getApplicationContext());
        reproduce = (Button) findViewById(R.id.btn_reproduce);
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
        cartItemsAdapter = new CartItemsAdapter(getApplicationContext(), p);
        cartItemsAdapter.notifyDataSetChanged();
        // if (isListView) {
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        // } else {
        //   mStaggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // }
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cartItemsAdapter);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void print() {
        Receipt r = new Receipt(getApplicationContext());
        try {
            myFile = r.re(p, Double.valueOf(transactionsPojos.get(position).getTransaction_total_sp()), Double.valueOf(transactionsPojos.get(position).getTransaction_total_sp()), "My Shop\n0714406984\nerickogi14@gmail.com", "Eric Kogi", "9173");
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        dialog = new Dialog(Transaction_details.this);
        dialog.setContentView(R.layout.dialog_receipt_action);
        TextView view = (TextView) dialog.findViewById(R.id.txt_view);
        TextView share = (TextView) dialog.findViewById(R.id.txt_share);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPdf(myFile);
                dialog.dismiss();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareReciept(myFile);
                dialog.dismiss();
            }
        });
        //viewPdf(myFile);
        dialog.show();
    }

    private void viewPdf(File myFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void shareReciept(File f) {
        Uri uri = Uri.fromFile(f);
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(share);
    }
}
