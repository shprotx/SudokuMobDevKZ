package ru.shprot.sudokumobdevkz.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import ru.shprot.sudokumobdevkz.R;
import ru.shprot.sudokumobdevkz.databinding.FragmentUpvoteDialogBinding;

public class UpvoteDialog extends DialogFragment {

    FragmentUpvoteDialogBinding binding;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(), R.style.RoundedCornersDialog);
        LayoutInflater inflater = getLayoutInflater();
        binding = FragmentUpvoteDialogBinding.bind(inflater.inflate(R.layout.fragment_upvote_dialog, null));
        builder.setView(binding.getRoot());
        AlertDialog dialog = builder.create();
        binding.buttonGreatFragment.setOnClickListener(v -> dismiss());
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
