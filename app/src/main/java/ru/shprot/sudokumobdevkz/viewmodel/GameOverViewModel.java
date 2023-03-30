package ru.shprot.sudokumobdevkz.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class GameOverViewModel extends AndroidViewModel {

    private int[] items;
    private String[] info;
    private int gridSize;
    private int cardSize;
    private boolean win;

    public GameOverViewModel(@NonNull Application application) {
        super(application);
    }


    public int[] getItems() {
        return items;
    }

    public void setItems(int[] items) {
        this.items = items;
    }

    public String[] getInfo() {
        return info;
    }

    public void setInfo(String[] info) {
        this.info = info;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
        this.cardSize = gridSize / 9 -2;
    }

    public int getCardSize() {
        return cardSize;
    }

    public void setCardSize(int cardSize) {
        this.cardSize = cardSize;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }
}
