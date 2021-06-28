package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
        etValorMax = findViewById(R.id.etValorMax);
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

        etValorMin.setText((dados.getMinValue() == 0.0 ? "" : String.valueOf(dados.getMinValue())));
        etValorMax.setText((dados.getMaxValue() == 0.0 ? "" : String.valueOf(dados.getMaxValue())));
        acUf.setText(dados.getUf());
        acMunicipio.setText(dados.getCidade());

        filterController.initValuesFilter(this);
    }

    public void selectEspecie(View view){
        filterController.selectEspecie(view.getId(), this);
    }

    public void filtrar(View view){

        String[] especies = filterController.especiesSelected();
        Double minValue = GeralUtils.getDouble(etValorMin.getText().toString());
        Double maxValue = GeralUtils.getDouble(etValorMax.getText().toString());
        String uf = acUf.getText().toString();
        String cidade = acMunicipio.getText().toString();

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