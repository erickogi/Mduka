package com.erickogi14gmail.mduka.Items;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.erickogi14gmail.mduka.Controller;
import com.erickogi14gmail.mduka.Db.DbOperations;
import com.erickogi14gmail.mduka.Db.StockItemsPojo;
import com.erickogi14gmail.mduka.R;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ItemDetails extends AppCompatActivity {
    private final int CAMERA_REQUEST = 1888;
    private ArrayList<StockItemsPojo> stockItemsPojos;
    private int position;
    private EditText editTextItem_name, editText_ItemSp, editTextItem_bp, editTextItem_discount, editTextItem_quantity, editTextItem_unit;
    private Spinner spinner;
    private ImageButton imageButton;
    private DbOperations dbOperations;
    private ArrayList<String> categories = new ArrayList<>();
    private ImageView imgC, img;
    private Dialog dialog;
    private String imagePath = "1";
    private boolean forUpdate = false;
    private Controller controller;

    private boolean validate() {
        return !(editTextItem_name.getText().toString().equals("")
                || editText_ItemSp.getText().toString().equals("")
                || editTextItem_bp.getText().toString().equals("")
                || editTextItem_discount.getText().toString().equals("")
                || editTextItem_quantity.getText().toString().equals("")
                || editTextItem_unit.getText().toString().equals(""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbOperations = new DbOperations(getApplicationContext());
        controller = new Controller();
        spinner = (Spinner) findViewById(R.id.spinner_category);
        imageButton = (ImageButton) findViewById(R.id.button_add_category);
        editTextItem_name = (EditText) findViewById(R.id.item_name);
        editText_ItemSp = (EditText) findViewById(R.id.item_sp);
        editTextItem_bp = (EditText) findViewById(R.id.item_bp);
        editTextItem_discount = (EditText) findViewById(R.id.item_discount);
        editTextItem_quantity = (EditText) findViewById(R.id.item_quantity);
        editTextItem_unit = (EditText) findViewById(R.id.item_unit);
        img = (ImageView) findViewById(R.id.item_image);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editTextItem_name.getText().toString().equals("")) {
                    Toast.makeText(ItemDetails.this, "Enter product details First", Toast.LENGTH_SHORT).show();
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });


        try {
            categories = dbOperations.getAllCategories1();
            categories.add("All");


            ArrayAdapter<String> simpleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories);
            spinner.setAdapter(simpleAdapter);
            int size = categories.size();
            spinner.setSelection(size - 1);
            //spinner=controller.setUpSpinner(getApplicationContext(),R.id.spinner_category,imageButton.getRootView());
        } catch (NullPointerException m) {
            m.printStackTrace();
            ArrayList<String> c = new ArrayList<>();
            c.add("All");
            ArrayAdapter<String> simpleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, c);
            spinner.setAdapter(simpleAdapter);
            spinner.setSelection(0);
        }


        Intent intent = getIntent();
        position = intent.getIntExtra("dataPosition", 0);
        stockItemsPojos = (ArrayList<StockItemsPojo>) intent.getSerializableExtra("data");
        try {
            editTextItem_name.setText(stockItemsPojos.get(position).getItem_name());
            editText_ItemSp.setText(stockItemsPojos.get(position).getItem_selling_price());
            editTextItem_bp.setText(stockItemsPojos.get(position).getItem_buying_price());
            editTextItem_quantity.setText(stockItemsPojos.get(position).getItem_quantity());
            editTextItem_unit.setText(stockItemsPojos.get(position).getItem_unit_type());
            editTextItem_discount.setText(stockItemsPojos.get(position).getItem_discount());

            for (int a = 0; a < categories.size(); a++) {
                if (categories.get(a).equalsIgnoreCase(stockItemsPojos.get(position).getItem_category())) {
                    spinner.setSelection(a);
                }

            }
            forUpdate = true;


        } catch (NullPointerException m) {
            m.printStackTrace();
        }

        try {
            imagePath = stockItemsPojos.get(position).getItem_image();
            img.setImageBitmap(getThumbnail(imagePath));

        } catch (Exception m) {

            img.setImageResource(R.drawable.ic_dashboard_black_24dp);
        }


    }


    public void colorChooser(View view) {

        final ColorPicker cp = new ColorPicker(ItemDetails.this);
        cp.show();
        Button okColor = (Button) cp.findViewById(R.id.okColorButton);
        okColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sc = cp.getColor();
                imgC.setBackgroundColor(sc);
                cp.dismiss();
            }
        });

    }

    public Bitmap getThumbnail(String filename) {
        Bitmap thumnail = null;
        try {
            File filepath = this.getFileStreamPath(filename);
            FileInputStream fi = new FileInputStream(filepath);
            thumnail = BitmapFactory.decodeStream(fi);

        } catch (Exception m) {
            m.printStackTrace();
        }
        return thumnail;
    }

    private boolean storeImage(Bitmap b) {
        try {
            String name = editTextItem_name.getText().toString();
            imagePath = name + ".png";
            FileOutputStream fos = this.openFileOutput(name + ".png", Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return true;
        } catch (Exception m) {
            Toast.makeText(this, "not sac" + m.getMessage(), Toast.LENGTH_SHORT).show();
            m.printStackTrace();
            return false;
        }
    }

    protected void onActivityResult(int resquestCode, int resultCode, Intent data) {
        if (resquestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap sb = Bitmap.createScaledBitmap(photo, 60, 60, false);

            img.setImageBitmap(sb);
            if (storeImage(sb)) {
                Toast.makeText(this, "stored", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void dismiss(View view) {
        super.onBackPressed();
    }

    public void save(View view) {
        if (validate()) {

            if (forUpdate) {
                StockItemsPojo su = new StockItemsPojo(stockItemsPojos.get(position).getItem_id(), editTextItem_name.getText().toString()
                        , editTextItem_quantity.getText().toString(), editTextItem_unit.getText().toString(),
                        editTextItem_bp.getText().toString(), editText_ItemSp.getText().toString(), spinner.getSelectedItem().toString()
                        , editTextItem_discount.getText().toString(), imagePath);
                updateItem(su);
            } else {
                StockItemsPojo si = new StockItemsPojo(1, editTextItem_name.getText().toString()
                        , editTextItem_quantity.getText().toString(), editTextItem_unit.getText().toString(),
                        editTextItem_bp.getText().toString(), editText_ItemSp.getText().toString(), spinner.getSelectedItem().toString()
                        , editTextItem_discount.getText().toString(), imagePath);
                insertNew(si);
            }


        } else {
            Toast.makeText(this, "Empty Fields Detected", Toast.LENGTH_SHORT).show();
        }
    }

    public void addCategory(View view) {
        dialog = new Dialog(this);
        dialog.setTitle("New Category");
        dialog.setContentView(R.layout.dialog_add_category);
        Button submit = (Button) dialog.findViewById(R.id.btnSubmit);
        Button cancel = (Button) dialog.findViewById(R.id.btnCancel);
        final EditText edtSearch = (EditText) dialog.findViewById(R.id.editTextC);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtSearch.getText().toString().isEmpty()) {
                    edtSearch.setError("Field Cannot be Empty");
                } else {
                    if (dbOperations.insertCategory(edtSearch.getText().toString())) {
                        Toast.makeText(ItemDetails.this, "Category Inserted", Toast.LENGTH_SHORT).show();

                        ArrayList<String> categories = new ArrayList<String>();
                        categories = dbOperations.getAllCategories1();
                        // categories.add("All");

                        ArrayAdapter<String> simpleAdapter = new ArrayAdapter<String>(ItemDetails.this, android.R.layout.simple_spinner_dropdown_item, categories);
                        spinner.setAdapter(simpleAdapter);
                        dialog.dismiss();
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void insertNew(StockItemsPojo s) {
        if (dbOperations.insertItem(s)) {
            editTextItem_name.setText("");
            editText_ItemSp.setText("");
            editTextItem_bp.setText("");
            editTextItem_discount.setText("");
            editTextItem_quantity.setText("");
            editTextItem_unit.setText("");
            spinner.setSelection(0);
            img.setImageResource(R.drawable.ic_dashboard_black_24dp);
            forUpdate = false;


            // fragment_items.updateItems(getApplicationContext());
            Toast.makeText(this, "Item Inserted Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error Insertng Item", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateItem(StockItemsPojo s) {
        if (dbOperations.updateItem(s)) {
            editTextItem_name.setText("");
            editText_ItemSp.setText("");
            editTextItem_bp.setText("");
            editTextItem_discount.setText("");
            editTextItem_quantity.setText("");
            editTextItem_unit.setText("");
            spinner.setSelection(0);
            img.setImageResource(R.drawable.ic_dashboard_black_24dp);
            forUpdate = false;


            //fragment_items.updateItems(getApplicationContext());
            Toast.makeText(this, "Item Updated Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error Updating Item", Toast.LENGTH_SHORT).show();
        }
    }
}
