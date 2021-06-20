package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.OngMainController;
import br.com.bdt.ipet.data.model.DadosBancario;
import br.com.bdt.ipet.singleton.OngSingleton;
import br.com.bdt.ipet.util.RvDadosBancariosAdapter;

public class ListDadosBancarios extends AppCompatActivity {
    private RvDadosBancariosAdapter rvDadosBancariosAdapter;
    private OngSingleton ongSingleton;
    private RecyclerView rvDadosBancarios;
    private List<DadosBancario> dadosBancarioList;
   private  OngMainController ongMainController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_dados_bancarios);
        Toolbar myToolbar = findViewById(R.id.tbNormal);
        TextView title = findViewById(R.id.toolbar_title);
        TextView title_extra = findViewById(R.id.toolbar_extra);
        title.setText("Dados Bancarios");
        title_extra.setText("");
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setNavigationOnClickListener(v -> onBackPressed());
        Objects.requireNonNull(myToolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        ongSingleton = OngSingleton.getOngSingleton();
        ongMainController = new OngMainController();

        rvDadosBancarios = findViewById(R.id.rvDadosBancarios);
        rvDadosBancarios.setLayoutManager(new LinearLayoutManager(this));
        rvDadosBancarios.setItemAnimator(new DefaultItemAnimator());
        rvDadosBancarios.setHasFixedSize(true);
        dadosBancarioList=new ArrayList<>();
        dadosBancarioList.addAll(ongSingleton.getOng().getDadosBancarios());

        ongMainController.listenner();
        rvDadosBancariosAdapter = new RvDadosBancariosAdapter(getApplicationContext(), dadosBancarioList, (position, btTrash) -> {

            ongMainController.updateDocRemoveFild(this,ongSingleton.getOng().getEmail(),dadosBancarioList.get(position));
            dadosBancarioList.remove(position);
            rvDadosBancariosAdapter.notifyItemRemoved(position);
        });
        rvDadosBancarios.setAdapter(rvDadosBancariosAdapter);
    }

    public void addDadosBancario(View v) {
        Intent it = new Intent(getApplicationContext(), CadastroInfoBanco.class);
        it.putExtra("isAdd", true);
        it.putExtra("email",ongSingleton.getOng().getEmail());

        startActivityForResult(it,1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if(resultCode == RESULT_OK) {
                Log.d("SaidaListenner2",OngSingleton.getOngSingleton().getOng().getDadosBancarios().toString());
                dadosBancarioList.clear();
                dadosBancarioList.addAll(ongSingleton.getOng().getDadosBancarios());
                rvDadosBancariosAdapter.notifyDataSetChanged();
            }
        }
    }
}