package br.com.bdt.ipet.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.model.Ong;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class MetodoPagamentoDialog extends DialogFragment {
    private Ong ong;

    public static MetodoPagamentoDialog newInstance(Ong ong) {
        MetodoPagamentoDialog f = new MetodoPagamentoDialog();


        Bundle args = new Bundle();
        args.putParcelable("ong", (Parcelable) ong);
        f.setArguments(args);
        return f;
    }

    @SuppressLint("ResourceType")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Ong ong = getArguments().getParcelable("ong");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        System.out.println(ong);
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_metodo_pagamento, null);

        builder.setView(view);


        ConstraintLayout btnMetodoUm = (ConstraintLayout) view.findViewById(R.id.ctMetodoUm);
        View.OnClickListener listenerMetodoUm = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = PagamentoDialog.newInstance(ong);


                dialog.show(getActivity().getSupportFragmentManager(), "pagamento");
                getActivity().getSupportFragmentManager().executePendingTransactions();

                dialog.getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);


            }
        };
        btnMetodoUm.setOnClickListener(listenerMetodoUm);

        ConstraintLayout btnMetodoDois = (ConstraintLayout) view.findViewById(R.id.ctMetodoDois);
        View.OnClickListener listenerMetodoDois = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = PagamentoDialog.newInstance(ong);
                dialog.show(getActivity().getSupportFragmentManager(), "pagamento");
                getActivity().getSupportFragmentManager().executePendingTransactions();
                dialog.getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            }
        };
        btnMetodoDois.setOnClickListener(listenerMetodoDois);

        ConstraintLayout btnMetodoTres = (ConstraintLayout) view.findViewById(R.id.ctMetodoTres);
        View.OnClickListener listenerMetodoTres = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = PagamentoDialog.newInstance(ong);
                dialog.show(getActivity().getSupportFragmentManager(), "pagamento");
                getActivity().getSupportFragmentManager().executePendingTransactions();
                dialog.getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }
        };
        btnMetodoTres.setOnClickListener(listenerMetodoTres);

        ConstraintLayout btnMetodoQuatro = (ConstraintLayout) view.findViewById(R.id.ctMetodoQuatro);
        View.OnClickListener listenerMetodoQuatro = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = PagamentoDialog.newInstance(ong);
                dialog.show(getActivity().getSupportFragmentManager(), "pagamento");
                getActivity().getSupportFragmentManager().executePendingTransactions();
                dialog.getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }
        };
        btnMetodoQuatro.setOnClickListener(listenerMetodoQuatro);

        return builder.create();
    }
}
