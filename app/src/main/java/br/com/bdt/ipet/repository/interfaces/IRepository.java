package br.com.bdt.ipet.repository.interfaces;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public interface IRepository<T> {
    Task<Void> save(T t);
    Task<Void> findAll();
    Task<DocumentSnapshot> findById(String id);
    void delete(T t);
    void update(String id, T t);
}
