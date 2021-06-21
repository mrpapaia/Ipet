package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.CasoController;
import br.com.bdt.ipet.data.model.DadosFiltro;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.repository.OngRepository;
import br.com.bdt.ipet.singleton.CasoSingleton;
import br.com.bdt.ipet.singleton.OngSingleton;
import br.com.bdt.ipet.util.GeralUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Filter extends AppCompatActivity {

    private ImageView ivEspCao;
    private ImageView ivEspGato;
    private ImageView ivEspCoelho;
    private EditText etValorMin;
    private EditText etValorMax;
    private HashMap<Integer, Boolean> especiesEscolhidas;
    private CasoSingleton casoSingleton;
    private AutoCompleteTextView acUf;
    private AutoCompleteTextView acMunicipio;
    private AutoCompleteTextView acOng;
    private OngSingleton ongSingleton;

    @SuppressLint({"SourceLockedOrientationActivity", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        casoSingleton = CasoSingleton.getCasoSingleton();
        ongSingleton = OngSingleton.getOngSingleton();
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
        ivEspCao = findViewById(R.id.ivEspCao);
        ivEspGato = findViewById(R.id.ivEspGato);
        ivEspCoelho = findViewById(R.id.ivEspCoelho);
        etValorMin = findViewById(R.id.etValorMin);
        etValorMax = findViewById(R.id.etValorMax);
        acUf = findViewById(R.id.acUF);
        acMunicipio = findViewById(R.id.acMunicipio);
        GeralUtils.initAutoCompletUfCity(getApplicationContext(), acUf, acMunicipio);
        acOng = findViewById(R.id.acOng);
        initNomeOngs();
    }

    public void initNomeOngs(){

        if(ongSingleton.getOngs() == null){
            OngRepository ongRepository = new OngRepository(FirebaseFirestore.getInstance());

            ongRepository.findAll().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ongSingleton.setOngs(Objects.requireNonNull(task.getResult()).toObjects(Ong.class));
                    setNomesOngAdapter();
                }
            });

        }else{
            setNomesOngAdapter();
        }

        acOng.setOnItemClickListener((parent, view, position, id) -> {
            Ong ong = (Ong) parent.getItemAtPosition(position);
            casoSingleton.getDadosFiltro().setEmailOng(ong.getEmail());
        });

    }

    public void setNomesOngAdapter(){
        acOng.setAdapter(new ArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,
                ongSingleton.getOngs())
        );
    }

    public void initValues(){

        DadosFiltro dados = casoSingleton.getDadosFiltro();

        etValorMin.setText((dados.getMinValue() == 0.0 ? "" : String.valueOf(dados.getMinValue())));
        etValorMax.setText((dados.getMaxValue() == 0.0 ? "" : String.valueOf(dados.getMaxValue())));

        //Inicia os valores das espécies selecionadas com false(não selecionada)
        especiesEscolhidas = new HashMap<>();
        especiesEscolhidas.put(R.id.ivEspCao, false);
        especiesEscolhidas.put(R.id.ivEspGato, false);
        especiesEscolhidas.put(R.id.ivEspCoelho, false);

        //Verifica quais espécies estão ativas e seta true
        for(int i=0; i<dados.getEspecies().length; i++){
            switch (dados.getEspecies()[i]){
                case "Cachorro": especiesEscolhidas.put(R.id.ivEspCao, true); break;
                case "Gato": especiesEscolhidas.put(R.id.ivEspGato, true); break;
                case "Coelho": especiesEscolhidas.put(R.id.ivEspCoelho, true); break;
            }
        }

        changeImgEspecie(R.id.ivEspCao, especiesEscolhidas.get(R.id.ivEspCao));
        changeImgEspecie(R.id.ivEspGato, especiesEscolhidas.get(R.id.ivEspGato));
        changeImgEspecie(R.id.ivEspCoelho, especiesEscolhidas.get(R.id.ivEspCoelho));

        if(ongSingleton.getOngs() != null){
            acOng.setText(getNomeByEmailOng(dados.getEmailOng()));
        }

        acUf.setText(dados.getUf());
        acMunicipio.setText(dados.getCidade());
    }

    private String getNomeByEmailOng(String email){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            Ong ong = ongSingleton.getOngs()
                    .stream()
                    .filter(x -> x.getEmail().equals(email))
                    .findAny()
                    .orElse(null);

            return (ong != null) ? ong.getNome() : "";
        }

        return null;
    }

    public void filtrar(View view){

        String[] especies = especiesSelected();
        Double minValue = GeralUtils.getDouble(etValorMin.getText().toString());
        Double maxValue = GeralUtils.getDouble(etValorMax.getText().toString());
        String uf = acUf.getText().toString();
        String cidade = acMunicipio.getText().toString();

        DadosFiltro dadosFiltro = casoSingleton.getDadosFiltro();
        dadosFiltro.setEspecies(especies);
        dadosFiltro.setMinValue(minValue);
        dadosFiltro.setMaxValue(maxValue);
        dadosFiltro.setUf(uf);
        dadosFiltro.setCidade(cidade);

        if(!casoSingleton.getDadosFiltro().isClear()) {
            CasoController casoController = new CasoController();
            casoSingleton.getiFilter().onFilter(casoController.casosFiltrados());
        }

        onBackPressed();
    }

    @SuppressWarnings("ConstantConditions")
    public String[] especiesSelected(){

        List<String> especies = new ArrayList<>();

        Boolean espCao = especiesEscolhidas.get(R.id.ivEspCao);
        Boolean espGato = especiesEscolhidas.get(R.id.ivEspGato);
        Boolean espCoelho = especiesEscolhidas.get(R.id.ivEspCoelho);

        if(espCao) especies.add("Cachorro");
        if(espGato) especies.add("Gato");
        if(espCoelho) especies.add("Coelho");

        return especies.toArray(new String[0]);
    }

    @SuppressWarnings("ConstantConditions")
    public void selectEspecie(View view){
        Integer id = view.getId();
        Boolean isSelected = !especiesEscolhidas.get(id);
        especiesEscolhidas.put(id, isSelected);
        changeImgEspecie(id, isSelected);
    }

    @SuppressLint("NonConstantResourceId")
    public void changeImgEspecie(Integer id, Boolean selected){
        switch (id){
            case R.id.ivEspCao: ivEspCao.setBackgroundResource(
                    selected ? R.drawable.icone_dog2 : R.drawable.icone_dog1); break;

            case R.id.ivEspGato: ivEspGato.setBackgroundResource(
                    selected ? R.drawable.icone_cat2 : R.drawable.icone_cat1); break;

            case R.id.ivEspCoelho: ivEspCoelho.setBackgroundResource(
                    selected ? R.drawable.icone_bunny2 : R.drawable.icone_bunny1); break;
        }
    }

    public void voltar(View view){
        onBackPressed();
    }

    public void limpar(View view){
        casoSingleton.setDadosFiltro(null);
        casoSingleton.getiFilter().onClearFilter();
        GeralUtils.toast(getApplicationContext(), "Filtro(s) Removido(s).");
        finish();
    }

}