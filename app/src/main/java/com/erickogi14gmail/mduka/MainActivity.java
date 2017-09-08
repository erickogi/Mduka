package com.erickogi14gmail.mduka;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.Toast;

import com.erickogi14gmail.mduka.Db.DbOperations;
import com.erickogi14gmail.mduka.Db.StockItemsPojo;
import com.erickogi14gmail.mduka.Items.fragment_items;
import com.erickogi14gmail.mduka.Report.fragment_transactions_reports;
import com.erickogi14gmail.mduka.Sell.fragmentSellMain;
import com.erickogi14gmail.mduka.Sell.fragment_cart;
import com.erickogi14gmail.mduka.Transactions.fragment_transactions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static Fragment fragment = null;

    boolean itemsFilled;
    DbOperations dbOperations;



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
//        bottomNavigationView = (BottomBar)findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnTabSelectListener(new OnTabSelectListener() {
//            @Override
//            public void onTabSelected(@IdRes int tabId) {
//                if(tabId==R.id.tab_cart){
//                    popOutFragments();
//                    fragment = new fragment_cart();
//                    setUpView();
//                }else {
//                    popOutFragments();
//                    fragment = new fragment_sell();
//                    setUpView();
//                }
//            }
//        });
        try {
            if (dbOperations.getNoOfItemsInCart() > 0) {

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

    private void populateItemsDb() {

        ArrayList<StockItemsPojo> data = new ArrayList<>();

        for (int a = 0; a < 40; a++) {
            StockItemsPojo stockItems1 = new StockItemsPojo(a, "Iphones " + a, "250", "pcs", "17000", "35000", "Mobile Phones", "0");
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

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
                Toast.makeText(this, "Items In Cart should  be Cleared first", Toast.LENGTH_LONG).show();
            } else {
                popOutFragments();
                fragment = new fragment_items();
                setUpView();
            }


        } else if (id == R.id.nav_Reports_analysis) {
            popOutFragments();
            fragment = new fragment_transactions_reports();
            setUpView();
        } else if (id == R.id.nav_transactions) {
            popOutFragments();
            fragment = new fragment_transactions();
            setUpView();
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_feedback) {

        }

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
