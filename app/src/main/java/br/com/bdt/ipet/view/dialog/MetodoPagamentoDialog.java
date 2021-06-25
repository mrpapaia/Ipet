package br.com.bdt.ipet.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.CasoController;
import br.com.bdt.ipet.data.model.Banco;
import br.com.bdt.ipet.data.model.DadosBancario;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.singleton.CasoSingleton;
import br.com.bdt.ipet.singleton.OngSingleton;
import br.com.bdt.ipet.util.RvDadosBancariosAdapter;
import br.com.bdt.ipet.util.RvMetodoPagamentoAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MetodoPagamentoDialog extends DialogFragment {

    private Ong ong;
    private RvMetodoPagamentoAdapter rvMetodoPagamentoAdapter;
    private RecyclerView rvMetodoPagamento;
    private List<DadosBancario> dadosBanco;

    public static MetodoPagamentoDialog newInstance(Ong ong, Double valor) {
        MetodoPagamentoDialog f = new MetodoPagamentoDialog();


        Bundle args = new Bundle();
        args.putParcelable("ong", (Parcelable) ong);
        args.putDouble("valor", valor);
        System.out.println("valor " + valor);
        f.setArguments(args);
        return f;
    }

    @SuppressLint("ResourceType")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Ong ong = getArguments().getParcelable("ong");
        Double valor = getArguments().getDouble("valor");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        System.out.println(ong);
        System.out.println(valor);
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_metodo_pagamento, null);

        rvMetodoPagamento = view.findViewById(R.id.rvMetodoPagamento);
        rvMetodoPagamento.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMetodoPagamento.setItemAnimator(new DefaultItemAnimator());
        rvMetodoPagamento.setHasFixedSize(true);

        rvMetodoPagamentoAdapter = new RvMetodoPagamentoAdapter(getContext(), ong.getDadosBancarios(), index -> {
            DialogFragment dialog = PagamentoDialog.newInstance(ong, valor, index);


            dialog.show(getActivity().getSupportFragmentManager(), "pagamento");
            getActivity().getSupportFragmentManager().executePendingTransactions();

            dialog.getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        });

        rvMetodoPagamento.setAdapter(rvMetodoPagamentoAdapter);

        builder.setView(view);

       /* // Dialogo do metodo do pagamento.

        ConstraintLayout btnMetodoUm = (ConstraintLayout) view.findViewById(R.id.ctMetodoUm);
         View.OnClickListener listenerMetodoUm = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = PagamentoDialog.newInstance(ong, valor, index);


                dialog.show(getActivity().getSupportFragmentManager(), "pagamento");
                getActivity().getSupportFragmentManager().executePendingTransactions();

                dialog.getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }
         };

       btnMetodoUm.setOnClickListener(listenerMetodoUm);*/

        return builder.create();
    }
}
