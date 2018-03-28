package com.erickogi14gmail.mduka;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.erickogi14gmail.mduka.Login.Login;
import com.erickogi14gmail.mduka.Prefrence.Prefrence;

public class SplashScreen extends AppCompatActivity {
    private static int spalsh_time_out = 500;
    boolean debug = true;
    private Prefrence prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        prefManager = new Prefrence(SplashScreen.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!debug) {
                    if (prefManager.isLoggedIn()) {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        finish();
                    }//else if(prefManager.isWaitingForSms()){

                    //}
                    else {
                        Intent intent = new Intent(SplashScreen.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        finish();

                    }
                } else {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    finish();
                }
            }

        },spalsh_time_out);
    }
}
