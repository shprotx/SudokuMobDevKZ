package ru.shprot.sudokumobdevkz.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import ru.shprot.sudokumobdevkz.R;


public class PauseDialog extends DialogFragment {

    private final String diff;
    private final String errors;
    private final String timer;


    public PauseDialog(String diff, String errors, String timer) {
        this.diff = diff;
        this.errors = errors;
        this.timer = timer;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(), R.style.RoundedCornersDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_pause_dialog, null);
        builder.setView(view);
        builder.setBackground(new ColorDrawable(Color.TRANSPARENT));
        AlertDialog dialog = builder.create();
        TextView diffTextView = view.findViewById(R.id.pauseDialogDiff);
        TextView errorsTextView = view.findViewById(R.id.pauseDialogErrors);
        TextView timerTextView = view.findViewById(R.id.pauseDialogTimer);
        Button continueGame = view.findViewById(R.id.buttonContinFragment);
        diffTextView.setText(diff);
        errorsTextView.setText(errors);
        timerTextView.setText(timer);
        continueGame.setOnClickListener(v -> dismiss());
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
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
