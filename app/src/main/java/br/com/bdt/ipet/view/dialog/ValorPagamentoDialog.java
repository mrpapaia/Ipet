package br.com.bdt.ipet.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.util.GeralUtils;
import br.com.bdt.ipet.util.Mask;

import static br.com.bdt.ipet.util.GeralUtils.isValidInput;

public class ValorPagamentoDialog extends DialogFragment {

    public static ValorPagamentoDialog newInstance(Caso caso) {
        ValorPagamentoDialog f = new ValorPagamentoDialog();
        Bundle args = new Bundle();
        args.putParcelable("caso", caso);
        f.setArguments(args);
        return f;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Caso caso = getArguments().getParcelable("caso");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_valor_pagamento, null);

        EditText etValorDoacao = view.findViewById(R.id.etValorDoacao);

        builder.setView(view);
        etValorDoacao.addTextChangedListener(Mask.insertCurrency(etValorDoacao));

        Button btnConfirmar = view.findViewById(R.id.bConfirmar);
        View.OnClickListener listenerConfirmar = v -> {

            String valorString = etValorDoacao.getText().toString().replaceAll("[R$.]", "").replaceAll("\\s","").replaceAll("[,]", ".");

            if(isValidInput(valorString, "double")){
                Double valor = Double.parseDouble(valorString);

                DialogFragment dialog = MetodoPagamentoDialog.newInstance(caso, valor);
                dialog.show(getActivity().getSupportFragmentManager(), "metodoPagamento");

                getActivity().getSupportFragmentManager().executePendingTransactions();
                dialog.getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                this.dismiss();
            }else{
                GeralUtils.setErrorInput(etValorDoacao, "Insira um valor válido");
            }

        };

        btnConfirmar.setOnClickListener(listenerConfirmar);

        return builder.create();
    }
}
