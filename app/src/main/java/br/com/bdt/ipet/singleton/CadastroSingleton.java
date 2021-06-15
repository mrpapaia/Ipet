package br.com.bdt.ipet.singleton;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import br.com.bdt.ipet.control.AuthController;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.repository.OngRepository;
import br.com.bdt.ipet.repository.StorageRepository;
import br.com.bdt.ipet.repository.interfaces.IRepository;
import br.com.bdt.ipet.repository.interfaces.IStorage;
import br.com.bdt.ipet.view.FimCadastro;

public class CadastroSingleton {

    private final String TAG = "CadastroSingleton.class";

    private static CadastroSingleton cadastroSingleton;
    private Ong ong;
    private String senha;
    private Uri uri;

    private CadastroSingleton() {
    }

    public static CadastroSingleton getCadastroSingleton() {
        if (cadastroSingleton == null) {
            cadastroSingleton = new CadastroSingleton();
        }
        return cadastroSingleton;
    }

    public void criarUserOng(Activity act) {

        AuthController authController = new AuthController();

        authController.create(ong.getEmail(), senha)
                .addOnCompleteListener(act, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Cadastrou");
                        sendImg(act);
                    } else {
                        Log.d(TAG, "Erro no cadastro");
                    }
                });
    }

    public void salvarDadosOng(Activity act) {

        IRepository<Ong> ongRepository = new OngRepository(FirebaseFirestore.getInstance());

        ongRepository.save(ong).addOnSuccessListener(aVoid -> {
            Log.d(TAG, "Sucesso save");
            act.startActivity(new Intent(act.getApplicationContext(), FimCadastro.class));
            act.finish();
        }).addOnFailureListener(aVoid -> {
            Log.d(TAG, ong.toString());
        });
    }

    public void sendImg(Activity act) {

        IStorage storageRepository = new StorageRepository(FirebaseStorage.getInstance());

        storageRepository.saveImg(ong.getEmail(), uri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Sucesso save IMG");
                ong.setImgPerfil(task.getResult().toString());
                salvarDadosOng(act);
            } else {
                Log.d(TAG, "Erro save IMG");
            }
        });

    }

    public Ong getOng() {
        return ong;
    }

    public void setOng(Ong ong) {
        this.ong = ong;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
