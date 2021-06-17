package br.com.bdt.ipet.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.singleton.OngSingleton;
import br.com.bdt.ipet.util.SpinnerUtils;
import br.com.bdt.ipet.data.model.Ong;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static br.com.bdt.ipet.util.GeralUtils.isValidInput;
import static br.com.bdt.ipet.util.GeralUtils.toast;

public class CriarCasoActivity extends AppCompatActivity {

    FirebaseFirestore db;
    Ong ong;
    EditText etTituloCaso, etDescricaoCaso, etAnimalCaso, etValorCaso;
    TextView tvVoltar;
    Spinner spEspecieCaso;
    Button btCriarCaso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_caso);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        OngSingleton ongSingleton = OngSingleton.getOngSingleton();

        db = FirebaseFirestore.getInstance();
        ong = ongSingleton.getOng();

        etTituloCaso = findViewById(R.id.etTituloCaso);
        etDescricaoCaso = findViewById(R.id.etDescricaoCaso);
        etAnimalCaso = findViewById(R.id.etAnimalCaso);
        etValorCaso = findViewById(R.id.etValorCaso);

        tvVoltar = findViewById(R.id.tvVoltar);

        spEspecieCaso = findViewById(R.id.spEspecieCaso);

        btCriarCaso = findViewById(R.id.btCriarCaso);

        SpinnerUtils.setDataSpinner(
                spEspecieCaso,  //spinner
                getApplicationContext(),   //contexto
                "Espécie",     //hint do spinner
                Arrays.asList("Cachorro", "Gato", "Coelho") //conteudo do spinner
        );
    }

    /*
    * Extrai os dados da interface de criar caso e salva na subcoleção casos no doc da ong atual
    * no firestore.
    * */
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

        String nomeAnimal = etAnimalCaso.getText().toString();
        if(!isValidInput(nomeAnimal, "text")){
            etAnimalCaso.setError("Insira o nome do animal");
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

        Map<String, Object> caso = new HashMap<>();
        caso.put("id", id);
        caso.put("titulo", titulo);
        caso.put("descricao", descricao);
        caso.put("nomeAnimal", nomeAnimal);
        caso.put("especie", especie);
        caso.put("valor", valor);

        setEnableViews(false);

        db.collection("ongs")
                .document(ong.getEmail())
                .collection("casos")
                .document(id)
                .set(caso)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Caso Criado.",
                                Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Falha ao criar um caso.",
                        Toast.LENGTH_LONG).show();
                setEnableViews(true);
            }
        });
    }

    /*
    * Método para habilidar/desabilidar todas views da interface de criar um caso
    * */
    public void setEnableViews(boolean op){
        etTituloCaso.setEnabled(op);
        etDescricaoCaso.setEnabled(op);
        etValorCaso.setEnabled(op);
        etAnimalCaso.setEnabled(op);
        spEspecieCaso.setEnabled(op);
        btCriarCaso.setEnabled(op);
        tvVoltar.setEnabled(op);
    }

    /*
    * Simula o click no botão voltar
    * */
    public void voltar(View view){
        onBackPressed();
    }

    /*
     * Método que recebe o id de um Spinner e pega o conteudo selecionado
     * */
    private String getDataOfSp(int idSpinner){
        Spinner sp = findViewById(idSpinner);
        Object selected = sp.getSelectedItem();
        return selected == null ? "" : selected.toString();
    }
}