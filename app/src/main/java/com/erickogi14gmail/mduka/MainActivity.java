package com.erickogi14gmail.mduka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.erickogi14gmail.mduka.Db.DbOperations;
import com.erickogi14gmail.mduka.Db.StockItemsPojo;
import com.erickogi14gmail.mduka.Items.fragment_items;
import com.erickogi14gmail.mduka.Prefrence.Settings;
import com.erickogi14gmail.mduka.Sell.fragmentSellMain;
import com.erickogi14gmail.mduka.Sell.fragment_cart;
import com.erickogi14gmail.mduka.Transactions.fragment_transactions;

import java.util.ArrayList;

import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static Fragment fragment = null;

    boolean itemsFilled;
    DbOperations dbOperations;
    private View viewReceiptsMenu;
    private View viewNotificationMenu;
    private View viewSync;
    private int itemsCountInCart;
    private Controller controller;



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        if (this.getFragmentManager().getBackStackEntryCount() == 0) {
            //if(dbOperations.getNoOfItemsInCart()>0){
            //    dbOperations.clearCart();
            //    Toast.makeText(this, "cart cleared", Toast.LENGTH_SHORT).show();
            // }
            super.onBackPressed();

        } else if (this.getFragmentManager().getBackStackEntryCount() > 0) {

            getFragmentManager().popBackStack();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbOperations = new DbOperations(getApplicationContext());
        controller = new Controller();
        itemsCountInCart = dbOperations.getNoOfItemsInCart();
        try {
            if (itemsCountInCart > 0) {

                fragment = new fragmentSellMain();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "msf").commit();

                popOutFragments();
                fragment = new fragment_cart();
                setUpView();
            } else {

                SharedPreferences sharedPreferences = getSharedPreferences("testItems", Context.MODE_PRIVATE);


                itemsFilled = sharedPreferences.getBoolean("testItemsFilled", false);

                fragment = new fragmentSellMain();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "msf").commit();
            }

        } catch (Exception m) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void clearCart() {
        ArrayList<StockItemsPojo> stockItemsPojos;
        try {
            stockItemsPojos = dbOperations.getAllItemsInCart();
            for (int a = 0; a < stockItemsPojos.size(); a++) {
                double quantity = Double.valueOf(stockItemsPojos.get(a).getItem_quantity());
                int item_id = stockItemsPojos.get(a).getItem_id();
                double quantityInStock = Double.valueOf(dbOperations.getItemQuantity(item_id));
                double newquantity = quantity + quantityInStock;
                if (dbOperations.updateItemQuantity(item_id, String.valueOf(newquantity))) {
                    dbOperations.deleteCartItem(item_id);

                    if (itemsCountInCart > 0) {
                        new QBadgeView(getApplicationContext()).bindTarget(viewReceiptsMenu).setBadgeNumber(itemsCountInCart);
                    } else if (itemsCountInCart == 0) {
                        new QBadgeView(getApplicationContext()).bindTarget(viewReceiptsMenu).setBadgeText(String.valueOf(itemsCountInCart));
                    }
                    //cartItemsAdapter.updateList(dbOperations.getAllItemsInCart());
                    // setCharge();
                } else {
                    Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException m) {
            Toast.makeText(getApplicationContext(), "No Items To Clear", Toast.LENGTH_SHORT).show();
        }
    }
    private void populateItemsDb() {

        ArrayList<StockItemsPojo> data = new ArrayList<>();
        for (int a = 0; a < 40; a++) {
            StockItemsPojo stockItems1 = new StockItemsPojo(a, "TestItem" + a, "1000", "pcs", "17000", "35000", "Category" + a, "0", "", "0");
            // StockItemsPojo stockItemsPojo2=new StockItemsPojo()
            data.add(stockItems1);
        }
        DbOperations dbOperations = new DbOperations(getApplicationContext());
        for (StockItemsPojo s : data) {
            if (dbOperations.insertItem(s)) {
                Log.d("itemsFilled", "oneF");
            }
        }

        SharedPreferences sharedPreferences = this.getSharedPreferences("testItems", Context.MODE_PRIVATE);


        SharedPreferences.Editor editor = sharedPreferences.edit();


        editor.putBoolean("testItemsFilled", true);


        editor.apply();

    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                viewReceiptsMenu = findViewById(R.id.action_receipt);
                // viewNotificationMenu=findViewById(R.id.action_notifications);
                // viewSync=findViewById(R.id.action_sync);

                if (itemsCountInCart > 0) {
                    new QBadgeView(getApplicationContext()).bindTarget(viewReceiptsMenu).setBadgeNumber(itemsCountInCart);
                } else if (itemsCountInCart == 0) {
                    new QBadgeView(getApplicationContext()).bindTarget(viewReceiptsMenu).setBadgeText(String.valueOf(itemsCountInCart)).setBadgeBackgroundColor(R.color.colorAccent);
                }
                //new QBadgeView(getApplicationContext()).bindTarget(viewNotificationMenu).setBadgeNumber(7);
                // new QBadgeView(getApplicationContext()).bindTarget(viewSync).setBadgeBackground(BitmapDrawable.cr)

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //final View menuitemview=findViewById(R.id.action_notifications);

        // new QBadgeView(getApplicationContext()).bindTarget(menuitemview).setBadgeNumber(5);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_receipt) {
            popOutFragments();
            fragment = new fragment_cart();
            setUpView();
            return true;
        }
// else if(id==R.id.action_clear){
//         clearCart();
//     }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sell) {

            popOutFragments();
            fragment = new fragmentSellMain();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "msf").commit();
        } else if (id == R.id.nav_items) {
            if (dbOperations.getNoOfItemsInCart() > 0) {
                controller.toast("Items in cart should be cleared first", MainActivity.this, R.drawable.ic_error_outline_black_24dp);
                // Toast.makeText(this, "Items In Cart should  be Cleared first", Toast.LENGTH_LONG).show();
            } else {
                popOutFragments();
                fragment = new fragment_items();
                setUpView();
            }


        }
//        else if (id == R.id.nav_Reports_analysis) {
//            popOutFragments();
//            fragment = new fragment_transactions_reports();
//            setUpView();
//        }
        else if (id == R.id.nav_transactions) {
            popOutFragments();
            fragment = new fragment_transactions();
            setUpView();
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(MainActivity.this, Settings.class));
        }
//         else if (id == R.id.nav_help) {
//              //   startActivity(new Intent(MainActivity.this, Reports.class));
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_feedback) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void setUpView() {
        if (fragment != null) {
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment)
                    .addToBackStack(null).commit();
        }

    }

    void popOutFragments() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }
}
