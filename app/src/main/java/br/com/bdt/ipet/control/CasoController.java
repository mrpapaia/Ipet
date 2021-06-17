package br.com.bdt.ipet.control;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.bdt.ipet.control.interfaces.IChanges;
import br.com.bdt.ipet.control.interfaces.IRecycler;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.singleton.CasoSingleton;
import br.com.bdt.ipet.util.UserUtils;

public class CasoController {

    private final CasoSingleton casoSingleton;
    private final FirebaseFirestore db;
    private final String emailOng;
    private IChanges iChanges;

    public CasoController() {
        casoSingleton = CasoSingleton.getCasoSingleton();
        db = FirebaseFirestore.getInstance();
        emailOng = UserUtils.getEmail();
    }

    public void initDataRecyclerViewAll(IRecycler irc){
        casoSingleton.setCasosAll(new ArrayList<>());
        irc.init(casoSingleton.getCasosAll());
    }

    public void initDataRecyclerViewOng(IRecycler irc){
        casoSingleton.setCasosOng(new ArrayList<>());
        irc.init(casoSingleton.getCasosOng());
    }

    public void listenerCasosAll(){
        db.collectionGroup("casos").addSnapshotListener((value, error) -> {
            if(value != null){
                runActions(value.getDocumentChanges());
            }
        });
    }

    public void listenerCasosOng(){
        db.collection("ongs").document(emailOng).collection("casos")
                .addSnapshotListener((value, error) -> {
                    if(value != null){
                        runActions(value.getDocumentChanges());
                    }
                });
    }

    public void runActions(List<DocumentChange> docs){

        for(DocumentChange documentChange : docs){

            DocumentChange.Type tipoAcao = documentChange.getType();
            QueryDocumentSnapshot document = documentChange.getDocument();

            switch (tipoAcao){
                case ADDED: addDoc(document); break;
                case REMOVED: removeDoc(document); break;
                case MODIFIED: modifyDoc(document); break;
            }

        }
    }

    public void addDoc(final QueryDocumentSnapshot document){

        final String email = emailOng != null ? emailOng : document.getReference().getPath().split("/")[1];

        db.collection("ongs")
                .document(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        Ong ong = Objects.requireNonNull(task.getResult()).toObject(Ong.class);

                        adicionarCaso(new Caso(
                                document.getString("id"),
                                document.getString("titulo"),
                                document.getString("descricao"),
                                document.getString("nomeAnimal"),
                                document.getString("especie"),
                                document.getDouble("valor"),
                                ong
                        ));

                    }
                });
    }

    public void removeDoc(QueryDocumentSnapshot document){

        int posicao = getPosiCaso(document.getId());

        if(posicao != -1){
            removerCaso(posicao);
        }
    }

    public void modifyDoc(QueryDocumentSnapshot document){

        int posicao = getPosiCaso(document.getId());

        if(posicao != -1){
            modificarCaso(posicao, document);
        }
    }

    public void adicionarCaso(Caso caso){

        if (emailOng != null) {
            casoSingleton.getCasosOng().add(caso);
            iChanges.onChange(casoSingleton.getCasosOng().size());
        } else {
            casoSingleton.getCasosAll().add(caso);
            iChanges.onChange(casoSingleton.getCasosAll().size());
        }

    }

    public void removerCaso(int index){

        if (emailOng != null) {
            casoSingleton.getCasosOng().remove(index);
            iChanges.onChange(casoSingleton.getCasosOng().size());
        } else {
            casoSingleton.getCasosAll().remove(index);
            iChanges.onChange(casoSingleton.getCasosAll().size());
        }
    }

    public void modificarCaso(int index, QueryDocumentSnapshot document){

        if (emailOng != null) {
            Caso caso = casoSingleton.getCasosOng().get(index);
            updateFields(caso, document);
            iChanges.onChange(casoSingleton.getCasosOng().size());
        } else {
            Caso caso = casoSingleton.getCasosAll().get(index);
            updateFields(caso, document);
            iChanges.onChange(casoSingleton.getCasosAll().size());
        }
    }

    public void updateFields(Caso caso, QueryDocumentSnapshot document){
        caso.setId(document.getString("id"));
        caso.setTitulo(document.getString("titulo"));
        caso.setDescricao(document.getString("descricao"));
        caso.setNomeAnimal(document.getString("nomeAnimal"));
        caso.setEspecie(document.getString("especie"));
        caso.setValor(document.getDouble("valor"));
    }

    public int getPosiCaso(String id){

        List<Caso> casos = emailOng != null
                ? casoSingleton.getCasosOng()
                : casoSingleton.getCasosAll();

        for(int i=0; i<casos.size(); i++){
            if(casos.get(i).getId().equals(id)){
                return i;
            }
        }

        return -1;
    }

    public void setiChanges(IChanges iChanges) {
        this.iChanges = iChanges;
    }

}
