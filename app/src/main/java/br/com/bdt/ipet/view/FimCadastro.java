package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.singleton.CadastroSingleton;
import br.com.bdt.ipet.singleton.CasoSingleton;
import br.com.bdt.ipet.util.GeralUtils;

public class FimCadastro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fim_cadastro);
        GeralUtils.setFullscreen(this);

        boolean isCaso = getIntent().getBooleanExtra("isCaso", false);

        if(isCaso){
            ConstraintLayout constraintLayout = new ConstraintLayout(this);
            constraintLayout.setBackgroundResource(R.drawable.caso_adicionado_sucess);
            setContentView(constraintLayout);
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if(isCaso){
                clearCasoSingleton();
            }else{
                clearCadastroSingleton();
            }
            Intent intent = new Intent(getBaseContext(), isCaso ? OngMain.class : MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }, 3000);
    }

    public void clearCasoSingleton(){
        CasoSingleton casoSingleton = CasoSingleton.getCasoSingleton();
        casoSingleton.setCaso(null);
        casoSingleton.setUri(null);
    }
    public void clearCadastroSingleton(){
        CadastroSingleton cadastroSingleton = CadastroSingleton.getCadastroSingleton();
        cadastroSingleton.setOng(null);
        cadastroSingleton.setUri(null);
        cadastroSingleton.setSenha(null);
    }
}