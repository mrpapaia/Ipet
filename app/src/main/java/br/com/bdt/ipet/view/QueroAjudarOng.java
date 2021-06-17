package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.CasoController;
import br.com.bdt.ipet.util.RvTodosCasosOngAdapter;

public class QueroAjudarOng extends AppCompatActivity {

    private RecyclerView rvQueroAjudar;
    private RvTodosCasosOngAdapter rvTodosCasosOngAdapter;
    private TextView tvTotalCasos;
    private CasoController casoController;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quero_ajudar_ong);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tvTotalCasos = findViewById(R.id.tvLimpar);
        rvQueroAjudar = findViewById(R.id.rvQueroAjudar);
        rvQueroAjudar.setLayoutManager(new LinearLayoutManager(this));
        rvQueroAjudar.setItemAnimator(new DefaultItemAnimator());
        rvQueroAjudar.setHasFixedSize(true);

        casoController = new CasoController();

        casoController.initDataRecyclerViewAll(casos -> {

            rvTodosCasosOngAdapter = new RvTodosCasosOngAdapter(getApplicationContext(), casos,
                    (RvTodosCasosOngAdapter.CasoOnClickListener) position -> {
                        Intent intent = new Intent(getApplicationContext(), DetalhesCasoActivity.class);
                        intent.putExtra("casoOng", (Parcelable) casos.get(position));
                        startActivity(intent);
            });

            rvQueroAjudar.setAdapter(rvTodosCasosOngAdapter);

            casoController.setiChanges((qtd) -> {
                rvTodosCasosOngAdapter.notifyDataSetChanged();
                tvTotalCasos.setText(String.valueOf(qtd));
            });

            casoController.listenerCasosAll();
        });

    }

    public void voltar(View view){
        onBackPressed();
    }

}