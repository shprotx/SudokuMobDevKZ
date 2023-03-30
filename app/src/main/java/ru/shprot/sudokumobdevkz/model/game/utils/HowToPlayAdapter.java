package ru.shprot.sudokumobdevkz.model.game.utils;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import ru.shprot.sudokumobdevkz.ui.ItemHowFragment;

public class HowToPlayAdapter extends FragmentStateAdapter {
    public HowToPlayAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new ItemHowFragment();
        Bundle args = new Bundle();
        args.putInt(ItemHowFragment.ARG_OBJECT, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
