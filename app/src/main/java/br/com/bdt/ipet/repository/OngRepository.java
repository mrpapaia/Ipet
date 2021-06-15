package br.com.bdt.ipet.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.repository.interfaces.IRepository;

public class OngRepository implements IRepository<Ong> {

    private FirebaseFirestore db;

    public OngRepository(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public Task<Void> save(Ong ong) {
         return db.collection("ongs").document(ong.getEmail()).set(ong);
    }

    @Override
    public List<Ong> findAll() {
        db.collection("ongs").get();
        return null;
    }

    @Override
    public Ong findById(String id) {
        Task<DocumentSnapshot> task = db.collection("ongs").document(id).get();
        DocumentSnapshot ong = task.getResult();
        assert ong != null;
        return ong.toObject(Ong.class);
    }

    @Override
    public void delete(Ong ong) {
        db.collection("ongs").document(ong.getEmail()).delete();
    }

    @Override
    public void update(String id, Ong ong) {
        db.collection("ongs").document(id).set(ong);
    }
}
