package br.com.bdt.ipet.view;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.bdt.ipet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ImageView titulo;
    Button botao1, botao2;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        titulo= findViewById(R.id.ivTitulo);
        botao1= findViewById(R.id.bSouUmaOng);
        botao2= findViewById(R.id.bQueroAjudarOng);

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

    /*
     * Acessa a quantidade de conexões já realizadas (quando o usuário entra em contato com a ong)
     * que está dentro do documento "counter" da coleção "conexoes", é acessado em específico o
     * atributo quantidade, que é onde esse valor está sendo guardado. Quando houver resposta,
     * setará na textview de conexões desta tela.
     * */
    public void getQtdConexoes(){
        FirebaseFirestore.getInstance()
                .collection("conexoes")
                .document("counter")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Integer qtd = document.get("quantidade", Integer.class);

                            if (document.exists()) { //Se exisitir, le o valor e seta no textview
                                setTextQtdConexoes(qtd);
                            } else { //Caso não exista, irá criar com valor 0

                                HashMap<String, Integer> dataQuantidade = new HashMap<>();
                                dataQuantidade.put("quantidade", 0);

                                FirebaseFirestore.getInstance()
                                        .collection("conexoes")
                                        .document("counter")
                                        .set(dataQuantidade);

                                setTextQtdConexoes(0);
                            }
                        }
                    }
                });
    }

    /*
     * Ciclo de Vida para atualizar as informações de conexões quando o usuario voltar
     * a tela principal
     * */
    @Override
    protected void onResume() {
        super.onResume();
        getQtdConexoes();
    }

    /*
    * Recebe a quantidade de conexões e atualiza o texto da textview de informação sobre conexões.
    * */
    public void setTextQtdConexoes(Integer qtd){

        TextView tvApresentacao = findViewById(R.id.tvApresentacao);

        String msg = "Total de " + qtd + " conexões já realizadas";

        tvApresentacao.setText(msg);
    }
}