package br.com.bdt.ipet.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.model.Ong;

public class PagamentoDialog extends DialogFragment {

    private Ong ong;

    public static PagamentoDialog newInstance(Ong ong) {
        PagamentoDialog f = new PagamentoDialog();


        Bundle args = new Bundle();
        args.putParcelable("ong", (Parcelable)ong);
        f.setArguments(args);
        return f;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Ong ong = getArguments().getParcelable("ong");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_pagamento, null);
        setTextTv(R.id.tvNomeOngPagamento,ong.getNome(),view);
        builder.setView(view);
        return builder.create();
    }

    private void setTextTv(int idTextView, String text,View view) {
        TextView tv = view.findViewById(idTextView);
        tv.setText(text);
    }
}