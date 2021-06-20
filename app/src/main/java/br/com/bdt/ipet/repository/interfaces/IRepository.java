package br.com.bdt.ipet.repository.interfaces;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import br.com.bdt.ipet.control.interfaces.IChanges;

public interface IRepository<T,C> {
    Task<Void> save(T t);
    Task<QuerySnapshot> findAll();
    Task<DocumentSnapshot> findById(String id);
    Task<Void> delete(String id);
    Task<Void> update(String id, T t);
    Task<Void> updateDadosBancarios(String path, C list);

}
