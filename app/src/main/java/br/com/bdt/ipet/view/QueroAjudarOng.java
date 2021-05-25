package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.DadosFiltro;
import br.com.bdt.ipet.util.CasoUtils;
import br.com.bdt.ipet.util.RvTodosCasosOngAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class QueroAjudarOng extends AppCompatActivity {

    FirebaseFirestore db;
    List<Caso> casosOngs;
    RecyclerView rvQueroAjudar;
    RvTodosCasosOngAdapter rvTodosCasosOngAdapter;
    CasoUtils<RvTodosCasosOngAdapter.CasoViewHolder> casoUtils;
    TextView tvTotalCasos;
    public static Filter.DataFilter dataFilter;
    public static DadosFiltro dadosDoFiltro;
    List<Caso> casosFiltrados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quero_ajudar_ong);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        db = FirebaseFirestore.getInstance();
        casosOngs = new ArrayList<>();

        tvTotalCasos = findViewById(R.id.tvLimpar);

        //inicia as configurações do recyclerview
        rvQueroAjudar = findViewById(R.id.rvQueroAjudar);
        rvQueroAjudar.setLayoutManager(new LinearLayoutManager(this));
        rvQueroAjudar.setItemAnimator(new DefaultItemAnimator());
        rvQueroAjudar.setHasFixedSize(true);

        rvTodosCasosOngAdapter = new RvTodosCasosOngAdapter(this, casosOngs,
                new RvTodosCasosOngAdapter.CasoOnClickListener() {
                    @Override
                    public void onClickDetails(int position) {
                        Intent intent = new Intent(getApplicationContext(),
                                DetalhesCasoActivity.class);
                        intent.putExtra("casoOng",
                                dadosDoFiltro.isClear()
                                ? (Parcelable) casosOngs.get(position)
                                : (Parcelable) casosFiltrados.get(position)
                        );
                        startActivity(intent);
                    }
         });

        rvQueroAjudar.setAdapter(rvTodosCasosOngAdapter);

        //inicia a lógica do filtro
        dadosDoFiltro = new DadosFiltro(new String[0], 0.0, 0.0, "",
                "", -1, -1);
        casosFiltrados = new ArrayList<>();
        initCallbackFilter();

        //inicia o "gerenciador" dos casos no recyclerview
        casoUtils = new CasoUtils<>(db, casosOngs,
                rvTodosCasosOngAdapter, new CasoUtils.Changes() {
            //quanto houver requisições dentro do casoUtils, será coletado a quantidade de casos
            //foi usado callbacks para conseguir alterar o valor do textview da forma certa.
            @Override
            public void setarQuantidadeCasos(int qtd) {

                tvTotalCasos.setText(String.valueOf(qtd));

                if(!dadosDoFiltro.isClear()){
                    tvTotalCasos.setText(String.valueOf(casosFiltrados.size()));
                    rvQueroAjudar.scrollToPosition(0);
                }

            }
        }, false, null);

        //incia o listener dos casos
        casoUtils.listenerAllCasos();
    }

    public void initCallbackFilter(){

        dataFilter =  new Filter.DataFilter() {
            @Override
            public void executeFilter() {

                String[] especiesSelecteds = dadosDoFiltro.getEspecies();
                Double minValue = dadosDoFiltro.getMinValue();
                Double maxValue = dadosDoFiltro.getMaxValue();
                String uf = dadosDoFiltro.getUf();
                String cidade = dadosDoFiltro.getCidade();

                casosFiltrados.clear();

                for (Caso caso : casosOngs) {

                    if(filterEspecie(caso.getEspecie(), especiesSelecteds) &&
                       filterValor(caso.getValor(), minValue, maxValue) &&
                       filterUf(caso.getOng().getUf(), uf) &&
                       filterCidade(caso.getOng().getCidade(), cidade)
                    ) {
                        casosFiltrados.add(caso);
                    }

                }

                rvTodosCasosOngAdapter.setCasosOng(casosFiltrados);
                rvTodosCasosOngAdapter.notifyDataSetChanged();
                rvQueroAjudar.scrollToPosition(0);
                tvTotalCasos.setText(String.valueOf(casosFiltrados.size()));
            }
        };
    }

    public void voltar(View view){
        onBackPressed();
    }

    public void abrirFiltro(View view) {
        Intent intent = new Intent(this, Filter.class);
        startActivity(intent);
    }

    public boolean filterEspecie(String especieCaso, String[] especiesEscolhidas){

        if(especiesEscolhidas.length == 0){
            return true;
        }

        for(String especieEscolhida : especiesEscolhidas){
            if(especieCaso.equals(especieEscolhida)){
                return true;
            }
        }

        return false;
    }

    public boolean filterValor(Double valorCaso, Double valorMin, Double valorMax){

        //sem filtro de valor
        if(valorMin == 0.0 && valorMax == 0.0){
            return true;
        }

        //filtro de apenas valor mínimo
        if(valorMin != 0.0 && valorMax == 0.0){
            return valorCaso >= valorMin;
        }

        //filtro de apenas valor máximo
        if(valorMin == 0.0 && valorMax != 0.0){
            return valorCaso <= valorMax;
        }

        //filtro usando mínimo e máximo
        if(valorMin != 0.0 && valorMax != 0.0){
            return valorCaso >= valorMin && valorCaso <= valorMax;
        }

        //valor não apto, não passou por nenhuma condição
        return false;
    }

    public boolean filterUf(String ufCaso, String ufFiltro){

        //sem filtro de uf
        if(ufFiltro.equals("")){
            return true;
        }

        //verifica se o uf do caso bate com o uf do filtro
        return ufCaso.equals(ufFiltro);
    }

    public boolean filterCidade(String cidadeCaso, String cidadeFiltro){

        //sem filtro de cidade
        if(cidadeFiltro.equals("")){
            return true;
        }

        //verifica se a cidade do caso bate com a cidade do filtro
        return cidadeCaso.equals(cidadeFiltro);
    }

}