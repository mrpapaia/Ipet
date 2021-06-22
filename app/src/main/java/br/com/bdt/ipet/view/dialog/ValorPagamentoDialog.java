package br.com.bdt.ipet.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.model.Ong;

public class ValorPagamentoDialog extends DialogFragment {

    private Ong ong;

    public static ValorPagamentoDialog newInstance(Ong ong) {
        ValorPagamentoDialog f = new ValorPagamentoDialog();


        Bundle args = new Bundle();
        args.putParcelable("ong", (Parcelable) ong);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Ong ong = getArguments().getParcelable("ong");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        System.out.println(ong);
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_valor_pagamento, null);

        builder.setView(view);

        // Dialogo do valor do Pagamento.

        Button btnConfirmar = (Button) view.findViewById(R.id.bConfirmar);
        View.OnClickListener listenerConfirmar = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double valor = Double.parseDouble(((EditText)
                        view.findViewById(R.id.etValorDoacao)).getText().toString());

                System.out.println("valor" + valor);

                DialogFragment dialog = MetodoPagamentoDialog.newInstance(ong, valor);

                dialog.show(getActivity().getSupportFragmentManager(), "metodoPagamento");
                getActivity().getSupportFragmentManager().executePendingTransactions();

                dialog.getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            }
        };

        btnConfirmar.setOnClickListener(listenerConfirmar);

        return builder.create();
    }
}
