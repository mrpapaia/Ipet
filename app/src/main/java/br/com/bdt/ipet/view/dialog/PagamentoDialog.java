package br.com.bdt.ipet.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.model.DadosBancario;
import br.com.bdt.ipet.data.model.Ong;

public class PagamentoDialog extends DialogFragment {

    private static ValorPagamentoDialog valorPagamentoDialog;
    private Ong ong;
    EditText et;

    public static PagamentoDialog newInstance(Ong ong, Double valor, int index) {
        PagamentoDialog f = new PagamentoDialog();


        Bundle args = new Bundle();
        args.putParcelable("ong", (Parcelable)ong);
        args.putDouble("valor", valor);
        args.putInt("indexBanco", index);
        System.out.println("valor 2" + valor);
        System.out.println("indexBanco" + index);
        f.setArguments(args);
        return f;
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Ong ong = getArguments().getParcelable("ong");
        Double valor = getArguments().getDouble("valor");
        int index = getArguments().getInt("indexBanco");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_pagamento, null);

        DadosBancario dadosBancario = ong.getDadosBancarios().get(index);

        et = view.findViewById(R.id.etValorPagar);
        et.setText(valor.toString());

        //et.setInputType(InputType.TYPE_NULL);
        et.setFocusable(false);
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("sdklfjn");
                et.setFocusableInTouchMode(true);
            }
        });

        setTextTv(R.id.tvNomeOngPagamento,ong.getNome(),view);
        setTextTv(R.id.tvChavePix, dadosBancario.getChavePix(), view);
        setTextTv(R.id.tvCNPJPagamento, dadosBancario.getCpfCNPJ(), view);
        setTextTv(R.id.tvInstituicaoPagamento, dadosBancario.getBanco(), view);
        setTextTv(R.id.tvAgenciaPagamento, dadosBancario.getAgencia(), view);
        setTextTv(R.id.tvContaPagamento, dadosBancario.getConta(), view);
        builder.setView(view);
        return builder.create();
    }

    private void setTextTv(int idTextView, String text,View view) {
        TextView tv = view.findViewById(idTextView);
        tv.setText(text);
    }
}