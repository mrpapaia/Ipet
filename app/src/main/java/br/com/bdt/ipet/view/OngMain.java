package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.AuthController;
import br.com.bdt.ipet.control.CasoController;
import br.com.bdt.ipet.control.OngMainController;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.singleton.OngSingleton;
import br.com.bdt.ipet.util.GeralUtils;
import br.com.bdt.ipet.util.RvCasoOngAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class OngMain extends AppCompatActivity {

    private FloatingActionButton btCriarCaso;
    private RecyclerView rvCasosOng;
    private RvCasoOngAdapter rvCasoOngAdapter;
    private DrawerLayout dLayout;
    private TextView tvNomeDaOng;
    private TextView tvNomeHeader;
    private ImageView ivUser;
    private OngSingleton ongSingleton;
    private CasoController casoController;

    @SuppressLint({"RtlHardcoded", "SourceLockedOrientationActivity", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ong_main);
        Toolbar tbMain = findViewById(R.id.tbMain);
        tbMain.setNavigationOnClickListener(view -> dLayout.openDrawer(Gravity.LEFT));
        ongSingleton = OngSingleton.getOngSingleton();

        TextView title = findViewById(R.id.toolbar_title);
        title.setText("iPet");
        title.setTextSize(26);
        tvNomeDaOng = findViewById(R.id.tvNomeDaOng);
        tvNomeDaOng.setText("");

        btCriarCaso = findViewById(R.id.btAddCase);
        btCriarCaso.setEnabled(false);

        rvCasosOng = findViewById(R.id.rvCasosOng);
        rvCasosOng.setLayoutManager(new LinearLayoutManager(this));
        rvCasosOng.setItemAnimator(new DefaultItemAnimator());
        rvCasosOng.setHasFixedSize(true);

        casoController = new CasoController();

        casoController.initDataRecyclerView(casos -> {

            rvCasoOngAdapter = new RvCasoOngAdapter(OngMain.this, casos, (position) -> {
                Log.d("Valtenis", "OngMain:InitAdapter " + this.getClass().getName());
                casoController.apagarCaso(casos.get(position).getCaso()).addOnSuccessListener(aVoid -> {
                    GeralUtils.toast(getApplicationContext(), "Caso apagado");
                }).addOnFailureListener(e -> {
                    GeralUtils.toast(getApplicationContext(), "Erro ao apagar, tente novamente mais tarde");
                });
            });

            rvCasosOng.setAdapter(rvCasoOngAdapter);
        });

        setNavigationDrawer();

        OngMainController ongMainController = new OngMainController();

        ongMainController.initOng().addOnCompleteListener(command ->  getDadosOng());
    }

    @SuppressLint("SetTextI18n")
    public void getDadosOng(){

        Ong ong = ongSingleton.getOng();

        if(ong.getImgPerfil()!=null){
            Picasso.get().load(ongSingleton.getOng().getImgPerfil()).into(ivUser);
        }
        tvNomeDaOng.setText("Bem-vinda, " + ong.getNome());

        tvNomeHeader.setText(ongSingleton.getOng().getNome());
        casoController.setiChanges(() -> rvCasoOngAdapter.notifyDataSetChanged());
        casoController.listenerCasosOng();

        btCriarCaso.setEnabled(true);
        btCriarCaso.setOnClickListener(v -> criarCaso());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        casoController.removeListener();
    }

    public void criarCaso(){

        Ong ong = ongSingleton.getOng();

        if(ong.getDadosBancarios() != null && ong.getDadosBancarios().size() > 0){
            startActivity(new Intent(getApplicationContext(), CriarCasoActivity.class));
        }else{
            GeralUtils.builderDialog(
                    OngMain.this,
                    //android.R.drawable.ic_dialog_alert,
                    R.drawable.ic_alerta,
                    "Ops.. Algo não está certo!",
                    ong.getNome() + ", não é possível criar um caso sem dados bancários, vamos cadastrar um banco ? ",
                    "Cadastrar Banco", (dialog1, buttonId) -> initCadastroBancario()
            ).show();
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
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