package com.erickogi14gmail.mduka.Sell;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.erickogi14gmail.mduka.Controller;
import com.erickogi14gmail.mduka.Db.DbOperations;
import com.erickogi14gmail.mduka.Db.StockItemsPojo;
import com.erickogi14gmail.mduka.Db.TransactionsPojo;
import com.erickogi14gmail.mduka.R;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import q.rorbin.badgeview.QBadgeView;

//import com.roughike.bottombar.BottomBar;

/**
 * Created by Eric on 8/29/2017.
 */

public class fragment_cart extends Fragment {
    Controller controller;
    double quantityFromPreviousFragment = 0.0;
    File myFile = null;
    String fle;
    RadioGroup r;
    ProgressDialog progressDialog;
    private View view;
    private boolean cardViewVisibile = true;
    private ArrayList<StockItemsPojo> stockItemsPojos = new ArrayList<>();
    private RecyclerView recyclerView;
    private CartItemsAdapter cartItemsAdapter;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private DbOperations dbOperations;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            final int pos = viewHolder.getAdapterPosition();
            stockItemsPojos = dbOperations.getAllItemsInCart();
            int itemID = stockItemsPojos.get(pos).getItem_id();
            double qtyIncART = Double.valueOf(stockItemsPojos.get(pos).getItem_quantity());
            double qtyInStock = Double.valueOf(dbOperations.getItemQuantity(itemID));
            double newQty = qtyInStock + qtyIncART;
            alert(itemID, String.valueOf(newQty));
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            final int pos = viewHolder.getAdapterPosition();
            stockItemsPojos = dbOperations.getAllItemsInCart();
            int itemID = stockItemsPojos.get(pos).getItem_id();
            double qtyIncART = Double.valueOf(stockItemsPojos.get(pos).getItem_quantity());
            double qtyInStock = Double.valueOf(dbOperations.getItemQuantity(itemID));
            double newQty = qtyInStock + qtyIncART;
            alert(itemID, String.valueOf(newQty));

        }
    };
    private Button buttonClear, buttonCharge;
    private Dialog dialog;
    private FloatingActionButton fab;
    private Vibrator vi;
    private TextView txtEmpty;
    private String LOG_TAG = "pdflog";
    private EditText edtCharge;
    private TextView txtBalance;
    private RadioButton radioSend;
    private RadioButton radioView;

    public void setView(boolean isListView, Context context) {

        dbOperations = new DbOperations(context);
        stockItemsPojos = dbOperations.getAllItemsInCart();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        txtEmpty = (TextView) view.findViewById(R.id.empty_view);
        cartItemsAdapter = new CartItemsAdapter(getContext(), stockItemsPojos, new cartItemClickListener() {
            @Override
            public void onBtnRemoveClicked(int position) {
                stockItemsPojos = dbOperations.getAllItemsInCart();
                int itemID = stockItemsPojos.get(position).getItem_id();
                Log.d("dbgd", stockItemsPojos.get(position).getItem_name() + "  " + stockItemsPojos.get(position).getItem_id());
                double qtyIncART = Double.valueOf(stockItemsPojos.get(position).getItem_quantity());
                double qtyInStock = Double.valueOf(dbOperations.getItemQuantity(itemID));
                double newQty = qtyInStock + qtyIncART;

                alert(itemID, String.valueOf(newQty));
                cartItemsAdapter.updateList(dbOperations.getAllItemsInCart());
                setCharge();
            }

            @Override
            public void onBtnChangeClicked(final int position) {
                dialog = new Dialog(getContext());
                //dialog.setTitle("Update");
                dialog.setContentView(R.layout.dialog_cart_item_quantity);
                final EditText edtQ = (EditText) dialog.findViewById(R.id.edt_quantity);
                final ImageButton btnP = (ImageButton) dialog.findViewById(R.id.btn_add);
                ImageButton btnM = (ImageButton) dialog.findViewById(R.id.btn_minus);
                Button btnD = (Button) dialog.findViewById(R.id.btn_dismiss);
                Button btnU = (Button) dialog.findViewById(R.id.btn_update);


                edtQ.setText(stockItemsPojos.get(position).getItem_quantity());

                quantityFromPreviousFragment = Double.valueOf(stockItemsPojos.get(position).getItem_quantity());


                //   final DbOperations dbOperations = new DbOperations(getContext());

                btnP.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Vibrator vi = (Vibrator) getActivity().getSystemService(getContext().VIBRATOR_SERVICE);

                        Double quantityInStock = Double.valueOf(dbOperations.getItemQuantity(stockItemsPojos.get(position).getItem_id()));
                        if (quantityInStock < 1) {
                            vi.vibrate(300);

                        } else {
                            Double newQuantityToCheck = (Double.valueOf(edtQ.getText().toString())) - quantityFromPreviousFragment;
                            if (quantityInStock - newQuantityToCheck <= 0) {

                                vi.vibrate(300);

                            } else {

                                Double v1 = Double.valueOf(edtQ.getText().toString());
                                edtQ.setText(String.valueOf(v1 + 1));
                            }

                        }


                    }
                });
                btnM.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (Double.valueOf(edtQ.getText().toString()) < 2) {
                            vi.vibrate(300);
                        } else {
                            double v1 = Double.valueOf(edtQ.getText().toString());
                            edtQ.setText(String.valueOf(v1 - 1));
                        }

                    }
                });
                btnD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnU.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double quantityInStock = Double.valueOf(dbOperations.getItemQuantity(stockItemsPojos.get(position).getItem_id()));

                        double quantityInCart = Double.valueOf(stockItemsPojos.get(position).getItem_quantity());
                        double newQuantityToCart = Double.valueOf(edtQ.getText().toString());


                        if (newQuantityToCart > quantityInCart) {

                            double qtc = newQuantityToCart - quantityInCart;
                            double newTotalrice = newQuantityToCart * Double.valueOf(stockItemsPojos.get(position).getItem_selling_price());
                            double newTotalBp = newQuantityToCart * Double.valueOf(stockItemsPojos.get(position).getItem_buying_price());
                            dbOperations.updateItemQuantity(stockItemsPojos.get(position).getItem_id(),
                                    String.valueOf(quantityInStock - qtc));
                            dbOperations.updateQuantityOfItemInCart(stockItemsPojos.get(position).getItem_id(),
                                    String.valueOf(newQuantityToCart), String.valueOf(newTotalrice), String.valueOf(newTotalBp));
                            cartItemsAdapter.updateList(dbOperations.getAllItemsInCart());
                            stockItemsPojos = dbOperations.getAllItemsInCart();
                            dialog.dismiss();
                            setCharge();
                        } else if (newQuantityToCart < quantityInCart) {

                            double qtc = quantityInCart - newQuantityToCart;
                            double newTotalrice = newQuantityToCart * Double.valueOf(stockItemsPojos.get(position).getItem_selling_price());
                            double newTotalBp = newQuantityToCart * Double.valueOf(stockItemsPojos.get(position).getItem_buying_price());
                            dbOperations.updateItemQuantity(stockItemsPojos.get(position).getItem_id(),
                                    String.valueOf(quantityInStock + qtc));
                            dbOperations.updateQuantityOfItemInCart(stockItemsPojos.get(position).getItem_id(),
                                    String.valueOf(newQuantityToCart), String.valueOf(newTotalrice), String.valueOf(newTotalBp));
                            cartItemsAdapter.updateList(dbOperations.getAllItemsInCart());
                            stockItemsPojos = dbOperations.getAllItemsInCart();
                            dialog.dismiss();
                            setCharge();
                        } else {
                            dialog.dismiss();
                        }
                        //dbOperations.updateItem(stockItemsPojos)
                    }
                });
                dialog.show();
            }
        });
        cartItemsAdapter.notifyDataSetChanged();
        if (isListView) {
            mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        } else {
            mStaggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        }
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cartItemsAdapter);


        if (cartItemsAdapter.getItemCount() < 1) {
            recyclerView.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            txtEmpty.setVisibility(View.GONE);
        }


    }

    public void setCharge() {
        buttonCharge.setText("");
        buttonCharge.setText("CHARGE " + String.valueOf(dbOperations.sumOfTotalPricesOfItemsInCart()));
        int itemsCountInCart = dbOperations.getNoOfItemsInCart();
        try {

            View view = getActivity().findViewById(R.id.action_receipt);
            if (itemsCountInCart > 0) {
                new QBadgeView(getActivity()).bindTarget(view).setBadgeNumber(itemsCountInCart);
            } else if (itemsCountInCart == 0) {
                new QBadgeView(getActivity()).bindTarget(view).setBadgeText(String.valueOf(itemsCountInCart)).setBadgeBackgroundColor(R.color.colorAccent);
                // Toast.makeText(getContext(), "bade", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception m) {
            m.printStackTrace();
            // Toast.makeText(getContext(), "error"+m, Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart, container, false);

        setView(true, getContext());
        dbOperations = new DbOperations(getContext());
        controller = new Controller();
        vi = (Vibrator) getActivity().getSystemService(getContext().VIBRATOR_SERVICE);


        buttonClear = (Button) view.findViewById(R.id.button_clear);
        buttonCharge = (Button) view.findViewById(R.id.button_charge);
        try {
            buttonClear.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_delete_black_24dp, 0, 0, 0);
            buttonCharge.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);
        } catch (Exception nm) {

        }
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    stockItemsPojos = dbOperations.getAllItemsInCart();
                    for (int a = 0; a < stockItemsPojos.size(); a++) {
                        double quantity = Double.valueOf(stockItemsPojos.get(a).getItem_quantity());
                        int item_id = stockItemsPojos.get(a).getItem_id();
                        double quantityInStock = Double.valueOf(dbOperations.getItemQuantity(item_id));
                        double newquantity = quantity + quantityInStock;
                        if (dbOperations.updateItemQuantity(item_id, String.valueOf(newquantity))) {
                            dbOperations.deleteCartItem(item_id);
                            cartItemsAdapter.updateList(dbOperations.getAllItemsInCart());
                            setCharge();
                        } else {
                            controller.toast("Error Ocooured", getContext(), R.drawable.ic_error_outline_black_24dp);
                            // Toast.makeText(getContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (NullPointerException m) {
                    controller.toast("No Items To Clear", getContext(), R.drawable.ic_error_outline_black_24dp);
                    //Toast.makeText(getContext(), "No Items To Clear", Toast.LENGTH_SHORT).show();
                }


                //  if (dbOperations.clearCart()) {
                //      setView(true, getContext());
                //  }
            }
        });
        setCharge();
        buttonCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (dbOperations.sumOfTotalPricesOfItemsInCart() < 1) {

                        vi.vibrate(200);
                    } else {
                        charge();
                    }
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        fab = controller.fab(getActivity(), true, R.drawable.ic_receipt_white_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (dbOperations.sumOfTotalPricesOfItemsInCart() < 1) {
                        vi.vibrate(200);
                    } else {
                        charge();

                    }
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
//        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
//            @Override
//            public void onClick(View view, final int position) {
//                dialog = new Dialog(getContext());
//                //dialog.setTitle("Update");
//                dialog.setContentView(R.layout.dialog_cart_item_quantity);
//                final EditText edtQ = (EditText) dialog.findViewById(R.id.edt_quantity);
//                final ImageButton btnP = (ImageButton) dialog.findViewById(R.id.btn_add);
//                ImageButton btnM = (ImageButton) dialog.findViewById(R.id.btn_minus);
//                Button btnD = (Button) dialog.findViewById(R.id.btn_dismiss);
//                Button btnU = (Button) dialog.findViewById(R.id.btn_update);
//
//
//                edtQ.setText(stockItemsPojos.get(position).getItem_quantity());
//
//                quantityFromPreviousFragment = Double.valueOf(stockItemsPojos.get(position).getItem_quantity());
//
//
//                //   final DbOperations dbOperations = new DbOperations(getContext());
//
//                btnP.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        Vibrator vi = (Vibrator) getActivity().getSystemService(getContext().VIBRATOR_SERVICE);
//
//                        Double quantityInStock = Double.valueOf(dbOperations.getItemQuantity(stockItemsPojos.get(position).getItem_id()));
//                        if (quantityInStock < 1) {
//                            vi.vibrate(300);
//
//                        } else {
//                            Double newQuantityToCheck = (Double.valueOf(edtQ.getText().toString())) - quantityFromPreviousFragment;
//                            if (quantityInStock - newQuantityToCheck <= 0) {
//
//                                vi.vibrate(300);
//
//                            } else {
//
//                                Double v1 = Double.valueOf(edtQ.getText().toString());
//                                edtQ.setText(String.valueOf(v1 + 1));
//                            }
//
//                        }
//
//
//                    }
//                });
//                btnM.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//
//                        if (Double.valueOf(edtQ.getText().toString()) < 2) {
//                            vi.vibrate(300);
//                        } else {
//                            double v1 = Double.valueOf(edtQ.getText().toString());
//                            edtQ.setText(String.valueOf(v1 - 1));
//                        }
//
//                    }
//                });
//                btnD.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                btnU.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        double quantityInStock = Double.valueOf(dbOperations.getItemQuantity(stockItemsPojos.get(position).getItem_id()));
//
//                        double quantityInCart = Double.valueOf(stockItemsPojos.get(position).getItem_quantity());
//                        double newQuantityToCart = Double.valueOf(edtQ.getText().toString());
//
//
//                        if (newQuantityToCart > quantityInCart) {
//
//                            double qtc = newQuantityToCart - quantityInCart;
//                            double newTotalrice = newQuantityToCart * Double.valueOf(stockItemsPojos.get(position).getItem_selling_price());
//                            double newTotalBp = newQuantityToCart * Double.valueOf(stockItemsPojos.get(position).getItem_buying_price());
//                            dbOperations.updateItemQuantity(stockItemsPojos.get(position).getItem_id(),
//                                    String.valueOf(quantityInStock - qtc));
//                            dbOperations.updateQuantityOfItemInCart(stockItemsPojos.get(position).getItem_id(),
//                                    String.valueOf(newQuantityToCart), String.valueOf(newTotalrice), String.valueOf(newTotalBp));
//                            cartItemsAdapter.updateList(dbOperations.getAllItemsInCart());
//                            stockItemsPojos = dbOperations.getAllItemsInCart();
//                            dialog.dismiss();
//                            setCharge();
//                        } else if (newQuantityToCart < quantityInCart) {
//
//                            double qtc = quantityInCart - newQuantityToCart;
//                            double newTotalrice = newQuantityToCart * Double.valueOf(stockItemsPojos.get(position).getItem_selling_price());
//                            double newTotalBp = newQuantityToCart * Double.valueOf(stockItemsPojos.get(position).getItem_buying_price());
//                            dbOperations.updateItemQuantity(stockItemsPojos.get(position).getItem_id(),
//                                    String.valueOf(quantityInStock + qtc));
//                            dbOperations.updateQuantityOfItemInCart(stockItemsPojos.get(position).getItem_id(),
//                                    String.valueOf(newQuantityToCart), String.valueOf(newTotalrice), String.valueOf(newTotalBp));
//                            cartItemsAdapter.updateList(dbOperations.getAllItemsInCart());
//                            stockItemsPojos = dbOperations.getAllItemsInCart();
//                            dialog.dismiss();
//                            setCharge();
//                        } else {
//                            dialog.dismiss();
//                        }
//                        //dbOperations.updateItem(stockItemsPojos)
//                    }
//                });
//                dialog.show();
//
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//                stockItemsPojos=dbOperations.getAllItemsInCart();
//                int itemID = stockItemsPojos.get(position).getItem_id();
//                double qtyIncART = Double.valueOf(stockItemsPojos.get(position).getItem_quantity());
//                double qtyInStock = Double.valueOf(dbOperations.getItemQuantity(itemID));
//                double newQty = qtyInStock + qtyIncART;
//
//                alert(itemID, String.valueOf(newQty));
//                cartItemsAdapter.updateList(dbOperations.getAllItemsInCart());
//                setCharge();
//            }
//        }));
//        //ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this.simpleCallback);
        //itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    private void charge() throws DocumentException, FileNotFoundException {
        dialog = new Dialog(getContext());
        dialog.setTitle(String.valueOf(dbOperations.sumOfTotalPricesOfItemsInCart()));
        dialog.setContentView(R.layout.dialog_sell);
        dialog.setCanceledOnTouchOutside(false);
        txtBalance = (TextView) dialog.findViewById(R.id.cashback);
        edtCharge = (EditText) dialog.findViewById(R.id.cashin);
        radioSend = (RadioButton) dialog.findViewById(R.id.radioSend);
        radioView = (RadioButton) dialog.findViewById(R.id.radioView);

        r = (RadioGroup) dialog.findViewById(R.id.radio);
        r.clearCheck();
        Button btnDismiss = (Button) dialog.findViewById(R.id.btn_dismiss);
        Button btnFinish = (Button) dialog.findViewById(R.id.btn_finish);
        final double total = dbOperations.sumOfTotalPricesOfItemsInCart();

        txtBalance.setText(String.valueOf(total));
        edtCharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    double newTotal = total - Double.valueOf(s.toString());
                    txtBalance.setText(String.valueOf(newTotal));
                } catch (Exception m) {


                    //txtBalance.setText(String.valueOf(total));
                    //edtCharge.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtCharge.getText().toString().equals("")) {
                    double paidAmount = Double.valueOf(edtCharge.getText().toString());

                    if (paidAmount >= total && dbOperations.getNoOfItemsInCart() >= 1) {


                        Gson gson = new Gson();
                        String data = gson.toJson(dbOperations.getAllItemsInCart());

                        Date date = new Date();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd ,yyyy  HH : mm : ss");
                        String timeStamp = simpleDateFormat.format(date);
                        //String timeStamp = new SimpleDateFormat("MMM dd ,yyyy_HH mm ss").format(date);
                        // for(int a=0;a<stockItemsPojos.size();a++){
                        TransactionsPojo transactionsPojo = new TransactionsPojo();
                        transactionsPojo.setTransaction_id(1);
                        transactionsPojo.setTransaction_items(data);
                        transactionsPojo.setTransaction_date(timeStamp);
                        transactionsPojo.setTransaction_quantity(String.valueOf(stockItemsPojos.size()));
                        transactionsPojo.setTransaction_total_bp(String.valueOf(dbOperations.sumOfTotalBpPricesOfItemsInCart()));
                        transactionsPojo.setTransaction_total_sp(String.valueOf(dbOperations.sumOfTotalPricesOfItemsInCart()));
                        transactionsPojo.setTransaction_cash_in(String.valueOf(paidAmount));

                        if (dbOperations.insertTransaction(transactionsPojo)) {

                            controller.toast("Transaction complete", getContext(), R.drawable.ic_done_black_24dp);
                            //Toast.makeText(getContext(), "Transaction Complete", Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, data);
                            if (radioSend.isChecked()) {
                                //shareReciept(myFile);
                                progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setMessage("Generating Receipt ...");
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.setIndeterminate(true);
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                Back b = new Back(0, stockItemsPojos, dbOperations.sumOfTotalPricesOfItemsInCart(), paidAmount, "My Shop\n0714406984\nerickogi14@gmail.com", "Eric Kogi", "9173");
                                b.execute();
                                //dialog.dismiss();
                            } else if (radioView.isChecked()) {
                                dialog.dismiss();
                                progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setMessage("Generating Receipt ...");
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.setIndeterminate(true);
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                //viewPdf(myFile);
                                Back b = new Back(1, stockItemsPojos, dbOperations.sumOfTotalPricesOfItemsInCart(), paidAmount, "My Shop\n0714406984\nerickogi14@gmail.com", "Eric Kogi", "9173");
                                b.execute();
                                //dialog.dismiss();
                            } else {
                                dialog.dismiss();
                            }

                            if (dbOperations.clearCart()) {
                                setCharge();
                                setView(true, getContext());
                            }

                        } else {
                            controller.toast("Transaction Failed ", getContext(), R.drawable.ic_error_outline_black_24dp);
                            //Toast.makeText(getContext(), "Transaction Failed", Toast.LENGTH_SHORT).show();
                        }
                        // }


                    } else {
                        vi.vibrate(200);
                    }

                } else {
                    vi.vibrate(200);
                }
            }
        });

        dialog.show();

    }

    private boolean storeImage(Bitmap b) {
        try {
            Date date = new Date();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
            //String name=editTextItem_name.getText().toString();
            // imagePath=name+".png";
            fle = timeStamp + ".pdf";

            FileOutputStream fos = getActivity().openFileOutput(fle, Context.MODE_PRIVATE);
            // b.compress(Bitmap.CompressFormat.PNG,100,fos);
            Document document = new Document();
            PdfWriter.getInstance(document, fos);
            document.open();
            document.add(new Paragraph("paragraph title"));
            document.add(new Paragraph("Body of paragraph....................." +
                    ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,jjjjjjjjjjjjjjjhhhhhhhhhhhhhdhdhdh"));
            document.close();
            fos.close();
            viewPdf(myFile);
            return true;
        } catch (Exception m) {
            // Toast.makeText(this, "not sac"+m.getMessage(), Toast.LENGTH_SHORT).show();
            m.printStackTrace();
            return false;
        }

    }

    private void viewPdf(File myFile) {
        // dialog.dismiss();
        progressDialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    private void emailNote() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_SUBJECT, "Pdf Test");
        email.putExtra(Intent.EXTRA_TEXT, "This is a test");
        Uri uri = Uri.parse(myFile.getAbsolutePath());
        email.putExtra(Intent.EXTRA_STREAM, uri);
        email.setType("message/rfc822");
        startActivity(email);
    }

    public void alert(final int pos, final String newQuantit) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (dbOperations.updateItemQuantity(pos, newQuantit)) {
                            if (dbOperations.deleteCartItem(pos)) {
                                cartItemsAdapter.updateList(dbOperations.getAllItemsInCart());
                                //Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                controller.toast("Deleted", getContext(), R.drawable.ic_check_black_24dp);
                                cartItemsAdapter.updateList(dbOperations.getAllItemsInCart());
                                setCharge();

                            } else {
                                controller.toast("Error Deleting Cart Item", getContext(), R.drawable.ic_error_outline_black_24dp);
                                //  Toast.makeText(getContext(), "Error Deleting cart", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            controller.toast("Error Updating Quantity", getContext(), R.drawable.ic_error_outline_black_24dp);
                            // Toast.makeText(getContext(), "Error Updating Quantity", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        cartItemsAdapter.updateList(dbOperations.getAllItemsInCart());
                        break;
                }
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("This will delete the item from cart  . continue ?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void shareReciept(File f) {
        dialog.dismiss();
        progressDialog.dismiss();
        Uri uri = Uri.fromFile(f);
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, uri);

        getContext().startActivity(share);
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
            Receipt r = new Receipt(getContext());
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
