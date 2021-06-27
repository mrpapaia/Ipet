package br.com.bdt.ipet.control;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.CasoComDoacao;
import br.com.bdt.ipet.data.model.Doacao;
import br.com.bdt.ipet.repository.DoacoesRepository;
import br.com.bdt.ipet.singleton.CasoSingleton;

public class DoacaoController {
    private DoacoesRepository repository;
    private CasoSingleton casoSingleton;

    public DoacaoController() {
        this.repository = new DoacoesRepository(FirebaseFirestore.getInstance());
        this.casoSingleton = CasoSingleton.getCasoSingleton();
    }

    public Task<QuerySnapshot> getAllByCaso(CasoComDoacao caso ) {
        return repository.findAll("/ongs/"+ FirebaseAuth.getInstance().getCurrentUser().getEmail() +"/casos/"+caso.getCaso().getId()).addOnSuccessListener(queryDocumentSnapshots -> {
            casoSingleton.getCasos().get(casoSingleton.getCasos().indexOf(caso)).setDoacaoList(queryDocumentSnapshots.toObjects(Doacao.class));
        });
    }

    public Task<QuerySnapshot> getQtdPendente(CasoComDoacao caso ) {

          return   repository.getQtdPendente("/ongs/"+ FirebaseAuth.getInstance().getCurrentUser().getEmail() +"/casos/"+caso.getCaso().getId()).addOnSuccessListener(
                    queryDocumentSnapshots -> {
                        casoSingleton.getCasos().get(casoSingleton.getCasos().indexOf(caso)).setQtdDoacaoPendente(queryDocumentSnapshots.getDocuments().size());

                    });


    }

}
