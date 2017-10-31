package com.erickogi14gmail.mduka.Transactions;

import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.erickogi14gmail.mduka.Controller;
import com.erickogi14gmail.mduka.Db.DbOperations;
import com.erickogi14gmail.mduka.Db.TransactionsPojo;
import com.erickogi14gmail.mduka.R;
import com.erickogi14gmail.mduka.utills.RecyclerTouchListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.R.drawable.ic_input_add;


/**
 * Created by Eric on 9/6/2017.
 */

public class fragment_transactions extends Fragment {
    private View view;
    private FloatingActionButton fab;
    private Controller controller;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private DbOperations dbOperations;
    private ArrayList<TransactionsPojo> transactionsPojos;
    private TransactionListAdapter transactionListAdapter;
    private LinearLayout linearLayoutEmpty;
    private SearchView search;
    private Button btnChooseDate;
    private int mYear, mMonth, mDay, mHour, mMinute, cl;
    private TextView textViewDetails;

    public void setView(boolean isListView, Context context) {

        dbOperations = new DbOperations(context);
        transactionsPojos = dbOperations.getAllTransactions(getSearchText(), getDateText());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        linearLayoutEmpty = (LinearLayout) view.findViewById(R.id.empty_layout);
        transactionListAdapter = new TransactionListAdapter(getContext(), transactionsPojos);
        transactionListAdapter.notifyDataSetChanged();
        if (isListView) {
            mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        } else {
            mStaggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        }
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(transactionListAdapter);
        textViewDetails = (TextView) view.findViewById(R.id.details);
        double totals[] = dbOperations.getSumsOfTransactions(getSearchText(), getDateText());
        textViewDetails.setText("Items : " + totals[2] + " , Bp : " + String.valueOf(totals[0]) + " , Sp : " + String.valueOf(totals[1]) + " ,P : " + String.valueOf(totals[1] - totals[0]));

        if (transactionListAdapter.getItemCount() > 0) {
            linearLayoutEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

        } else {
            linearLayoutEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }


    }

    public String getSearchText() {
        try {
            return search.getQuery().toString();
        } catch (Exception n) {
            return "";
        }

    }

    public String getDateText() {
        try {
            if (btnChooseDate.getText().toString().equals("Choose Date")) {
                return "";
            } else {
                return btnChooseDate.getText().toString();
            }
        } catch (Exception n) {
            return "";
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transactions, container, false);
        controller = new Controller();
        fab = controller.fab(getActivity(), false, ic_input_add);
        btnChooseDate = (Button) view.findViewById(R.id.chooseDate);
        btnChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        setView(true, getContext());
        dbOperations = new DbOperations(getContext());
        search = (SearchView) view.findViewById(R.id.search_bar);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {


            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), Transaction_details.class);
                intent.putExtra("data", transactionsPojos);
                intent.putExtra("dataPosition", position);
                startActivity(intent);


                // Toast.makeText(getContext(), "Items ---"+transactionsPojos.get(position).getTransaction_items()+
                //        "\nBp ==  "+transactionsPojos.get(position).getTransaction_total_bp(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));
        SearchManager manager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);

        final SearchView search = (SearchView) view.findViewById(R.id.search_bar);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setIconified(false);
            }
        });
        search.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                transactionListAdapter.updateList(dbOperations.getAllTransactions(getSearchText(), getDateText()));
                double totals[] = dbOperations.getSumsOfTransactions(getSearchText(), getDateText());
                textViewDetails.setText(" Items : " + totals[2] + " , Bp : " + String.valueOf(totals[0]) + " , Sp : " + String.valueOf(totals[1]) + " , P : " + String.valueOf(totals[1] - totals[0]));
                return false;
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setView(true, getContext());
        fab = controller.fab(getActivity(), false, ic_input_add);

    }

    public void datePicker() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),

                new DatePickerDialog.OnDateSetListener() {


                    @Override

                    public void onDateSet(DatePicker view, int year,

                                          int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        //Date date = new Date();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd ,yyyy");
                        String date = simpleDateFormat.format(calendar.getTime());


                        //  btnChooseDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        btnChooseDate.setText(date);
                        transactionListAdapter.updateList(dbOperations.getAllTransactions(getSearchText(), date));
                        double totals[] = dbOperations.getSumsOfTransactions(getSearchText(), getDateText());
                        textViewDetails.setText(" Items : " + totals[2] + " , Bp : " + String.valueOf(totals[0]) + " , Sp : " + String.valueOf(totals[1]) + " ,P : " + String.valueOf(totals[1] - totals[0]));


                    }

                }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    public void timePicker() {

        // Get Current Time

        final Calendar c = Calendar.getInstance();

        mHour = c.get(Calendar.HOUR_OF_DAY);

        mMinute = c.get(Calendar.MINUTE);


        // Launch Time Picker Dialog

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),

                new TimePickerDialog.OnTimeSetListener() {


                    @Override

                    public void onTimeSet(TimePicker view, int hourOfDay,

                                          int minute) {
                        String AM_PM;
                        if (hourOfDay < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }


                        // textViewTime.setText(hourOfDay + ":" +  minute+"  "+AM_PM);

                    }

                }, mHour, mMinute, false);

        timePickerDialog.show();
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        setView(true, getContext());
//
//
//    }

}
