package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.CadastroController;
import br.com.bdt.ipet.data.model.Banco;
import br.com.bdt.ipet.data.model.DadosBancario;
import br.com.bdt.ipet.singleton.CadastroSingleton;
import br.com.bdt.ipet.util.GeralUtils;
import br.com.bdt.ipet.util.Mask;

import static br.com.bdt.ipet.util.GeralUtils.isValidInput;

public class CadastroInfoBanco extends AppCompatActivity {

    private CadastroController cadastroController;
    private AutoCompleteTextView acBanco;
    private EditText etConta;
    private EditText etAgencia;
    private CheckBox cbHablitapix;
    private EditText etChavePix;
    private EditText etCNPJContaBanco;
    private CadastroSingleton cadastroSingleton;
    private boolean isAdd;
    private String email;


    @SuppressWarnings("ConstantConditions")
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_info_banco);
        cadastroController = new CadastroController();
        Toolbar myToolbar = findViewById(R.id.tbNormal);
        TextView title = findViewById(R.id.toolbar_title);
        TextView title_extra = findViewById(R.id.toolbar_extra);
        acBanco = findViewById(R.id.acBanco);
        etConta = findViewById(R.id.etConta);
        etAgencia = findViewById(R.id.etAgencia);
        cbHablitapix = findViewById(R.id.cbHabilitaPix);
        etChavePix = findViewById(R.id.etChavePix);
        etCNPJContaBanco = findViewById(R.id.etCNPJContaBanco);
        etCNPJContaBanco.addTextChangedListener(Mask.insert(Mask.CPF_MASK, etCNPJContaBanco));

        isAdd = getIntent().getBooleanExtra("isAdd", false);
        if (isAdd) {
            email=getIntent().getStringExtra("email");
            Button btPularEtapa = findViewById(R.id.btPularEtapa);
            btPularEtapa.setText("Cancelar");
        }

        title.setText("Dados Bancarios");
        title_extra.setText("");
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        myToolbar.setNavigationOnClickListener(v -> {

            onBackPressed();
        });

        etChavePix.setVisibility(View.INVISIBLE);
        cadastroSingleton = CadastroSingleton.getCadastroSingleton();
        initAutoComplet();
    }

    private void initAutoComplet() {

        cadastroController.buscarBancos().addOnCompleteListener(task -> {

            ArrayAdapter<Banco> adapterBancos = new ArrayAdapter<>(CadastroInfoBanco.this,
                    android.R.layout.simple_dropdown_item_1line, cadastroSingleton.getBancos());
            acBanco.setAdapter(adapterBancos);

        });

    }

    public void habilitaPix(View v) {
        if (cbHablitapix.isChecked()) {
            etChavePix.setVisibility(View.VISIBLE);
            return;
        }
        etChavePix.setVisibility(View.INVISIBLE);
    }

    public void finalizarCadastro(View v) {

        String banco = acBanco.getText().toString();
        if (!isValidInput(banco, "text")) {
            GeralUtils.setErrorInput(acBanco, "Informe um Banco");
            return;
        }

        String conta = etConta.getText().toString();
        if (!isValidInput(conta, "text")) {
            GeralUtils.setErrorInput(etConta, "Informe uma Conta");
            return;
        }

        String agencia = etAgencia.getText().toString();
        if (!isValidInput(agencia, "text")) {
            GeralUtils.setErrorInput(etAgencia, "Informe uma Agência");
            return;
        }

        String CNPJContaBanco = etCNPJContaBanco.getText().toString();
        if (!isValidInput(CNPJContaBanco, "text")) {
            GeralUtils.setErrorInput(etCNPJContaBanco, "Informe um CNPJ/CPF");
            return;
        }

        String chavePix = etChavePix.getText().toString();
        if(cbHablitapix.isChecked() && !isValidInput(chavePix, "text")){
            GeralUtils.setErrorInput(etChavePix, "Informe uma Chave Pix");
            return;
        }

        DadosBancario dadosBancario = new DadosBancario(banco, conta, agencia, chavePix, CNPJContaBanco);

        if(isAdd){
            cadastroController.updateDadosBancario(this, email, dadosBancario);
            GeralUtils.toast(this, "Banco criado com sucesso");
            return;
        }

        List<DadosBancario> listDadosBancario = new ArrayList<>();
        listDadosBancario.add(dadosBancario);

        cadastroSingleton.getOng().setDadosBancarios(listDadosBancario);
        cadastroController.saveDadosOng(this);
    }

    public void end(View v) {
        if(isAdd) {
            onBackPressed();
            return;
        }
        cadastroController.saveDadosOng(this);
    }

}