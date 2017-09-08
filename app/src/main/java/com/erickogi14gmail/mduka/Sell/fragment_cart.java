package com.erickogi14gmail.mduka.Sell;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
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
import android.widget.Toast;

import com.erickogi14gmail.mduka.Controller;
import com.erickogi14gmail.mduka.Db.DbOperations;
import com.erickogi14gmail.mduka.Db.StockItemsPojo;
import com.erickogi14gmail.mduka.Db.TransactionsPojo;
import com.erickogi14gmail.mduka.R;
import com.erickogi14gmail.mduka.utills.RecyclerTouchListener;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.roughike.bottombar.BottomBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Eric on 8/29/2017.
 */

public class fragment_cart extends Fragment {
    Controller controller;
    double quantityFromPreviousFragment = 0.0;
    File myFile = null;
    String fle;
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
            int itemID = stockItemsPojos.get(pos).getItem_id();
            double qtyIncART = Double.valueOf(stockItemsPojos.get(pos).getItem_quantity());
            double qtyInStock = Double.valueOf(dbOperations.getItemQuantity(itemID));
            double newQty = qtyInStock + qtyIncART;
            alert(itemID, String.valueOf(newQty));

        }
    };
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context;
    private ImageButton imageButton;
    private CardView cardview;
    private BottomBar bottomNavigationView;
    private Button buttonClear, buttonCharge;
    private Dialog dialog;
    private FloatingActionButton fab;
    private Vibrator vi;
    private TextView txtEmpty;
    private String LOG_TAG = "pdflog";

    public void setView(boolean isListView, Context context) {

        dbOperations = new DbOperations(context);
        stockItemsPojos = dbOperations.getAllItemsInCart();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        txtEmpty = (TextView) view.findViewById(R.id.empty_view);
        cartItemsAdapter = new CartItemsAdapter(getContext(), stockItemsPojos);
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

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int a = 0; a < stockItemsPojos.size(); a++) {
                    double quantity = Double.valueOf(stockItemsPojos.get(a).getItem_quantity());
                    int item_id = stockItemsPojos.get(a).getItem_id();
                    double quantityInStock = Double.valueOf(dbOperations.getItemQuantity(item_id));
                    double newquantity = quantity + quantityInStock;
                    if (dbOperations.updateItemQuantity(item_id, String.valueOf(newquantity))) {
                        dbOperations.deleteCartItem(item_id);
                        cartItemsAdapter.updateList(dbOperations.getAllItemsInCart());
                    } else {
                        Toast.makeText(getContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                    }
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
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
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

            @Override
            public void onLongClick(View view, int position) {
                int itemID = stockItemsPojos.get(position).getItem_id();
                double qtyIncART = Double.valueOf(stockItemsPojos.get(position).getItem_quantity());
                double qtyInStock = Double.valueOf(dbOperations.getItemQuantity(itemID));
                double newQty = qtyInStock + qtyIncART;

                alert(itemID, String.valueOf(newQty));
                cartItemsAdapter.updateList(dbOperations.getAllItemsInCart());
                setCharge();
            }
        }));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this.simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    private void charge() throws DocumentException, FileNotFoundException {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_sell);
        final TextView txtBalance = (TextView) dialog.findViewById(R.id.cashback);
        final EditText edtCharge = (EditText) dialog.findViewById(R.id.cashin);
        final RadioButton radioSend = (RadioButton) dialog.findViewById(R.id.radioSend);
        final RadioButton radioView = (RadioButton) dialog.findViewById(R.id.radioView);

        RadioGroup r = (RadioGroup) dialog.findViewById(R.id.radio);
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
                    txtBalance.setText(String.valueOf(total));
                    edtCharge.setText("");
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
                double paidAmount = Double.valueOf(edtCharge.getText().toString());

                if (paidAmount >= total && dbOperations.getNoOfItemsInCart() >= 1) {


                    Receipt r = new Receipt(getContext());
                    try {
                        myFile = r.re(stockItemsPojos, dbOperations.sumOfTotalPricesOfItemsInCart(), paidAmount, "My Shop\n0714406984\nerickogi14@gmail.com", "Eric Kogi", "9173");
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

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

                    if (dbOperations.insertTransaction(transactionsPojo)) {
                        Toast.makeText(getContext(), "Transaction Complete", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, data);
                        if (radioSend.isChecked()) {
                            shareReciept(myFile);
                            dialog.dismiss();
                        } else if (radioView.isChecked()) {
                            viewPdf(myFile);
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                        }

                        if (dbOperations.clearCart()) {
                            setCharge();
                            setView(true, getContext());
                        }

                    } else {
                        Toast.makeText(getContext(), "Transaction Failed", Toast.LENGTH_SHORT).show();
                    }
                    // }


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
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
    //////////////

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
                                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                setCharge();
                                cartItemsAdapter.updateList(dbOperations.getAllItemsInCart());
                            } else {
                                Toast.makeText(getContext(), "Error Deleting cart", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Error Updating Quantity", Toast.LENGTH_SHORT).show();
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
        Uri uri = Uri.fromFile(f);
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, uri);

        getContext().startActivity(share);
    }


}
