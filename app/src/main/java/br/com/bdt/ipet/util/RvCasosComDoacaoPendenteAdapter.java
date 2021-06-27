package br.com.bdt.ipet.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.Ong;

public class RvCasosComDoacaoPendenteAdapter extends RecyclerView.Adapter< RvCasosComDoacaoPendenteAdapter.CasosDoacaoPendenteViewHolder> {

    private final Context context;
    private List<Caso> casosOng;
    private final RvCasosComDoacaoPendenteAdapter.CasoOnClickListener onClickListener;

    public interface CasoOnClickListener {
        void onClickDetails(int position);
    }

    public RvCasosComDoacaoPendenteAdapter(Context context, List<Caso> casosOng,
                                           RvCasosComDoacaoPendenteAdapter.CasoOnClickListener onClickListener) {
        this.context = context;
        this.casosOng = casosOng != null ? casosOng : new ArrayList<Caso>();
        this.onClickListener = onClickListener;
    }

    public void setCasosOng(List<Caso> casosOng) {
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

        if(getItemCount()!=0){
            holder.tvNomeCaso.setText(casosOng.get(position).getTitulo());
            holder.tvNomeAnimalCaso.setText(casosOng.get(position).getNomeAnimal());
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


        public CasosDoacaoPendenteViewHolder(View view) {
            super(view);

                tvNomeCaso = view.findViewById(R.id.tvNomeCaso);
                tvNomeAnimalCaso = view.findViewById(R.id.tvNomeAnimalCaso);

        }
    }

}
