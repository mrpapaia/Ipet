package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.DoacaoController;

public class MainActivity extends AppCompatActivity {

    private DoacaoController doacaoController;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        doacaoController = new DoacaoController();
        getQtdConexoes();
    }

    public void openSouUmaOng(View view){
        Intent intent = new Intent(this, OngLogin.class);
        startActivity(intent);
    }

    public void openQueroAjudarOng(View view){
        Intent intent = new Intent(this, QueroAjudarOng.class);
        startActivity(intent);
    }

    public void getQtdConexoes(){
        doacaoController.getQuantidadeDoacoesAll().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot doc = task.getResult();
                assert doc != null;
                int quantidade = Objects.requireNonNull(doc.getLong("quantidade")).intValue();
                setTextQtdConexoes(quantidade);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getQtdConexoes(); //atualizar quantidade ao voltar para tela
    }

    public void setTextQtdConexoes(Integer qtd){
        TextView tvApresentacao = findViewById(R.id.tvApresentacao);
        String msg = "Total de " + qtd + " doações já realizadas";
        tvApresentacao.setText(msg);
    }
}