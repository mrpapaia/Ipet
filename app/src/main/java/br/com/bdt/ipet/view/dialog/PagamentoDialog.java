package br.com.bdt.ipet.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.PagamentoController;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.DadosBancario;
import br.com.bdt.ipet.data.model.Doacao;

public class PagamentoDialog extends DialogFragment {

    public static PagamentoDialog newInstance(Caso caso, Double valor, int index) {
        PagamentoDialog f = new PagamentoDialog();
        Bundle args = new Bundle();
        args.putParcelable("caso", caso);
        args.putDouble("valor", valor);
        args.putInt("indexBanco", index);
        f.setArguments(args);
        return f;
    }

    @SuppressWarnings("ConstantConditions")
    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Caso caso = getArguments().getParcelable("caso");
        double valor = getArguments().getDouble("valor");
        int index = getArguments().getInt("indexBanco");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_pagamento, null);

        DadosBancario dadosBancario = caso.getOng().getDadosBancarios().get(index);

        EditText et = view.findViewById(R.id.etValorPagar);
        et.setText(Double.toString(valor));

        et.setFocusable(false);
        et.setOnClickListener(view1 -> {
            System.out.println("sdklfjn");
            et.setFocusableInTouchMode(true);
        });

        setTextTv(R.id.tvNomeOngPagamento,caso.getOng().getNome(), view);
        setTextTv(R.id.tvChavePix, dadosBancario.getChavePix(), view);
        setTextTv(R.id.tvCNPJPagamento, dadosBancario.getCpfCNPJ(), view);
        setTextTv(R.id.tvInstituicaoPagamento, dadosBancario.getBanco(), view);
        setTextTv(R.id.tvAgenciaPagamento, dadosBancario.getAgencia(), view);
        setTextTv(R.id.tvContaPagamento, dadosBancario.getConta(), view);

        Spinner spMetodoPagamento = view.findViewById(R.id.spMetodoPagamento);

        List<String> meiosPagamento = new ArrayList<>();

        meiosPagamento.add("TED");

        if(!dadosBancario.getChavePix().equals("")){
            meiosPagamento.add("Pix");
        }

        spMetodoPagamento.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, meiosPagamento));

        spMetodoPagamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent2, View view2, int position, long id) {

                View dividerPix = view.findViewById(R.id.divider8);
                TextView tvPix = view.findViewById(R.id.textView19);
                TextView tvChavePix = view.findViewById(R.id.tvChavePix);
                ImageButton copyPix = view.findViewById(R.id.imageButton5);
                View dividerCNPJ = view.findViewById(R.id.divider9);

                String meioPagamento = meiosPagamento.get(position);
                boolean isPix = meioPagamento.equals("Pix");
                int isVisible = isPix ? View.VISIBLE : View.INVISIBLE;

                dividerPix.setVisibility(isVisible);
                tvPix.setVisibility(isVisible);
                tvChavePix.setVisibility(isVisible);
                copyPix.setVisibility(isVisible);
                dividerCNPJ.setVisibility(isVisible);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button btConfirmar = view.findViewById(R.id.btConfirmar);

        btConfirmar.setOnClickListener(v -> {

            String bancoDoacao = dadosBancario.getBanco();
            String metodoDoacao = spMetodoPagamento.getSelectedItem().toString();
            double valorDoacao = Double.parseDouble(et.getText().toString());
            Date dataDoacao = new Date();

            Doacao doacao = new Doacao(bancoDoacao, metodoDoacao, valorDoacao, dataDoacao);
            System.out.println(doacao.toString());

            PagamentoController pagamentoController = new PagamentoController();
            pagamentoController.saveDoacao(caso, doacao);

            DialogFragment dialog = new FimPagamento();

            dialog.show(getActivity().getSupportFragmentManager(), "fimPagamento");
            getActivity().getSupportFragmentManager().executePendingTransactions();

            dialog.getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            this.dismiss();
        });

        builder.setView(view);

        return builder.create();
    }

    private void setTextTv(int idTextView, String text,View view) {
        TextView tv = view.findViewById(idTextView);
        tv.setText(text);
    }
}