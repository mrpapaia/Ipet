package br.com.bdt.ipet.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.OngMainController;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.DadosBancario;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.singleton.OngSingleton;
import br.com.bdt.ipet.util.CasoUtils;
import br.com.bdt.ipet.util.UserUtils;
import br.com.bdt.ipet.util.RvCasoOngAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class OngMain extends AppCompatActivity {

    FloatingActionButton btCriarCaso;
    RecyclerView rvCasosOng;
    TextView tvNomeDaOng;

    String emailOng;
    Ong ong;
    FirebaseFirestore db;
    List<Caso> casosOng;

    RvCasoOngAdapter rvCasoOngAdapter;
    CasoUtils<RvCasoOngAdapter.CasoViewHolder> casoUtils;
    private DrawerLayout dLayout;
    private Toolbar tbMain;
    private TextView tvNomeHeader;
    private TextView title;
    private ImageView ivUser;
    private OngMainController ongMainController;
    private OngSingleton ongSingleton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_de_casos);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        tbMain=findViewById(R.id.tbMain);
        tbMain.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });
        ongMainController= new OngMainController();
        ongSingleton=OngSingleton.getOngSingleton();

        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("IPet");
        emailOng = UserUtils.getEmail();

        db = FirebaseFirestore.getInstance();

        casosOng = new ArrayList<>();

        tvNomeDaOng = findViewById(R.id.tvNomeDaOng);
        tvNomeDaOng.setText("");

        btCriarCaso = findViewById(R.id.btAddCase);
        btCriarCaso.setEnabled(false); //desativa até que os dados da ong sejam carregados

        rvCasosOng = findViewById(R.id.rvCasosOng);
        rvCasosOng.setLayoutManager(new LinearLayoutManager(this));
        rvCasosOng.setItemAnimator(new DefaultItemAnimator());
        rvCasosOng.setHasFixedSize(true);

        rvCasoOngAdapter = new RvCasoOngAdapter(this, casosOng,
                (position, tv) -> {
                    tv.setEnabled(false); //garante que o icone nao será clicável durante a op
                    apagarCaso(casosOng.get(position).getId(), tv);
                });

        rvCasosOng.setAdapter(rvCasoOngAdapter);

        setNavigationDrawer();

        ongMainController.initOng(emailOng).addOnCompleteListener(command ->  getDadosOng());


    }

    /*
    * Obtem os dados da Ong filtrada pelo email vindo do login
    * */

    public void getDadosOng(){
        if(ongSingleton.getOng().getImgPerfil().isEmpty()){

        }else {
            Picasso.get().load(ongSingleton.getOng().getImgPerfil()).into(ivUser);
        }
        tvNomeDaOng.setText("Bem-vinda, " + ongSingleton.getOng().getNome());
        btCriarCaso.setEnabled(true); //dados da ong carregado, pode criar caso
        tvNomeHeader.setText(ongSingleton.getOng().getNome());
        casoUtils = new CasoUtils<>(db, casosOng, rvCasoOngAdapter,
                null, true, emailOng);

        casoUtils.listenerCasosOng();
    }

    /*
    * Método setado no onClick do botão de criar caso, irá apenas iniciar a activity para criação
    * de um novo caso, passando os dados da ong atual
    * */
    public void criarCaso(View view){
        Intent intent = new Intent(getApplicationContext(), CriarCasoActivity.class);
        intent.putExtra("ong", (Parcelable)ong);
        startActivity(intent);
    }

    /*
    * Desloga o usuário atual presente no sistema de autenticação do firebase authenticator
    * e volta para a tela inicial
    * */
    public void deslogar(View view){
        UserUtils.logoutUser();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    /*
    * Método que apaga um documento de caso na sub-coleção casos do documento da ong atual pelo id
    * */
    public void apagarCaso(String id, final TextView tv){

        db.collection("ongs")
                .document(emailOng)
                .collection("casos")
                .document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Caso apagado",
                                Toast.LENGTH_LONG).show();
                        tv.setEnabled(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Erro ao apagar, " +
                                        "tente novamente mais tarde", Toast.LENGTH_LONG).show();
                    }
                });
    }

    /*
    * Método para fechar o app quando o botão de voltar for pressionado na tela de gerenciamento
    * de ong, forçando o usuário a deslogar caso deseje voltar nas opções da main activity.
    * */
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
    private Bitmap getImageBitmap(String url) throws IOException {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("TAG", "Error getting bitmap", e);
        }
        return bm;
    }
}