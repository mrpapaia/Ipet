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
    private final CountCasoListener countCasoListener;

    public interface CasoOnClickListener {
        void onClickDetails(int position);
    }

    public interface CountCasoListener {
        void onUpdateCount(int qtdCasos);
    }

    public RvCasosComDoacaoPendenteAdapter(Context context, List<CasoComDoacao> casosOng, CasoOnClickListener onClickListener, CountCasoListener countCasoListener) {
        this.context = context;
        this.casosOng = casosOng != null ? casosOng : new ArrayList<>();
        this.onClickListener = onClickListener;
        this.countCasoListener = countCasoListener;
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
            doacaoController.initListenerDoacoes(caso.getId(), () -> refreshQtdPendentes(holder, casoComDoacao));
            holder.clCasoComDoacaoPendente.setOnClickListener(v -> {
                if(casosOng.get(position).getQtdDoacaoPendente() != 0) {
                    onClickListener.onClickDetails(position);
                }
            });

        }

    }

    @SuppressLint("SetTextI18n")
    public void refreshQtdPendentes(CasosDoacaoPendenteViewHolder holder, CasoComDoacao casoComDoacao){
        doacaoController.getQtdPendente(casoComDoacao).addOnCompleteListener(task -> {
            int qtdDoacoesPendentes = casoComDoacao.getQtdDoacaoPendente();
            if(qtdDoacoesPendentes != 0){
                holder.tvAviso.setText("Há " + qtdDoacoesPendentes + " doações pendentes!");
                holder.show();
            }else{
                holder.hide();
            }
            countCasoListener.onUpdateCount(getQtdCasosComDoacoes());
        });
    }

    public int getQtdCasosComDoacoes(){
        int cont = 0;

        for(CasoComDoacao c : casosOng){
            if(c.getQtdDoacaoPendente() != 0){
                cont++;
            }
        }

        return cont;
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
            hide();
        }

        public void hide(){
            if(isVisible()) {
                this.itemView.setVisibility(View.GONE);
                this.itemView.getLayoutParams().height = 0;
            }
        }

        public void show(){
            if(!isVisible()) {
                this.itemView.setVisibility(View.VISIBLE);
                this.itemView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        }

        public boolean isVisible(){
            return this.itemView.getVisibility() == View.VISIBLE;
        }
    }

}
