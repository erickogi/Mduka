package com.erickogi14gmail.mduka.Prefrence;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Eric on 10/20/2017.
 */

public class Prefrence {
    // Shared preferences file name
    private static final String PREF_NAME = "com.erickogi14@gmail.mduka";
    // All Shared Preferences Keys
    private static final String KEY_IS_WAITING_FOR_SMS = "IsWaitingForSms";
    private static final String KEY_MOBILE_NUMBER = "mobile_number";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "shopemail";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_SHOP = "zone";
    private static final String KEY_IMAGE = "image";

    private static final String KEY_SHOPPHONE = "shopphone";
    private static final String KEY_SHOPEMAIL = "shopemail";
    private static final String KEY_SHOPNAME = "shopname";


    private static final String KEY_PROFILE_SET = "profile_set";
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    public Prefrence(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIsWaitingForSms(boolean isWaiting) {
        editor.putBoolean(KEY_IS_WAITING_FOR_SMS, isWaiting);
        editor.commit();
    }

    public boolean isWaitingForSms() {
        return pref.getBoolean(KEY_IS_WAITING_FOR_SMS, false);
    }

    public String getMobileNumber() {
        return pref.getString(KEY_MOBILE_NUMBER, null);
    }

    public void setMobileNumber(String mobileNumber) {
        editor.putString(KEY_MOBILE_NUMBER, mobileNumber);
        editor.commit();
    }

    public void createLogin(String name, String email, String mobile) {
        editor.putString(KEY_NAME, name);
        // editor.putString(KEY_SHOPEMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("name", pref.getString(KEY_NAME, ""));

        profile.put("mobile", pref.getString(KEY_MOBILE, ""));

        profile.put("shopname", pref.getString(KEY_SHOPNAME, ""));
        profile.put("shopemail", pref.getString(KEY_SHOPEMAIL, ""));
        profile.put("shopphone", pref.getString(KEY_SHOPPHONE, ""));

        return profile;
    }

    public void storeImg(String imagePath) {
        editor.putString(KEY_IMAGE, imagePath);
        editor.commit();
    }

    public String getImg() {
        return pref.getString(KEY_IMAGE, "null");
    }


    public void updateUser(String name, String shopname, String shopemail, String shopphone) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_SHOPNAME, shopname);

        editor.putString(KEY_SHOPEMAIL, shopemail);
        editor.putString(KEY_SHOPPHONE, shopphone);

        editor.putBoolean(KEY_PROFILE_SET, true);

        editor.commit();
    }

    public boolean isProfileSet() {
        return pref.getBoolean(KEY_PROFILE_SET, false);
    }
}
