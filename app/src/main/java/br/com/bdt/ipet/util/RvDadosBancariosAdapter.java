package br.com.bdt.ipet.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.model.DadosBancario;
import br.com.bdt.ipet.singleton.OngSingleton;

public class RvDadosBancariosAdapter extends RecyclerView.Adapter<RvDadosBancariosAdapter.DadosBancariosViewHolder> {

    private final Context context;
    private List<DadosBancario> dadosBancarios;
    private final RvDadosBancariosAdapter.DadosBancariosOnClickListener onClickListener;

    public interface DadosBancariosOnClickListener {
        void onClickTrash(int position, ImageButton btTash);
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
        holder.tvpix.setText("PIX: " + (!dadosBancario.getChavePix().equals("") ? dadosBancario.getChavePix() : "Não Cadastrado"));
        holder.btTrash.setOnClickListener(v -> apagarDadosBancarios(dadosBancarios.indexOf(dadosBancario), holder.btTrash, dadosBancario.getBanco()));
    }

    public void apagarDadosBancarios(int position, ImageButton btTrash, String banco){

        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            if(which == DialogInterface.BUTTON_POSITIVE){
                onClickListener.onClickTrash(position, btTrash);
            }
        };

        GeralUtils.builderDialog(
                context,
                android.R.drawable.ic_dialog_alert,
                "Atenção",
                OngSingleton.getOngSingleton().getOng().getNome() + ", você deseja realmente apagar o banco (" + banco + ") ?",
                "Sim", dialogClickListener,
                "Não", dialogClickListener
        ).show();
    }

    public static class DadosBancariosViewHolder extends RecyclerView.ViewHolder {

        TextView tvNomeBanco;
        TextView tvAgenciaContaBanco;
        TextView tvContaBanco;
        TextView tvcpforcnpj;
        TextView tvpix;
        ImageView ivBank;
        ImageButton btTrash;
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
            btTrash=view.findViewById(R.id.btTrash);
        }
    }
}