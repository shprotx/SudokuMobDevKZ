package ru.shprot.sudokumobdevkz.model.game.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import androidx.cardview.widget.CardView;

public abstract class MyCardLayout extends CardView {

    public MyCardLayout(Context context) {
        super(context);
    }

    public MyCardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    protected int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }


}
