package br.com.bdt.ipet.control;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

import br.com.bdt.ipet.data.model.DadosBancario;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.repository.OngRepository;
import br.com.bdt.ipet.repository.interfaces.IRepository;
import br.com.bdt.ipet.singleton.OngSingleton;

public class OngMainController {

    private final OngSingleton ongSingleton;

    public OngMainController() {
        this.ongSingleton = OngSingleton.getOngSingleton();
    }

    public Task<DocumentSnapshot> initOng(){

        AuthController authController = new AuthController();
        String email = authController.getCurrentEmail();
        IRepository<Ong> ongRepository = new OngRepository(FirebaseFirestore.getInstance());

        return ongRepository.findById(email).addOnSuccessListener(documentSnapshot -> {

            Ong ong = new Ong();
            ong.setEmail(documentSnapshot.getString("email"));
            ong.setCidade(documentSnapshot.getString("cidade"));
            ong.setNome(documentSnapshot.getString("nome"));
            ong.setUf(documentSnapshot.getString("uf"));
            ong.setWhatsapp(documentSnapshot.getString("whatsapp"));

            Object dadosbanco = Objects.requireNonNull(documentSnapshot.getData()).get("dadosBancarios");
            @SuppressWarnings("unchecked")
            List<DadosBancario> listDadosBancarios = (List<DadosBancario>) dadosbanco;

            if(listDadosBancarios != null){
                ong.setDadosBancarios(listDadosBancarios);
            }

            if(documentSnapshot.get("imgPerfil") != null){
                ong.setImgPerfil(documentSnapshot.getString("imgPerfil"));
            }else{
                ong.setImgPerfil("");
            }

            ongSingleton.setOng(ong);
        });
    }
}
