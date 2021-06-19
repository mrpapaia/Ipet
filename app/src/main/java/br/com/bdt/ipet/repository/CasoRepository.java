package br.com.bdt.ipet.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import br.com.bdt.ipet.control.AuthController;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.repository.interfaces.IRepository;

public class CasoRepository implements IRepository<Caso> {

    private final FirebaseFirestore db;

    public CasoRepository(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public Task<Void> save(Caso caso) {
        return null;
    }

    @Override
    public Task<QuerySnapshot> findAll() {
        return null;
    }

    @Override
    public Task<DocumentSnapshot> findById(String id) {
        return null;
    }

    @Override
    public Task<Void> delete(String id) {
        AuthController authController = new AuthController();
        String emailOng = authController.getCurrentEmail();
        return db.collection("ongs")
                .document(emailOng)
                .collection("casos")
                .document(id)
                .delete();
    }

    @Override
    public Task<Void> update(String id, Caso caso) {
        return null;
    }
}
