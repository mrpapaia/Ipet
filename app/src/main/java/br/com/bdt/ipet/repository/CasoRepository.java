package br.com.bdt.ipet.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

import br.com.bdt.ipet.control.AuthController;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.repository.interfaces.IRepositoryCaso;
import br.com.bdt.ipet.util.GeralUtils;

public class CasoRepository implements IRepositoryCaso {

    private final FirebaseFirestore db;
    private final AuthController authController;

    public CasoRepository(FirebaseFirestore db) {
        this.db = db;
        authController = new AuthController();
    }

    @Override
    public Task<Void> save(Caso caso) {

        Map<String, Object> docCaso = GeralUtils.CasoToMap(caso);

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
    public Task<Void> updateField(String campo, Double valor, String id) {
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
    public Task<Void> update(Caso caso) {

        Map<String, Object> casoEditado = GeralUtils.CasoToMap(caso);

        return db.collection("ongs")
                .document(authController.getCurrentEmail())
                .collection("casos")
                .document(caso.getId())
                .update(casoEditado);
    }

    @Override
    public CollectionReference findByOng(String email) {
        return db.collection("ongs").document(email).collection("casos");
    }

}
