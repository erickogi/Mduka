package com.erickogi14gmail.mduka;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.erickogi14gmail.mduka.Db.DbOperations;

import java.util.ArrayList;

/**
 * Created by Eric on 9/2/2017.
 */

public class Controller {
    private DbOperations dbOperations;

    public FloatingActionButton fab(Activity activity, boolean show, int image) {

        final FloatingActionButton fab = (FloatingActionButton) activity.findViewById(R.id.fab);

        if (show) {
            fab.setImageResource(image);
            fab.show();
        } else {
            fab.setImageResource(image);
            fab.hide();
        }
        return fab;
    }


    public Spinner setUpSpinner(Context context, int spinnerId, View view, int pos) {
        ArrayList<String> categories = new ArrayList<>();
        dbOperations = new DbOperations(context);
        Spinner spinnerCategory = (Spinner) view.findViewById(spinnerId);
        try {
            categories = dbOperations.getAllCategories1();
            categories.add("All");

            ArrayAdapter<String> simpleAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, categories);
            spinnerCategory.setAdapter(simpleAdapter);
            int size = categories.size();
            spinnerCategory.setSelection(size - 1);
        } catch (NullPointerException m) {
            m.printStackTrace();
            ArrayList<String> c = new ArrayList<>();
            c.add("All");
            ArrayAdapter<String> simpleAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, c);
            spinnerCategory.setAdapter(simpleAdapter);
            spinnerCategory.setSelection(pos);

        }
        return spinnerCategory;

    }

    public void hideViews(FloatingActionButton fab) {
        fab.hide();
    }

    public void showViews(FloatingActionButton fab) {
        fab.show();
    }

}
