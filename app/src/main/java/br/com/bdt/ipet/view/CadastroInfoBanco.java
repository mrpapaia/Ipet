package br.com.bdt.ipet.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.model.DadosBancario;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.singleton.CadastroSingleton;

public class CadastroInfoBanco extends AppCompatActivity {
    private Toolbar myToolbar;
    private TextView title;
    private AutoCompleteTextView acBanco;
    private EditText etConta;
    private EditText etAgencia;
    private CheckBox cbHablitapix;
    private EditText etChavePix;


    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private CadastroSingleton cadastroSingleton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_info_banco);
        myToolbar = (Toolbar) findViewById(R.id.tbNormal);
        title = (TextView) findViewById(R.id.toolbar_title);
        acBanco=findViewById(R.id.acBanco);
        etConta= findViewById(R.id.etConta);
        etAgencia=findViewById(R.id.etConta);
        cbHablitapix=findViewById(R.id.cbHabilitaPix);
        etChavePix=findViewById(R.id.etChavePix);
        title.setText("Dados Bancarios");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setNavigationOnClickListener(v -> onBackPressed());
        etChavePix.setVisibility(View.INVISIBLE);
        cadastroSingleton=CadastroSingleton.getCadastroSingleton();


    }


    public void hablitaPix(View v){
        if(cbHablitapix.isChecked()){
            etChavePix.setVisibility(View.VISIBLE);
            return;
        }
        etChavePix.setVisibility(View.INVISIBLE);
    }

    public void finalizarCadastro(View v){
        List<DadosBancario>listDadosBancario=new ArrayList<>();
        listDadosBancario.add(
                new DadosBancario(
                        acBanco.getText().toString(),
                        etConta.getText().toString(),
                        etAgencia.getText().toString(),
                        etChavePix.getText().toString()
                )
        );
       cadastroSingleton.getOng().setDadosBancarios(listDadosBancario);
       cadastroSingleton.criarUserOng(this);



    }
    public void end(View v){
        cadastroSingleton.criarUserOng(this);



    }



}