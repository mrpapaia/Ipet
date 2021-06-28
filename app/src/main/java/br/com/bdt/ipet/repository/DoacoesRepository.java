package br.com.bdt.ipet.repository;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import br.com.bdt.ipet.control.AuthController;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.Doacao;
import br.com.bdt.ipet.repository.interfaces.IRepositoryDoacao;

public class DoacoesRepository implements IRepositoryDoacao {
    private final FirebaseFirestore db;
    private final AuthController authController;

    public DoacoesRepository(FirebaseFirestore db) {
        this.db = db;
        authController = new AuthController();
    }

    @Override
    public Task<Void> save(Caso caso, Doacao doacao) {

        if (caso == null) {
            System.out.println("caso nulo");
        }

        if (caso.getOng() == null) {
            System.out.println("ong nula");
        }

        Map<String, Object> docDoacao = new HashMap<>();
        docDoacao.put("banco", doacao.getBanco());
        docDoacao.put("tipo",  doacao.getTipo());
        docDoacao.put("valor",  doacao.getValor());
        docDoacao.put("data",  doacao.getData());

        return db.collection("ongs")
                .document(caso.getOng().getEmail())
                .collection("casos")
                .document(caso.getId())
                .collection("doacao")
                .document().set(docDoacao);
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
    public Task<Void> delete(DocumentReference id) {
        return id.delete();
    }
}
