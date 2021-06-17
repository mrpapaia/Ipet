package br.com.bdt.ipet.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.bdt.ipet.data.model.Banco;
import br.com.bdt.ipet.repository.interfaces.IRepository;

public class BancoRepository implements IRepository<Banco> {

    private final FirebaseFirestore db;

    public BancoRepository(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public Task<Void> save(Banco banco) {
        return null;
    }

    @Override
    public Task<Void> findAll() {
        return null;
    }

    public Task<DocumentSnapshot> getAll(){
        return db.collection("utils").document("bancos").get();
    }

    @Override
    public Task<DocumentSnapshot> findById(String id) {
        return null;
    }

    @Override
    public Task<Void> delete(String id) {
        return null;
    }

    @Override
    public Task<Void> update(String id, Banco banco) {
        return null;
    }
}
