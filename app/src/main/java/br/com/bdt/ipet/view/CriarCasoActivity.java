package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.CasoController;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.singleton.CasoSingleton;
import br.com.bdt.ipet.singleton.OngSingleton;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.util.GeralUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static br.com.bdt.ipet.util.GeralUtils.isValidInput;

public class CriarCasoActivity extends AppCompatActivity {

    private Ong ong;
    private EditText etTituloCaso;
    private EditText etDescricaoCaso;
    private EditText etNomeAnimalCaso;
    private EditText etValorCaso;
    private Spinner spEspecieCaso;
    private Caso casoEdit;

    @SuppressLint({"SourceLockedOrientationActivity", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_caso);

        casoEdit = getIntent().getParcelableExtra("casoEdit");

        Toolbar myToolbar = findViewById(R.id.tbNormal);
        TextView title = findViewById(R.id.toolbar_title);
        TextView title_extra = findViewById(R.id.toolbar_extra);
        title.setText(casoEdit != null ? "Editar Caso" : "Criar caso");
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

        List<String> especies = new ArrayList<>();

        especies.add("Cachorro");
        especies.add("Coelho");
        especies.add("Gato");


        spEspecieCaso.setAdapter(new ArrayAdapter<>(this, R.layout.adpter_spinner, especies));

        if(casoEdit != null){

            Button bCriarCaso = findViewById(R.id.bCriarCaso);
            bCriarCaso.setText("Salvar alterações");

            TextView tvMsgCriarCaso = findViewById(R.id.tvMsgCriarCaso);
            tvMsgCriarCaso.setText("Edite um caso");

            etTituloCaso.setText(casoEdit.getTitulo());
            etDescricaoCaso.setText(casoEdit.getDescricao());
            etNomeAnimalCaso.setText(casoEdit.getNomeAnimal());
            spEspecieCaso.setSelection(especies.indexOf(casoEdit.getEspecie()));
            etValorCaso.setText(String.valueOf(casoEdit.getValor()));
        }

    }

    public Caso obterDadosCaso(){

        String titulo = etTituloCaso.getText().toString();
        if(!isValidInput(titulo, "text")){
            GeralUtils.setErrorInput(etTituloCaso, "Insira o título do caso");
            return null;
        }

        String descricao = etDescricaoCaso.getText().toString();
        if(!isValidInput(descricao, "text")){
            GeralUtils.setErrorInput(etDescricaoCaso, "Insira a descrição do caso");
            return null;
        }

        String nomeAnimal = etNomeAnimalCaso.getText().toString();
        if(!isValidInput(nomeAnimal, "text")){
            GeralUtils.setErrorInput(etNomeAnimalCaso, "Insira o nome do animal");
            return null;
        }

        String especie = GeralUtils.getDataOfSp(this, R.id.spEspecieCaso);

        String valorString = etValorCaso.getText().toString();
        if(!isValidInput(valorString, "double")){
            GeralUtils.setErrorInput(etValorCaso, "Insira um valor válido");
            return null;
        }

        Double valor = Double.parseDouble(valorString);

        Caso caso = new Caso(titulo, descricao, nomeAnimal, especie, valor);

        if(casoEdit == null){
            caso.setId(UUID.randomUUID().toString());
            caso.setArrecadado(0.0);
            caso.setLinkImg("");
            caso.setOng(ong);
        }else{
            caso.setId(casoEdit.getId());
            caso.setArrecadado(casoEdit.getArrecadado());
            caso.setLinkImg(casoEdit.getLinkImg());
        }

        return caso;
    }

    public void criarUmCaso(View view){

        Caso caso = obterDadosCaso();

        if(caso == null) return;

        if(casoEdit != null){
            new CasoController().alterarCaso(caso).addOnCompleteListener(task -> {
                GeralUtils.toast(this, "Caso Atualizado com Sucesso");
                onBackPressed();
            });
        }else{
            CasoSingleton.getCasoSingleton().setCaso(caso);
            Intent intent = new Intent(getApplicationContext(), EnviarFoto.class);
            intent.putExtra("isCaso", true);
            startActivity(intent);
        }

    }

    public void voltar(View view){
        onBackPressed();
    }
}