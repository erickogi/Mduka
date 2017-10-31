package com.erickogi14gmail.mduka.Login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.erickogi14gmail.mduka.MainActivity;
import com.erickogi14gmail.mduka.R;

public class Login extends AppCompatActivity {
    SignUpFragment signUpFragment;
    private Fragment fragment;
    private BroadcastReceiver codeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("closeoperation", "truecode received");
            if (intent.getAction().equals("com.mduka.codereceived")) {
                try {
                    // Intent hhtpIntent = new Intent(LoginActivity.this, HttpService.class);
                    // hhtpIntent.putExtra("otp", intent.getExtras().getString("code"));

                    //startService(hhtpIntent);

                    signUpFragment = (SignUpFragment) getSupportFragmentManager().findFragmentByTag("fragmentsignup");
                    signUpFragment.setText(intent.getExtras().getString("code"));


                    //signUpFragment.setText("23432");
                } catch (NullPointerException nm) {
                    nm.printStackTrace();
                }

            }

        }
    };
    private BroadcastReceiver closeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("closeoperation", "close received");

            if (intent.getAction().equals("com.mduka.close")) {
                startActivity(new Intent(Login.this, MainActivity.class));
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        registerReceiver(codeReceiver, new IntentFilter("com.mduka.codereceived"));
        registerReceiver(closeReceiver, new IntentFilter("com.mduka.close"));


        fragment = new WelcomFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment, "fragmentWelcome").commit();


    }

    public void newloginBtnPressed(View view) {
        popOutFragments();
        fragment = new Login_Fragmet();
        setUpView();
    }

    public void newAccountBtnPressed(View view) {
        popOutFragments();
        fragment = new SignUpFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment, "fragmentsignup").commit();

        signUpFragment = (SignUpFragment) getSupportFragmentManager().findFragmentByTag("fragmentsignup");


    }

    void setUpView() {
        if (fragment != null) {
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment)
                    .addToBackStack(null).commit();
        }

    }

    void popOutFragments() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(closeReceiver);

        unregisterReceiver(codeReceiver);

    }
}
