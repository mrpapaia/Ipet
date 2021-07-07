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
import android.util.Log;
import android.widget.TextView;

import java.util.Objects;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.DoacaoController;
import br.com.bdt.ipet.control.OngMainController;
import br.com.bdt.ipet.singleton.CasoSingleton;
import br.com.bdt.ipet.singleton.OngSingleton;
import br.com.bdt.ipet.util.RvCasosComDoacaoPendenteAdapter;

public class ListaCasosComDoacaoPendente extends AppCompatActivity {

    private RvCasosComDoacaoPendenteAdapter rvCasosComDoacaoPendenteAdapter;
    private CasoSingleton casoSingleton;
    private RecyclerView rvCasosComDoacaoPendenter;
    private OngMainController ongMainController;
    private DoacaoController doacaoController;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_casos_com_doacao_pendente);

        Toolbar myToolbar = findViewById(R.id.tbNormal);
        TextView title = findViewById(R.id.toolbar_title);
        TextView title_extra = findViewById(R.id.toolbar_extra);
        title.setText("Confirmar Doações");
        title_extra.setText("");
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setNavigationOnClickListener(v -> onBackPressed());
        Objects.requireNonNull(myToolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        doacaoController = new DoacaoController();
        casoSingleton = CasoSingleton.getCasoSingleton();
        ongMainController = new OngMainController();

        rvCasosComDoacaoPendenter = findViewById(R.id.rvCasosComDoacaoPendente);
        rvCasosComDoacaoPendenter.setLayoutManager(new LinearLayoutManager(this));
        rvCasosComDoacaoPendenter.setItemAnimator(new DefaultItemAnimator());
        rvCasosComDoacaoPendenter.setHasFixedSize(true);

        ongMainController.listenner();

        TextView tvQtdCasosPendentes = findViewById(R.id.tvQtdCasosPendentes);
        TextView tvMsgOlaOng = findViewById(R.id.tvMsgOlaOng);
        tvMsgOlaOng.setText("Olá, " + OngSingleton.getOngSingleton().getOng().getNome());

        rvCasosComDoacaoPendenteAdapter = new RvCasosComDoacaoPendenteAdapter(getApplicationContext(), casoSingleton.getCasos(), position -> {

            doacaoController.getAllByCaso(casoSingleton.getCasos().get(position)).addOnCompleteListener(command -> {
                Log.d("Valtenis", casoSingleton.getCasos().get(position).getDoacaoList().toString());
                Intent it = new Intent(getApplicationContext(), ListaDeDoacoesPendentes.class);
                it.putExtra("position", position);
                startActivity(it);
            });

        }, qtdCasos -> {
            tvQtdCasosPendentes.setText("No momento há " + qtdCasos + " caso(s) pendente(s)!");
        });

        rvCasosComDoacaoPendenter.setAdapter(rvCasosComDoacaoPendenteAdapter);
    }

}