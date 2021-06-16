package br.com.bdt.ipet.control;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import br.com.bdt.ipet.data.api.ConsumerData;
import br.com.bdt.ipet.data.api.ConsumerData.SendDataObject;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.repository.OngRepository;
import br.com.bdt.ipet.repository.StorageRepository;
import br.com.bdt.ipet.repository.interfaces.IRepository;
import br.com.bdt.ipet.repository.interfaces.IStorage;
import br.com.bdt.ipet.singleton.CadastroSingleton;
import br.com.bdt.ipet.view.FimCadastro;

public class CadastroController {

    private static final String TAG = "CadastroController";
    private final CadastroSingleton cadastroSingleton;


    public CadastroController() {
        cadastroSingleton=CadastroSingleton.getCadastroSingleton();
    }

    public void isValidCNPJ(String CNPJ, Context context, SendDataObject sendDataObject){
        String url = "https://www.receitaws.com.br/v1/cnpj/" + CNPJ;
        new ConsumerData().sendGetObject(context, url, sendDataObject);
    }


    public void criarUserOng(Activity act) {

        AuthController authController = new AuthController();

        authController.create(cadastroSingleton.getOng().getEmail(), cadastroSingleton.getSenha())
                .addOnCompleteListener(act, task -> {
                    if (task.isSuccessful()) {

                        sendImg(act);
                    } else {
                        Log.d(TAG, "Erro no cadastro");
                    }
                });
    }

    public void salvarDadosOng(Activity act) {

        IRepository<Ong> ongRepository = new OngRepository(FirebaseFirestore.getInstance());

        ongRepository.save(cadastroSingleton.getOng()).addOnSuccessListener(aVoid -> {
            Log.d(TAG, "Sucesso save");
            act.startActivity(new Intent(act.getApplicationContext(), FimCadastro.class));
            act.finish();
        }).addOnFailureListener(aVoid -> {
            Log.d(TAG, cadastroSingleton.getOng().toString());
        });
    }

    public void sendImg(Activity act) {

        IStorage storageRepository = new StorageRepository(FirebaseStorage.getInstance());

        storageRepository.saveImg(cadastroSingleton.getOng().getEmail(), cadastroSingleton.getUri()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Sucesso save IMG");
                cadastroSingleton.getOng().setImgPerfil(task.getResult().toString());
                salvarDadosOng(act);
            } else {
                Log.d(TAG, "Erro save IMG");
            }
        });

    }
}
