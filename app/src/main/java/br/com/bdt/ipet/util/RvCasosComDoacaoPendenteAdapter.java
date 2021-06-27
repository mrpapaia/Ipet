package br.com.bdt.ipet.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.DoacaoController;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.CasoComDoacao;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.singleton.CasoSingleton;

public class RvCasosComDoacaoPendenteAdapter extends RecyclerView.Adapter< RvCasosComDoacaoPendenteAdapter.CasosDoacaoPendenteViewHolder> {

    private final Context context;
    private List<CasoComDoacao> casosOng;
    private final RvCasosComDoacaoPendenteAdapter.CasoOnClickListener onClickListener;

    public interface CasoOnClickListener {
        void onClickDetails(int position);
    }

    public RvCasosComDoacaoPendenteAdapter(Context context, List<CasoComDoacao> casosOng,
                                           RvCasosComDoacaoPendenteAdapter.CasoOnClickListener onClickListener) {
        this.context = context;
        this.casosOng = casosOng != null ? casosOng : new ArrayList<CasoComDoacao>();
        this.onClickListener = onClickListener;
    }

    public void setCasosOng(List<CasoComDoacao> casosOng) {
        this.casosOng = casosOng;
        notifyDataSetChanged();
    }

    @Override
    public  RvCasosComDoacaoPendenteAdapter.CasosDoacaoPendenteViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.adapter_lista_casos_com_doacao_pendente,
                viewGroup, false);
        return new RvCasosComDoacaoPendenteAdapter.CasosDoacaoPendenteViewHolder(view);
    }



    @Override
    public int getItemCount() {
        return casosOng != null ? casosOng.size()  : 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final  RvCasosComDoacaoPendenteAdapter.CasosDoacaoPendenteViewHolder holder, int position) {
        DoacaoController doacaoController = new DoacaoController();
        if(getItemCount()!=0){
            holder.tvNomeCaso.setText(casosOng.get(position).getCaso().getTitulo());
            holder.tvNomeAnimalCaso.setText(casosOng.get(position).getCaso().getNomeAnimal());
            doacaoController.getQtdPendente(casosOng.get(position)).addOnCompleteListener(task -> holder.tvAviso.setText("Há "+casosOng.get(position).getQtdDoacaoPendente().toString()+" doações pendentes!"));
            holder.clCasoComDoacaoPendente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClickDetails(position);
                }
            });
        }




    }

    //Overriden so that I can display custom rows in the recyclerview
    @Override
    public int getItemViewType(int position) {
        int viewType = 1; //Default is 1
        if (position == 0) viewType = 0; //if zero, it will be a header view
        return viewType;
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
                tvAviso= view.findViewById(R.id.tvAviso);
            clCasoComDoacaoPendente=view.findViewById(R.id.clCasoComDoacaoPendente);
        }
    }

}
