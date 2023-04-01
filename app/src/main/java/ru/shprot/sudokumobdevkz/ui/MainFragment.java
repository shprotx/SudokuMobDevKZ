package ru.shprot.sudokumobdevkz.ui;

import static ru.shprot.sudokumobdevkz.model.game.utils.Library.KEY_DIFF;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.KEY_GAME_STATE;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.KEY_ITEMS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.shprot.sudokumobdevkz.MainActivity;
import ru.shprot.sudokumobdevkz.R;
import ru.shprot.sudokumobdevkz.databinding.FragmentMainBinding;
import ru.shprot.sudokumobdevkz.model.game.utils.AppRater;
import ru.shprot.sudokumobdevkz.viewmodel.MenuViewModel;

public class MainFragment extends Fragment implements MenuProvider {

    private FragmentMainBinding binding;
    private MenuViewModel viewModel;
    private Bundle bundle;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().addMenuProvider(this, this, Lifecycle.State.STARTED);
        OnBackPressedDispatcher back = getActivity().getOnBackPressedDispatcher();
        back.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        });
        viewModel = new ViewModelProvider(this).get(MenuViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.bind(inflater.inflate(R.layout.fragment_main, container, false));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getGameState().observe(getViewLifecycleOwner(), gameState -> {
            if (gameState == null || gameState.isGameFinished()) {
                binding.newGameButton.setOnClickListener(v -> showOrHideMenu(true));
                binding.continueGameButton.setVisibility(View.GONE);
            } else {
                binding.newGameButton.setOnClickListener(v -> showOrHideMenu(false));
                viewModel.gameState = gameState;
                viewModel.getItems();
                String s = getString(R.string.continue_game) + "\n(" + viewModel.gameState.getDifficultyString()
                        + ": " + viewModel.gameState.getTimer() + ")";
                binding.continueGameButton.setText(s);
                binding.continueGameButton.setVisibility(View.VISIBLE);
                binding.continueGameButton.setOnClickListener(v ->
                        prepareNewGame(viewModel.gameState.getDifficulty(), true));
            }
        });

        binding.buttonNewGameEasy.setOnClickListener(v -> prepareNewGame(1,false));
        binding.buttonNewGameMedium.setOnClickListener(v -> prepareNewGame(2,false));
        binding.buttonNewGameExpert.setOnClickListener(v -> prepareNewGame(3,false));

        binding.howtoplayButton.setOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_mainFragment2_to_howToPlayFragment));
        binding.statisticButton.setOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_mainFragment2_to_statisticFragment));

        if (MainActivity.needToShowRate)
            showRate();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.main_page);
    }

    
    
    
    
    private void showRate() {
        binding.logoText.setVisibility(View.GONE);
        binding.rateFirstStep.setVisibility(View.VISIBLE);
        binding.noLikeButton.setOnClickListener(v -> doNotLikeClicked());
        binding.yesLikeButton.setOnClickListener(v -> doLikeClicked());
    }

    private void doLikeClicked() {
        animate(binding.rateFirstStep, "rate_exit");
        binding.rateFirstStep.setVisibility(View.GONE);
        binding.rateSecondStep.setVisibility(View.VISIBLE);
        animate(binding.rateSecondStep, "rate_enter");
        binding.noRateButton.setOnClickListener(v -> noRateClicked());
        binding.yesRateButton.setOnClickListener(v -> yesRateClicked());
    }

    private void yesRateClicked() {
        showRateDialog();
        hideRateBanner(2);
        AppRater appRater = new AppRater(getContext());
        appRater.applyNeverShowRate();
        MainActivity.needToShowRate = false;
    }

    private void noRateClicked() {
        AppRater appRater = new AppRater(getContext());
        appRater.increaseInterval();
        hideRateBanner(2);
    }

    private void doNotLikeClicked() {
        AppRater appRater = new AppRater(getContext());
        appRater.applyNeverShowRate();
        MainActivity.needToShowRate = false;
        hideRateBanner(1);
    }

    private void hideRateBanner(int position) {
        if (position == 1) {
            animate(binding.rateFirstStep, "rate_exit");
            binding.rateFirstStep.setVisibility(View.GONE);
            binding.logoText.setVisibility(View.VISIBLE);
        } else {
            animate(binding.rateSecondStep, "rate_exit");
            binding.rateSecondStep.setVisibility(View.GONE);
            binding.logoText.setVisibility(View.VISIBLE);
        }
    }

    public void showRateDialog() {
        ReviewManager reviewManager = ReviewManagerFactory.create(getContext());
        reviewManager.requestReviewFlow().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                reviewManager.launchReviewFlow(getActivity(), task.getResult());
                new AppRater(getContext()).applyNeverShowRate();
            }
        });
        reviewManager.requestReviewFlow()
                .addOnCanceledListener(() -> new AppRater(getContext()).increaseInterval());
        reviewManager.requestReviewFlow().addOnFailureListener(e ->
                Toast.makeText(getContext(),
                        getString(R.string.rate_error_message),
                        Toast.LENGTH_LONG).show()
        );
    }

    private void animate(View view, String type) {
        Animation a;
        switch (type) {
            case "enter":
                a = AnimationUtils.loadAnimation(getContext(), R.anim.anim_from_right);
                break;
            case "exit":
                a = AnimationUtils.loadAnimation(getContext(), R.anim.anim_to_right);
                break;
            case "rate_exit":
                a = AnimationUtils.loadAnimation(getContext(), R.anim.anim_rate_out);
                break;
            default:
                a = AnimationUtils.loadAnimation(getContext(), R.anim.anim_rate_in);
                break;
        }
        view.startAnimation(a);
    }

    private void showOrHideMenu(boolean oldGameFinished) {
        if (binding.newGameMenu.getVisibility() == View.GONE) {
            binding.continueGameButton.setVisibility(View.GONE);
            binding.newGameMenu.setVisibility(View.VISIBLE);
            animate(binding.newGameMenu, "enter");
        } else {
            binding.newGameMenu.setVisibility(View.GONE);
            animate(binding.newGameMenu, "exit");
            if (!oldGameFinished)
                binding.continueGameButton.setVisibility(View.VISIBLE);
        }
    }

    private void showProgress() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void prepareNewGame(int difficulty, boolean isContinue) {
        Completable.fromAction(() -> {
            binding.newGameMenu.setVisibility(View.GONE);
            if (!isContinue) {
                viewModel.items = viewModel.generateGrid(difficulty);
                viewModel.gameState = viewModel.calculateNumbers(difficulty);
            }
            bundle = new Bundle();
            bundle.putParcelableArrayList(KEY_ITEMS, viewModel.items);
            bundle.putInt(KEY_DIFF, difficulty);
            bundle.putParcelable(KEY_GAME_STATE, viewModel.gameState);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                               @Override
                               public void onSubscribe(Disposable d) {
                                   showProgress();
                               }
                               @Override
                               public void onComplete() {
                                   Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                                           .navigate(R.id.action_mainFragment2_to_gameFragment2, bundle);
                               }
                               @Override
                               public void onError(Throwable e) {
                                    //Log.e("MyError", "An error occurred: " + e.getLocalizedMessage());
                               }
                           }
                );
    }

    
    
    
    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();
        menuInflater.inflate(R.menu.menu_header, menu);

    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.settingsButtonMenu) {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_mainFragment2_to_settingsFragment);
            return true;
        }
        return false;
    }



}
