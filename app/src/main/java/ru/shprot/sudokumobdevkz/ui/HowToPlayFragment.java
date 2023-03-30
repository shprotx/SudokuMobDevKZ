package ru.shprot.sudokumobdevkz.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import ru.shprot.sudokumobdevkz.R;
import ru.shprot.sudokumobdevkz.databinding.FragmentHowBinding;
import ru.shprot.sudokumobdevkz.model.game.utils.HowToPlayAdapter;

public class HowToPlayFragment extends Fragment implements MenuProvider {

    FragmentHowBinding binding;
    HowToPlayAdapter howToPlayAdapter;
    private int currentImage = 0;
    private TextView[] dots;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedDispatcher back = getActivity().getOnBackPressedDispatcher();
        back.addCallback(this, new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {

            }
        });
        getActivity().addMenuProvider(this, this, Lifecycle.State.STARTED);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHowBinding.bind(inflater.inflate(R.layout.fragment_how, container, false));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        howToPlayAdapter = new HowToPlayAdapter(this);
        binding.pager.setAdapter(howToPlayAdapter);
        binding.pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentImage = position;
                updateDots();
                if (currentImage == 2) {
                    binding.howFinish.setVisibility(View.VISIBLE);
                    binding.howSkip.setText(getString(R.string.to_the_begining));
                } else {
                    binding.howFinish.setVisibility(View.GONE);
                    binding.howSkip.setText(getString(R.string.skip));
                }
            }
        });

        initDots();
        initButtons();
        updateDots();
    }

    private void initButtons() {
        binding.howSkip.setOnClickListener(v -> {
            if (currentImage != 2) currentImage = 2;
            else currentImage = 0;
            binding.pager.setCurrentItem(currentImage);
        });
        binding.howFinish.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_howToPlayFragment_to_mainFragment2);
        });
    }

    private void initDots() {
        binding.dotsLinearLayout.removeAllViews();
        dots = new TextView[howToPlayAdapter.getItemCount()];
        for (int i = 0; i < howToPlayAdapter.getItemCount(); i++) {
            dots[i] = new TextView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30,30);
            params.setMargins(8,8,8,8);
            dots[i].setLayoutParams(params);
            binding.dotsLinearLayout.addView(dots[i]);
        }
    }

    private void updateDots() {
        for (int i = 0; i < howToPlayAdapter.getItemCount(); i++) {
            if (i == currentImage)
                dots[i].setBackgroundResource(R.drawable.indicator1);
            else
                dots[i].setBackgroundResource(R.drawable.indicator0);
        }
    }


    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();
        menuInflater.inflate(R.menu.menu_header, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

            Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_howToPlayFragment_to_settingsFragment);
            return true;

    }

    @Override
    public void onMenuClosed(@NonNull Menu menu) {
        menu.close();
        MenuProvider.super.onMenuClosed(menu);
    }
}



