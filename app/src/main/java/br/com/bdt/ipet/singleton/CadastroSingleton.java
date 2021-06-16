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



    public Ong getOng() {
        return ong;
    }
    public void setOng(Ong ong) {
        this.ong = ong;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public String getSenha() {
        return senha;
    }
    public Uri getUri() {
        return uri;
    }
    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
