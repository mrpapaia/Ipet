package br.com.bdt.ipet.control;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import br.com.bdt.ipet.control.interfaces.IChanges;
import br.com.bdt.ipet.data.model.CasoComDoacao;
import br.com.bdt.ipet.data.model.Doacao;
import br.com.bdt.ipet.repository.DoacaoRepository;
import br.com.bdt.ipet.repository.interfaces.IRepositoryDoacao;
import br.com.bdt.ipet.singleton.CasoSingleton;

public class DoacaoController {

    private IRepositoryDoacao repository;
    private CasoSingleton casoSingleton;
    private String emailOng;
    private List<ListenerRegistration> listeners;

    public DoacaoController() {
        this.repository = new DoacaoRepository(FirebaseFirestore.getInstance());
        this.casoSingleton = CasoSingleton.getCasoSingleton();
        this.emailOng = new AuthController().getCurrentEmail();
        this.listeners = new ArrayList<>();
    }

    public Task<QuerySnapshot> getAllByCaso(CasoComDoacao caso) {
        return repository.findAll("/ongs/"+ emailOng +"/casos/" + caso.getCaso().getId()).addOnSuccessListener(queryDocumentSnapshots -> {
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

    public Task<QuerySnapshot> getQtdPendente(CasoComDoacao caso) {
          return repository.getQtdPendente("/ongs/" + emailOng + "/casos/"+caso.getCaso().getId()).addOnSuccessListener(
                    queryDocumentSnapshots -> casoSingleton.getCasos()
                                                           .get(casoSingleton.getCasos().indexOf(caso))
                                                           .setQtdDoacaoPendente(queryDocumentSnapshots.getDocuments().size())
          );
    }

    public Task<Void> delete(DocumentReference docRef){
        return repository.delete(docRef).addOnSuccessListener(unused -> {});
    }

    public void initListenerDoacoes(String idCaso, IChanges iChanges){
        ListenerRegistration listener = repository.getCollectionDoacoes(emailOng, idCaso).addSnapshotListener((value, error) -> iChanges.onChange());
        listeners.add(listener);
    }

    public void removeListeners(){
        for(ListenerRegistration l : listeners){
            l.remove();
        }
    }

    public Task<Void> updateQuantidadeDoacoesAll(){
        return repository.updateQuantidadeDoacoesAll();
    }

    public Task<DocumentSnapshot> getQuantidadeDoacoesAll(){
        return repository.getQuantidadeDoacoesAll();
    }

}
