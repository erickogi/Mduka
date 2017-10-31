package com.erickogi14gmail.mduka.Items;

import android.view.View;

/**
 * Created by Eric on 10/19/2017.
 */

public interface itemsClickListener {
    void onBtnMoreClicked(int position, View view);

    void onFavIconClicked(int position);

    void onBtnDeleteClicked(int position);

    void onBtnEditClicked(int position);
}
