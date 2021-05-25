package br.com.bdt.ipet.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.bdt.ipet.R;
import br.com.bdt.ipet.util.GeralUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static br.com.bdt.ipet.util.GeralUtils.heightTela;
import static br.com.bdt.ipet.util.GeralUtils.setMargins;
import static br.com.bdt.ipet.util.GeralUtils.validateEmailFormat;

public class SouUmaOngActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText etEmail, etSenha;
    Button bLogin;
    TextView tvNaoTenhoCadastro, tvEsqueciSenha, voltar;
    ImageView ivTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sou_uma_ong);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        bLogin = findViewById(R.id.bLogin);
        tvNaoTenhoCadastro = findViewById(R.id.tvNaoTenhoCadastro);
        tvEsqueciSenha = findViewById(R.id.tvEsqueciSenha);
        voltar = findViewById(R.id.voltar);
        ivTitulo = findViewById(R.id.ivtitulo1);

        setarInformacoes();

        mAuth = FirebaseAuth.getInstance();
    }


    /*
    * Habilida/Desabilida as views desta tela por meio da variável op
    * */
    public void enableViews(boolean op){
        etEmail.setEnabled(op);
        etSenha.setEnabled(op);
        bLogin.setEnabled(op);
        tvNaoTenhoCadastro.setEnabled(op);
        tvEsqueciSenha.setEnabled(op);
        voltar.setEnabled(op);
    }

    /*
    * Método executado dentro do onClick do botão Login
    * Irá extrair as informações dos campos email e senha, repassando para o método realizarLogin
    * */
    public void login(View view){

        String email = etEmail.getText().toString();

        if(!validateEmailFormat(email)){
            GeralUtils.toast(getApplicationContext(), "Insira um email válido!");
            return;
        }

        String senha = etSenha.getText().toString();

        if(senha.equals("")){
            GeralUtils.toast(getApplicationContext(), "Insira uma senha!");
            return;
        }

        realizarLogin(email, senha);
    }

    /*
     * Realiza ajustes nas views para telas menores
     * */
    public void setarInformacoes() {

        int heighScreen = heightTela(SouUmaOngActivity.this);

        if(heighScreen < 1400){
            ivTitulo.getLayoutParams().width = (int)(heighScreen*0.34);
            setMargins(ivTitulo,0, -50, 0, 0);
        }
    }

    /*
    * Utilizando a autenticação do Firebase, o método recebe email e senha
    * Com os dados ele verificará se as credenciais estão corretas
    * Caso sim, o método listagemDeCasos será chamado para ir na tela de gerenciamento da ong
    * */
    public void realizarLogin(String email, String senha){

        enableViews(false); //desativa as views enquanto aguarda a requisição, para evitar bug

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            listagemDeCasos();
                        } else {
                            enableViews(true); //se deu falha, ativa as views para tentar dnv
                            GeralUtils.toast(getApplicationContext(),"Credenciais Inválidas!");
                        }
                    }
                });
    }

    /*
    * Caso o usuário esquecer a senha, esse método sera acionado, sendo necessário apenas o valor
    * do input email, caso o email exista no bd, será mandado um email com um link para alterar
    * a mesma.
    * */
    public void esqueciSenha(View view){

        final String emailAtual = etEmail.getText().toString();

        if(!validateEmailFormat(emailAtual)){
            GeralUtils.toast(getApplicationContext(), "Insira um email válido " +
                    "no campo E-mail para recuperar sua senha!");
            return;
        }

        mAuth.sendPasswordResetEmail(emailAtual)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        GeralUtils.toast(getApplicationContext(), "Email não encontrado " +
                                "no nosso sistema!");
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            GeralUtils.toast(getApplicationContext(),
                                    "Email para recuperação de senha enviado para: "
                                            + emailAtual);
                        }
                    }
                });
    }

    /*
     * Método chamado assim que as credenciais forem validadas
     * Serve para chamar a activity de listagem de casos passando a informação do email da ong
     * */
    public void listagemDeCasos(){
        Intent intent = new Intent(this, ListagemDeCasos.class);
        startActivity(intent);
    }

    /*
    * Método do onClick do TextView não tenho cadastro
    * Serve para chamar a activity de cadastro
    * */
    public void naoTenhoCadastro(View view){
        Intent intent = new Intent(this, CadastroOng.class);
        startActivity(intent);
    }

    /*
    * Método chamado quando clicado na setinha de voltar do proprio layout, simula o pressionamento
    * do botão de voltar do telefone
    * */
    public void voltar(View view){
        onBackPressed();
    }

}