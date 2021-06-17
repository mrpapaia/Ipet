package br.com.bdt.ipet.control;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

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

        return ongRepository.findById(email).addOnSuccessListener(doc -> {

            Ong ong = new Ong(
                    doc.getString("nome"),
                    doc.getString("email"),
                    doc.getString("whatsapp"),
                    doc.getString("cnpj"),
                    doc.getString("uf"),
                    doc.getString("cidade")
            );

            Object dadosbancarios = doc.get("dadosBancarios");
            @SuppressWarnings("unchecked")
            List<DadosBancario> listDadosBancarios = (dadosbancarios != null)
                    ? (List<DadosBancario>) dadosbancarios
                    : new ArrayList<>();
            ong.setDadosBancarios(listDadosBancarios);

            String imgPerfil = doc.getString("imgPerfil");
            ong.setImgPerfil(imgPerfil != null ? imgPerfil : "");

            ongSingleton.setOng(ong);
        });
    }
}
