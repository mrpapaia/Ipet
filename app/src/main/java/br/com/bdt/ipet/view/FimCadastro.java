package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.util.UserUtils;

public class FimCadastro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fim_cadastro);
        new Handler().postDelayed(() -> {
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                finish();

        }, 2000);
    }
}