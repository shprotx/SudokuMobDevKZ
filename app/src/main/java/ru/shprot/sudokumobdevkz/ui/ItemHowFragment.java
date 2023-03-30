package ru.shprot.sudokumobdevkz.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.shprot.sudokumobdevkz.R;

public class ItemHowFragment extends Fragment {
    public static final String ARG_OBJECT = "position_how";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_how_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        int pageNumber = args.getInt(ARG_OBJECT, 0);
        ImageView image = view.findViewById(R.id.image_how);
        TextView text = view.findViewById(R.id.text_how);
        TextView title = view.findViewById(R.id.titleHowTextView);

        switch (pageNumber) {
            case 0: image.setImageResource(R.drawable.howone);
                text.setText(getString(R.string.how_desc_one));
                title.setText(getString(R.string.no_repetitions));
                break;
            case 1: image.setImageResource(R.drawable.howtwo);
                text.setText(getString(R.string.how_desc_two));
                title.setText(getString(R.string.always_unique));
                break;
            case 2: image.setImageResource(R.drawable.howthree);
                text.setText(R.string.how_desc_three);
                title.setText(getString(R.string.use_notes));
                break;
        }
    }
}
