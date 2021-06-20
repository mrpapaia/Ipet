package br.com.bdt.ipet.repository;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import br.com.bdt.ipet.control.OngMainController;
import br.com.bdt.ipet.control.interfaces.IChanges;
import br.com.bdt.ipet.data.model.DadosBancario;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.repository.interfaces.IRepository;
import br.com.bdt.ipet.singleton.OngSingleton;

public class OngRepository implements IRepository<Ong,DadosBancario> {

    private final FirebaseFirestore db;

    public OngRepository(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public Task<Void> save(Ong ong) {
        return db.collection("ongs").document(ong.getEmail()).set(ong);
    }

    @Override
    public Task<QuerySnapshot> findAll() {
        return db.collection("ongs").get();
    }

    @Override
    public Task<DocumentSnapshot> findById(String email) {
        return db.collection("ongs").document(email).get();
    }

    @Override
    public Task<Void> delete(String id) {
        return null;
    }

    @Override
    public Task<Void> update(String id, Ong ong) {
        return null;
    }

    @Override
    public Task<Void> updateDadosBancarios(String path, DadosBancario dadosBanco) {

        return   db.collection("ongs").document(path).update("dadosBancarios", FieldValue.arrayUnion(dadosBanco));

    }




}
