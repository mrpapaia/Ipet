package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.FilterController;
import br.com.bdt.ipet.data.model.DadosFiltro;
import br.com.bdt.ipet.util.GeralUtils;
import br.com.bdt.ipet.util.Mask;

import java.util.Objects;

public class Filter extends AppCompatActivity {

    private EditText etValorMin;
    private EditText etValorMax;

    private AutoCompleteTextView acUf;
    private AutoCompleteTextView acMunicipio;
    private AutoCompleteTextView acOng;
    private FilterController filterController;

    @SuppressWarnings("ConstantConditions")
    @SuppressLint({"SourceLockedOrientationActivity", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar myToolbar = findViewById(R.id.tbNormal);
        TextView title = findViewById(R.id.toolbar_title);
        TextView title_extra = findViewById(R.id.toolbar_extra);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        myToolbar.setNavigationOnClickListener(v -> onBackPressed());
        myToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        title.setText("Filtro");
        title_extra.setText("Limpar");
        title_extra.setOnClickListener(this::limpar);

        initViews();
        initValues();
    }

    public void initViews(){
        etValorMin = findViewById(R.id.etValorMin);
        etValorMin.addTextChangedListener(Mask.insertCurrency(etValorMin));
        etValorMax = findViewById(R.id.etValorMax);
        etValorMax.addTextChangedListener(Mask.insertCurrency(etValorMax));
        acUf = findViewById(R.id.acUF);
        acMunicipio = findViewById(R.id.acMunicipio);
        GeralUtils.initAutoCompletUfCity(getApplicationContext(), acUf, acMunicipio);
        acOng = findViewById(R.id.acOng);
        filterController = new FilterController();
        initNomeOngs();
    }

    public void initNomeOngs(){
        if(filterController.haNomesOng()){
            setNomesOngAdapter();
        }else{
            filterController.requestNomeOngs().addOnCompleteListener(task -> setNomesOngAdapter());
        }
        acOng.setOnItemClickListener((parent, view, position, id) -> filterController.setarOngEscolhida(parent, position));
    }

    public void setNomesOngAdapter(){
        acOng.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,
                filterController.getOngs())
        );
    }

    public void initValues(){

        DadosFiltro dados = filterController.getDadosFiltro();

        if(dados.getMinValue() != 0.0){
            etValorMin.setText(GeralUtils.formatarValor(dados.getMinValue()));
        }

        if(dados.getMaxValue() != 0.0){
            etValorMax.setText(GeralUtils.formatarValor(dados.getMaxValue()));
        }

        acUf.setText(dados.getUf());
        acMunicipio.setText(dados.getCidade());

        acOng.setText(filterController.getNomeByEmailOng(dados.getEmailOng()));

        filterController.initValuesFilter(this);
    }

    public void selectEspecie(View view){
        filterController.selectEspecie(view.getId(), this);
    }

    public void filtrar(View view){

        String[] especies = filterController.especiesSelected();

        String strMinValue = Mask.unMaskBRL(etValorMin.getText().toString());
        String strMaxValue = Mask.unMaskBRL(etValorMax.getText().toString());

        Double minValue = GeralUtils.getDouble(strMinValue.equals("") ? "0.0" : strMinValue);
        Double maxValue = GeralUtils.getDouble(strMaxValue.equals("") ? "0.0" : strMaxValue);

        if(maxValue < minValue && !strMaxValue.equals("")){
            GeralUtils.setErrorInput(etValorMax, "Informe um valor máximo maior que o valor mínimo.");
            return;
        }

        String uf = acUf.getText().toString();
        if(!uf.equals("") && !GeralUtils.existeEstado(uf)){
            GeralUtils.setErrorInput(acUf, "Informe um Estado Válido");
            return;
        }

        String cidade = acMunicipio.getText().toString();
        if(!cidade.equals("") && uf.equals("")){
            GeralUtils.setErrorInput(acMunicipio, "Informe um Estado Válido Primeiro");
            return;
        }else if(!cidade.equals("") && !GeralUtils.existeCidade(cidade)){
            GeralUtils.setErrorInput(acMunicipio, "Informe um Município Válido");
            return;
        }

        String nomeOng = acOng.getText().toString();
        if(!nomeOng.equals("") && !GeralUtils.existeNomeOng(nomeOng)){
            GeralUtils.setErrorInput(acOng, "Informe um Nome de Ong Válida");
            return;
        }

        filterController.setDadosFiltro(especies, minValue, maxValue, uf, cidade);

        if(!filterController.getDadosFiltro().isClear()) {
            filterController.activeFilter();
        }

        onBackPressed();
    }

    public void voltar(View view){
        onBackPressed();
    }

    public void limpar(View view){
        filterController.clearFilter();
        GeralUtils.toast(getApplicationContext(), "Filtro(s) Removido(s).");
        finish();
    }

}