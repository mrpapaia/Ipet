package br.com.bdt.ipet.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import br.com.bdt.ipet.control.interfaces.IChanges;
import br.com.bdt.ipet.data.model.Banco;
import br.com.bdt.ipet.repository.interfaces.IRepository;

public class BancoRepository implements IRepository<Banco,Object> {

    private final FirebaseFirestore db;

    public BancoRepository(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public Task<Void> save(Banco banco) {
        return null;
    }

    @Override
    public Task<QuerySnapshot> findAll() {
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

    @Override
    public Task<Void> updateDocAddFild(String path, Object list) {
        return null;
    }

    @Override
    public Task<Void> updateDocRemoveFild(String path, Object list) {
        return null;
    }


    @Override
    public DocumentReference listennerDoc(String id) {
        return null;
    }


}
