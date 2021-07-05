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
import br.com.bdt.ipet.control.DoacaoController;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.Doacao;
import br.com.bdt.ipet.singleton.CasoSingleton;

public class RvDoacoesPendentesAdapter extends RecyclerView.Adapter<RvDoacoesPendentesAdapter.DoacaoPendenteViewHolder> {

    public interface UpdateDetails {
        void onConfirm(double newValue);
    }

    private DoacaoController doacaoController;
    private final Context context;
    private List<Doacao> doacaoList;
    private int indexCaso;
    private UpdateDetails updateDetails;

    public RvDoacoesPendentesAdapter(Context context, List<Doacao> doacaoList,DoacaoController doacaoController, int indexCaso, UpdateDetails updateDetails) {
        this.context = context;
        this.doacaoList = doacaoList != null ? doacaoList : new ArrayList<>();
        this.doacaoController=doacaoController;
        this.indexCaso = indexCaso;
        this.updateDetails = updateDetails;
    }

    @Override
    public RvDoacoesPendentesAdapter.DoacaoPendenteViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_lista_de_doacoes_pendentes, viewGroup, false);

        return new RvDoacoesPendentesAdapter.DoacaoPendenteViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return doacaoList != null ? doacaoList.size() : 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RvDoacoesPendentesAdapter.DoacaoPendenteViewHolder holder, int position) {

        CasoController casoController = new CasoController();
        Doacao doacao = doacaoList.get(position);

        if (doacao == null) return; //Evitando bugs

        holder.tvBancoDynamic.setText(doacao.getBanco());
        holder.tvDataDyanamic.setText(doacao.getData().toString());
        holder.tvTipoTransDyanamic.setText(doacao.getTipo());
        holder.tvValorDyanamic.setText(doacao.getValor().toString());

        holder.btConfirmarDoacao.setOnClickListener(v -> {

            Caso caso = CasoSingleton.getCasoSingleton().getCasos().get(indexCaso).getCaso();
            double newValue = caso.getArrecadado() + doacao.getValor();

            Log.d("valtenis", "clicou");
            casoController.updateValor("arrecadado", newValue, caso.getId())
                    .addOnCompleteListener(task -> {
                        doacaoController.delete(doacao.getId())
                                        .addOnCompleteListener(task1 -> {
                                            doacaoList.remove(position);
                                            notifyItemRemoved(position);
                                            updateDetails.onConfirm(newValue);
                                        });
                            }
                    );
        });

        holder.btNaoRecebido.setOnClickListener(v -> doacaoController.delete(doacao.getId())
                .addOnCompleteListener(task -> {
                    doacaoList.remove(position);
                    notifyItemRemoved(position);
                })
        );

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
            btConfirmarDoacao = view.findViewById(R.id.btConfirmarDoacao);
        }
    }

}
