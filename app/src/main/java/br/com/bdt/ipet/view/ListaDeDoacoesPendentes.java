package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.DoacaoController;
import br.com.bdt.ipet.control.OngMainController;
import br.com.bdt.ipet.data.model.DadosBancario;
import br.com.bdt.ipet.singleton.CasoSingleton;
import br.com.bdt.ipet.util.RvCasosComDoacaoPendenteAdapter;
import br.com.bdt.ipet.util.RvDoacoesPendentesAdapter;

public class ListaDeDoacoesPendentes extends AppCompatActivity {
    private RvDoacoesPendentesAdapter rvDoacoesPendentesAdapter;
    private CasoSingleton casoSingleton;
    private RecyclerView rvDoacaoPendenter;
    private List<DadosBancario> dadosBancarioList;
    private  OngMainController ongMainController;
    private DrawerLayout dLayout;
    private TextView tvNomeCasoDynamic;
    private TextView tvNomeAnimalDynamic;
    private TextView tvMetaDynamic;
    private TextView tvArrecadadoDynamic;
    private ImageView ivUser;
    private DoacaoController doacaoController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_doacoes_pendentes);
        Toolbar myToolbar = findViewById(R.id.tbMain);
        TextView title = findViewById(R.id.toolbar_title);

        title.setText("Dados Bancarios");

        setSupportActionBar(myToolbar);
        doacaoController=new DoacaoController();
        casoSingleton = CasoSingleton.getCasoSingleton();
        ongMainController = new OngMainController();

        rvDoacaoPendenter = findViewById(R.id.rvDoacoesPendentes);

        rvDoacaoPendenter.setLayoutManager(new LinearLayoutManager(this));
        rvDoacaoPendenter.setItemAnimator(new DefaultItemAnimator());
        rvDoacaoPendenter.setHasFixedSize(true);
      tvNomeCasoDynamic=findViewById(R.id.tvNomeCasoDynamic);
       tvNomeAnimalDynamic=findViewById(R.id.tvNomeAnimalDynamic);
       tvMetaDynamic=findViewById(R.id.tvMetaDynamic);
         tvArrecadadoDynamic=findViewById(R.id.tvArrecadadoDynamic);
        Intent it = getIntent();
      int position = it.getIntExtra("position",-1);
        Log.d("Valtenis",casoSingleton.getCasos().get(position).getDoacaoList().toString());
        tvNomeCasoDynamic.setText(casoSingleton.getCasos().get(position).getCaso().getTitulo());
        tvNomeAnimalDynamic.setText(casoSingleton.getCasos().get(position).getCaso().getNomeAnimal());
        tvMetaDynamic.setText(casoSingleton.getCasos().get(position).getCaso().getValor().toString());
        tvArrecadadoDynamic.setText(casoSingleton.getCasos().get(position).getCaso().getValor().toString());
        rvDoacoesPendentesAdapter = new RvDoacoesPendentesAdapter(getApplicationContext(),casoSingleton.getCasos().get(position).getDoacaoList(),null);
        rvDoacaoPendenter.setAdapter(rvDoacoesPendentesAdapter);

    }
}