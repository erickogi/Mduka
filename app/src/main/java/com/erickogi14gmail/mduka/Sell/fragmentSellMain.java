package com.erickogi14gmail.mduka.Sell;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.erickogi14gmail.mduka.Controller;
import com.erickogi14gmail.mduka.Db.DbOperations;
import com.erickogi14gmail.mduka.R;
import com.roughike.bottombar.BottomBar;

import java.util.ArrayList;
import java.util.List;

import static com.erickogi14gmail.mduka.R.drawable.ic_shopping_cart_white_24dp;

/**
 * Created by Eric on 8/29/2017.
 */

public class fragmentSellMain extends Fragment {
    static boolean isListView = true;
    private DbOperations dbOperations;
    private fragment_sell f;
    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton imageButton;
    private CardView cardview;
    private BottomBar bottomNavigationView;
    private Fragment fragment = null;
    private TextView textView;
    private SearchView search;
    private FloatingActionButton fab;
    private Controller controller;

    public void setCharge() {
        textView.setText("Cash  " + String.valueOf(dbOperations.sumOfTotalPricesOfItemsInCart()));
    }

    @Override
    public void onResume() {
        super.onResume();
        f = (fragment_sell) getChildFragmentManager().findFragmentByTag(getFragmentTag(0));
        dbOperations = new DbOperations(getContext());
        controller = new Controller();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public String getSearchText() {
        try {
            return search.getQuery().toString();
        } catch (Exception n) {
            return "";
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sell_main, container, false);
        dbOperations = new DbOperations(getContext());
        controller = new Controller();
        search = (SearchView) view.findViewById(R.id.search_bar);
        imageButton = (ImageButton) view.findViewById(R.id.button_list);
        textView = (TextView) view.findViewById(R.id.textView_charge);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        // fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        // fab.setImageResource(ic_shopping_cart_white_24dp);
        fab = controller.fab(getActivity(), true, ic_shopping_cart_white_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popOutFragments();
                fragment = new fragment_cart();
                setUpView();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popOutFragments();
                fragment = new fragment_cart();
                setUpView();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f = (fragment_sell) getChildFragmentManager().findFragmentByTag(getFragmentTag(0));
                if (isListView) {
                    isListView = false;
                    f.setView(getActivity(), getSearchText());

                    imageButton.setImageResource(R.drawable.ic_apps_black_24dp);

                } else {
                    isListView = true;
                    f.setView(getActivity(), getSearchText());

                    imageButton.setImageResource(R.drawable.ic_list_black_24dp);
                }

            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            SearchManager manager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);

            SearchView search = (SearchView) view.findViewById(R.id.search_bar);

            search.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


                @Override
                public boolean onQueryTextSubmit(String query) {


                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    try {
                        f = (fragment_sell) getChildFragmentManager().findFragmentByTag(getFragmentTag(0));
                        f.updateList(newText);
                    } catch (Exception m) {

                    }
                    return false;
                }
            });

        }


        return view;
    }

    public void hideViews() {
        fab.hide();
    }

    public void showViews() {
        fab.show();
    }

    private String getFragmentTag(int pos) {
        return "android:switcher:" + R.id.viewpager + ":" + pos;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new fragment_sell(), "lo");
        adapter.addFragment(new fragment_favorites(), "li");


        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(adapter);

    }

    private void setupTabIcons() {

        tabLayout.getTabAt(0).setText("Items");
        tabLayout.getTabAt(1).setText("Favorites");


    }

    void setUpView() {
        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment)
                    .addToBackStack(null).commit();
        }

    }

    void popOutFragments() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

}
