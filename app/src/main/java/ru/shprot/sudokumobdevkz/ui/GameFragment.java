package ru.shprot.sudokumobdevkz.ui;

import static android.content.ContentValues.TAG;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.AD_AFTER_GAME;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.DIALOG_FRAGMENT_RESULT;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.DIALOG_PAUSE;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.KEY_DIFF;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.KEY_GAME_RESULT;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.KEY_GAME_STATE;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.KEY_GRID;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.KEY_ITEMS;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.KEY_WIN;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.color.MaterialColors;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.shprot.sudokumobdevkz.R;
import ru.shprot.sudokumobdevkz.databinding.FragmentGameBinding;
import ru.shprot.sudokumobdevkz.model.game.Square;
import ru.shprot.sudokumobdevkz.model.game.utils.SquareAdapter;
import ru.shprot.sudokumobdevkz.viewmodel.GameViewModel;

public class GameFragment extends Fragment implements MenuProvider, FragmentResultListener {

    FragmentGameBinding binding;
    GameViewModel viewModel;

    private InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().addMenuProvider(this, this, Lifecycle.State.STARTED);
        OnBackPressedDispatcher back = getActivity().getOnBackPressedDispatcher();
        back.addCallback(this, new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {

            }
        });
        viewModel = new ViewModelProvider(this).get(GameViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGameBinding.bind(inflater.inflate(R.layout.fragment_game, container, false));
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getGrid();
        initBase();
        initRecyclerView();
        initButtons();
        addListenersToButtons();
        initAd();

    }

    private void initAd() {
        MobileAds.initialize(getContext());
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(getContext(), AD_AFTER_GAME, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        Log.i(TAG, "onAdLoaded");
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        mInterstitialAd = null;

                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        mInterstitialAd = null;

                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {

                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
    }

    private void showAd() {
        if (mInterstitialAd != null)
            mInterstitialAd.show(getActivity());
    }


    private void startTimer() {
        viewModel.timer = Flowable.interval(0, 1, TimeUnit.SECONDS)
                .map(aLong -> viewModel.getTimerString(aLong))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> binding.timerTextView.setText(result));
    }

    private void getGrid() {
        if (!viewModel.isGameRestarted) {
            Bundle bundle = getArguments();
            assert bundle != null;
            viewModel.items = bundle.getParcelableArrayList(KEY_ITEMS);
            viewModel.gameState = bundle.getParcelable(KEY_GAME_STATE);
            viewModel.gameState.setDifficulty(bundle.getInt(KEY_DIFF));
        }
    }

    private void initBase() {
        viewModel.getPreferences();
        binding.diffTextView.setText(getDiffTitle(viewModel.gameState.getDifficulty()));
        if (viewModel.possibleMistakes > 3)
            binding.mistakeTextView.setVisibility(View.INVISIBLE);
        else
            binding.mistakeTextView.setText(getString(R.string.mistakes) + " " + viewModel.gameState.getErrorCounter() + getString(R.string.mis_count));
        viewModel.calculateTimerStartValue(viewModel.gameState.getTimer());
        viewModel.gameState.setGameFinished(false);
        viewModel.gameState.setGamePaused(false);
        viewModel.getStatisticFromDb(viewModel.gameState.getDifficulty());
        binding.iconButtonDraft.setEnabled(viewModel.gameState.isDraftEnabled());
        binding.iconButtonLight.setEnabled(viewModel.gameState.isHintEnabled());
        if ((viewModel.possibleMistakes == 3) && (viewModel.gameState.getErrorCounter() > 2))
            gameOver(false);
    }
    private void initRecyclerView() {
        int[] colors = new int[8];
        colors[0] = attrColor(R.attr.areaColorTheme);
        colors[1] = attrColor(R.attr.otherCellsColorTheme);
        colors[2] = attrColor(R.attr.itemBumberColorTheme);
        colors[3] = attrColor(R.attr.theSameNumberColorTheme);
        colors[4] = attrColor(R.attr.selectedColorTheme);
        colors[5] = attrColor(R.attr.draftTheSameTheme);
        colors[6] = attrColor(R.attr.draftNumbersColorTheme);
        colors[7] = attrColor(R.attr.solvedNumberColorTheme);
        //
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 9,
                RecyclerView.VERTICAL, false);
        binding.recyclerView.setLayoutManager(gridLayoutManager);
        viewModel.adapter = new SquareAdapter(viewModel.items, binding.recyclerView, colors);
        binding.recyclerView.setAdapter(viewModel.adapter);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.suppressLayout(true);
        setRecyclerViewBorders();
    }

    private void setRecyclerViewBorders() {
        int measuredwidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        while (true) {
            float temp = measuredwidth % 9;
            if (temp == 0) break;
            else measuredwidth--;
        }
        ViewGroup.LayoutParams r = binding.rLay.getLayoutParams();
        r.height = measuredwidth;
        r.width = measuredwidth;
    }

    private void initButtons() {
        viewModel.buttons = new TextView[]{binding.buttonOne, binding.buttonTwo, binding.buttonTree,
                binding.buttonFour, binding.buttonFive, binding.buttonSix, binding.buttonSeven,
                binding.buttonEight, binding.buttonNine};
        for (int i = 0; i < viewModel.buttons.length; i++)
            if (viewModel.gameState.isOver(i + 1)) viewModel.buttons[i].setVisibility(View.INVISIBLE);
        if (viewModel.gameState.isDraftPressed())
            for (TextView t : viewModel.buttons)
                t.setTextColor(resColor(R.color.light_gray));
    }

    private void addListenersToButtons() {
        binding.buttonDraft.setOnClickListener(v -> {
            if (!viewModel.gameState.isDraftPressed()) {
                viewModel.gameState.setDraftPressed(true);
                viewModel.gameState.setDraftEnabled(false);
                binding.iconButtonDraft.setEnabled(false);
                for (TextView t : viewModel.buttons)
                    t.setTextColor(attrColor(R.attr.draftOnColorTheme));
            } else {
                viewModel.gameState.setDraftPressed(false);
                viewModel.gameState.setDraftEnabled(true);
                binding.iconButtonDraft.setEnabled(true);
                for (TextView t : viewModel.buttons)
                    t.setTextColor(attrColor(R.attr.draftOfColorTheme));
            }
        });
        binding.buttonAsk.setOnClickListener(v -> {
            if (viewModel.adapter.selectedItem == -1) return;
            if (viewModel.gameState.getHintCounter() == viewModel.possibleHints) {
                Toast.makeText(getContext(), R.string.hins_over, Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getContext(), R.string.hint_used, Toast.LENGTH_SHORT).show();
            TextView[] squareContent = getSquareContent(viewModel.adapter.selectedItem);
            for (int i = 1; i < squareContent.length; i++)
                squareContent[i].setVisibility(View.INVISIBLE);
            int number = viewModel.items.get(viewModel.adapter.selectedItem).getValue();
            squareContent[0].setText(String.valueOf(number));
            squareContent[0].setVisibility(View.VISIBLE);
            squareContent[0].setTextColor(attrColor(R.attr.solvedNumberColorTheme));
            viewModel.items.get(viewModel.adapter.selectedItem).setVisible(true);
            viewModel.items.get(viewModel.adapter.selectedItem).setColor(attrColor(R.attr.solvedNumberColorTheme));
            calcSelectedItems(number);
            makeDraftNumberBold(number);
            hideDraftNumbers(number, viewModel.adapter.selectedItem);
            int counter = viewModel.gameState.getHintCounter() + 1;
            viewModel.gameState.setHintCounter(counter);
            viewModel.gameState.increment(viewModel.items.get(viewModel.adapter.selectedItem).getValue());
            viewModel.gameState.emptySquareCounter--;
            if (viewModel.gameState.isOver(number)) viewModel.buttons[number-1].setVisibility(View.INVISIBLE);
            if(counter == viewModel.possibleHints){
                binding.iconButtonLight.setEnabled(false);
                viewModel.gameState.setHintEnabled(false);
            } else {
                binding.iconButtonLight.setEnabled(true);
                viewModel.gameState.setHintEnabled(true);
            }
            if (viewModel.gameState.emptySquareCounter == 0)
                gameOver(true);
        });
        binding.buttonClear.setOnClickListener(v -> cleanSquare());
        binding.buttonBack.setOnClickListener(v -> goBack());
        for (int i = 0; i < viewModel.buttons.length; i++)
            setOnClick(viewModel.buttons[i], i + 1);
    }

    private void setOnClick(TextView textView, int number) {
        textView.setOnClickListener(v -> {
            if (viewModel.gameState.isOver(number) || viewModel.adapter.selectedItem == -1
                    || viewModel.items.get(viewModel.adapter.selectedItem).isVisible()) return;
            if (!viewModel.gameState.isDraftPressed()) {
                View itemView = binding.recyclerView.getChildAt(viewModel.adapter.selectedItem);
                TextView bigNumber = itemView.findViewById(R.id.bigNumber);
                itemView.findViewById(R.id.itemGridLayout).setVisibility(View.INVISIBLE);
                doBackUp();
                if (isNumberPossible(number, viewModel.adapter.selectedItem)) {
                    bigNumber.setTextColor(attrColor(R.attr.solvedNumberColorTheme));
                    viewModel.items.get(viewModel.adapter.selectedItem).setVisible(true);
                    viewModel.items.get(viewModel.adapter.selectedItem).setColor(attrColor(R.attr.solvedNumberColorTheme));
                    calcSelectedItems(number);
                    makeDraftNumberBold(number);
                    hideDraftNumbers(number, viewModel.adapter.selectedItem);
                    viewModel.gameState.increment(number);
                    viewModel.gameState.emptySquareCounter--;
                }
                else {
                    bigNumber.setTextColor(resColor(R.color.red));
                    viewModel.items.get(viewModel.adapter.selectedItem).setColor(resColor(R.color.red));
                    anErrorOccurred();
                }
                bigNumber.setText(String.valueOf(number));
                bigNumber.setVisibility(View.VISIBLE);
                if (viewModel.gameState.isOver(number)) textView.setVisibility(View.INVISIBLE);
                if (viewModel.gameState.emptySquareCounter == 0 && isNumberPossible(number, viewModel.adapter.selectedItem))
                    gameOver(true);
            } else {
                TextView[] squareContent = getSquareContent(viewModel.adapter.selectedItem);
                if (squareContent[number].getVisibility() == View.INVISIBLE) {
                    squareContent[number].setVisibility(View.VISIBLE);
                    viewModel.items.get(viewModel.adapter.selectedItem).draftsVisibility[number - 1] = 0;
                } else {
                    squareContent[number].setVisibility(View.INVISIBLE);
                    viewModel.items.get(viewModel.adapter.selectedItem).draftsVisibility[number - 1] = 4;
                }
            }
        });
    }

    private void makeDraftNumberBold(int number) {
        for (int i = 0; i < viewModel.items.size(); i++) {
            TextView[] textViews = getSquareContent(i);
            for (int j = 1; j < textViews.length; j++) {
                if (j == number) {
                    textViews[j].setTextColor(attrColor(R.attr.draftTheSameTheme));
                    textViews[j].setShadowLayer(2,0,0, attrColor(R.attr.draftTheSameTheme));
                } else
                    textViews[j].setShadowLayer(0,0,0, attrColor(R.attr.draftTheSameTheme));
            }
        }
    }

    private void hideDraftNumbers(int number, int position) {
        Square s = viewModel.items.get(position);
        int x = s.getX();
        int y = s.getY();
        int region = s.getRegion();
        for (int i = 0; i < viewModel.items.size(); i++) {
            Square square = viewModel.items.get(i);
            if (square.getX() == x || square.getY() == y || square.getRegion() == region) {
                getSquareContent(i)[number].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void calcSelectedItems(int number) {
        for (int i = 0; i < viewModel.items.size(); i++) {
            View view = binding.recyclerView.getChildAt(i);
            if (viewModel.items.get(i).getValue() == number && viewModel.items.get(i).isVisible())
                view.setBackgroundColor(attrColor(R.attr.theSameNumberColorTheme));
        }
        View view = binding.recyclerView.getChildAt(viewModel.adapter.selectedItem);
        view.setBackgroundColor(attrColor(R.attr.selectedColorTheme));
    }

    private void cleanSquare() {
        if (viewModel.adapter.selectedItem == -1) return;
        TextView[] textViews = getSquareContent(viewModel.adapter.selectedItem);
        if (textViews[0].getCurrentTextColor() != resColor(R.color.red))
            return;
        for (TextView t : getSquareContent(viewModel.adapter.selectedItem))
            t.setVisibility(View.INVISIBLE);
    }

    private void anErrorOccurred() {
        viewModel.gameState.setErrorCounter(viewModel.gameState.getErrorCounter() + 1);
        binding.mistakeTextView.setText(String.format("%s %d%s", getString(R.string.mistakes), viewModel.gameState.getErrorCounter(), getString(R.string.mis_count)));
        if (viewModel.gameState.getErrorCounter() == viewModel.possibleMistakes)
            gameOver(false);
    }

    private void gameOver(boolean win) {
        viewModel.isWin = win;
        viewModel.isGameRestarted = false;
        viewModel.gameState.setGameFinished(true);
        insertStatisticToDb(win);
        deleteDataFromDb();
        //showAd();
        openGameOverFragment(win);
    }

    private void openGameOverFragment(boolean win) {
        Bundle bundle = new Bundle();
        bundle.putIntArray(KEY_GRID, getGridForGameOverScreen());
        bundle.putBoolean(KEY_WIN, win);
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_gameFragment2_to_gameOverFragment, bundle);
    }

    private int[] getGridForGameOverScreen() {
        int[] solvedGrid = new int[81];
        for (int i = 0; i < 81; i++)
            solvedGrid[i] = viewModel.items.get(i).getValue();
        return solvedGrid;
    }

    private void insertStatisticToDb(boolean win) {
        viewModel.statistic.updateStatistic(
                win,
                binding.timerTextView.getText().toString(),
                viewModel.gameState.getErrorCounter()
        );
        viewModel.insertStatistic(viewModel.statistic);
    }


    private void doBackUp() {
        viewModel.backup.clear();
        TextView[] textViews = getSquareContent(viewModel.adapter.selectedItem);
        for (int i = 1; i < textViews.length; i++) {
            viewModel.backup.put(i, textViews[i].getVisibility());
        }
    }

    private void goBack() {
        if (viewModel.adapter.selectedItem == -1) return;
        TextView[] textViews = getSquareContent(viewModel.adapter.selectedItem);
        if (textViews[0].getCurrentTextColor() != resColor(R.color.red))
            return;
        textViews[0].setVisibility(View.INVISIBLE);
        for (Map.Entry<Integer, Integer> map : viewModel.backup.entrySet()) {
            int position = map.getKey();
            int value = map.getValue();
            textViews[position].setVisibility(value);
        }
        binding.recyclerView.getChildAt(viewModel.adapter.selectedItem)
                .findViewById(R.id.itemGridLayout).setVisibility(View.VISIBLE);
    }

    private TextView[] getSquareContent(int position) {
        SquareAdapter.SquareHolder v = (SquareAdapter.SquareHolder) binding.recyclerView
                .getChildViewHolder(binding.recyclerView.getChildAt(position));
        return v.content;
    }

    private int resColor(int res) {
        return getResources().getColor(res, getContext().getTheme());
    }

    private int attrColor(int res) {
        return MaterialColors.getColor(getView(), res);
    }

    private boolean isNumberPossible(int number, int position) {
        return viewModel.items.get(position).getValue() == number;
    }


    private void showItems() {
        for (int i = 0; i < viewModel.items.size(); i++) {
            binding.recyclerView.getChildAt(i).setVisibility(View.VISIBLE);
        }
    }

    private void hideItems() {
        for (int i = 0; i < viewModel.items.size(); i++) {
            binding.recyclerView.getChildAt(i).setVisibility(View.INVISIBLE);
        }
    }
    private void openPauseDialog() {
        viewModel.timer.dispose();
        hideItems();
        viewModel.calculateTimerStartValue(binding.timerTextView.getText().toString());
        FragmentManager f = getActivity().getSupportFragmentManager();
        PauseDialog pauseDialog = new PauseDialog(
                binding.diffTextView.getText().toString(),
                binding.mistakeTextView.getText().toString(),
                binding.timerTextView.getText().toString()
        );
        f.setFragmentResultListener(DIALOG_FRAGMENT_RESULT, this, this);
        pauseDialog.show(f, DIALOG_PAUSE);
    }


    private void putGameToDatabase() {
        viewModel.gameState.setDifficultyString(binding.diffTextView.getText().toString());
        viewModel.gameState.setTimer(binding.timerTextView.getText().toString());
        viewModel.insertGridToDb(viewModel.items);
        viewModel.insertStateToDb(viewModel.gameState);
    }

    private void deleteDataFromDb() {
        viewModel.deleteStateFromDb();
        viewModel.deleteGridFromDb();
    }

    public String getDiffTitle(int difficulty) {
        switch (difficulty) {
            case 1: return getString(R.string.difficulty_easy);
            case 2: return getString(R.string.difficulty_middle);
            case 3: return getString(R.string.difficulty_expert);
            default:return getString(R.string.unknown);
        }
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();
        menuInflater.inflate(R.menu.game_menu_header, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.pauseButtonMenu) {
            viewModel.gameState.setGamePaused(true);
            openPauseDialog();
            return true;
        }
        else if (menuItem.getItemId() == R.id.settingsButtonMenu) {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_gameFragment2_to_settingsFragment);
            return true;
        } else if (menuItem.getItemId() == R.id.newGameButtonMenu) {
            viewModel.gameState.setGamePaused(true);
            openNewGameDialog();
            return true;
        } else
            return false;
    }

    private void openNewGameDialog() {
        viewModel.timer.dispose();
        hideItems();
        viewModel.calculateTimerStartValue(binding.timerTextView.getText().toString());
        FragmentManager f = getActivity().getSupportFragmentManager();
        NewGameDialog newGameDialog = new NewGameDialog(viewModel);
        f.setFragmentResultListener(DIALOG_FRAGMENT_RESULT, this, this);
        newGameDialog.show(f, DIALOG_PAUSE);
    }


    @Override
    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
        if (requestKey.equals(DIALOG_FRAGMENT_RESULT)) {
            showItems();
            startTimer();
            viewModel.gameState.setGamePaused(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!viewModel.gameState.isGamePaused())
            startTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.calculateTimerStartValue(binding.timerTextView.getText().toString());
        viewModel.gameState.setTime(viewModel.gameState.getTime());
        viewModel.timer.dispose();
    }


    @Override
    public void onStop() {
        if (!viewModel.gameState.isGameFinished()) {
            putGameToDatabase();
        }
        super.onStop();
    }
}
