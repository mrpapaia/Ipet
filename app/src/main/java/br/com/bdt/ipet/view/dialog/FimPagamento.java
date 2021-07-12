package br.com.bdt.ipet.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import br.com.bdt.ipet.R;

public class FimPagamento extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.activity_fim_pagamento, null);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            this.dismiss();
        }, 2000);

        builder.setView(view);
        return builder.create();
    }
}
