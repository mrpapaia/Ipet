package br.com.bdt.ipet.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import br.com.bdt.ipet.control.AuthController;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.repository.interfaces.IRepository;

public class CasoRepository implements IRepository<Caso,Object> {

    private final FirebaseFirestore db;
    private final AuthController authController;

    public CasoRepository(FirebaseFirestore db) {
        this.db = db;
        authController = new AuthController();
    }

    @Override
    public Task<Void> save(Caso caso) {

        Map<String, Object> docCaso = new HashMap<>();
        docCaso.put("id", caso.getId());
        docCaso.put("titulo", caso.getTitulo());
        docCaso.put("descricao", caso.getDescricao());
        docCaso.put("nomeAnimal", caso.getNomeAnimal());
        docCaso.put("especie", caso.getEspecie());
        docCaso.put("valor", caso.getValor());
        docCaso.put("linkImg", caso.getLinkImg());

        return db.collection("ongs")
                .document(authController.getCurrentEmail())
                .collection("casos")
                .document(caso.getId())
                .set(docCaso);
    }

    @Override
    public Task<QuerySnapshot> findAll() {
        return null;
    }

    @Override
    public Task<DocumentSnapshot> findById(String id) {
        return null;
    }

    @Override
    public Task<Void> delete(String id) {
        return db.collection("ongs")
                .document(authController.getCurrentEmail())
                .collection("casos")
                .document(id)
                .delete();
    }

    @Override
    public Task<Void> update(String id, Caso caso) {
        return null;
    }

    @Override
    public Task<Void> updateDadosBancarios(String path, Object list) {
        return null;
    }


}
