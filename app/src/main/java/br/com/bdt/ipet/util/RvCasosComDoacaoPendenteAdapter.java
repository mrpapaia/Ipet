package br.com.bdt.ipet.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.DoacaoController;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.CasoComDoacao;
import br.com.bdt.ipet.data.model.Doacao;

public class RvCasosComDoacaoPendenteAdapter extends RecyclerView.Adapter<RvCasosComDoacaoPendenteAdapter.CasosDoacaoPendenteViewHolder> {

    private final Context context;
    private List<CasoComDoacao> casosOng;
    private final RvCasosComDoacaoPendenteAdapter.CasoOnClickListener onClickListener;
    private DoacaoController doacaoController;

    public interface CasoOnClickListener {
        void onClickDetails(int position);
    }

    public RvCasosComDoacaoPendenteAdapter(Context context, List<CasoComDoacao> casosOng, RvCasosComDoacaoPendenteAdapter.CasoOnClickListener onClickListener) {
        this.context = context;
        this.casosOng = casosOng != null ? casosOng : new ArrayList<>();
        this.onClickListener = onClickListener;
        this.doacaoController = new DoacaoController();
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
    public void onBindViewHolder(final CasosDoacaoPendenteViewHolder holder, int position) {

        if (getItemCount() != 0) {

            CasoComDoacao casoComDoacao = casosOng.get(position);
            Caso caso = casoComDoacao.getCaso();

            holder.tvNomeCaso.setText(caso.getTitulo());
            holder.tvNomeAnimalCaso.setText(caso.getNomeAnimal());
            refreshQtdPendentes(holder.tvAviso, casoComDoacao);
            holder.clCasoComDoacaoPendente.setOnClickListener(v -> {
                if(casosOng.get(position).getQtdDoacaoPendente() != 0) {
                    onClickListener.onClickDetails(position);
                }
            });

            FirebaseFirestore.getInstance().collection("/ongs/"+caso.getOng().getEmail()+"/casos/"+caso.getId()+"/doacoes").addSnapshotListener((value, error) -> {
                refreshQtdPendentes(holder.tvAviso, casoComDoacao);
            });

        }
    }

    @SuppressLint("SetTextI18n")
    public void refreshQtdPendentes(TextView tv, CasoComDoacao casoComDoacao){
        doacaoController.getQtdPendente(casoComDoacao).addOnCompleteListener(task -> {
            tv.setText("Há " + casoComDoacao.getQtdDoacaoPendente() + " doações pendentes!");
        });
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
