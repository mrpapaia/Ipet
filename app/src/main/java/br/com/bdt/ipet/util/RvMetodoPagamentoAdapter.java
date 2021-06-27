package br.com.bdt.ipet.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.model.Banco;
import br.com.bdt.ipet.data.model.DadosBancario;

public class RvMetodoPagamentoAdapter extends RecyclerView.Adapter<RvMetodoPagamentoAdapter.MetodoPagamentoViewHolder> {

    private final Context context;
    private List<DadosBancario> dadosBancarios;
    private final RvMetodoPagamentoAdapter.MetodoPagamentoOnClickListener onClickListener;

    public interface MetodoPagamentoOnClickListener {
        void onClick (int index);
    }

    public RvMetodoPagamentoAdapter(Context context, List<DadosBancario> banco, MetodoPagamentoOnClickListener onClickListener) {
        this.context = context;
        this.dadosBancarios = banco;
        this.onClickListener = onClickListener;
    }

    @Override
    public RvMetodoPagamentoAdapter.MetodoPagamentoViewHolder onCreateViewHolder( ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_metodo_pagamento,
                viewGroup, false);
        return new RvMetodoPagamentoAdapter.MetodoPagamentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RvMetodoPagamentoAdapter.MetodoPagamentoViewHolder holder, final int position) {
        DadosBancario dadosbanco = dadosBancarios.get(position);

        holder.tvNomedoBanco.setText(dadosbanco.getBanco());

        if (dadosbanco.getChavePix().equals("") ) {
            holder.logoPix.setVisibility(View.INVISIBLE);
        }

        holder.view.setOnClickListener(view -> onClickListener.onClick(position));
    }

    @Override
    public int getItemCount() {
        return dadosBancarios != null ? dadosBancarios.size() : 0;
    }

    public static class MetodoPagamentoViewHolder extends RecyclerView.ViewHolder {

        TextView tvNomedoBanco;
        ImageView logoPix;
        View view;

        public MetodoPagamentoViewHolder(View view) {
            super(view);
            this.view = view;
            tvNomedoBanco = view.findViewById(R.id.tvNomedoBanco);
            logoPix = view.findViewById(R.id.ivlogoPix);
        }
    }

}
