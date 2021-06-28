package br.com.bdt.ipet.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.Doacao;
import br.com.bdt.ipet.repository.interfaces.IRepositoryDoacao;

public class DoacaoRepository implements IRepositoryDoacao {

    private final FirebaseFirestore db;

    public DoacaoRepository(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public Task<Void> save(Caso caso, Doacao doacao) {

        Map<String, Object> docDoacao = new HashMap<>();
        docDoacao.put("banco", doacao.getBanco());
        docDoacao.put("tipo",  doacao.getTipo());
        docDoacao.put("valor",  doacao.getValor());
        docDoacao.put("data",  doacao.getData());

        return db.collection("ongs")
                .document(caso.getOng().getEmail())
                .collection("casos")
                .document(caso.getId())
                .collection("doacoes")
                .document()
                .set(docDoacao);
    }

    @Override
    public Task<QuerySnapshot> findAll(String id) {
        return db.document(id).collection("doacoes").get();
    }

    @Override
    public Task<QuerySnapshot> getQtdPendente(String id) {
        return  db.document(id).collection("doacoes").get();
    }

    @Override
    public Task<Void> delete(DocumentReference id) {
        return id.delete();
    }
}
