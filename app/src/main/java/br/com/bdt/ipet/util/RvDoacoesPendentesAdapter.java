package br.com.bdt.ipet.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.CasoController;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.Doacao;
import br.com.bdt.ipet.data.model.Ong;

public class RvDoacoesPendentesAdapter extends RecyclerView.Adapter< RvDoacoesPendentesAdapter.DoacaoPendenteViewHolder> {

    private final Context context;
    private List<Doacao> doacaoList;
    private final RvDoacoesPendentesAdapter.DoacaoOnClickListener onClickListener;

    public interface DoacaoOnClickListener {
        void onClickDetails(int position);
    }

    public RvDoacoesPendentesAdapter(Context context, List<Doacao> doacaoList,
                                     RvDoacoesPendentesAdapter.DoacaoOnClickListener onClickListener) {
        this.context = context;
        this.doacaoList = doacaoList != null ? doacaoList : new ArrayList<Doacao>();
        this.onClickListener = onClickListener;
    }



    @Override
    public  RvDoacoesPendentesAdapter.DoacaoPendenteViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_lista_de_doacoes_pendentes,
                viewGroup, false);
        return new RvDoacoesPendentesAdapter.DoacaoPendenteViewHolder(view);
    }



    @Override
    public int getItemCount() {
        return doacaoList != null ? doacaoList.size()  : 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final  RvDoacoesPendentesAdapter.DoacaoPendenteViewHolder holder, int position) {

        CasoController casoController =new CasoController();

            //Subtrai 1 posição pois a primeira é apenas outro layout com avisos



            Doacao doacao = doacaoList.get(position);
            if(doacao == null) return; //Evitando bugs



            holder.tvBancoDynamic.setText(doacao.getBanco());
            holder.tvDataDyanamic.setText(doacao.getData().toString());
            holder.tvTipoTransDyanamic.setText(doacao.getTipo());
            holder.tvValorDyanamic.setText(doacao.getValor().toString());


          holder.btConfirmarDoacao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("valtenis","clicou");
                  casoController.updateValor("valor",doacao.getValor(),position);
                    casoController.updateValor("arrecadado",doacao.getValor(),position);
                }
            });
        holder.btNaoRecebido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    //Overriden so that I can display custom rows in the recyclerview
    @Override
    public int getItemViewType(int position) {
        int viewType = 1; //Default is 1
        if (position == 0) viewType = 0; //if zero, it will be a header view
        return viewType;
    }

    public static class DoacaoPendenteViewHolder extends RecyclerView.ViewHolder {


        TextView tvBancoDynamic;
        TextView tvTipoTransDyanamic;
        TextView tvValorDyanamic;
        TextView tvDataDyanamic;
        Button btNaoRecebido;
        Button btConfirmarDoacao;
        public DoacaoPendenteViewHolder(View view) {
            super(view);

                tvBancoDynamic = view.findViewById(R.id.tvBancoDynamic);
                tvTipoTransDyanamic = view.findViewById(R.id.tvTipoTransDyanamic);
                tvValorDyanamic = view.findViewById(R.id.tvValorDyanamic);
                tvDataDyanamic = view.findViewById(R.id.tvDataDyanamic);
             btNaoRecebido = view.findViewById(R.id.btNaoRecebido);
             btConfirmarDoacao= view.findViewById(R.id.btConfirmarDoacao);

        }
    }

}
