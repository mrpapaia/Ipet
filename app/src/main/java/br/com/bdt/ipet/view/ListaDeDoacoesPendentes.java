package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Objects;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.DoacaoController;
import br.com.bdt.ipet.control.OngMainController;
import br.com.bdt.ipet.data.model.CasoComDoacao;
import br.com.bdt.ipet.singleton.CasoSingleton;
import br.com.bdt.ipet.util.GeralUtils;
import br.com.bdt.ipet.util.RvDoacoesPendentesAdapter;

public class ListaDeDoacoesPendentes extends AppCompatActivity {

    private RvDoacoesPendentesAdapter rvDoacoesPendentesAdapter;
    private CasoSingleton casoSingleton;
    private RecyclerView rvDoacaoPendenter;
    private  OngMainController ongMainController;
    private TextView tvNomeCasoDynamic;
    private TextView tvNomeAnimalDynamic;
    private TextView tvMetaDynamic;
    private TextView tvArrecadadoDynamic;
    private DoacaoController doacaoController;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_doacoes_pendentes);

        Toolbar myToolbar = findViewById(R.id.tbNormal);
        TextView title = findViewById(R.id.toolbar_title);
        TextView title_extra = findViewById(R.id.toolbar_extra);
        title.setText("Confirmar Doações");
        title.setTextSize(22);
        title_extra.setText("");
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setNavigationOnClickListener(v -> onBackPressed());
        Objects.requireNonNull(myToolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        doacaoController=new DoacaoController();
        casoSingleton = CasoSingleton.getCasoSingleton();
        ongMainController = new OngMainController();

        rvDoacaoPendenter = findViewById(R.id.rvDoacoesPendentes);
        rvDoacaoPendenter.setLayoutManager(new LinearLayoutManager(this));
        rvDoacaoPendenter.setItemAnimator(new DefaultItemAnimator());
        rvDoacaoPendenter.setHasFixedSize(true);
        tvNomeCasoDynamic = findViewById(R.id.tvNomeCasoDynamic);
        tvNomeAnimalDynamic = findViewById(R.id.tvNomeAnimalDynamic);
        tvMetaDynamic = findViewById(R.id.tvMetaDynamic);
        tvArrecadadoDynamic = findViewById(R.id.tvArrecadadoDynamic);

        Intent it = getIntent();
        int position = it.getIntExtra("position",-1);
        //Log.d("Valtenis",casoSingleton.getCasos().get(position).getDoacaoList().toString());

        CasoComDoacao caso = casoSingleton.getCasos().get(position);

        tvNomeCasoDynamic.setText(caso.getCaso().getTitulo());
        tvNomeAnimalDynamic.setText(caso.getCaso().getNomeAnimal() + " (" + caso.getCaso().getEspecie() + ")");
        tvMetaDynamic.setText(GeralUtils.formatarValor(caso.getCaso().getValor()));
        tvArrecadadoDynamic.setText(GeralUtils.formatarValor(caso.getCaso().getArrecadado()));

        rvDoacoesPendentesAdapter = new RvDoacoesPendentesAdapter(ListaDeDoacoesPendentes.this,
                caso.getDoacaoList(),
                doacaoController,
                position,
                newValue -> tvArrecadadoDynamic.setText(GeralUtils.formatarValor(newValue))
        );

        rvDoacaoPendenter.setAdapter(rvDoacoesPendentesAdapter);
    }

}