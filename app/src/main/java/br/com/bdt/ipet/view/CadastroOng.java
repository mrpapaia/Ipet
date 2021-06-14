package br.com.bdt.ipet.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.api.ConsumerData;
import br.com.bdt.ipet.data.api.DadosApi;
import br.com.bdt.ipet.data.model.DadosBancario;
import br.com.bdt.ipet.data.model.Estado;
import br.com.bdt.ipet.util.GeralUtils;
import br.com.bdt.ipet.util.SpinnerUtils;
import br.com.bdt.ipet.data.model.Ong;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static br.com.bdt.ipet.util.GeralUtils.heightTela;
import static br.com.bdt.ipet.util.GeralUtils.isValidInput;
import static br.com.bdt.ipet.util.GeralUtils.setMargins;
import static br.com.bdt.ipet.util.GeralUtils.toast;

public class CadastroOng extends AppCompatActivity {

    EditText etNome, etEmail, etSenha, etWhatsapp;

    Button bCadastrar;
    ImageView ivCadastro;
    private Toolbar myToolbar;
    private TextView title;
    private AutoCompleteTextView acUf;
    private AutoCompleteTextView acMunicipio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_ong);
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myToolbar = (Toolbar) findViewById(R.id.tbNormal);
        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Cadastro");
        acUf = findViewById(R.id.acUF);
        acMunicipio = findViewById(R.id.acMunicipio);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setNavigationOnClickListener(v -> onBackPressed());

        etNome = findViewById(R.id.etNome);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        etWhatsapp = findViewById(R.id.etWhatsapp);
        initAutoComplet();

        bCadastrar = findViewById(R.id.bCadastrar);
        //setarInformacoes();
    }

//    public void setarInformacoes() {
//        if (heightTela(CadastroOng.this) < 1400) {
//            setMargins(ivCadastro, 0, 80, 0, 0);
//            setMargins(bCadastrar, 0, 40, 0, 0);
//        }
//    }

    public void cadastrar(View view) {

        String nome = etNome.getText().toString();
        if (!isValidInput(nome, "text")) {
            etNome.setError("Insira o nome da Ong");
            return;
        }

        String email = etEmail.getText().toString();
        if (!isValidInput(email, "email")) {
            etEmail.setError("Insira um email válido");
            return;
        }

        String senha = etSenha.getText().toString();
        if (!isValidInput(senha, "text")) {
            etSenha.setError("Insira uma senha");
            return;
        }

        String whatsapp = etWhatsapp.getText().toString();
        if (!isValidInput(whatsapp, "number") || whatsapp.length() < 8) {
            etWhatsapp.setError("Insira um telefone válido");
            return;
        }

        String uf = acUf.getText().toString();

        String cidade = acMunicipio.getText().toString();
        List<DadosBancario> d= new ArrayList<>();
        d.add(new DadosBancario("caixa","11111","11","1"));
        Ong ong = new Ong(nome, email, whatsapp, uf, cidade, d);

        setEnableViews(false); //desativa as views enquanto o cadastro esta sendo realizado

        Intent it = new Intent(this, EnviarFoto.class);
        it.putExtra("ong", (Parcelable) ong);
        it.putExtra("senha",senha);
        startActivity(it);
    }

    /*
     * Método para habilidar/desabilidar todas views da interface de cadastro ong
     * */
    public void setEnableViews(boolean op) {
        etNome.setEnabled(op);
        etEmail.setEnabled(op);
        etSenha.setEnabled(op);
        etWhatsapp.setEnabled(op);
        acUf.setEnabled(op);
        acMunicipio.setEnabled(op);
        bCadastrar.setEnabled(op);

    }

    public void voltar(View view) {
        onBackPressed();
    }

    private void initAutoComplet(){

        new ConsumerData().getEstados(getApplicationContext(), estados -> {
            ArrayAdapter<Estado> adapterUF = new ArrayAdapter<>(CadastroOng.this,
                    android.R.layout.simple_dropdown_item_1line, estados);
            acUf.setAdapter(adapterUF);
        });

        acUf.setOnItemClickListener((parent, view, position, id) -> {

            Estado estado = (Estado)parent.getItemAtPosition(position);

            new ConsumerData().getCidades(getApplicationContext(), estado.getUf(), cidades -> {
                ArrayAdapter<String> adapterMunicipio = new ArrayAdapter<>(CadastroOng.this,
                    android.R.layout.simple_dropdown_item_1line, cidades);
                acMunicipio.setAdapter(adapterMunicipio);
            });

        });

        acMunicipio.setOnFocusChangeListener((view, b) -> {
            if(acUf.getText().toString().isEmpty()){
                GeralUtils.toast(getApplicationContext(), "Informe o UF primeiro para carregar as cidades!");
            }
        });

    }

}