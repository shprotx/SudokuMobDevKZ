package ru.shprot.sudokumobdevkz.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import ru.shprot.sudokumobdevkz.R;
import ru.shprot.sudokumobdevkz.model.game.utils.StatisticAdapter;

public class StatisticFragment extends Fragment implements MenuProvider {

    StatisticAdapter statisticAdapter;
    ViewPager2 viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedDispatcher back = getActivity().getOnBackPressedDispatcher();
        back.addCallback(this, new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {

            }
        });
        getActivity().addMenuProvider(this,this, Lifecycle.State.STARTED);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        statisticAdapter = new StatisticAdapter(this);
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(statisticAdapter);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        TabLayoutMediator.TabConfigurationStrategy strategy = (tab, position) -> {
            switch (position) {
                case 0: tab.setText(getString(R.string.difficulty_easy));
                    break;
                case 1: tab.setText(getString(R.string.difficulty_middle));
                    break;
                case 2: tab.setText(getString(R.string.difficulty_expert));
                    break;
                default: tab.setText(getString(R.string.unknown));}
        };
        new TabLayoutMediator(tabLayout, viewPager, strategy).attach();

    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_header, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                .navigate(R.id.action_statisticFragment_to_settingsFragment);
        return true;
    }
}

