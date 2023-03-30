package ru.shprot.sudokumobdevkz.model.game.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

public class SquareCardLayout extends MyCardLayout {

    private final int length;


    public SquareCardLayout(Context context) {
        super(context);
        length = calculateSize();
    }

    public SquareCardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        length = calculateSize();
    }

    public SquareCardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        length = calculateSize();
    }

    private int calculateSize() {
        int size = getScreenWidth();
        while (true) {
            float temp = size % 9;
            if (temp == 0) return size;
            else size--;
        }
    }


    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        setMeasuredDimension(length, length);
    }
}
