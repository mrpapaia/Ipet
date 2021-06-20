package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.OngMainController;
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

        rvDadosBancarios = findViewById(R.id.rvDadosBancarios);
        rvDadosBancarios.setLayoutManager(new LinearLayoutManager(this));
        rvDadosBancarios.setItemAnimator(new DefaultItemAnimator());
        rvDadosBancarios.setHasFixedSize(true);
        OngMainController ongMainController = new OngMainController();
        if(ongSingleton.getOng().getDadosBancarios()==null){

            ongMainController.initOng().addOnCompleteListener(command -> {
                rvDadosBancariosAdapter = new RvDadosBancariosAdapter(getApplicationContext(), ongSingleton.getOng().getDadosBancarios(), null);
                rvDadosBancarios.setAdapter(rvDadosBancariosAdapter);
            });
        }else{
        rvDadosBancariosAdapter = new RvDadosBancariosAdapter(getApplicationContext(), ongSingleton.getOng().getDadosBancarios(), null);
            rvDadosBancarios.setAdapter(rvDadosBancariosAdapter);

        }

    }

    public void addDadosBancario(View v) {
        Intent it = new Intent(getApplicationContext(), CadastroInfoBanco.class);
        it.putExtra("isAdd", true);
        it.putExtra("email",ongSingleton.getOng().getEmail());
        startActivity(it);
    }
}