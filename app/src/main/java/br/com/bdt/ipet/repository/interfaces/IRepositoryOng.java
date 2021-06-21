package br.com.bdt.ipet.repository.interfaces;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import br.com.bdt.ipet.data.model.DadosBancario;
import br.com.bdt.ipet.data.model.Ong;

public interface IRepositoryOng {
    Task<Void> save(Ong ong);
    Task<QuerySnapshot> findAll();
    Task<DocumentSnapshot> findById(String email);
    Task<Void> updateDocAddFild(String path, DadosBancario dadosBanco);
    Task<Void> updateDocRemoveFild(String path, DadosBancario dadosBanco);
    DocumentReference listennerDoc(String email);
}
