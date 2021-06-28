package br.com.bdt.ipet.control;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import br.com.bdt.ipet.control.interfaces.IChanges;
import br.com.bdt.ipet.control.interfaces.IRecycler;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.CasoComDoacao;
import br.com.bdt.ipet.data.model.DadosFiltro;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.repository.CasoRepository;
import br.com.bdt.ipet.repository.OngRepository;
import br.com.bdt.ipet.repository.StorageRepository;
import br.com.bdt.ipet.repository.interfaces.IRepositoryCaso;
import br.com.bdt.ipet.repository.interfaces.IStorage;
import br.com.bdt.ipet.singleton.CasoSingleton;
import br.com.bdt.ipet.util.FiltroUtils;
import br.com.bdt.ipet.view.FimCadastro;

public class CasoController {

    private final static String TAG = "CasoController.class";

    private final CasoSingleton casoSingleton;
    private final IRepositoryCaso repositoryCaso;
    private final FirebaseFirestore db;
    private final String emailOng;
    private IChanges iChanges;

    public CasoController() {
        casoSingleton = CasoSingleton.getCasoSingleton();
        repositoryCaso = new CasoRepository(FirebaseFirestore.getInstance());
        db = FirebaseFirestore.getInstance();
        AuthController authController = new AuthController();
        emailOng = authController.getCurrentEmail();
    }

    public void salvarCaso(Activity act) {

        ProgressDialog progressDialog = ProgressDialog.show(act, "Aguarde um momento", "Estamos salvando os dados do caso...");

        if(casoSingleton.getUri() != null){
            IStorage storageRepository = new StorageRepository(FirebaseStorage.getInstance());
            storageRepository.saveImg(casoSingleton.getCaso().getOng().getEmail(), casoSingleton.getUri())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            casoSingleton.getCaso().setLinkImg(Objects.requireNonNull(task.getResult()).toString());
                            Log.d(TAG, "Sucesso save IMG Caso");
                            salvarDadosCaso(act, progressDialog);
                        } else {
                            Log.d(TAG, "Falha save IMG Caso");
                        }
                    });
        }else{
            salvarDadosCaso(act, progressDialog);
        }
    }

    public void salvarDadosCaso(Activity act, ProgressDialog progressDialog){
        repositoryCaso.save(casoSingleton.getCaso()).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            Intent intent = new Intent(act, FimCadastro.class);
            intent.putExtra("isCaso", true);
            act.startActivity(intent);
        });
    }

    public void initDataRecyclerView(IRecycler irc){
        casoSingleton.setCasos(new ArrayList<>());
        irc.init(casoSingleton.getCasos());
    }

    public void listenerCasosAll(){
        repositoryCaso.findAll().addSnapshotListener((value, error) -> {
            if(value != null){
                runActions(value.getDocumentChanges());
            }
        });
    }

    public void listenerCasosOng(){
        repositoryCaso.findByOng(emailOng).addSnapshotListener((value, error) -> {
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
                        document.getDouble("arrecadado"),
                        document.getString("linkImg"),
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
        casoSingleton.getCasos().add(new CasoComDoacao(caso));
        iChanges.onChange();
    }

    public void removerCaso(int index){
        casoSingleton.getCasos().remove(index);
        iChanges.onChange();
    }

    public void modificarCaso(int index, QueryDocumentSnapshot document){

        Caso caso = casoSingleton.getCasos().get(index).getCaso();

        caso.setId(document.getString("id"));
        caso.setTitulo(document.getString("titulo"));
        caso.setDescricao(document.getString("descricao"));
        caso.setNomeAnimal(document.getString("nomeAnimal"));
        caso.setEspecie(document.getString("especie"));
        caso.setValor(document.getDouble("valor"));

        iChanges.onChange();
    }

    public int getPosiCaso(String id){

        List<CasoComDoacao> casos = casoSingleton.getCasos();

        for(int i=0; i<casos.size(); i++){
            if(casos.get(i).getCaso().getId().equals(id)){
                return i;
            }
        }

        return -1;
    }

    public void setiChanges(IChanges iChanges) {
        this.iChanges = iChanges;
    }

    public Task<Void> updateValor(String campo, Double valor,int position){
     /*   if(campo.equals("valor")){
            return  repositoryCaso.update(campo, casoSingleton.getCasos().get(position).getCaso().getValor()-valor,casoSingleton.getCasos().get(position).getCaso().getId());

        }*/
        return repositoryCaso.update(campo, casoSingleton.getCasos().get(position).getCaso().getArrecadado()+valor,casoSingleton.getCasos().get(position).getCaso().getId());
    }
}
