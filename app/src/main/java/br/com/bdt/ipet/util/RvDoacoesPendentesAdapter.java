package br.com.bdt.ipet.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.Ong;

public class RvDoacoesPendentesAdapter extends RecyclerView.Adapter< RvDoacoesPendentesAdapter.DoacaoPendenteViewHolder> {

    private final Context context;
    private List<Caso> casosOng;
    private final RvDoacoesPendentesAdapter.DoacaoOnClickListener onClickListener;

    public interface DoacaoOnClickListener {
        void onClickDetails(int position);
    }

    public RvDoacoesPendentesAdapter(Context context, List<Caso> casosOng,
                                     RvDoacoesPendentesAdapter.DoacaoOnClickListener onClickListener) {
        this.context = context;
        this.casosOng = casosOng != null ? casosOng : new ArrayList<Caso>();
        this.onClickListener = onClickListener;
    }

    public void setCasosOng(List<Caso> casosOng) {
        this.casosOng = casosOng;
        notifyDataSetChanged();
    }

    @Override
    public  RvDoacoesPendentesAdapter.DoacaoPendenteViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        RvDoacoesPendentesAdapter.DoacaoPendenteViewHolder casoViewHolder = null;




        return casoViewHolder;
    }



    @Override
    public int getItemCount() {
        return casosOng != null ? casosOng.size() + 1 : 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final  RvDoacoesPendentesAdapter.DoacaoPendenteViewHolder holder, int position) {

        if(position != 0) {

            //Subtrai 1 posição pois a primeira é apenas outro layout com avisos
            final int p = position - 1;

            Caso caso = casosOng.get(p);
            if(caso == null) return; //Evitando bugs

            Ong ong = caso.getOng();
            if(ong == null) return; //Evitando bugs

            holder.tvOng.setText(caso.getOng().getNome());
            holder.tvTitulo.setText(caso.getTitulo());
            holder.tvDescricao.setText(caso.getDescricao());
            holder.tvValor.setText(String.valueOf(caso.getValor()));
            holder.tvAnimalData.setText(caso.getNomeAnimal() + " (" + caso.getEspecie() + ")");

            holder.tvMaisDetalhes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClickDetails(p);
                }
            });
        } else {
            holder.tvsubtitulo2.setText(
                    casosOng.size() == 0
                            ? "Não foi encontrado nenhum caso."
                            : "Escolha um dos casos abaixo e salve o dia."
            );
        }
    }

    //Overriden so that I can display custom rows in the recyclerview
    @Override
    public int getItemViewType(int position) {
        int viewType = 1; //Default is 1
        if (position == 0) viewType = 0; //if zero, it will be a header view
        return viewType;
    }

    public static class DoacaoPendenteViewHolder extends RecyclerView.ViewHolder {

        TextView tvOng;
        TextView tvTitulo;
        TextView tvDescricao;
        TextView tvValor;
        TextView tvAnimalData;
        TextView tvMaisDetalhes;
        TextView tvsubtitulo2;

        public DoacaoPendenteViewHolder(View view, int viewType) {
            super(view);
            if(viewType != 0) {
                tvOng = view.findViewById(R.id.tvOngData);
                tvTitulo = view.findViewById(R.id.tvTitleData);
                tvDescricao = view.findViewById(R.id.tvDescricaoData);
                tvValor = view.findViewById(R.id.tvValorData);
                tvAnimalData = view.findViewById(R.id.tvAnimalData);
                tvMaisDetalhes = view.findViewById(R.id.tvMaisDetalhes);
            }else{
                tvsubtitulo2 = view.findViewById(R.id.tvsubtitulo2);
            }
        }
    }

}
