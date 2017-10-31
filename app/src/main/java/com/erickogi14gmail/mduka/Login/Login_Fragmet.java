package com.erickogi14gmail.mduka.Login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.erickogi14gmail.mduka.Configs;
import com.erickogi14gmail.mduka.MainActivity;
import com.erickogi14gmail.mduka.MyApplication;
import com.erickogi14gmail.mduka.Prefrence.Prefrence;
import com.erickogi14gmail.mduka.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 10/20/2017.
 */

public class Login_Fragmet extends Fragment {
    ProgressDialog progressDialog;
    private View view;
    private Prefrence pref;
    private Dialog dialoge;
    private TextInputEditText mEmail;
    private Button btnLogin;

    public final static boolean isValidEmail(CharSequence target) {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment, container, false);
        view = inflater.inflate(R.layout.login_fragment, container, false);
        mEmail = (TextInputEditText) view.findViewById(R.id.edt_email);
        btnLogin = (Button) view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiate();
            }
        });
        return view;
    }

    private void initiate() {
        if (isFilled(mEmail)) {
            //if(validateFields())
            //{
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Login in ....");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            requestForSMS(mEmail.getText().toString());
            //}
        }
    }

    private boolean validateFields() {
        if (!isValidEmail(mEmail.getText().toString())) {
            mEmail.setError("Invalid Email");
            return false;
        } else {
            return isValidPhoneNumber(mEmail.getText().toString());
        }


    }

    private boolean isFilled(TextInputEditText textInputEditText) {
        if (textInputEditText.getText().toString().equals("")) {
            textInputEditText.setError("Required");
            return false;
        } else {
            return isValidPhoneNumber(mEmail.getText().toString());
        }

    }

    private void requestForSMS(final String mobile) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Configs.LOGIN_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Log.d("response", response.toString());
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
                        JSONObject profileObj = responseObj.getJSONObject("profile");

                        String name = profileObj.getString("name");

                        String mobile = profileObj.getString("mobile");

                        String email = profileObj.getString("email");

                        String api = profileObj.getString("apikey");


                        Prefrence pref = new Prefrence(getContext());
                        pref.createLogin(name, email, mobile);

                        startActivity(new Intent(getActivity(), MainActivity.class));

                        getActivity().finish();
                        // Bitmap b=getThumbnail(imageurl);
                        // if(storeImage(b,name)) {

                        // startActivity(new Intent(getContext(), MainActivity.class));

                        // }
                        // Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getActivity(), "Error Login In .\n If you don't have an account ,create One first", Toast.LENGTH_LONG).show();

                        String message = responseObj.getString("error");

                        //Toast.makeText(getContext(),
                        //       "Error: " + message,
                        //      Toast.LENGTH_LONG).show();

                    }

                    // hiding the progress bar
                    progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("ERRRRR", e.toString());
                    Toast.makeText(getContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                //progressBar.setVisibility(View.GONE);
            }
        }) {

            /**
             * Passing user parameters to our server
             * @return
             */
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", mobile);

                Log.e("posting params", "Posting params: " + params.toString());

                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    //
//    public Bitmap getThumbnail(String filename, final String name) {
//        //   final Bitmap thumnail = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ic_person_black_24dp);
////
//        Picasso.with(getContext()).load(filename).into(new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                Log.d("bitmap", "Loaded");
//                // Toast.makeText(getContext(), "Bitmap loaded"+bitmap, Toast.LENGTH_SHORT).show();
//                thumnail = bitmap;
//                if (
//                        storeImage(bitmap, name)) {
//                    progressDialog.dismiss();
//
//                    startActivity(new Intent(getContext(), MainActivity.class));
//
//                } else {
//                    progressDialog.dismiss();
//
//                    startActivity(new Intent(getContext(), MainActivity.class));
//
//                }
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//                Log.d("bitmap", "Failed");
//                progressDialog.dismiss();
//
//                startActivity(new Intent(getContext(), MainActivity.class));
//
//                // Toast.makeText(getContext(), "Bitmap failed"+errorDrawable, Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//                //progressDialog.dismiss();
//
//                //startActivity(new Intent(getContext(), MainActivity.class));
//
//                Log.d("bitmap", "prepared");
//                // Toast.makeText(getContext(), "Bitmap prepare"+placeHolderDrawable, Toast.LENGTH_SHORT).show();
//
//            }
//        });
////        Bitmap thumnail = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ic_person_black_24dp);
////        try {
////            File filepath = getContext().getFileStreamPath(filename);
////            FileInputStream fi = new FileInputStream(filepath);
////            thumnail = BitmapFactory.decodeStream(fi);
////
////        } catch (Exception m) {
////            m.printStackTrace();
////        }
//        return thumnail;
//    }

    private boolean storeImage(Bitmap b, String nameo) {
        try {
            Prefrence pref = new Prefrence(getContext());

            String name = "T" + nameo;
            String imagePath = name + ".png";
            FileOutputStream fos = getContext().openFileOutput(name + ".png", Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            // pref.storeImg(imagePath);
            Log.d("imagestored", imagePath);


            // prefManager.storeImg(imagePath);
            return true;
        } catch (Exception m) {
            Log.d("imagestored", "" + m.getMessage());

            //controller.toast("Error Storing Image",ItemDetails.this,R.drawable.ic_error_outline_black_24dp);
            Toast.makeText(getContext(), "not sac" + m.getMessage(), Toast.LENGTH_SHORT).show();
            m.printStackTrace();
            return false;
        }
    }
}
