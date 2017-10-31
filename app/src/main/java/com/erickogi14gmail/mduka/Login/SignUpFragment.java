package com.erickogi14gmail.mduka.Login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.erickogi14gmail.mduka.Configs;
import com.erickogi14gmail.mduka.Firebase.SharedPrefManager;
import com.erickogi14gmail.mduka.MyApplication;
import com.erickogi14gmail.mduka.Prefrence.Prefrence;
import com.erickogi14gmail.mduka.R;
import com.erickogi14gmail.mduka.service.HttpService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 9/12/2017.
 */


public class SignUpFragment extends Fragment implements View.OnClickListener {
    public static Fragment fragment = null;
    private View view;
    private TextInputEditText mFirstName, mLastName, mEmail, mMobile;
    private Button btnRegister;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private Prefrence pref;
    private RelativeLayout relativeLayoutOtp, relativeLayoutSignup;

    private EditText edtCode;
    private Button btnSignup, btnVerify, btnResend;


    private Dialog dialoge;
    private ImageView imagback;

    private static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sign_up_fragment, container, false);
        pref = new Prefrence(getContext());
        mFirstName = (TextInputEditText) view.findViewById(R.id.txt_firstname);
        mLastName = (TextInputEditText) view.findViewById(R.id.txt_lastname);
        // mEmail = (TextInputEditText) view.findViewById(R.id.txt_emailAdress);
        mMobile = (TextInputEditText) view.findViewById(R.id.txt_mobile);
        relativeLayoutOtp = (RelativeLayout) view.findViewById(R.id.otp);
        relativeLayoutSignup = (RelativeLayout) view.findViewById(R.id.signup);
        edtCode = (EditText) view.findViewById(R.id.edt_code);


        // dialoge = new Dialog(getContext());
        // dialoge.setContentView(R.layout.dialog_otp);
        // dialoge.setCancelable(false);
        // dialoge.setCanceledOnTouchOutside(false);
        btnVerify = (Button) view.findViewById(R.id.btn_verify);
        btnResend = (Button) view.findViewById(R.id.btn_resend);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setText(edtCode.getText().toString());
            }
        });
        btnResend.setOnClickListener(this);


        btnRegister = (Button) view.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);

        //progressBar = (ProgressBar)view. findViewById(R.id.progressBar);
        imagback = (ImageView) view.findViewById(R.id.img_back);

        imagback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayoutOtp.setVisibility(View.GONE);
                relativeLayoutSignup.setVisibility(View.VISIBLE);
            }
        });


        return view;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_register || v.getId() == R.id.btn_resend) {

            if (isFilled(mFirstName) && isFilled(mLastName) && isFilled(mMobile)) {

                if (validateFields()) {

                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Registering you in....");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    String name = mFirstName.getText().toString() + "  " + mLastName.getText().toString();
                    // String email = mEmail.getText().toString();
                    String mobile = mMobile.getText().toString();

                    relativeLayoutOtp.setVisibility(View.VISIBLE);
                    relativeLayoutSignup.setVisibility(View.GONE);
                    String token = SharedPrefManager.getInstance(getContext()).getDeviceToken();

                    requestForSMS(name, mobile, token);

                }
            } else {

            }


        }
    }

    private boolean validateFields() {
        if (!isValidPhoneNumber(mMobile.getText().toString())) {
            mMobile.setError("Invalid Number");
            return false;
        } else {
            return true;
        }


    }

    private boolean isFilled(TextInputEditText textInputEditText) {
        if (textInputEditText.getText().toString().equals("")) {
            textInputEditText.setError("Required");
            return false;
        } else {
            return true;
        }

    }


    /**
     * Method initiates the SMS request on the server
     *
     * @param name   user name
     * @param email  user email address
     * @param mobile user valid mobile number
     */
    private void requestForSMS(final String name, final String mobile, final String token) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Configs.REGISTER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Log.d("response", response.toString());
                progressDialog.dismiss();
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
                        // boolean flag saying device is waiting for sms

                        pref.setIsWaitingForSms(true);
                        // progressDialog.dismiss();
                        // moving the screen to next pager item i.e otp screen
                        //dialoge.show();


                        //Toast.makeText(getActivity(), "sent", Toast.LENGTH_SHORT).show();

                    } else {
                        String message = responseObj.getString("message");
                        //relativeLayoutSignup.setVisibility(View.VISIBLE);
                        //relativeLayoutOtp.setVisibility(View.GONE);
                        //    Toast.makeText(getContext(),
                        //          "Error: " + message,
                        //          Toast.LENGTH_LONG).show();
                        // dialoge.dismiss();

                    }

                    // hiding the progress bar
                    progressDialog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("ERRRRR", e.toString());
                    Toast.makeText(getContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    // dialoge.dismiss();
                    //relativeLayoutSignup.setVisibility(View.VISIBLE);
                    // relativeLayoutOtp.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //relativeLayoutSignup.setVisibility(View.VISIBLE);
                //relativeLayoutOtp.setVisibility(View.GONE);
                Log.e("error", "Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                // dialoge.dismiss();
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
                params.put("name", name);
                // params.put("email", email);
                params.put("mobile", mobile);
                params.put("token", token);

                Log.e("posting params", "Posting params: " + params.toString());

                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    public void setText(String code) {
        Log.d("Closeoperation", "true333");
        try {
            try {
                edtCode.setText(code);
                btnVerify.setEnabled(true);
                if (dialoge.isShowing()) {
                    btnVerify.setEnabled(true);
                    edtCode.setText(code);

                }
            } catch (Exception nm) {
                nm.printStackTrace();
            }

            Intent hhtpIntent = new Intent(getContext(), HttpService.class);
            hhtpIntent.putExtra("otp", code);

            getActivity().startService(hhtpIntent);
        } catch (Exception nm) {
            nm.printStackTrace();
        }


    }


    void setUpView() {
        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment)
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