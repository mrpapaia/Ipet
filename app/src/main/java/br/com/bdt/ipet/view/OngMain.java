package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.CasoController;
import br.com.bdt.ipet.control.OngMainController;
import br.com.bdt.ipet.control.interfaces.IChanges;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.repository.CasoRepository;
import br.com.bdt.ipet.repository.interfaces.IRepository;
import br.com.bdt.ipet.singleton.OngSingleton;
import br.com.bdt.ipet.util.UserUtils;
import br.com.bdt.ipet.util.RvCasoOngAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class OngMain extends AppCompatActivity {

    private FloatingActionButton btCriarCaso;
    private TextView tvNomeDaOng;
    private RecyclerView rvCasosOng;
    private RvCasoOngAdapter rvCasoOngAdapter;
    private DrawerLayout dLayout;
    private Toolbar tbMain;
    private TextView tvNomeHeader;
    private TextView title;
    private ImageView ivUser;
    private OngMainController ongMainController;
    private OngSingleton ongSingleton;
    private CasoController casoController;

    @SuppressLint({"RtlHardcoded", "SourceLockedOrientationActivity", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_de_casos);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        tbMain=findViewById(R.id.tbMain);
        tbMain.setNavigationOnClickListener(view -> dLayout.openDrawer(Gravity.LEFT));
        ongMainController= new OngMainController();
        ongSingleton=OngSingleton.getOngSingleton();

        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("IPet");

        tvNomeDaOng = findViewById(R.id.tvNomeDaOng);
        tvNomeDaOng.setText("");

        btCriarCaso = findViewById(R.id.btAddCase);
        btCriarCaso.setEnabled(false); //desativa até que os dados da ong sejam carregados

        rvCasosOng = findViewById(R.id.rvCasosOng);
        rvCasosOng.setLayoutManager(new LinearLayoutManager(this));
        rvCasosOng.setItemAnimator(new DefaultItemAnimator());
        rvCasosOng.setHasFixedSize(true);

        casoController = new CasoController();
        IRepository<Caso> casoRepository = new CasoRepository(FirebaseFirestore.getInstance());

        casoController.initDataRecyclerViewOng(casos -> {

            rvCasoOngAdapter = new RvCasoOngAdapter(getApplicationContext(), casos, (position, tv) -> {

                tv.setEnabled(false);

                casoRepository.delete(casos.get(position).getId()).addOnSuccessListener(aVoid -> {
                            Toast.makeText(getApplicationContext(), "Caso apagado", Toast.LENGTH_LONG).show();
                            tv.setEnabled(true);
                }).addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Erro ao apagar, tente novamente mais tarde", Toast.LENGTH_LONG).show();
                });
            });

            rvCasosOng.setAdapter(rvCasoOngAdapter);
        });

        setNavigationDrawer();

        ongMainController.initOng().addOnCompleteListener(command ->  getDadosOng());
    }

    @SuppressLint("SetTextI18n")
    public void getDadosOng(){
        if(!ongSingleton.getOng().getImgPerfil().isEmpty()){
            //Lógica para ONG sem foto
        } else {
            Picasso.get().load(ongSingleton.getOng().getImgPerfil()).into(ivUser);
        }
        tvNomeDaOng.setText("Bem-vinda, " + ongSingleton.getOng().getNome());
        btCriarCaso.setEnabled(true); //dados da ong carregado, pode criar caso
        tvNomeHeader.setText(ongSingleton.getOng().getNome());
        casoController.setiChanges(sizeList -> rvCasoOngAdapter.notifyDataSetChanged());
        casoController.listenerCasosOng();
    }

    public void criarCaso(View view){
        Intent intent = new Intent(getApplicationContext(), CriarCasoActivity.class);
        startActivity(intent);
    }

    public void deslogar(View view){
        UserUtils.logoutUser();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private  void setNavigationDrawer() {
        dLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.navigation);
        navView.setItemIconTintList(null);
        View headerView = navView.getHeaderView(0);
        tvNomeHeader=headerView.findViewById(R.id.tvNomeHeader);
        tvNomeHeader.setText("");
        ivUser=headerView.findViewById(R.id.ivUser);

        navView.setNavigationItemSelectedListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if(itemId==R.id.menuItemDadosBancarios){
                Intent it = new Intent(getApplicationContext(), CadastroInfoBanco.class);
                it.putExtra("frag",4);
                startActivity(it);
                dLayout.closeDrawers();
            }else if(itemId==R.id.menuItemConfirmarDoacao){

            } else if (itemId == R.id.menuItemSair) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
            return false;
        });
    }

}