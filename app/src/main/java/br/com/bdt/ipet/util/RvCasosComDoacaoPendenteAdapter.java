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
import br.com.bdt.ipet.data.model.CasoComDoacao;

public class RvCasosComDoacaoPendenteAdapter extends RecyclerView.Adapter<RvCasosComDoacaoPendenteAdapter.CasosDoacaoPendenteViewHolder> {

    private final Context context;
    private List<CasoComDoacao> casosOng;
    private final RvCasosComDoacaoPendenteAdapter.CasoOnClickListener onClickListener;

    public interface CasoOnClickListener {
        void onClickDetails(int position);
    }

    public RvCasosComDoacaoPendenteAdapter(Context context, List<CasoComDoacao> casosOng, RvCasosComDoacaoPendenteAdapter.CasoOnClickListener onClickListener) {
        this.context = context;
        this.casosOng = casosOng != null ? casosOng : new ArrayList<>();
        this.onClickListener = onClickListener;
    }

    public void setCasosOng(List<CasoComDoacao> casosOng) {
        this.casosOng = casosOng;
        notifyDataSetChanged();
    }

    @Override
    public RvCasosComDoacaoPendenteAdapter.CasosDoacaoPendenteViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_lista_casos_com_doacao_pendente, viewGroup, false);

        return new RvCasosComDoacaoPendenteAdapter.CasosDoacaoPendenteViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return casosOng != null ? casosOng.size() : 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RvCasosComDoacaoPendenteAdapter.CasosDoacaoPendenteViewHolder holder, int position) {
        DoacaoController doacaoController = new DoacaoController();
        if (getItemCount() != 0) {
            holder.tvNomeCaso.setText(casosOng.get(position).getCaso().getTitulo());
            holder.tvNomeAnimalCaso.setText(casosOng.get(position).getCaso().getNomeAnimal());
            doacaoController.getQtdPendente(casosOng.get(position)).addOnCompleteListener(task -> holder.tvAviso.setText("Há " + casosOng.get(position).getQtdDoacaoPendente().toString() + " doações pendentes!"));
            holder.clCasoComDoacaoPendente.setOnClickListener(v -> {
                if(casosOng.get(position).getQtdDoacaoPendente() != 0) {
                    onClickListener.onClickDetails(position);
                }
            });
        }
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
