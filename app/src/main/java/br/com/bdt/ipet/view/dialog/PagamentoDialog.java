package br.com.bdt.ipet.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.model.Ong;

public class PagamentoDialog extends DialogFragment {

    private static ValorPagamentoDialog valorPagamentoDialog;
    private Ong ong;
    EditText et;

    public static PagamentoDialog newInstance(Ong ong, Double valor) {
        PagamentoDialog f = new PagamentoDialog();


        Bundle args = new Bundle();
        args.putParcelable("ong", (Parcelable)ong);
        args.putDouble("valor", valor);
        System.out.println("valor 2" + valor);
        f.setArguments(args);
        return f;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Ong ong = getArguments().getParcelable("ong");
        Double valor = getArguments().getDouble("valor");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_pagamento, null);

        et = view.findViewById(R.id.etValorPagar);
        et.setText(valor.toString());
        setTextTv(R.id.tvNomeOngPagamento,ong.getNome(),view);
        builder.setView(view);
        return builder.create();
    }

    private void setTextTv(int idTextView, String text,View view) {
        TextView tv = view.findViewById(idTextView);
        tv.setText(text);
    }

}