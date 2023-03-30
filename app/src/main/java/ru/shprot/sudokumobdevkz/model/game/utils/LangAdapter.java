package ru.shprot.sudokumobdevkz.model.game.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ru.shprot.sudokumobdevkz.R;

public class LangAdapter extends ArrayAdapter<String> {

    private final String[] languages;
    private int[] icons;

    public LangAdapter(Context context, int textViewResourceId,
                           String[] objects) {
        super(context, textViewResourceId, objects);
        languages = objects;
        initContent();
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, parent);
    }

    public View getCustomView(int position, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.lang_layout, parent, false);
        TextView label = view.findViewById(R.id.mLangText);
        ImageView icon = view.findViewById(R.id.mLangIcon);
        label.setText(languages[position]);
        icon.setImageResource(icons[position]);
        return view;
    }

    private void initContent() {
        icons = new int[8];
        icons[0] = R.drawable.icon_us;
        icons[1] = R.drawable.icon_fr;
        icons[2] = R.drawable.icon_de;
        icons[3] = R.drawable.icon_kz;
        icons[4] = R.drawable.icon_pt;
        icons[5] = R.drawable.icon_ru;
        icons[6] = R.drawable.icon_sp;
        icons[7] = R.drawable.icon_ua;
    }
}
