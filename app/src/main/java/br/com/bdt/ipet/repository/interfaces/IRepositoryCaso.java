package br.com.bdt.ipet.repository.interfaces;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;

import br.com.bdt.ipet.data.model.Caso;

public interface IRepositoryCaso {
    Task<Void> save(Caso caso);
    Task<Void> delete(String id);
    Query findAll();
    CollectionReference findByOng(String email);
}