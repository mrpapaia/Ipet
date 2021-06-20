package br.com.bdt.ipet.control;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;

import br.com.bdt.ipet.data.api.ConsumerData;
import br.com.bdt.ipet.data.api.ConsumerData.SendDataObject;
import br.com.bdt.ipet.data.model.DadosBancario;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.repository.OngRepository;
import br.com.bdt.ipet.repository.StorageRepository;
import br.com.bdt.ipet.repository.interfaces.IRepository;
import br.com.bdt.ipet.repository.interfaces.IStorage;
import br.com.bdt.ipet.singleton.CadastroSingleton;
import br.com.bdt.ipet.view.CadastroInfoBanco;
import br.com.bdt.ipet.view.EnviarFoto;
import br.com.bdt.ipet.view.FimCadastro;

public class CadastroController {

    private static final String TAG = "CadastroController";
    private final CadastroSingleton cadastroSingleton;

    public CadastroController() {
        cadastroSingleton = CadastroSingleton.getCadastroSingleton();
    }

    public void isValidCNPJ(String CNPJ, Context context, SendDataObject sendDataObject){
        String url = "https://www.receitaws.com.br/v1/cnpj/" + CNPJ;
        new ConsumerData().sendGetObject(context, url, sendDataObject);
    }

    public void saveUserOng(Activity act) {

        ProgressDialog progressDialog = ProgressDialog.show(act, "Aguarde um momento", "Estamos criando seu usuário...");
        AuthController authController = new AuthController();

        authController.create(cadastroSingleton.getOng().getEmail(), cadastroSingleton.getSenha())
            .addOnCompleteListener(act, task -> {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Log.d(TAG, "Sucesso criarUserOng");
                    act.startActivity(new Intent(act, EnviarFoto.class));
                } else {
                    Log.d(TAG, "Falha criarUserOng");
                }
            });
    }

    public void saveImgOng(Activity act) {

        ProgressDialog progressDialog = ProgressDialog.show(act, "Aguarde um momento", "Estamos enviando sua foto...");

        if(cadastroSingleton.getUri() != null){
            IStorage storageRepository = new StorageRepository(FirebaseStorage.getInstance());
            storageRepository.saveImg(cadastroSingleton.getOng().getEmail(), cadastroSingleton.getUri())
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    cadastroSingleton.getOng().setImgPerfil(Objects.requireNonNull(task.getResult()).toString());
                    Log.d(TAG, "Sucesso save IMG");
                    progressDialog.dismiss();
                    act.startActivity(new Intent(act, CadastroInfoBanco.class));
                } else {
                    Log.d(TAG, "Falha save IMG");
                }
            });
        }
    }
public void updateDadosBancario(Activity act,String email, DadosBancario dadosBancario){
    IRepository<Ong, DadosBancario> ongRepository = new OngRepository(FirebaseFirestore.getInstance());
    ongRepository.updateDadosBancarios(email,dadosBancario).

            addOnCompleteListener(aVoid->{ act.onBackPressed();});
}
    public void saveDadosOng(Activity act){

        ProgressDialog progressDialog = ProgressDialog.show(act, "Aguarde um momento", "Estamos salvando todos seus dados...");
        IRepository<Ong, DadosBancario> ongRepository = new OngRepository(FirebaseFirestore.getInstance());

        ongRepository.save(cadastroSingleton.getOng())
            .addOnSuccessListener(aVoid -> {
                progressDialog.dismiss();
                AuthController authController = new AuthController();
                authController.logout();
                Log.d(TAG, "Sucesso save ONG");
                Intent intent = new Intent(act, FimCadastro.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                act.startActivity(intent);
                act.finish();
            }).addOnFailureListener(aVoid -> Log.d(TAG, "Falha save ONG"));
    }

}
