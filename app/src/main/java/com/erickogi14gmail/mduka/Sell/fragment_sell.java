package com.erickogi14gmail.mduka.Sell;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erickogi14gmail.mduka.Db.DbOperations;
import com.erickogi14gmail.mduka.Db.StockItemsPojo;
import com.erickogi14gmail.mduka.Items.fragment_items;
import com.erickogi14gmail.mduka.R;
import com.erickogi14gmail.mduka.utills.RecyclerTouchListener;
import com.erickogi14gmail.mduka.utills.StaggeredHiddingScrollListener;
import com.roughike.bottombar.BottomBar;

import java.util.ArrayList;

/**
 * Created by Eric on 8/28/2017.
 */

public class fragment_sell extends Fragment {
    boolean isListView = true;
    fragmentSellMain f;
    private View view;
    private boolean cardViewVisibile = true;
    private ArrayList<StockItemsPojo> stockItemsPojos = new ArrayList<>();
    private RecyclerView recyclerView;
    private SellListAdapter sellListAdapter;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private DbOperations dbOperations;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context;
    private ImageButton imageButton;
    private CardView cardview;
    private BottomBar bottomNavigationView;
    private Fragment fragment = null;
    private TextView textView;
    private LinearLayout linearLayoutEmpty;
    private ImageButton imageButtonAdd;


    private void hideViews() {
        // bottomNavigationView.animate().translationY(bottomNavigationView.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        f.hideViews();

    }

    private void showViews() {
        // bottomNavigationView.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        f.showViews();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    public void setView(Context context, String search) {


        stockItemsPojos = dbOperations.getAllItems(search);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        linearLayoutEmpty = (LinearLayout) view.findViewById(R.id.empty_layout);
        imageButtonAdd = (ImageButton) view.findViewById(R.id.add_items);

        sellListAdapter = new SellListAdapter(context, stockItemsPojos);
        sellListAdapter.notifyDataSetChanged();
        if (fragmentSellMain.isListView) {
            mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        } else {
            mStaggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        }
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(sellListAdapter);

        if (sellListAdapter.getItemCount() > 0) {
            linearLayoutEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

        } else {
            linearLayoutEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popOutFragments();
                fragment = new fragment_items();
                setUpView();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        setView(getContext(), "");
        f.showViews();

    }

    public void updateList(String search) {
        sellListAdapter.updateList(dbOperations.getAllItems(search));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sell, container, false);
        dbOperations = new DbOperations(getContext());


        f = (fragmentSellMain) getActivity().getSupportFragmentManager().findFragmentByTag("msf");
        setView(getContext(), f.getSearchText());
        f.setCharge();

        recyclerView.setOnScrollListener(new StaggeredHiddingScrollListener() {

            @Override
            public void onHide() {
                hideViews();
                //hide();
            }

            @Override
            public void onShow() {
                showViews();
                //  show();


            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                showViews();
                int item_id = stockItemsPojos.get(position).getItem_id();
                double initial_item_quantity = Double.valueOf(stockItemsPojos.get(position).getItem_quantity());
                double isThere[] = dbOperations.checkIfItemIsInCart(item_id);
                double item_price = Double.valueOf(stockItemsPojos.get(position).getItem_selling_price());
                double item_bp = Double.valueOf(stockItemsPojos.get(position).getItem_buying_price());

                if (isThere[0] == 1.0) {
                    double newQuantity = isThere[1] + 1;
                    double total_price = (newQuantity * item_price);
                    double total_bp = (newQuantity * item_bp);
                    dbOperations.updateQuantityOfItemInCart(stockItemsPojos.get(position).getItem_id(),
                            String.valueOf(newQuantity),
                            String.valueOf(total_price), String.valueOf(total_bp));
                    dbOperations.updateItemQuantity(item_id, String.valueOf(initial_item_quantity - 1));

                    try {
                        if (initial_item_quantity - 1 == 0.0) {
                            updateList(f.getSearchText());
                        } else {
                            sellListAdapter.updateItemItem(position, dbOperations.getAllItems("").get(position));
                        }
                    } catch (NullPointerException m) {
                        updateList(f.getSearchText());
                    }
                    f.setCharge();
                } else {
                    dbOperations.insertItemToCart(stockItemsPojos.get(position));
                    dbOperations.updateItemQuantity(item_id, String.valueOf(initial_item_quantity - 1));
                    f.setCharge();
                    try {
                        if (initial_item_quantity - 1 == 0.0) {
                            updateList(f.getSearchText());
                        } else {
                            sellListAdapter.updateItemItem(position, dbOperations.getAllItems("").get(position));
                        }
                    } catch (NullPointerException m) {
                        updateList(f.getSearchText());
                    }
                }

            }

            @Override
            public void onLongClick(View view, int position) {


            }
        }));


        return view;
    }


    public Context getApplicationContext() {
        Context applicationContext = getContext();
        context = applicationContext;
        return applicationContext;
    }

    void setUpView() {
        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment)
                    .addToBackStack(null).commit();
        }

    }

    void popOutFragments() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }
}
