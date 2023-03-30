package ru.shprot.sudokumobdevkz.model.game.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import ru.shprot.sudokumobdevkz.R;

public class GameOverAdapter extends RecyclerView.Adapter<GameOverAdapter.GameOverViewHolder> {

    int[] items;
    int cardSize;

    public GameOverAdapter(int[] items, int cardSize) {
        this.cardSize = cardSize;
        this.items = items;
    }

    @NonNull
    @Override
    public GameOverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GameOverViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_over_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GameOverViewHolder holder, int position) {
        holder.params.height = cardSize;
        holder.params.width = cardSize;
        holder.number.setText(String.valueOf(items[position]));
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    static class GameOverViewHolder extends RecyclerView.ViewHolder {

        TextView number;
        CardView container;
        ViewGroup.LayoutParams params;
        public GameOverViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.numberGameOverTextView);
            container = itemView.findViewById(R.id.cardGameOverContainer);
            params = container.getLayoutParams();
        }
    }
}
