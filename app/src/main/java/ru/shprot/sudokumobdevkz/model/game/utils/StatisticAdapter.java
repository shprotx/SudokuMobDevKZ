package ru.shprot.sudokumobdevkz.model.game.utils;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ru.shprot.sudokumobdevkz.ui.ItemStatisticFragment;

public class StatisticAdapter extends FragmentStateAdapter {

    public StatisticAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new ItemStatisticFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
