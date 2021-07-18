package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.CadastroController;
import br.com.bdt.ipet.singleton.CadastroSingleton;
import br.com.bdt.ipet.singleton.EstadoSingleton;
import br.com.bdt.ipet.util.GeralUtils;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.util.Mask;

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

    @SuppressWarnings("ConstantConditions")
    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n", "SourceLockedOrientationActivity"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_ong);
        Toolbar myToolbar = findViewById(R.id.tbNormal);
        TextView title = findViewById(R.id.toolbar_title);
        TextView title_extra = findViewById(R.id.toolbar_extra);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationOnClickListener(v -> onBackPressed());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        title_extra.setText("");
        title.setText("Cadastro");
        acUf = findViewById(R.id.acUF);
        acMunicipio = findViewById(R.id.acMunicipio);
        GeralUtils.initAutoCompletUfCity(getApplicationContext(), acUf, acMunicipio);
        cadastroSingleton = CadastroSingleton.getCadastroSingleton();

        etNome = findViewById(R.id.etNome);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        etWhatsapp = findViewById(R.id.etWhatsapp);
        etCNPJ = findViewById(R.id.etCNPJ);
        bCadastrar = findViewById(R.id.bFiltrar);
        etWhatsapp.addTextChangedListener(Mask.insert(Mask.CELULAR_MASK, etWhatsapp));
        etCNPJ.addTextChangedListener(Mask.insert(Mask.CNPJ_MASK, etCNPJ));
    }

    public void cadastrar(View view) {

        CadastroController cadastroController = new CadastroController();

        String nome = etNome.getText().toString();
        if (!isValidInput(nome, "text")) {
            GeralUtils.setErrorInput(etNome, "Insira o nome da Ong");
            return;
        }

        String email = etEmail.getText().toString();
        if (!isValidInput(email, "email")) {
            GeralUtils.setErrorInput(etEmail, "Insira um email válido");
            return;
        }

        String senha = etSenha.getText().toString();
        if (!isValidInput(senha, "text")) {
            GeralUtils.setErrorInput(etSenha, "Insira uma senha");
            return;
        }

        String whatsapp = etWhatsapp.getText().toString();
        if (!isValidInput(whatsapp, "whatsapp")) {
            GeralUtils.setErrorInput(etWhatsapp, "Insira um telefone válido");
            return;
        }

        String CNPJ = etCNPJ.getText().toString();
        if (!isValidInput(CNPJ, "cnpj")) {
            GeralUtils.setErrorInput(etCNPJ, "Insira um CNPJ");
            return;
        }

        String uf = acUf.getText().toString();
        if (!isValidInput(uf, "text")) {
            GeralUtils.setErrorInput(acUf, "Informe um Estado");
            return;
        }else if(!GeralUtils.existeEstado(uf)){
            GeralUtils.setErrorInput(acUf, "Informe um Estado Válido");
            return;
        }

        String cidade = acMunicipio.getText().toString();
        if (!isValidInput(cidade, "text")) {
            GeralUtils.setErrorInput(acMunicipio, "Informe um Município");
            return;
        }else if(!GeralUtils.existeCidade(cidade)){
            GeralUtils.setErrorInput(acMunicipio, "Informe um Município Válido");
            return;
        }

        cadastroController.isValidCNPJ(Mask.unmask(CNPJ), getApplicationContext(), response -> {

            String status = response.getString("status");
            boolean valid = false;

            if(status.equals("OK")){

                String code = response.getJSONArray("atividade_principal")
                        .getJSONObject(0)
                        .getString("code");
                valid = code.equals("94.30-8-00");
            }

            valid = true; //Remover esta linha para validar corretamente um CNPJ

            if(valid) {
                Ong ong = new Ong(nome, email, Mask.unmask(whatsapp), CNPJ, uf, cidade);
                cadastroSingleton.setOng(ong);
                cadastroSingleton.setSenha(senha);
                cadastroController.saveUserOng(etEmail, this);
            }else{
                GeralUtils.setErrorInput(etCNPJ, "Insira um CNPJ válido!");
            }

        });

    }

//    public void setEnableViews(boolean op) {
//        etNome.setEnabled(op);
//        etEmail.setEnabled(op);
//        etSenha.setEnabled(op);
//        etWhatsapp.setEnabled(op);
//        etCNPJ.setEnabled(op);
//        acUf.setEnabled(op);
//        acMunicipio.setEnabled(op);
//        bCadastrar.setEnabled(op);
//    }

    public void voltar(View view) {
        onBackPressed();
    }
}