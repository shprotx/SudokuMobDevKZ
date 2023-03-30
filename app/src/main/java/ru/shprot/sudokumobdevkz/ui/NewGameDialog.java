package ru.shprot.sudokumobdevkz.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import ru.shprot.sudokumobdevkz.R;
import ru.shprot.sudokumobdevkz.databinding.FragmentNewGameDialogBinding;
import ru.shprot.sudokumobdevkz.model.game.GameState;
import ru.shprot.sudokumobdevkz.model.game.Square;
import ru.shprot.sudokumobdevkz.model.game.generator.Generator;
import ru.shprot.sudokumobdevkz.viewmodel.GameViewModel;


public class NewGameDialog extends DialogFragment {

    FragmentNewGameDialogBinding b;
    GameViewModel gameViewModel;

    public NewGameDialog(GameViewModel gameViewModel) {
        this.gameViewModel = gameViewModel;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(), R.style.RoundedCornersDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        b = FragmentNewGameDialogBinding.bind(inflater.inflate(R.layout.fragment_new_game_dialog, null));
        builder.setView(b.getRoot());
        builder.setBackground(new ColorDrawable(Color.TRANSPARENT));
        AlertDialog dialog = builder.create();
        b.buttonCancelFragment.setOnClickListener(v -> dismiss());
        b.easyNewGameTextView.setOnClickListener(v -> prepareNewGame(1));
        b.middleNewGameTextView.setOnClickListener(v -> prepareNewGame(2));
        b.expertNewGameTextView.setOnClickListener(v -> prepareNewGame(3));
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


    private void prepareNewGame(int difficulty) {
        gameViewModel.deleteGridFromDb();
        gameViewModel.deleteStateFromDb();
        Generator generator = new Generator();
        gameViewModel.items = generator.generate(difficulty);
        gameViewModel.gameState = new GameState();
        gameViewModel.gameState.setDifficulty(difficulty);
        for (Square item : gameViewModel.items)
            if (item.isVisible()) {
                gameViewModel.gameState.increment(item.getValue());
                gameViewModel.gameState.emptySquareCounter--;
            }
        gameViewModel.isGameRestarted = true;
        dismiss();
        getActivity().recreate();
    }



    @Override
    public int getTheme() {
        return R.style.RoundedCornersDialog;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        FragmentManager f = getParentFragmentManager();
        f.setFragmentResult("fragmentResult", new Bundle());
    }
}
