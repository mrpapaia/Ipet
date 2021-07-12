package br.com.bdt.ipet.repository.interfaces;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.Doacao;

public interface IRepositoryDoacao {
    Task<Void> save(Caso caso, Doacao doacao);
    Task<QuerySnapshot> findAll(String id);
    Task<QuerySnapshot> getQtdPendente(String id);
    Task<Void> delete(DocumentReference id);
    CollectionReference getCollectionDoacoes(String emailOng, String idCaso);
    Task<Void> updateQuantidadeDoacoesAll();
    Task<DocumentSnapshot> getQuantidadeDoacoesAll();
}
