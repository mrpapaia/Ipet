package br.com.bdt.ipet.control;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.Doacao;
import br.com.bdt.ipet.repository.DoacaoRepository;
import br.com.bdt.ipet.repository.interfaces.IRepositoryDoacao;

public class PagamentoController {

    public PagamentoController() {
    }

    public Task<Void> saveDoacao(Caso caso, Doacao doacao){
        IRepositoryDoacao iRepositoryDoacao = new DoacaoRepository(FirebaseFirestore.getInstance());
        return iRepositoryDoacao.save(caso, doacao);
    }
}
