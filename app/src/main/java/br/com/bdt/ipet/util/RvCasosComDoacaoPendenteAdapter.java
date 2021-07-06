package br.com.bdt.ipet.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.DoacaoController;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.CasoComDoacao;

public class RvCasosComDoacaoPendenteAdapter extends RecyclerView.Adapter<RvCasosComDoacaoPendenteAdapter.CasosDoacaoPendenteViewHolder> {

    private final Context context;
    private final List<CasoComDoacao> casosOng;
    private final RvCasosComDoacaoPendenteAdapter.CasoOnClickListener onClickListener;
    private final DoacaoController doacaoController;

    public interface CasoOnClickListener {
        void onClickDetails(int position);
    }

    public RvCasosComDoacaoPendenteAdapter(Context context, List<CasoComDoacao> casosOng, RvCasosComDoacaoPendenteAdapter.CasoOnClickListener onClickListener) {
        this.context = context;
        this.casosOng = casosOng != null ? casosOng : new ArrayList<>();
        this.onClickListener = onClickListener;
        this.doacaoController = new DoacaoController();
    }

    @Override
    public RvCasosComDoacaoPendenteAdapter.@NotNull CasosDoacaoPendenteViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_lista_casos_com_doacao_pendente, viewGroup, false);

        return new RvCasosComDoacaoPendenteAdapter.CasosDoacaoPendenteViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return casosOng.size();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final @NotNull CasosDoacaoPendenteViewHolder holder, int position) {

        if (getItemCount() != 0) {

            CasoComDoacao casoComDoacao = casosOng.get(position);
            Caso caso = casoComDoacao.getCaso();

            holder.tvNomeCaso.setText(caso.getTitulo());
            holder.tvNomeAnimalCaso.setText(caso.getNomeAnimal());
            doacaoController.initListenerDoacoes(caso.getId(), () -> refreshQtdPendentes(holder.tvAviso, casoComDoacao, holder.itemView));
            holder.clCasoComDoacaoPendente.setOnClickListener(v -> {
                if(casosOng.get(position).getQtdDoacaoPendente() != 0) {
                    onClickListener.onClickDetails(position);
                }
            });

        }
    }

    @SuppressLint("SetTextI18n")
    public void refreshQtdPendentes(TextView tv, CasoComDoacao casoComDoacao, View itemView){
        doacaoController.getQtdPendente(casoComDoacao).addOnCompleteListener(task -> {
            int qtdDoacoesPendentes = casoComDoacao.getQtdDoacaoPendente();
            if(qtdDoacoesPendentes != 0){
                tv.setText("Há " + qtdDoacoesPendentes + " doações pendentes!");
                if(itemView.getVisibility() == View.GONE){
                    showItemView(itemView);
                }
            }else{
                hideItemView(itemView);
            }
        });
    }

    public void hideItemView(View itemView){
        itemView.setVisibility(View.GONE);
        itemView.getLayoutParams().height = 0;
    }

    public void showItemView(View itemView){
        itemView.setVisibility(View.VISIBLE);
        itemView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    public static class CasosDoacaoPendenteViewHolder extends RecyclerView.ViewHolder {

        TextView tvNomeCaso;
        TextView tvNomeAnimalCaso;
        TextView tvAviso;
        ConstraintLayout clCasoComDoacaoPendente;

        public CasosDoacaoPendenteViewHolder(View view) {
            super(view);
            tvNomeCaso = view.findViewById(R.id.tvNomeCaso);
            tvNomeAnimalCaso = view.findViewById(R.id.tvNomeAnimalCaso);
            tvAviso = view.findViewById(R.id.tvAviso);
            clCasoComDoacaoPendente = view.findViewById(R.id.clCasoComDoacaoPendente);
        }
    }

}
