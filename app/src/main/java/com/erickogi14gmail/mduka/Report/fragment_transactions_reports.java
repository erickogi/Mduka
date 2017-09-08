package com.erickogi14gmail.mduka.Report;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.erickogi14gmail.mduka.Controller;
import com.erickogi14gmail.mduka.Db.DbOperations;
import com.erickogi14gmail.mduka.Db.StockItemsPojo;
import com.erickogi14gmail.mduka.Db.TransactionsPojo;
import com.erickogi14gmail.mduka.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Eric on 9/7/2017.
 */

public class fragment_transactions_reports extends Fragment {
    private View view;
    private FloatingActionButton fab;
    private Controller controller;
    private TextView txtNoOfItems, txtTotalSellingPrice, txtTotalBuyingPrice, txtExpectedProfit, txtPercentageProfit;
    private DbOperations dbOperations;
    private ArrayList<StockItemsPojo> stockItemsPojos;
    private CardView cardView;
    private RelativeLayout relativeLayout;
    private TransactionsPojo transactionsPojo;
    private ArrayList<TransactionsPojo> transactionsPojos;
    private Button button;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reports_main, container, false);
        controller = new Controller();
        fab = controller.fab(getActivity(), false, R.drawable.ic_apps_black_24dp);
        dbOperations = new DbOperations(getContext());


        txtNoOfItems = (TextView) view.findViewById(R.id.txt_items_quantity);
        txtTotalSellingPrice = (TextView) view.findViewById(R.id.txt_items_total_sp);
        txtTotalBuyingPrice = (TextView) view.findViewById(R.id.txt_items_total_bp);
        txtExpectedProfit = (TextView) view.findViewById(R.id.txt_expected_profit);
        txtPercentageProfit = (TextView) view.findViewById(R.id.txt_percentage_profit);


        txtNoOfItems.setText(String.valueOf(dbOperations.getItemsCount()));


        stockItemsPojos = dbOperations.getAllItems1("", "");
        double totalSp = 0.0;
        double totalBp = 0.0;

        for (int a = 0; a < stockItemsPojos.size(); a++) {
            totalSp = totalSp + (Double.valueOf(stockItemsPojos.get(a).getItem_selling_price()));
            totalBp = totalBp + (Double.valueOf(stockItemsPojos.get(a).getItem_buying_price()));

        }


        txtTotalSellingPrice.setText(String.valueOf(totalSp));
        txtTotalBuyingPrice.setText(String.valueOf(totalBp));

        double profit = totalSp - totalBp;

        txtExpectedProfit.setText(String.valueOf(profit));

        double percentageProfit = (profit / totalBp) * 100;

        txtPercentageProfit.setText(String.valueOf(percentageProfit));
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relative);

        //  LineChart chart=new LineChart(getContext());


        transactionsPojos = dbOperations.getAllTransactions("", "");
        List<BarEntry> entries = new ArrayList<>();
        for (TransactionsPojo t : transactionsPojos) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd ,yyyy  HH : mm : ss");
            //String date =t.getTransaction_date();
            float a = 0;
            try {
                Date date = simpleDateFormat.parse(t.getTransaction_date());
                a = (float) date.getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            entries.add(new BarEntry(Float.valueOf(t.getTransaction_quantity()), a));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Sales");

        ArrayList<String> labels = new ArrayList<>();
        for (int a = 0; a < transactionsPojos.size(); a++) {
            labels.add(transactionsPojos.get(a).getTransaction_date());
        }

        BarChart chart = new BarChart(getContext());
        relativeLayout.addView(chart);

        BarData data = new BarData(dataSet);
        chart.setData(data);

        // dataSet.setColor();
        // LineData lineData=new LineData(dataSet);
        // chart.setData(lineData);
        chart.invalidate();


        return view;
        //  return super.onCreateView(inflater, container, savedInstanceState);
    }
}
