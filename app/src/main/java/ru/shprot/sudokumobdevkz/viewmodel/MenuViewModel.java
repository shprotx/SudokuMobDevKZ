package ru.shprot.sudokumobdevkz.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.shprot.sudokumobdevkz.model.Repository;
import ru.shprot.sudokumobdevkz.model.game.GameState;
import ru.shprot.sudokumobdevkz.model.game.Square;
import ru.shprot.sudokumobdevkz.model.game.generator.Generator;

public class MenuViewModel extends AndroidViewModel {

    private final Repository repository;
    public ArrayList<Square> items;
    public GameState gameState = new GameState();




    public MenuViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }



    public ArrayList<Square> generateGrid(int diff) {
        Generator generator = new Generator();
        return generator.generate(diff);
    }


    public GameState calculateNumbers(int diff) {
        GameState gameState = new GameState();
        gameState.setDifficulty(diff);
        for (Square item : items)
            if (item.isVisible()) {
                gameState.increment(item.getValue());
                gameState.emptySquareCounter--;
            }
        return gameState;
    }

    public LiveData<GameState> getGameState() {
        return repository.getGameState();
    }

    public void getItems() {
        repository.getItems().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Square>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<Square> squares) {
                        items = (ArrayList<Square>) squares;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
