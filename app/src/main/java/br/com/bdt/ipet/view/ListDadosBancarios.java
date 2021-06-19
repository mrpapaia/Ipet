package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.singleton.OngSingleton;
import br.com.bdt.ipet.util.RvDadosBancariosAdapter;

public class ListDadosBancarios extends AppCompatActivity {
    private RvDadosBancariosAdapter rvDadosBancariosAdapter;
    private OngSingleton ongSingleton;
    private RecyclerView rvDadosBancarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_dados_bancarios);
        ongSingleton = OngSingleton.getOngSingleton();
        Log.d("DadosBancarios",ongSingleton.getOng().getDadosBancarios().get(0).toString());
        rvDadosBancarios = findViewById(R.id.rvDadosBancarios);
        rvDadosBancarios.setLayoutManager(new LinearLayoutManager(this));
        rvDadosBancarios.setItemAnimator(new DefaultItemAnimator());
        rvDadosBancarios.setHasFixedSize(true);
        rvDadosBancariosAdapter= new RvDadosBancariosAdapter(getApplicationContext(),ongSingleton.getOng().getDadosBancarios(),null);
            rvDadosBancarios.setAdapter(rvDadosBancariosAdapter);
    }
}