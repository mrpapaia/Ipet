package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.singleton.CadastroSingleton;
import br.com.bdt.ipet.util.GeralUtils;

public class FimCadastro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fim_cadastro);
        GeralUtils.setFullscreen(this);
        clearCadastroSingleton();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            finish();
        }, 2000);
    }

    public void clearCadastroSingleton(){
        CadastroSingleton cadastroSingleton = CadastroSingleton.getCadastroSingleton();
        cadastroSingleton.setOng(null);
        cadastroSingleton.setUri(null);
        cadastroSingleton.setSenha(null);
    }
}