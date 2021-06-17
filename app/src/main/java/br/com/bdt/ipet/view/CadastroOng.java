package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.CadastroController;
import br.com.bdt.ipet.data.api.ConsumerData;
import br.com.bdt.ipet.data.model.Estado;
import br.com.bdt.ipet.singleton.CadastroSingleton;
import br.com.bdt.ipet.util.GeralUtils;
import br.com.bdt.ipet.data.model.Ong;

import static br.com.bdt.ipet.util.GeralUtils.isValidInput;

public class CadastroOng extends AppCompatActivity {

    private EditText etNome;
    private EditText etEmail;
    private EditText etSenha;
    private EditText etWhatsapp;
    private EditText etCNPJ;
    private Button bCadastrar;
    private AutoCompleteTextView acUf;
    private AutoCompleteTextView acMunicipio;
    private CadastroSingleton cadastroSingleton;

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n", "SourceLockedOrientationActivity"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_ong);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar myToolbar = findViewById(R.id.tbNormal);
        TextView title = findViewById(R.id.toolbar_title);
        title.setText("Cadastro");
        acUf = findViewById(R.id.acUF);
        acMunicipio = findViewById(R.id.acMunicipio);
        cadastroSingleton=CadastroSingleton.getCadastroSingleton();

        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setNavigationOnClickListener(v -> onBackPressed());

        etNome = findViewById(R.id.etNome);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        etWhatsapp = findViewById(R.id.etWhatsapp);
        etCNPJ = findViewById(R.id.etCNPJ);
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

        CadastroController cadastroController = new CadastroController();

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

        String CNPJ = etCNPJ.getText().toString();
        if (!isValidInput(CNPJ, "text")) {
            etCNPJ.setError("Insira um CNPJ");
            return;
        }

        String uf = acUf.getText().toString();

        String cidade = acMunicipio.getText().toString();

        cadastroController.isValidCNPJ(CNPJ, getApplicationContext(), response -> {

            String status = response.getString("status");
            boolean valid = false;

            if(status.equals("OK")){
                String code = response.getJSONArray("atividade_principal")
                        .getJSONObject(0)
                        .getString("code");
                valid = code.equals("94.30-8-00");
            }

            if(!valid) {
                Ong ong = new Ong(nome, email, whatsapp, CNPJ, uf, cidade);
                cadastroSingleton.setOng(ong);
                cadastroSingleton.setSenha(senha);
                setEnableViews(false);
                cadastroController.saveUserOng(this);
            }else{
                etCNPJ.setError("Insira um CNPJ válido!");
            }

        });

    }

    public void setEnableViews(boolean op) {
        etNome.setEnabled(op);
        etEmail.setEnabled(op);
        etSenha.setEnabled(op);
        etWhatsapp.setEnabled(op);
        etCNPJ.setEnabled(op);
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