package com.erickogi14gmail.mduka.Prefrence;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.erickogi14gmail.mduka.Configs;
import com.erickogi14gmail.mduka.Controller;
import com.erickogi14gmail.mduka.MyApplication;
import com.erickogi14gmail.mduka.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {
    private final int CAMERA_REQUEST = 1888;
    private TextInputEditText edtPersonName, edtPersonPhone, edtShopName, edtShopPhone, edtShopEmail;
    private Button btnSave;
    private ImageView imgPic;
    private Controller controller;
    private Prefrence prefrenceManager;

    private String imagePath = "1";
    private Bitmap bitmap;

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefrenceManager = new Prefrence(Settings.this);
        setContentView(R.layout.activity_settings);
        controller = new Controller();


        edtPersonName = (TextInputEditText) findViewById(R.id.persons_name);
        edtPersonPhone = (TextInputEditText) findViewById(R.id.persons_mobile);
        edtShopName = (TextInputEditText) findViewById(R.id.shop_name);
        edtShopPhone = (TextInputEditText) findViewById(R.id.shops_no);
        edtShopEmail = (TextInputEditText) findViewById(R.id.shops_email);
        imgPic = (ImageView) findViewById(R.id.image);
        //btnSave=(Button)findViewById(R.id.btn_s)

        imgPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        //btnSave=(Button)findViewById(R.id.btn)

        String image = prefrenceManager.getImg();

        //Toast.makeText(getContext(), ""+image, Toast.LENGTH_SHORT).show();

        if (!image.equals("null")) {
            imgPic.setImageBitmap(getThumbnail(image));
            bitmap = getThumbnail(image);
            imagePath = image;
        } else {


        }
        HashMap<String, String> profile = new HashMap<>();
//        profile.put("name", pref.getString(KEY_NAME, ""));
//
//        profile.put("mobile", pref.getString(KEY_MOBILE, ""));
//
//        profile.put("shopname", pref.getString(KEY_SHOPNAME, ""));
//        profile.put("shopemail", pref.getString(KEY_SHOPEMAIL, ""));
//        profile.put("shopphone", pref.getString(KEY_SHOPPHONE, ""));

        HashMap<String, String> details = prefrenceManager.getUserDetails();

        edtPersonName.setText(details.get("name"));
        edtPersonPhone.setText(details.get("mobile"));
        edtShopName.setText(details.get("shopname"));
        edtShopEmail.setText(details.get("shopemail"));
        edtShopPhone.setText(details.get("shopphone"));


    }

    private void UpdateDetails() {
        String image;
        if (bitmap != null) {
            image = getStringImage(bitmap);


            update(edtPersonName.getText().toString(), edtPersonPhone.getText().toString(), edtShopName.getText().toString(),
                    edtPersonPhone.getText().toString(), edtShopEmail.getText().toString(), image);
        } else {
            Toast.makeText(this, "Some Fields Are Missing", Toast.LENGTH_SHORT).show();
        }
        //  prefManager.updateUser(name,email,img,zone);
        //fragmentManager.popBackStack();
        //popOutFragments();

    }

    boolean checkIfFilled() {
        if (edtPersonName.getText().toString().equals("") || edtPersonName.getText().toString().isEmpty()) {
            edtPersonName.setError("Required");

            return false;
        } else if (edtPersonPhone.getText().toString().isEmpty() || edtPersonPhone.getText().toString().equals("")) {

            edtPersonPhone.setError("Required");
            return false;
        } else if (edtShopName.getText().toString().equals("") || edtShopName.getText().toString().isEmpty()) {
            edtShopName.setError("Required");
            return false;
        } else if (edtShopPhone.getText().toString().isEmpty() || edtShopPhone.getText().toString().equals("")) {
            edtShopPhone.setError("Required");
            return false;
        } else if (edtShopEmail.getText().toString().equals("") || edtShopEmail.getText().toString().isEmpty()) {
            edtShopEmail.setError("Required");
            return false;
        } else {
            return true;
        }


    }

    public Bitmap getThumbnail(String filename) {
        Bitmap thumnail = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_person_outline_black_24dp);
        try {
            File filepath = getApplicationContext().getFileStreamPath(filename);
            FileInputStream fi = new FileInputStream(filepath);
            thumnail = BitmapFactory.decodeStream(fi);

        } catch (Exception m) {
            m.printStackTrace();
        }
        return thumnail;
    }


    private boolean storeImage(Bitmap b) {
        try {
            String name = "T" + edtPersonName.getText().toString();
            imagePath = name + ".png";
            FileOutputStream fos = getApplicationContext().openFileOutput(name + ".png", Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();


            prefrenceManager.storeImg(imagePath);
            return true;
        } catch (Exception m) {
            //controller.toast("Error Storing Image",ItemDetails.this,R.drawable.ic_error_outline_black_24dp);
            Toast.makeText(Settings.this, "not sac" + m.getMessage(), Toast.LENGTH_SHORT).show();
            m.printStackTrace();
            return false;
        }
    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void update(final String name, final String phone, final String shopname, final String shopphone, final String shopemail, final String image) {
        //   edtPersonName,edtPersonPhone,edtShopName,edtShopPhone,edtShopEmail
        final ProgressDialog loading = ProgressDialog.show(Settings.this, "Uploading...", "Please wait...", false, false);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Configs.UPDATE_PROFILE_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Log.d("response", response.toString());
                //progressDialog.dismiss();
                // dialoge.show();
                try {
                    JSONObject responseObj = new JSONObject(response);

                    // Parsing json object response
                    // response will be a json object
                    boolean error = responseObj.getBoolean("error");
                    //  String message = responseObj.getString("message");

                    // checking for error, if not error SMS is initiated
                    // device should receive it shortly
                    if (!error) {

                        loading.dismiss();
                        Toast.makeText(Settings.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();

                        // boolean flag saying device is waiting for sms

                        //pref.setIsWaitingForSms(true);
                        // progressDialog.dismiss();
                        // moving the screen to next pager item i.e otp screen
                        //dialoge.show();


                        // Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

                    } else {
                        loading.dismiss();
                        String message = responseObj.getString("message");
                        Toast.makeText(Settings.this, "Profile Update Failed Try Again", Toast.LENGTH_SHORT).show();


                    }

                    // hiding the progress bar
                    // progressDialog.dismiss();


                } catch (JSONException e) {
                    loading.dismiss();
                    e.printStackTrace();
                    Log.d("ERRRRR", e.toString());
                    Toast.makeText(Settings.this, "Profile Update Failed Try Again", Toast.LENGTH_SHORT).show();

                    // Toast.makeText(getContext(),
                    //         "Error: " + e.getMessage(),
                    //        Toast.LENGTH_LONG).show();
                    // dialoge.dismiss();
                    //relativeLayoutSignup.setVisibility(View.VISIBLE);
                    // relativeLayoutOtp.setVisibility(View.GONE);
                    // progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(Settings.this, "Profile Update Failed Try Again", Toast.LENGTH_SHORT).show();

                //relativeLayoutSignup.setVisibility(View.VISIBLE);
                //relativeLayoutOtp.setVisibility(View.GONE);
                Log.e("error", "Error: " + error.getMessage());

            }
        }) {

            /**
             * Passing user parameters to our server
             * @return
             */
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //edtPersonName,edtPersonPhone,edtShopName,edtShopPhone,edtShopEmail
                params.put("name", name);
                params.put("mobile", phone);
                params.put("shopname", shopname);
                params.put("shopname", shopphone);
                params.put("shopemail", shopemail);
                params.put("image", image);

                Log.e("posting params", "Posting params: " + params.toString());

                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }


    public void save(View view) {
        if (checkIfFilled()) {
            if (isValidPhoneNumber(edtShopPhone.getText().toString()) && isValidPhoneNumber(edtPersonPhone.getText().toString()) && isValidEmail(edtShopEmail.getText().toString())) {


                UpdateDetails();//prefrenceManager.updateUser();


            } else {
                controller.toast("Email or Phone Is Invalid", Settings.this, R.drawable.ic_error_outline_black_24dp);
            }
        }


    }

    public void onActivityResult(int resquestCode, int resultCode, Intent data) {
        if (resquestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap sb = Bitmap.createScaledBitmap(photo, 70, 70, false);
            bitmap = sb;


            imgPic.setImageBitmap(sb);
            if (storeImage(sb)) {
                // controller.toast("Image Retrieved Successfully",ItemDetails.this,R.drawable.ic_done_black_24dp);
                // Toast.makeText(getContext(), "stored", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
