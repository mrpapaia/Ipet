package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.singleton.CasoSingleton;
import br.com.bdt.ipet.singleton.OngSingleton;
import br.com.bdt.ipet.util.GeralUtils;
import br.com.bdt.ipet.data.model.Ong;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import static br.com.bdt.ipet.util.GeralUtils.isValidInput;
import static br.com.bdt.ipet.util.GeralUtils.toast;

public class CriarCasoActivity extends AppCompatActivity {

    private Ong ong;
    private EditText etTituloCaso;
    private EditText etDescricaoCaso;
    private EditText etNomeAnimalCaso;
    private EditText etValorCaso;
    private Spinner spEspecieCaso;

    @SuppressLint({"SourceLockedOrientationActivity", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_caso);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar myToolbar = findViewById(R.id.tbNormal);
        TextView title = findViewById(R.id.toolbar_title);
        TextView title_extra = findViewById(R.id.toolbar_extra);
        title.setText("Criar caso");
        title_extra.setText("");
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setNavigationOnClickListener(v -> onBackPressed());
        Objects.requireNonNull(myToolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        OngSingleton ongSingleton = OngSingleton.getOngSingleton();
        ong = ongSingleton.getOng();

        etTituloCaso = findViewById(R.id.etTituloCaso);
        etDescricaoCaso = findViewById(R.id.etDescricaoCaso);
        etNomeAnimalCaso = findViewById(R.id.etNomeAnimalCaso);
        etValorCaso = findViewById(R.id.etValorCaso);
        spEspecieCaso = findViewById(R.id.spEspecieCaso);
        Button bCriarCaso = findViewById(R.id.bCriarCaso);

        GeralUtils.setDataSpinner(
                spEspecieCaso,  //spinner
                getApplicationContext(),   //contexto
                "Espécie",     //hint do spinner
                Arrays.asList("Cachorro", "Gato", "Coelho") //conteudo do spinner
        );
    }

    public void criarUmCaso(View view){

        String id = UUID.randomUUID().toString();

        String titulo = etTituloCaso.getText().toString();
        if(!isValidInput(titulo, "text")){
            etTituloCaso.setError("Insira o título do caso");
            return;
        }

        String descricao = etDescricaoCaso.getText().toString();
        if(!isValidInput(descricao, "text")){
            etDescricaoCaso.setError("Insira a descrição do caso");
            return;
        }

        String nomeAnimal = etNomeAnimalCaso.getText().toString();
        if(!isValidInput(nomeAnimal, "text")){
            etNomeAnimalCaso.setError("Insira o nome do animal");
            return;
        }

        String especie = getDataOfSp(R.id.spEspecieCaso);
        if(!isValidInput(especie, "text")){
            ((TextView) spEspecieCaso.getSelectedView()).setError("");
            toast(getApplicationContext(), "Informe a espécie do animal");
            return;
        }

        String valorString = etValorCaso.getText().toString();
        if(!isValidInput(valorString, "double")){
            etValorCaso.setError("Insira um valor válido");
            return;
        }

        Double valor = Double.parseDouble(valorString);

        CasoSingleton casoSingleton = CasoSingleton.getCasoSingleton();
        casoSingleton.setCaso(new Caso(id, titulo, descricao, nomeAnimal, especie, valor, "", ong));

        Intent intent = new Intent(getApplicationContext(), EnviarFoto.class);
        intent.putExtra("isCaso", true);
        startActivity(intent);
    }

    public void voltar(View view){
        onBackPressed();
    }

    private String getDataOfSp(int idSpinner){
        Spinner sp = findViewById(idSpinner);
        Object selected = sp.getSelectedItem();
        return selected == null ? "" : selected.toString();
    }
}