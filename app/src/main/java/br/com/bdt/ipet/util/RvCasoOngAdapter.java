package br.com.bdt.ipet.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.model.Caso;

import java.util.ArrayList;
import java.util.List;

public class RvCasoOngAdapter extends RecyclerView.Adapter<RvCasoOngAdapter.CasoViewHolder> {

    private final Context context;
    private List<Caso> casosOng;
    private final CasoOnClickListener onClickListener;

    public interface CasoOnClickListener {
        void onClickTrash(int position, TextView tv);
    }

    public RvCasoOngAdapter(Context context, List<Caso> casosOng, CasoOnClickListener onClickListener) {
        this.context = context;
        this.casosOng = casosOng != null ? casosOng : new ArrayList<>();
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public CasoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_card_caso,
                viewGroup, false);
        return new CasoViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return casosOng != null ? casosOng.size() : 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final CasoViewHolder holder, final int position) {

        Caso caso = casosOng.get(position);

        holder.tvTitulo.setText(caso.getTitulo());
        holder.tvDescricao.setText(caso.getDescricao());
        holder.tvAnimalData.setText(caso.getNomeAnimal() + " (" + caso.getEspecie() + ")");
        holder.tvValor.setText(String.valueOf(caso.getValor()));

        holder.trashTv.setOnClickListener(v -> onClickListener.onClickTrash(position, holder.trashTv));
    }

    public static class CasoViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitulo;
        TextView tvDescricao;
        TextView tvValor;
        TextView trashTv;
        TextView tvAnimalData;
        View view;

        public CasoViewHolder(View view) {
            super(view);
            this.view = view;
            tvTitulo = view.findViewById(R.id.tvTitleData);
            tvDescricao = view.findViewById(R.id.tvDescricaoData);
            tvValor = view.findViewById(R.id.tvValorData);
            trashTv = view.findViewById(R.id.trashTv);
            tvAnimalData = view.findViewById(R.id.tvAnimalData);
        }
    }
}