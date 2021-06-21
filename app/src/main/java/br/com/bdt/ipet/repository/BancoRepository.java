package br.com.bdt.ipet.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.bdt.ipet.repository.interfaces.IRepositoryBanco;

public class BancoRepository implements IRepositoryBanco {

    private final FirebaseFirestore db;

    public BancoRepository(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public Task<DocumentSnapshot> findAll() {
        return db.collection("utils").document("bancos").get();
    }

}
