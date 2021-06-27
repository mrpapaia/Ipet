package br.com.bdt.ipet.repository;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import br.com.bdt.ipet.repository.interfaces.IRepositoryDoacao;

public class DoacoesRepository implements IRepositoryDoacao {
    private final FirebaseFirestore db;

    public DoacoesRepository(FirebaseFirestore db) {
        this.db = db;
    }


    @Override
    public Task<QuerySnapshot> findAll(String id) {
        return db.document(id).collection("doacao").get();
    }

    @Override
    public Task<QuerySnapshot> getQtdPendente(String id) {
        return  db.document(id).collection("doacao").get();
    }

    @Override
    public Task<Void> delete(String id) {
        return db.document(id).delete();
    }
}
