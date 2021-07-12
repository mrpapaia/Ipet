package br.com.bdt.ipet.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

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
    public void onBindViewHolder(final RvDoacoesPendentesAdapter.@NotNull DoacaoPendenteViewHolder holder, int position) {

        CasoController casoController = new CasoController();
        Doacao doacao = doacaoList.get(position);

        if (doacao == null) return; //Evitando bugs

        holder.tvBancoDynamic.setText(doacao.getBanco());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        holder.tvDataDyanamic.setText(sdf.format(doacao.getData()));

        holder.tvTipoTransDyanamic.setText(doacao.getTipo());
        holder.tvValorDyanamic.setText(GeralUtils.formatarValor(doacao.getValor()));

        holder.btConfirmarDoacao.setOnClickListener(v -> {

            Caso caso = CasoSingleton.getCasoSingleton().getCasos().get(indexCaso).getCaso();

            double newValue = caso.getArrecadado() + doacao.getValor();

            casoController.updateValor("arrecadado", newValue, caso.getId())
                    .addOnCompleteListener(task -> {
                        doacaoController.delete(doacao.getId())
                                        .addOnCompleteListener(task1 -> {
                                            int idx_doacao = getIndexByIdDoacao(doacao.getId().getId());
                                            doacaoList.remove(idx_doacao);
                                            notifyItemRemoved(idx_doacao);
                                            doacaoController.updateQuantidadeDoacoesAll();
                                            updateDetails.onConfirm(newValue);
                                            Toast.makeText(context, "Doação confirmada", Toast.LENGTH_SHORT).show();
                                        });
                            }
                    );
        });

        holder.btNaoRecebido.setOnClickListener(v -> doacaoController.delete(doacao.getId())
                .addOnCompleteListener(task -> {
                    int idx_doacao = getIndexByIdDoacao(doacao.getId().getId());
                    doacaoList.remove(idx_doacao);
                    notifyItemRemoved(idx_doacao);
                    Toast.makeText(context, "Doação removida", Toast.LENGTH_SHORT).show();
                })
        );

    }

    public int getIndexByIdDoacao(String id){

        for(int i=0; i<doacaoList.size(); i++){
            if(doacaoList.get(i).getId().getId().equals(id)){
                return i;
            }
        }

        return -1;
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
