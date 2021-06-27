package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.AuthController;
import br.com.bdt.ipet.control.OngMainController;
import br.com.bdt.ipet.data.model.DadosBancario;
import br.com.bdt.ipet.singleton.CasoSingleton;
import br.com.bdt.ipet.singleton.OngSingleton;
import br.com.bdt.ipet.util.RvCasosComDoacaoPendenteAdapter;
import br.com.bdt.ipet.util.RvDadosBancariosAdapter;

public class ListaCasosComDoacaoPendente extends AppCompatActivity {
    private RvCasosComDoacaoPendenteAdapter rvCasosComDoacaoPendenteAdapter;
    private CasoSingleton casoSingleton;
    private RecyclerView rvCasosComDoacaoPendenter;
    private List<DadosBancario> dadosBancarioList;
    private  OngMainController ongMainController;
    private DrawerLayout dLayout;
    private TextView tvNomeDaOng;
    private TextView tvNomeHeader;
    private ImageView ivUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_casos_com_doacao_pendente);
        Toolbar myToolbar = findViewById(R.id.tbMain);
        TextView title = findViewById(R.id.toolbar_title);

        title.setText("Dados Bancarios");

        setSupportActionBar(myToolbar);



        casoSingleton = CasoSingleton.getCasoSingleton();
        ongMainController = new OngMainController();

        rvCasosComDoacaoPendenter = findViewById(R.id.rvCasosComDoacaoPendente);
        rvCasosComDoacaoPendenter.setLayoutManager(new LinearLayoutManager(this));
        rvCasosComDoacaoPendenter.setItemAnimator(new DefaultItemAnimator());
        rvCasosComDoacaoPendenter.setHasFixedSize(true);


        ongMainController.listenner();
        rvCasosComDoacaoPendenteAdapter = new RvCasosComDoacaoPendenteAdapter(getApplicationContext(),casoSingleton.getCasos(),null);
        rvCasosComDoacaoPendenter.setAdapter(rvCasosComDoacaoPendenteAdapter);
        //setNavigationDrawer();
    }

    @SuppressLint("NonConstantResourceId")
    private  void setNavigationDrawer() {
        dLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.navigation);
        navView.setItemIconTintList(null);
        View headerView = navView.getHeaderView(0);
        tvNomeHeader = headerView.findViewById(R.id.tvNomeHeader);
        tvNomeHeader.setText("");
        ivUser = headerView.findViewById(R.id.ivUser);

        navView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.menuItemDadosBancarios: initCadastroBancario(); break;
                case R.id.menuItemConfirmarDoacao: initConfirmarDoacao(); break;
                case R.id.menuItemSair: initSair(); break;
            }
            return false;
        });
    }

    public void initCadastroBancario(){
        Intent it = new Intent(getApplicationContext(), ListDadosBancarios.class);
        it.putExtra("frag",4);
        startActivity(it);
        dLayout.closeDrawers();
    }

    public void initConfirmarDoacao(){
        Intent it = new Intent(getApplicationContext(), ListaCasosComDoacaoPendente.class);
        it.putExtra("frag",5);
        startActivity(it);
        dLayout.closeDrawers();
    }

    public void initSair(){
        AuthController authController = new AuthController();
        authController.logout();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}