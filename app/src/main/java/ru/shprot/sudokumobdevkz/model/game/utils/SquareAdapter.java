package ru.shprot.sudokumobdevkz.model.game.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.shprot.sudokumobdevkz.R;
import ru.shprot.sudokumobdevkz.databinding.GridItemBinding;
import ru.shprot.sudokumobdevkz.model.game.Square;

public class SquareAdapter extends RecyclerView.Adapter<SquareAdapter.SquareHolder> {

    private final RecyclerView recyclerView;
    private final List<Square> items;
    public int selectedItem = -1;
    private final int selectedColor;
    private final int areaColor; //#E2E7ED
    private final int whiteColor;
    private final int theSameNumberColor; //#C9CFDD
    private final int blackColor;
    private final int draftTheSameColor;
    private final int grayNumberColor;
    private final int solvedColor;

    public SquareAdapter(ArrayList<Square> items, RecyclerView recyclerView, int[] colors) {
        this.items = items;
        this.recyclerView = recyclerView;
        this.areaColor = colors[0];
        this.whiteColor = colors[1];
        this.blackColor = colors[2];
        this.theSameNumberColor = colors[3];
        this.selectedColor = colors[4];
        this.draftTheSameColor = colors[5];
        this.grayNumberColor = colors[6];
        this.solvedColor = colors[7];
    }

    @NonNull
    @Override
    public SquareHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SquareHolder(GridItemBinding.bind(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item
                        , parent
                        , false)));
    }

    @Override
    public void onBindViewHolder(@NonNull SquareHolder holder, int position) {
        Square square = items.get(position);
        if (square.isVisible()) {
            holder.content[0].setText(String.valueOf(square.getValue()));
            if (square.getColor() == R.color.black && blackColor != R.color.black)
                holder.content[0].setTextColor(blackColor);
            else
                holder.content[0].setTextColor(solvedColor);
        }else {
            holder.content[0].setVisibility(View.INVISIBLE);
            for (int i = 1; i < holder.content.length; i++)
                holder.content[i].setVisibility(square.draftsVisibility[i-1]);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class SquareHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public GridItemBinding b;
        public final TextView[] content = new TextView[10];

        public SquareHolder(@NonNull GridItemBinding binding) {
            super(binding.getRoot());
            this.b = binding;
            binding.getRoot().setOnClickListener(this);
            binding.getRoot().setBackgroundColor(whiteColor);

            content[0] = b.bigNumber;
            content[1] = b.smallOne;
            content[2] = b.smallTwo;
            content[3] = b.smallTree;
            content[4] = b.smallFour;
            content[5] = b.smallFive;
            content[6] = b.smallSix;
            content[7] = b.smallSeven;
            content[8] = b.smallEight;
            content[9] = b.smallNine;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Square square = items.get(position);
            int x = square.getX();
            int y = square.getY();
            int region = square.getRegion();
            int value = square.getValue();
            boolean b = square.isVisible();
            drawCells(x, y, region, value, b);
            makeDraftNumberBold(value, b);
            v.setBackgroundColor(selectedColor);
            selectedItem = position;
        }

        private void drawCells(int x, int y, int region, int value, boolean isSelectedVisible) {
            for (int i = 0; i < items.size(); i++) {
                View view = recyclerView.getChildAt(i);
                Square s = items.get(i);
                if (s.getX() == x || s.getY() == y || s.getRegion() == region)
                    view.setBackgroundColor(areaColor);
                else if (s.getValue() == value && s.isVisible() && isSelectedVisible)
                    view.setBackgroundColor(theSameNumberColor);
                else view.setBackgroundColor(whiteColor);
            }
        }

        private void makeDraftNumberBold(int number, boolean isVisible) {
            for (int i = 0; i < items.size(); i++) {
                SquareHolder h = (SquareHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                TextView[] textViews = h.content;
                if (isVisible) {
                    for (int j = 1; j < textViews.length; j++) {
                        if (j == number) {
                            textViews[j].setTextColor(draftTheSameColor);
                            textViews[j].setShadowLayer(2,0,0, draftTheSameColor);
                        } else {
                            textViews[j].setTextColor(grayNumberColor);
                            textViews[j].setShadowLayer(0,0,0,grayNumberColor);
                        }
                    }
                } else {
                    for (int j = 1; j < content.length; j++) {
                        textViews[j].setTextColor(grayNumberColor);
                        textViews[j].setShadowLayer(0,0,0,grayNumberColor);
                    }
                }
            }
        }


    }
}
