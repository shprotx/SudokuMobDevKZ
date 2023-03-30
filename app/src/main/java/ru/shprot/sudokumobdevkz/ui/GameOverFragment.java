package ru.shprot.sudokumobdevkz.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.shprot.sudokumobdevkz.R;
import ru.shprot.sudokumobdevkz.databinding.FragmentGameOverBinding;
import ru.shprot.sudokumobdevkz.model.game.utils.GameOverAdapter;
import ru.shprot.sudokumobdevkz.viewmodel.GameOverViewModel;

public class GameOverFragment extends Fragment implements MenuProvider {

    FragmentGameOverBinding binding;
    GameOverViewModel viewModel;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.game_over);
        getActivity().addMenuProvider(this, this, Lifecycle.State.STARTED);
        viewModel = new ViewModelProvider(this).get(GameOverViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentGameOverBinding.bind(inflater.inflate(R.layout.fragment_game_over,
                container, false));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        viewModel.setWin(bundle.getBoolean("win", true));
        viewModel.setItems(bundle.getIntArray("grid"));

        addListenersToButtons();
        setInfoAboutSolvedGame(viewModel.isWin());
        showSolvedGrid(viewModel.getItems());
    }

    private void showSolvedGrid(int[] items) {
        setCurrentSizeToGridContainer();
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 9);
        GameOverAdapter adapter = new GameOverAdapter(items, viewModel.getCardSize());
        binding.gameOverRecyclerView.setAdapter(adapter);
        binding.gameOverRecyclerView.setLayoutManager(manager);
        binding.gameOverRecyclerView.setHasFixedSize(true);
        binding.gameOverRecyclerView.setLayoutFrozen(true);
        binding.winCardView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_solved));
    }

    private void setCurrentSizeToGridContainer() {
        ViewGroup.LayoutParams p = binding.winCardView.getLayoutParams();
        int size = Resources.getSystem().getDisplayMetrics().widthPixels - 400;
        while (true) {
            float temp = size % 9;
            if (temp == 0) break;
            else size--;
        }
        viewModel.setGridSize(size);
        viewModel.setCardSize(size/9);
        p.width = viewModel.getGridSize();
        p.height = viewModel.getGridSize();

    }

    private void setInfoAboutSolvedGame(boolean win) {
        if (win)
            binding.titleGameOverDialog.setText(getString(R.string.win));
        else
            binding.titleGameOverDialog.setText(getString(R.string.lose));
    }

    private void addListenersToButtons() {
        binding.buttonToStatisticFromFragment.setOnClickListener(v ->
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_gameOverFragment_to_statisticFragment));
        binding.buttonToHomeFromFragment.setOnClickListener(v ->
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_gameOverFragment_to_mainFragment2));
    }


    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_header, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.settingsButtonMenu) {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_gameOverFragment_to_settingsFragment);
            return true;
        }
        return false;
    }
}
