package br.com.bdt.ipet.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import br.com.bdt.ipet.R;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class MetodoPagamentoDialog  extends DialogFragment {

    @SuppressLint("ResourceType")
    @Override
    public

    Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_metodo_pagamento, null);
        builder.setView( view);
        Button btn = (Button) view.findViewById(R.id.btSelecaoPagamento);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new PagamentoDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "pagamento");

            }
        };
        btn.setOnClickListener(listener);

        return builder.create();
    }
}
