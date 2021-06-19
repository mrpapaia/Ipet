package br.com.bdt.ipet.control;

import android.os.Build;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import br.com.bdt.ipet.control.interfaces.IChanges;
import br.com.bdt.ipet.control.interfaces.IRecycler;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.DadosFiltro;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.repository.OngRepository;
import br.com.bdt.ipet.singleton.CasoSingleton;
import br.com.bdt.ipet.util.FiltroUtils;

public class CasoController {

    private final CasoSingleton casoSingleton;
    private final FirebaseFirestore db;
    private final String emailOng;
    private IChanges iChanges;

    public CasoController() {
        casoSingleton = CasoSingleton.getCasoSingleton();
        db = FirebaseFirestore.getInstance();
        AuthController authController = new AuthController();
        emailOng = authController.getCurrentEmail();
    }

    public void initDataRecyclerView(IRecycler irc){
        casoSingleton.setCasos(new ArrayList<>());
        irc.init(casoSingleton.getCasos());
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

        OngRepository ongRepository = new OngRepository(db);

        ongRepository.findById(email).addOnCompleteListener(task -> adicionarCaso(
                new Caso(
                    document.getString("id"),
                    document.getString("titulo"),
                    document.getString("descricao"),
                    document.getString("nomeAnimal"),
                    document.getString("especie"),
                    document.getDouble("valor"),
                    Objects.requireNonNull(task.getResult()).toObject(Ong.class)
                )
        ));
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
        casoSingleton.getCasos().add(caso);
        iChanges.onChange();
    }

    public void removerCaso(int index){
        casoSingleton.getCasos().remove(index);
        iChanges.onChange();
    }

    public void modificarCaso(int index, QueryDocumentSnapshot document){

        Caso caso = casoSingleton.getCasos().get(index);

        caso.setId(document.getString("id"));
        caso.setTitulo(document.getString("titulo"));
        caso.setDescricao(document.getString("descricao"));
        caso.setNomeAnimal(document.getString("nomeAnimal"));
        caso.setEspecie(document.getString("especie"));
        caso.setValor(document.getDouble("valor"));

        iChanges.onChange();
    }

    public int getPosiCaso(String id){

        List<Caso> casos = casoSingleton.getCasos();

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

    public List<Caso> casosFiltrados(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return casoSingleton.getCasos().stream().filter(this::isfiltrado).collect(Collectors.toList());
        }
        return null;
    }

    public boolean isfiltrado(Caso caso){
        DadosFiltro dadosFiltro = casoSingleton.getDadosFiltro();
        return FiltroUtils.filterEspecie(caso.getEspecie(), dadosFiltro.getEspecies()) &&
                FiltroUtils.filterValor(caso.getValor(), dadosFiltro.getMinValue(), dadosFiltro.getMaxValue()) &&
                FiltroUtils.filterText(caso.getOng().getUf(), dadosFiltro.getUf()) &&
                FiltroUtils.filterText(caso.getOng().getCidade(), dadosFiltro.getCidade()) &&
                FiltroUtils.filterText(caso.getOng().getEmail(), dadosFiltro.getEmailOng());
    }

}
