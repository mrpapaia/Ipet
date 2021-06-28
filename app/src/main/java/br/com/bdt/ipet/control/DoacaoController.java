package br.com.bdt.ipet.control;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.CasoComDoacao;
import br.com.bdt.ipet.data.model.Doacao;
import br.com.bdt.ipet.repository.DoacoesRepository;
import br.com.bdt.ipet.repository.StorageRepository;
import br.com.bdt.ipet.repository.interfaces.IRepositoryDoacao;
import br.com.bdt.ipet.repository.interfaces.IStorage;
import br.com.bdt.ipet.singleton.CasoSingleton;
import br.com.bdt.ipet.view.FimCadastro;

public class DoacaoController {
    private IRepositoryDoacao repository;
    private CasoSingleton casoSingleton;

    public DoacaoController() {
        this.repository = new DoacoesRepository(FirebaseFirestore.getInstance());
        this.casoSingleton = CasoSingleton.getCasoSingleton();
    }

    public Task<QuerySnapshot> getAllByCaso(CasoComDoacao caso ) {
        return repository.findAll("/ongs/"+ FirebaseAuth.getInstance().getCurrentUser().getEmail() +"/casos/"+caso.getCaso().getId()).addOnSuccessListener(queryDocumentSnapshots -> {
         List<Doacao> listDoacao= new ArrayList<>();
         Doacao doacao;
          for (int i=0;i< queryDocumentSnapshots.getDocuments().size();i++){
              doacao=queryDocumentSnapshots.getDocuments().get(i).toObject(Doacao.class);
              doacao.setId(queryDocumentSnapshots.getDocuments().get(i).getReference());
              listDoacao.add(doacao);
          }
            casoSingleton.getCasos().get(casoSingleton.getCasos().indexOf(caso)).setDoacaoList(listDoacao);
        });
    }

    public Task<QuerySnapshot> getQtdPendente(CasoComDoacao caso ) {

          return   repository.getQtdPendente("/ongs/"+ FirebaseAuth.getInstance().getCurrentUser().getEmail() +"/casos/"+caso.getCaso().getId()).addOnSuccessListener(
                    queryDocumentSnapshots -> {
                        casoSingleton.getCasos().get(casoSingleton.getCasos().indexOf(caso)).setQtdDoacaoPendente(queryDocumentSnapshots.getDocuments().size());

                    });


    }
    public Task<Void> delete(DocumentReference docRef){
        return repository.delete(docRef).addOnSuccessListener(unused -> {

        });
    }

}
