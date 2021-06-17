package br.com.bdt.ipet.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.repository.interfaces.IRepository;
import br.com.bdt.ipet.util.UserUtils;

public class CasoRepository implements IRepository<Caso> {

    private FirebaseFirestore db;

    public CasoRepository(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public Task<Void> save(Caso caso) {
        return null;
    }

    @Override
    public Task<Void> findAll() {
        return null;
    }

    @Override
    public Task<DocumentSnapshot> findById(String id) {
        return null;
    }

    @Override
    public Task<Void> delete(String id) {
        String emailOng = Objects.requireNonNull(UserUtils.getEmail());
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
