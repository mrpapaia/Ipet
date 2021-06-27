package br.com.bdt.ipet.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.util.RvMetodoPagamentoAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MetodoPagamentoDialog extends DialogFragment {

    public static MetodoPagamentoDialog newInstance(Ong ong, Double valor) {
        MetodoPagamentoDialog f = new MetodoPagamentoDialog();
        Bundle args = new Bundle();
        args.putParcelable("ong", ong);
        args.putDouble("valor", valor);
        f.setArguments(args);
        return f;
    }

    @SuppressWarnings("ConstantConditions")
    @SuppressLint("ResourceType")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Ong ong = getArguments().getParcelable("ong");
        Double valor = getArguments().getDouble("valor");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_metodo_pagamento, null);

        RecyclerView rvMetodoPagamento = view.findViewById(R.id.rvMetodoPagamento);
        rvMetodoPagamento.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMetodoPagamento.setItemAnimator(new DefaultItemAnimator());
        rvMetodoPagamento.setHasFixedSize(true);

        RvMetodoPagamentoAdapter rvMetodoPagamentoAdapter = new RvMetodoPagamentoAdapter(getContext(), ong.getDadosBancarios(), index -> {
            DialogFragment dialog = PagamentoDialog.newInstance(ong, valor, index);
            dialog.show(getActivity().getSupportFragmentManager(), "pagamento");
            getActivity().getSupportFragmentManager().executePendingTransactions();
            dialog.getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            this.dismiss();
        });

        rvMetodoPagamento.setAdapter(rvMetodoPagamentoAdapter);

        builder.setView(view);

        return builder.create();
    }
}
