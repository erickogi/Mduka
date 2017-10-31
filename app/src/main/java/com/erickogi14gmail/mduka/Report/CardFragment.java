package com.erickogi14gmail.mduka.Report;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.erickogi14gmail.mduka.Controller;
import com.erickogi14gmail.mduka.Db.DbOperations;
import com.erickogi14gmail.mduka.Db.TransactionsPojo;
import com.erickogi14gmail.mduka.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class CardFragment extends Fragment {
    final String[] quaters = new String[]{"1", "ER", "ERT", "L", "V", "V", "K", "k", "jk", "K", "k", "jk", "1", "ER", "ERT", "L", "V", "V", "K", "k", "jk", "K", "k", "jk"};
    RelativeLayout relativeLayout;
    TextView title;
    ImageView imageView;
    Date dto, dfro;
    BarChart chart;
    LineChart lchart;
    ArrayList<Entry> entries;
    ArrayList<BarEntry> BARENTRY;
    ArrayList<String> BarEntryLabels;
    BarDataSet Bardataset;
    BarData BARDATA;
    LineDataSet lineDataSet;
    LineData lineData;
    View view;
    private CardView cardView;
    private ArrayList<TransactionsPojo> transactionsPojos;
    private Button buttonFrom, buttonTo;
    private int mYear, mMonth, mDay, mHour, mMinute, cl;
    private Controller controller;
    private TextView txtNoOfItems, txtTotalSellingPrice, txtTotalBuyingPrice, txtExpectedProfit, txtPercentageProfit;
    private DbOperations dbOperations;

    public static Fragment getInstance(int position) {
        CardFragment f = new CardFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        f.setArguments(args);

        return f;
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.item_viewpager, container, false);

        cardView = (CardView) view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);
//        imageView=(ImageView)view.findViewById(R.id.item_image);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//               // datePicker();
//            }
//        });

        title = (TextView) view.findViewById(R.id.title);


        controller = new Controller();
        // fab = controller.fab(getActivity(), false, R.drawable.ic_apps_black_24dp);
        dbOperations = new DbOperations(getContext());
        relativeLayout = (RelativeLayout) view.findViewById(R.id.chart);


        switch (getArguments().getInt("position")) {
            case 0:
                Calendar calendar = Calendar.getInstance();
                java.sql.Date m = new java.sql.Date(calendar.getTimeInMillis());
                title.setText("Today's Chart ");
                filter(dbOperations.getAllTransactionsInToday("", m), null, null);

                break;
            case 1:
                title.setText("This Weeks's Chart ");
                filter(dbOperations.getAllTransactionsInWeek(""), null, null);
                break;
            case 2:
                title.setText("This Month's Chart ");
                filter(dbOperations.getAllTransactionsInMonth(""), null, null);
        }


        return view;
    }

    public CardView getCardView() {
        return cardView;
    }

    public Date datePicker() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        final Date[] F = {null};
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),

                new DatePickerDialog.OnDateSetListener() {


                    @Override

                    public void onDateSet(DatePicker view, int year,

                                          int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        //Date date = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        //  Date date=new Date(calendar.getTime());


                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd ,yyyy");
                        // if(btn==buttonTo) {
                        // String date = dateFormat.format(calendar.getTime());
                        dto = new java.sql.Date(calendar.getTimeInMillis());

                        transactionsPojos = dbOperations.getAllTransactionsInRange("", dto, dto);//
                        filter(transactionsPojos, dfro, dto);
                        //Toast.makeText(getContext(), ""+transactionsPojos.get(0).getTransaction_items(), Toast.LENGTH_SHORT).show();
                        ///    }else {
                        ///     dfro = new java.sql.Date(calendar.getTimeInMillis());

                        //transactionsPojos = dbOperations.getAllTransactionsInRange("", dfro, dto);//
                        //     buttonTo.setEnabled(true);
                        ///  }

                        // btn.setText(simpleDateFormat.format(calendar.getTime()));


                    }

                }, mYear, mMonth, mDay);

        datePickerDialog.show();
        return F[0];
    }

    public long het(Date now, Date thn) {
        long day = 0;
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(thn);
        calendar2.setTime(now);
        long milsecs1 = calendar1.getTimeInMillis();
        long milsecs2 = calendar2.getTimeInMillis();
        long diff = milsecs2 - milsecs1;
        long dsecs = diff / 1000;
        long dminutes = diff / (60 * 1000);
        long dhours = diff / (60 * 60 * 1000);
        long ddays = diff / (24 * 60 * 60 * 1000);

        //System.out.println("Your Day Difference="+ddays);
        return ddays;
    }

    public void filter(final ArrayList<TransactionsPojo> tk, Date f, Date t) throws NullPointerException {

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Toast.makeText(getContext(), "" + value + "   kl", Toast.LENGTH_SHORT).show();


                ArrayList<String> flal = labels(tk);
                String lals[] = new String[flal.size()];
                lals = flal.toArray(lals);
                if (lals.length > (int) value) {
                    return lals[(int) value];
                } else {
                    axis.setValueFormatter(null);
                    return "";
                }


//                try {
//                    return lals[(int) value];
//                }catch (ArrayIndexOutOfBoundsException m){
//                    return lals[(int)value];
//                }
            }
//        @Override
//        public  int getDecimalDigits(){
//            return 0;
//        }

        };
//        chart = (BarChart)view. findViewById(R.id.chart1);
//
//
//
//        XAxis X=chart.getXAxis();
//        X.setGranularity(1f);
//        X.setValueFormatter(formatter);
//
//        BARENTRY = new ArrayList<>();
//
//
//        AddValuesToBARENTRY();
//
//        AddValuesToBarEntryLabels();
//
//        Bardataset = new BarDataSet(BARENTRY, "Projects");
//
//        BARDATA = new BarData( Bardataset);
//
//        Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
//
//
//        chart.setData(BARDATA);
//
//        chart.animateY(3000);
//
        relativeLayout.removeAllViewsInLayout();
        //lchart=(LineChart)view.findViewById(R.id.chart1);
        lchart = new LineChart(getContext());
        //XAxis X=lchart.getXAxis();
        //X.setLabelRotationAngle(-45);
        // X.setGranularityEnabled(true);
        // X.setGranularity(1f);
        // X.setValueFormatter(null);
        //X.setValueFormatter(formatter);
        entries = new ArrayList<>();
        AddValuesToBARENTRY(tk);
        lineDataSet = new LineDataSet(entries, "");
        lineData = new LineData(lineDataSet);
        lineDataSet.setColor(ColorTemplate.COLORFUL_COLORS[1]);
        //chart.setDescription("");
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawFilled(true);

        lchart.setData(lineData);
        lchart.animateY(1000);


        lchart.setFitsSystemWindows(true);
        lchart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        relativeLayout.addView(lchart);


    }

    public void AddValuesToBARENTRY(ArrayList<TransactionsPojo> transactionsPojos) throws NullPointerException {
        try {
            for (int a = 0; a < transactionsPojos.size(); a++) {
                double m = Double.valueOf(transactionsPojos.get(a).getTransaction_total_sp());
                entries.add(new BarEntry(2f * a, (int) m));

            }
        } catch (Exception m) {
            entries.add(new BarEntry(2f, 0));
        }

    }

    public void AddValuesToBarEntryLabels() {

        BarEntryLabels.add("January");
        BarEntryLabels.add("February");
        BarEntryLabels.add("March");
        BarEntryLabels.add("April");
        BarEntryLabels.add("May");
        BarEntryLabels.add("June");

    }

    public ArrayList<String> labels(ArrayList<TransactionsPojo> transactionsPojos) {
        ArrayList<String> labels = new ArrayList<>();
        labels.add("1");
        try {
            for (int a = 0; a < transactionsPojos.size(); a++) {
                labels.add(transactionsPojos.get(a).getTransaction_date());
            }
        } catch (NullPointerException m) {

        }
        labels.add("nm");
        return labels;
    }
}
