package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.CasoController;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.singleton.CasoSingleton;
import br.com.bdt.ipet.util.RvTodosCasosOngAdapter;
import br.com.bdt.ipet.util.interfaces.IFilter;

public class QueroAjudarOng extends AppCompatActivity {

    private RecyclerView rvQueroAjudar;
    private RvTodosCasosOngAdapter rvTodosCasosOngAdapter;
    private CasoController casoController;
    private CasoSingleton casoSingleton;

    @SuppressLint({"SourceLockedOrientationActivity", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quero_ajudar_ong);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar myToolbar = findViewById(R.id.tbNormal);
        TextView title = findViewById(R.id.toolbar_title);
        TextView title_extra = findViewById(R.id.toolbar_extra);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationOnClickListener(v -> onBackPressed());
        myToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        title.setText("");
        title_extra.setText("");

        rvQueroAjudar = findViewById(R.id.rvQueroAjudar);
        rvQueroAjudar.setLayoutManager(new LinearLayoutManager(this));
        rvQueroAjudar.setItemAnimator(new DefaultItemAnimator());
        rvQueroAjudar.setHasFixedSize(true);

        casoController = new CasoController();
        casoSingleton = CasoSingleton.getCasoSingleton();

        casoController.initDataRecyclerView(casos -> {

            rvTodosCasosOngAdapter = new RvTodosCasosOngAdapter(getApplicationContext(), casos, position -> {
                Intent intent = new Intent(getApplicationContext(), DetalhesCasoActivity.class);
                intent.putExtra("casoOng", (Parcelable) casos.get(position));
                startActivity(intent);
            });

            rvQueroAjudar.setAdapter(rvTodosCasosOngAdapter);

            casoController.setiChanges(() -> {
                rvTodosCasosOngAdapter.notifyDataSetChanged();
                if(casoSingleton.getDadosFiltro().isClear()){
                    title_extra.setText("Total de Casos: "+ casoSingleton.textSizeCasos());
                }
            });

            casoController.listenerCasosAll();
        });

        casoSingleton.setiFilter(new IFilter() {
            @Override
            public void onFilter(List<Caso> casosFiltrados) {
                rvTodosCasosOngAdapter.setCasosOng(casosFiltrados);
                title_extra.setText("Total de Casos: " + casosFiltrados.size());
            }

            @Override
            public void onClearFilter() {
                rvTodosCasosOngAdapter.setCasosOng(casoSingleton.getCasos());
                title_extra.setText("Total de Casos: " + casoSingleton.textSizeCasos());
            }
        });

    }

    public void initFilter(View view){
        Intent intent = new Intent(getApplicationContext(), Filter.class);
        startActivity(intent);
    }

    public void voltar(View view){
        onBackPressed();
    }

}