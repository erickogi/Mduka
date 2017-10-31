package com.erickogi14gmail.mduka.Items;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.erickogi14gmail.mduka.Controller;
import com.erickogi14gmail.mduka.Db.DbOperations;
import com.erickogi14gmail.mduka.Db.StockItemsPojo;
import com.erickogi14gmail.mduka.R;
import com.erickogi14gmail.mduka.utills.StaggeredHiddingScrollListener;

import java.util.ArrayList;

import static android.R.drawable.ic_input_add;

/**
 * Created by Eric on 8/28/2017.
 */

public class fragment_items extends Fragment implements PopupMenu.OnMenuItemClickListener {
    boolean isListView = true;
    Dialog dialog;
    boolean isFav = false;
    private ItemsAdapter itemsAdapter;
    private View view;
    private ArrayList<StockItemsPojo> stockItemsPojos = new ArrayList<>();
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private DbOperations dbOperations;
    private CardView cardview;
    private Spinner spinnerCategory;
    private SearchView search;
    private ArrayList<String> categories = new ArrayList<>();
    private FloatingActionButton fab;
    private Controller controller;
    private LinearLayout linearLayoutFav;
    private LinearLayout linearLayoutEmpty;
    private StockItemsPojo menustockItemsPojo;

    private String getSpinnerSelected() {
        if (spinnerCategory.getSelectedItem().toString().equals("All")) {
            return "";
        } else {
            return spinnerCategory.getSelectedItem().toString();
        }
    }

    public String getSearchText() {
        try {
            return search.getQuery().toString();
        } catch (Exception n) {
            return "";
        }

    }

    private StockItemsPojo getStockItemsPojo() {
        return menustockItemsPojo;
    }

    private void createMenu(View v, StockItemsPojo stockItemsPojo) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.setOnMenuItemClickListener(this);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.item_popup_menu, popupMenu.getMenu());
        menustockItemsPojo = stockItemsPojo;
        popupMenu.show();





    }

    public void setView(boolean isListView, String search) {


        stockItemsPojos = dbOperations.getAllItems1(getSpinnerSelected(), search);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        linearLayoutEmpty = (LinearLayout) view.findViewById(R.id.empty_layout);
        itemsAdapter = new ItemsAdapter(getContext(), stockItemsPojos, new itemsClickListener() {
            @Override
            public void onBtnMoreClicked(int position, View view) {
                createMenu(view, stockItemsPojos.get(position));
            }

            @Override
            public void onFavIconClicked(int position) {
                try {
                    if (stockItemsPojos.get(position).getFav().equals("1")) {
                        dbOperations.updateItemFav(stockItemsPojos.get(position).getItem_id(), "0");
                    } else {
                        dbOperations.updateItemFav(stockItemsPojos.get(position).getItem_id(), "1");

                    }
                } catch (NullPointerException nm) {
                    dbOperations.updateItemFav(stockItemsPojos.get(position).getItem_id(), "1");

                }

                itemsAdapter.updateItemItem(position, dbOperations.getAllItems1(getSpinnerSelected(), getSearchText()).get(position));


            }

            @Override
            public void onBtnDeleteClicked(int position) {

                alertDialogDelete("You are about to delete an item from your stock,Please note that ths step is irreversible." +
                        "\nContinue ?", stockItemsPojos.get(position).getItem_id());
            }

            @Override
            public void onBtnEditClicked(int position) {
                Intent intent = new Intent(getActivity(), ItemDetails.class);
                intent.putExtra("data", stockItemsPojos);
                intent.putExtra("dataPosition", position);
                intent.putExtra("update", true);
                startActivity(intent);

            }
        });










        itemsAdapter.notifyDataSetChanged();
        if (isListView) {
            mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        } else {
            mStaggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        }
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemsAdapter);


        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setView(true, getSearchText());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (itemsAdapter.getItemCount() > 0) {
            linearLayoutEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

        } else {
            linearLayoutEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setView(true, getSearchText());
        spinnerCategory = controller.setUpSpinner(getContext(), R.id.spinner_category, view, 0);
        fab = controller.fab(getActivity(), true, ic_input_add);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_items, container, false);
        dbOperations = new DbOperations(getContext());
        controller = new Controller();
        cardview = (CardView) view.findViewById(R.id.sell_bar_card);
        spinnerCategory = controller.setUpSpinner(getContext(), R.id.spinner_category, view, 0);


        setView(true, getSearchText());
        search = (SearchView) view.findViewById(R.id.search_bar);
        fab = controller.fab(getActivity(), true, ic_input_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ItemDetails.class));
            }
        });
        recyclerView.setOnScrollListener(new StaggeredHiddingScrollListener() {

            @Override
            public void onHide() {//hideViews();
                fab.hide();

            }

            @Override
            public void onShow() {//showViews();
                fab.show();


            }
        });
//        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
//
//            @Override
//            public void onClick(View view, int position) {
//                Intent intent = new Intent(getActivity(), ItemDetails.class);
//                intent.putExtra("data", stockItemsPojos);
//                intent.putExtra("dataPosition", position);
//                intent.putExtra("update",true);
//                startActivity(intent);
//
//
//            }
//
//            @Override
//            public void onLongClick(View view, final int position) {
//                dialog = new Dialog(getContext());
//                dialog.setContentView(R.layout.item_long_click_dialog);
//                dialog.setTitle("Actions");
//                TextView txtDelete = (TextView) dialog.findViewById(R.id.delete);
//                TextView txtShare = (TextView) dialog.findViewById(R.id.share);
//                TextView txtCopy = (TextView) dialog.findViewById(R.id.copy);
//                TextView txtUpdate = (TextView) dialog.findViewById(R.id.update);
//                LinearLayout linearLayoutFav = (LinearLayout) dialog.findViewById(R.id.layout_fav);
//                TextView textViewFav = (TextView) dialog.findViewById(R.id.fav);
//
//                try {
//                    if (stockItemsPojos.get(position).getFav().equals("1")) {
//                        textViewFav.setText("Remove From Favorites");
//                        isFav = true;
//
//                        // linearLayoutFav.setVisibility(View.GONE);
//                    } else {
//                        isFav = false;
//                        // linearLayoutFav.setVisibility(View.VISIBLE);
//                    }
//                } catch (NullPointerException m) {
//                    isFav = false;
//                    //linearLayoutFav.setVisibility(View.VISIBLE);
//                }
//                linearLayoutFav.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (isFav) {
//                            dbOperations.updateItemFav(stockItemsPojos.get(position).getItem_id(), "0");
//                            dialog.dismiss();
//
//
//                            itemsAdapter.updateItemItem(position, dbOperations.getAllItems1(getSpinnerSelected(), getSearchText()).get(position));
//
//
//                        } else {
//                            dbOperations.updateItemFav(stockItemsPojos.get(position).getItem_id(), "1");
//                            dialog.dismiss();
//
//                            itemsAdapter.updateItemItem(position, dbOperations.getAllItems1(getSpinnerSelected(), getSearchText()).get(position));
//
//                            // itemsAdapter.updateItemItem(position, dbOperations.getAllItemFavorites("").get(position));
//                        }
//                    }
//                });
//
//                txtDelete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (dbOperations.deleteItem(stockItemsPojos.get(position).getItem_id())) {
//                            dialog.dismiss();
//                            itemsAdapter.updateList(dbOperations.getAllItems1(getSpinnerSelected(), getSearchText()));
//                            //updateItems(getContext());
//                            Toast.makeText(getContext(), "Deleted .", Toast.LENGTH_SHORT).show();
//                        } else {
//                            dialog.dismiss();
//                            Toast.makeText(getContext(), "Error Deleting.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//                txtShare.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        shareEvent(stockItemsPojos.get(position).getItem_name() + "\n Qty" + stockItemsPojos.get(position).getItem_quantity() + "  " + stockItemsPojos.get(position).getItem_unit_type() + "\n @ " + stockItemsPojos.get(position).getItem_selling_price());
//                        dialog.dismiss();
//                    }
//                });
//                txtCopy.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        copyText(stockItemsPojos.get(position).getItem_name() + "\n Qty" + stockItemsPojos.get(position).getItem_quantity() + "  " + stockItemsPojos.get(position).getItem_unit_type() + "\n @ " + stockItemsPojos.get(position).getItem_selling_price());
//                        dialog.dismiss();
//                    }
//                });
//                txtUpdate.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getActivity(), ItemDetails.class);
//                        intent.putExtra("data", stockItemsPojos);
//                        intent.putExtra("dataPosition", position);
//                        intent.putExtra("update",true);
//                        startActivity(intent);
//                    }
//                });
//                dialog.show();
//
//
//            }
//        }));


        SearchManager manager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);

        final SearchView search = (SearchView) view.findViewById(R.id.search_bar);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setIconified(false);
            }
        });

        search.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemsAdapter.updateList(dbOperations.getAllItems1(getSpinnerSelected(), getSearchText()));
                return false;
            }
        });

        return view;
    }


    private void shareEvent(String text) {
        Intent in = new Intent();
        in.setAction(Intent.ACTION_SEND);
        in.putExtra(Intent.EXTRA_TEXT, text);
        in.setType("text/plain");
        startActivity(in);
    }

    private void copyText(String text) {
        ClipboardManager clip = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Product", text);
        clip.setPrimaryClip(clipData);
        Toast.makeText(getContext(), "Copied To Clipboard .", Toast.LENGTH_SHORT).show();
    }

    private void alertDialogDelete(final String message, final int id) {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (dbOperations.deleteItem(id)) {
                            dialog.dismiss();
                            itemsAdapter.updateList(dbOperations.getAllItems1(getSpinnerSelected(), getSearchText()));
                            //updateItems(getContext());
                            Toast.makeText(getContext(), "Deleted .", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Error Deleting.", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        //  dialog.dismiss();

                        break;
                }
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage(message).setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_item_share:
                shareEvent(getStockItemsPojo().getItem_name() + "\n Qty" + getStockItemsPojo().getItem_quantity() + "  " + getStockItemsPojo().getItem_unit_type() + "\n @ " + getStockItemsPojo().getItem_selling_price());

                break;
            case R.id.nav_item_copy:
                copyText(getStockItemsPojo().getItem_name() + "\n Qty" + getStockItemsPojo().getItem_quantity() + "  " + getStockItemsPojo().getItem_unit_type() + "\n @ " + getStockItemsPojo().getItem_selling_price());


                break;
        }
        return false;
    }
}
