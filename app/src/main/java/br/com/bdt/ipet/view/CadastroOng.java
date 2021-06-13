package br.com.bdt.ipet.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.util.SpinnerUtils;
import br.com.bdt.ipet.data.model.Ong;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import static br.com.bdt.ipet.util.GeralUtils.heightTela;
import static br.com.bdt.ipet.util.GeralUtils.isValidInput;
import static br.com.bdt.ipet.util.GeralUtils.setMargins;
import static br.com.bdt.ipet.util.GeralUtils.toast;

public class CadastroOng extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    EditText etNome, etEmail, etSenha, etWhatsapp;
    TextView voltar;
    Spinner spUf, spCidade;
    Button bCadastrar;
    ImageView ivCadastro;
    private Toolbar myToolbar;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_ong);
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myToolbar= (Toolbar) findViewById(R.id.tbNormal);
        title=(TextView) findViewById(R.id.toolbar_title);
        title.setText("Cadastro");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setNavigationOnClickListener(v -> onBackPressed());


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        etNome = findViewById(R.id.etNome);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        etWhatsapp = findViewById(R.id.etWhatsapp);

     //   spUf = findViewById(R.id.spUf);
       // spCidade = findViewById(R.id.spCidade);

      /*  SpinnerUtils.confSpinnersUfCity(getApplicationContext(),
                spUf, "UF",
                spCidade, "Cidade", -1, -1);
*/
        voltar = findViewById(R.id.voltar);

        bCadastrar = findViewById(R.id.bCadastrar);

       // ivCadastro = findViewById(R.id.ivCadastro);

        setarInformacoes();
    }

    public void setarInformacoes() {
        if(heightTela(CadastroOng.this) < 1400){
            setMargins(ivCadastro,0, 80, 0, 0);
            setMargins(bCadastrar,0, 40, 0, 0);
        }
    }

    /*
    * Método executado no onClick do botão cadastrar
    * Irá extrair informações da interface de cadastro, criando um novo usuário e documento
    * por meio do método criarUserOng
    * */
    public void cadastrar(View view){

        String nome = etNome.getText().toString();
        if(!isValidInput(nome, "text")){
            etNome.setError("Insira o nome da Ong");
            return;
        }

        String email = etEmail.getText().toString();
        if(!isValidInput(email, "email")){
            etEmail.setError("Insira um email válido");
            return;
        }

        String senha = etSenha.getText().toString();
        if(!isValidInput(senha, "text")){
            etSenha.setError("Insira uma senha");
            return;
        }

        String whatsapp = etWhatsapp.getText().toString();
        if(!isValidInput(whatsapp, "number") || whatsapp.length() < 8){
            etWhatsapp.setError("Insira um telefone válido");
            return;
        }

        String uf="" ;/*= getDataOfSp(R.id.spUf);
        if(!isValidInput(uf, "text")){
            ((TextView) spUf.getSelectedView()).setError("");
            toast(getApplicationContext(), "Informe UF");
            return;
        }*/

       String cidade="" ;/* getDataOfSp(R.id.spCidade);
        if(!isValidInput(cidade, "text")){
            ((TextView) spCidade.getSelectedView()).setError("");
            toast(getApplicationContext(), "Informe Cidade");
            return;
        }*/

        Ong ong = new Ong(nome, email, whatsapp, uf, cidade);

        setEnableViews(false); //desativa as views enquanto o cadastro esta sendo realizado

        criarUserOng(ong, senha);
    }

    /*
     * Método para habilidar/desabilidar todas views da interface de cadastro ong
     * */
    public void setEnableViews(boolean op){
        etNome.setEnabled(op);
        etEmail.setEnabled(op);
        etSenha.setEnabled(op);
        etWhatsapp.setEnabled(op);
        spUf.setEnabled(op);
        spCidade.setEnabled(op);
        bCadastrar.setEnabled(op);
        voltar.setEnabled(op);
    }

    /*
    * Método que receberá todas as informações dos campos da tela de cadastro
    * Primeiro passo: cria um usuário no firebase authentication utilizando somente email e senha
    * Segundo passo: quando o usuário for criado com sucesso, salvará todas as informações menos
    * a senha, em um novo documento.
    * */
    public void criarUserOng(final Ong ong, String senha){
        mAuth.createUserWithEmailAndPassword(ong.getEmail(), senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            salvarDadosOng(ong);
                        } else {
                            setEnableViews(true); //ativa caso algo deu errado

                            String msgErro = "Erro no cadastro.";

                            Exception e = task.getException();

                            if(e != null){
                                msgErro += " (" + e.getMessage() + ")";
                            }

                            Toast.makeText(getApplicationContext(), msgErro,Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /*
    * Recebe um objeto ong e salva um documento na coleção ongs no bd firestore.
    * */
    public void salvarDadosOng(Ong ong){
        db.collection("ongs")
                .document(ong.getEmail()).set(ong)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Cadastro Relizado.",
                                Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                });
    }

    /*
     * Método que recebe o id de um Spinner e pega o conteudo selecionado
     * */
    private String getDataOfSp(int idSpinner){
        Spinner sp = findViewById(idSpinner);
        Object selected = sp.getSelectedItem();
        return selected == null ? "" : selected.toString();
    }

    /*
    * Simula a ação de apertar para voltar
    * */
    public void voltar(View view){
        onBackPressed();
    }
}