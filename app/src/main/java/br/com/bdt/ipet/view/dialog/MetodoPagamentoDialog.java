package br.com.bdt.ipet.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.util.RvMetodoPagamentoAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MetodoPagamentoDialog extends DialogFragment {

    public static MetodoPagamentoDialog newInstance(Caso caso, Double valor) {
        MetodoPagamentoDialog f = new MetodoPagamentoDialog();
        Bundle args = new Bundle();
        args.putParcelable("caso", caso);
        args.putDouble("valor", valor);
        f.setArguments(args);
        return f;
    }

    @SuppressWarnings("ConstantConditions")
    @SuppressLint("ResourceType")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Caso caso = getArguments().getParcelable("caso");
        Double valor = getArguments().getDouble("valor");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_metodo_pagamento, null);

        RecyclerView rvMetodoPagamento = view.findViewById(R.id.rvMetodoPagamento);
        rvMetodoPagamento.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMetodoPagamento.setItemAnimator(new DefaultItemAnimator());
        rvMetodoPagamento.setHasFixedSize(true);

        RvMetodoPagamentoAdapter rvMetodoPagamentoAdapter = new RvMetodoPagamentoAdapter(getContext(), caso.getOng().getDadosBancarios(), index -> {
            DialogFragment dialog = PagamentoDialog.newInstance(caso, valor, index);
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
