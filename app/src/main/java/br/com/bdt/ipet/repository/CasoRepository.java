package br.com.bdt.ipet.repository;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

import br.com.bdt.ipet.control.AuthController;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.repository.interfaces.IRepositoryCaso;

public class CasoRepository implements IRepositoryCaso {

    private final FirebaseFirestore db;
    private final AuthController authController;

    public CasoRepository(FirebaseFirestore db) {
        this.db = db;
        authController = new AuthController();
    }

    @Override
    public Task<Void> save(Caso caso) {

        Map<String, Object> docCaso = new HashMap<>();
        docCaso.put("id", caso.getId());
        docCaso.put("titulo", caso.getTitulo());
        docCaso.put("descricao", caso.getDescricao());
        docCaso.put("nomeAnimal", caso.getNomeAnimal());
        docCaso.put("especie", caso.getEspecie());
        docCaso.put("valor", caso.getValor());
        docCaso.put("arrecadado", caso.getArrecadado());
        docCaso.put("linkImg", caso.getLinkImg());

        return db.collection("ongs")
                .document(authController.getCurrentEmail())
                .collection("casos")
                .document(caso.getId())
                .set(docCaso);
    }

    @Override
    public Task<Void> delete(String id) {
        return db.collection("ongs")
                .document(authController.getCurrentEmail())
                .collection("casos")
                .document(id)
                .delete();
    }
    @Override
    public Task<Void> update(String campo,Double valor,String id) {
        return db.collection("ongs")
                .document(authController.getCurrentEmail()).collection("casos")
                .document(id)
                .update(campo,valor);
    }
    @Override
    public Query findAll() {
        return db.collectionGroup("casos");
    }

    @Override
    public CollectionReference findByOng(String email) {
        return db.collection("ongs").document(email).collection("casos");
    }

}
