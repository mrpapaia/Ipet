package br.com.bdt.ipet.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.DadosBancario;

public class RvDadosBancariosAdapter extends RecyclerView.Adapter<RvDadosBancariosAdapter.DadosBancariosViewHolder> {

    private final Context context;
    private List<DadosBancario> dadosBancarios;
    private final RvDadosBancariosAdapter.DadosBancariosOnClickListener onClickListener;

    public interface DadosBancariosOnClickListener {
        void onClickTrash(int position, TextView tv);
    }

    public RvDadosBancariosAdapter(Context context, List<DadosBancario> dadosBancarios, RvDadosBancariosAdapter.DadosBancariosOnClickListener onClickListener) {
        this.context = context;
        this.dadosBancarios = dadosBancarios != null ? dadosBancarios : new ArrayList<>();
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public RvDadosBancariosAdapter.DadosBancariosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_list_dados_bancarios,
                viewGroup, false);
        return new RvDadosBancariosAdapter.DadosBancariosViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return dadosBancarios != null ? dadosBancarios.size() : 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RvDadosBancariosAdapter.DadosBancariosViewHolder holder, final int position) {

        DadosBancario dadosBancario = dadosBancarios.get(position);

        holder.tvNomeBanco.setText(dadosBancario.getBanco());
        holder.tvAgenciaContaBanco.setText("Agencia: "+dadosBancario.getAgencia());
        holder.tvContaBanco.setText("Conta: "+dadosBancario.getConta());
        holder.tvcpforcnpj.setText("CPF/CNPJ: "+dadosBancario.getCpfCNPJ());
        if(dadosBancario.getChavePix()!=null){
            holder.tvpix.setText("PIX: "+dadosBancario.getChavePix());
        }else{
            holder.tvpix.setText("");
        }

        //holder.tvpix.setOnClickListener(v -> onClickListener.onClickTrash(position, holder.trashTv));
    }

    public static class DadosBancariosViewHolder extends RecyclerView.ViewHolder {

        TextView tvNomeBanco;
        TextView tvAgenciaContaBanco;
        TextView tvContaBanco;
        TextView tvcpforcnpj;
        TextView tvpix;
        ImageView ivBank;
        View view;

        public DadosBancariosViewHolder(View view) {
            super(view);
            this.view = view;
            tvNomeBanco = view.findViewById(R.id.tvNomeBanco);
            tvAgenciaContaBanco = view.findViewById(R.id.tvAgenciaContaBanco);
            tvContaBanco = view.findViewById(R.id.tvContaBanco);
            tvcpforcnpj = view.findViewById(R.id.tvcpforcnpj);
            tvpix = view.findViewById(R.id.tvpix);
            ivBank= view.findViewById(R.id.ivBank);
        }
    }
}