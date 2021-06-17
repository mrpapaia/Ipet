package br.com.bdt.ipet.repository.interfaces;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public interface IRepository<T> {
    Task<Void> save(T t);
    Task<Void> findAll();
    Task<DocumentSnapshot> findById(String id);
    Task<Void> delete(String id);
    Task<Void> update(String id, T t);
}
