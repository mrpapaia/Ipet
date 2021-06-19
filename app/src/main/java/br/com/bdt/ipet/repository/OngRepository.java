package br.com.bdt.ipet.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.repository.interfaces.IRepository;

public class OngRepository implements IRepository<Ong> {

    private final FirebaseFirestore db;

    public OngRepository(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public Task<Void> save(Ong ong) {
        return db.collection("ongs").document(ong.getEmail()).set(ong);
    }

    @Override
    public Task<QuerySnapshot> findAll() {
        return db.collection("ongs").get();
    }

    @Override
    public Task<DocumentSnapshot> findById(String email) {
        return db.collection("ongs").document(email).get();
    }

    @Override
    public Task<Void> delete(String id) {
        return null;
    }

    @Override
    public Task<Void> update(String id, Ong ong) {
        return null;
    }

}
