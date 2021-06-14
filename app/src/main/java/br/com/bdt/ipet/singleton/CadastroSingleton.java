package br.com.bdt.ipet.singleton;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.Executor;

import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.view.FimCadastro;


public class CadastroSingleton {
    private static CadastroSingleton cadastroSingleton;
    private Ong ong;
    private String senha;
private Uri uri;

    private CadastroSingleton() {}

    public static CadastroSingleton getCadastroSingleton() {
        if (cadastroSingleton == null) {
            cadastroSingleton = new CadastroSingleton();
        }
        return cadastroSingleton;
    }
    public void criarUserOng(Activity act) {
         FirebaseAuth mAuth =  FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(ong.getEmail(), senha)
                .addOnCompleteListener(act , task -> {
                    if (task.isSuccessful()) {
                        Log.d("VALTENIS","Cadastrou");
                       sendImg(act);
                    } else {

                        String msgErro = "Erro no cadastro.";

                        Exception e = task.getException();

                        if (e != null) {
                            msgErro += " (" + e.getMessage() + ")";
                        }


                    }
                });
    }
    public void salvarDadosOng(Activity act) {
         FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("VALTENIS",ong.toString());
        db.collection("ongs")
                .document(ong.getEmail()).set(ong)
                .addOnSuccessListener(aVoid -> {
                    act.startActivity(new Intent(act.getApplicationContext(), FimCadastro.class));
                    act.finish();
                });
    }
    public void sendImg(Activity act) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference().child("images/"+ong.getEmail()+"/"+uri.getLastPathSegment());
        UploadTask uploadTask = ref.putFile(uri);
        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return ref.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ong.setImgPerfil(task.getResult().toString()) ;
                salvarDadosOng(act);
            } else {

            }
        });

    }

    public Ong getOng() {
        return ong;
    }

    public void setOng(Ong ong) {
        this.ong = ong;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
