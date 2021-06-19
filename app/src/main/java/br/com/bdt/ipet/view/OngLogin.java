package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.AuthController;
import br.com.bdt.ipet.util.GeralUtils;

import static br.com.bdt.ipet.util.GeralUtils.validateEmailFormat;

public class OngLogin extends AppCompatActivity {

    private EditText etEmail, etSenha;
    private Button bLogin;
    private TextView tvNaoTenhoCadastro;
    private TextView tvEsqueciSenha;
    private TextView voltar;
    private ImageView ivTitulo;
    private AuthController authController;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ong_login_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        bLogin = findViewById(R.id.bLogin);
        tvNaoTenhoCadastro = findViewById(R.id.tvNaoTenhoCadastro);
        tvEsqueciSenha = findViewById(R.id.tvEsqueciSenha);
        voltar = findViewById(R.id.voltar);
        ivTitulo = findViewById(R.id.ivtitulo1);
        authController = new AuthController();
    }

    public void enableViews(boolean op){
        etEmail.setEnabled(op);
        etSenha.setEnabled(op);
        bLogin.setEnabled(op);
        tvNaoTenhoCadastro.setEnabled(op);
        tvEsqueciSenha.setEnabled(op);
        voltar.setEnabled(op);
    }

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

        authController.login(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(this, OngMain.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        listagemDeCasos();
                    } else {
                        enableViews(true); //se deu falha, ativa as views para tentar dnv
                        GeralUtils.toast(getApplicationContext(),"Credenciais Inválidas!");
                    }
                });

    }

    public void esqueciSenha(View view){

        final String emailAtual = etEmail.getText().toString();

        if(!validateEmailFormat(emailAtual)){
            GeralUtils.toast(getApplicationContext(), "Insira um email válido no campo E-mail para recuperar sua senha!");
            return;
        }

        authController.recoveryPassword(emailAtual)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        GeralUtils.toast(getApplicationContext(),"Email para recuperação de senha enviado para: " + emailAtual);
                    }
                }).addOnFailureListener(e -> GeralUtils.toast(getApplicationContext(), "Email não encontrado no nosso sistema!"));
    }

    public void listagemDeCasos(){
        Intent intent = new Intent(this, OngMain.class);
        startActivity(intent);
    }

    public void naoTenhoCadastro(View view){
        Intent intent = new Intent(this, CadastroOng.class);
        startActivity(intent);
    }

    public void voltar(View view){
        onBackPressed();
    }

}