package br.com.bdt.ipet.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.AuthController;
import br.com.bdt.ipet.control.CasoController;
import br.com.bdt.ipet.control.OngMainController;
import br.com.bdt.ipet.repository.CasoRepository;
import br.com.bdt.ipet.repository.interfaces.IRepositoryCaso;
import br.com.bdt.ipet.singleton.OngSingleton;
import br.com.bdt.ipet.util.GeralUtils;
import br.com.bdt.ipet.util.RvCasoOngAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class OngMain extends AppCompatActivity {

    private FloatingActionButton btCriarCaso;
    private TextView tvNomeDaOng;
    private RecyclerView rvCasosOng;
    private RvCasoOngAdapter rvCasoOngAdapter;
    private DrawerLayout dLayout;
    private TextView tvNomeHeader;
    private ImageView ivUser;
    private OngSingleton ongSingleton;
    private CasoController casoController;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint({"RtlHardcoded", "SourceLockedOrientationActivity", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ong_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar tbMain = findViewById(R.id.tbMain);
        tbMain.setNavigationOnClickListener(view -> dLayout.openDrawer(Gravity.LEFT));
        ongSingleton = OngSingleton.getOngSingleton();

        TextView title = findViewById(R.id.toolbar_title);
        title.setText("iPet");

        tvNomeDaOng = findViewById(R.id.tvNomeDaOng);
        tvNomeDaOng.setText("");

        btCriarCaso = findViewById(R.id.btAddCase);
        btCriarCaso.setEnabled(false); //desativa até que os dados da ong sejam carregados

        rvCasosOng = findViewById(R.id.rvCasosOng);
        rvCasosOng.setLayoutManager(new LinearLayoutManager(this));
        rvCasosOng.setItemAnimator(new DefaultItemAnimator());
        rvCasosOng.setHasFixedSize(true);

        casoController = new CasoController();
        IRepositoryCaso casoRepository = new CasoRepository(FirebaseFirestore.getInstance());

        casoController.initDataRecyclerView(casos -> {

            rvCasoOngAdapter = new RvCasoOngAdapter(getApplicationContext(), casos, (position, tv) -> {
                tv.setEnabled(false);
                casoRepository.delete(casos.get(position).getId()).addOnSuccessListener(aVoid -> {
                    GeralUtils.toast(getApplicationContext(), "Caso apagado");
                    tv.setEnabled(true);
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
        if(ongSingleton.getOng().getImgPerfil()==null){
            //Lógica para ONG sem foto
        } else {
            Picasso.get().load(ongSingleton.getOng().getImgPerfil()).into(ivUser);
        }
        tvNomeDaOng.setText("Bem-vinda, " + ongSingleton.getOng().getNome());
        btCriarCaso.setEnabled(true); //dados da ong carregado, pode criar caso
        tvNomeHeader.setText(ongSingleton.getOng().getNome());
        casoController.setiChanges(() -> rvCasoOngAdapter.notifyDataSetChanged());
        casoController.listenerCasosOng();
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

    }

    public void initSair(){
        AuthController authController = new AuthController();
        authController.logout();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void criarCaso(View view){
        Intent intent = new Intent(getApplicationContext(), CriarCasoActivity.class);
        startActivity(intent);
    }

}