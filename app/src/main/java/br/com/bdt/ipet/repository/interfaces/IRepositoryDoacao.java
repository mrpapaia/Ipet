package br.com.bdt.ipet.repository.interfaces;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

public interface IRepositoryDoacao {
    Task<QuerySnapshot> findAll(String id);
    Task<QuerySnapshot> getQtdPendente(String id);
    Task<Void> delete(DocumentReference id);
}
