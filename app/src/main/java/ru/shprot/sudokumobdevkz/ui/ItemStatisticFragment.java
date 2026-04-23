package ru.shprot.sudokumobdevkz.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import ru.shprot.sudokumobdevkz.R;
import ru.shprot.sudokumobdevkz.databinding.FragmentItemStatisticBinding;
import ru.shprot.sudokumobdevkz.model.game.Statistic;
import ru.shprot.sudokumobdevkz.viewmodel.StatisticViewModel;

public class ItemStatisticFragment extends Fragment {

    FragmentItemStatisticBinding binding;
    StatisticViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentItemStatisticBinding.bind(inflater
                .inflate(R.layout.fragment_item_statistic,container, false));
        viewModel = new ViewModelProvider(this).get(StatisticViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        int position = bundle.getInt("position", 0);
        viewModel.getStatistic(position + 1, statistic -> {
            if (isAdded() && statistic != null)
                loadData(statistic);
        });
        binding.refreshButton.setOnClickListener(v -> clearStatistic(position + 1));
        binding.statToMainButton.setOnClickListener(v -> goToMainPage());
    }

    private void goToMainPage() {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                .navigate(R.id.action_statisticFragment_to_mainFragment2);
    }

    private void loadData(Statistic statistic) {
        binding.bestTimeTextView.setText(getTimerString(statistic.getBestTime()));
        binding.averageTimeTextView.setText(getTimerString(statistic.getAverageTime()));
        binding.gameStartedTextView.setText(String.valueOf(statistic.getGamesStarted()));
        binding.gameWonTextView.setText(String.valueOf(statistic.getGamesWon()));
        binding.percentTextView.setText(statistic.getPercentOfWins() + getString(R.string.percent));
        binding.mistakesTextView.setText(String.valueOf(statistic.getWinsWithoutErrors()));
        binding.bestLineTextView.setText(String.valueOf(statistic.getBestWinsLine()));
        binding.currentLineTextView.setText(String.valueOf(statistic.getCurrentWinsLine()));

        if (statistic.getAverageTime() > 0) {
            viewModel.getPercentile(statistic.getDifficulty(), statistic.getAverageTime(),
                    (percentile, totalPlayers) -> {
                        if (isAdded() && percentile >= 0) {
                            binding.percentileTextView.setText(
                                    getString(R.string.faster_than, percentile));
                            binding.percentileTextView.setVisibility(View.VISIBLE);
                        }
                    });
        }
    }

    private void clearStatistic(int difficulty) {
        binding.bestTimeTextView.setText(getString(R.string.timer));
        binding.averageTimeTextView.setText(getString(R.string.timer));
        binding.gameStartedTextView.setText(getString(R.string.zero));
        binding.gameWonTextView.setText(getString(R.string.zero));
        binding.percentTextView.setText(getString(R.string.zero) + getString(R.string.percent));
        binding.mistakesTextView.setText(getString(R.string.zero));
        binding.bestLineTextView.setText(getString(R.string.zero));
        binding.currentLineTextView.setText(getString(R.string.zero));
        viewModel.removeCurrentStatistic(difficulty);
        binding.percentileTextView.setVisibility(View.GONE);
    }

    public String getTimerString(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        if (hours == 0)
            return String.format("%02d:%02d", minutes, seconds);
        else
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}
