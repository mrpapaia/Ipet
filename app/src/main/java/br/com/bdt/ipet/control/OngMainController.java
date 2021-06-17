package br.com.bdt.ipet.control;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import br.com.bdt.ipet.data.model.DadosBancario;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.repository.OngRepository;
import br.com.bdt.ipet.repository.interfaces.IRepository;
import br.com.bdt.ipet.singleton.OngSingleton;
import br.com.bdt.ipet.util.CasoUtils;
import br.com.bdt.ipet.util.UserUtils;

public class OngMainController {
    private OngSingleton ongSingleton;

    public OngMainController() {
        this.ongSingleton = OngSingleton.getOngSingleton();
    }

    public Task<DocumentSnapshot> initOng(){

        String email = Objects.requireNonNull(UserUtils.getEmail());
        IRepository<Ong> ongRepository = new OngRepository(FirebaseFirestore.getInstance());

        return ongRepository.findById(email).addOnSuccessListener(documentSnapshot -> {

           Ong ong = new Ong();
           ong.setEmail(documentSnapshot.get("email").toString());
           ong.setCidade(documentSnapshot.get("cidade").toString());
           ong.setNome(documentSnapshot.get("nome").toString());
           ong.setUf(documentSnapshot.get("uf").toString());
           ong.setWhatsapp(documentSnapshot.get("whatsapp").toString());

            List<DadosBancario> listDadosBancarios= (List<DadosBancario>) documentSnapshot.getData().get("dadosBancarios");

            if(listDadosBancarios!=null){
                ong.setDadosBancarios(listDadosBancarios);
            }

            if(documentSnapshot.get("imgPerfil")!=null){
                ong.setImgPerfil(documentSnapshot.get("imgPerfil").toString());
            }else{
                ong.setImgPerfil("");
            }

            ongSingleton.setOng(ong);

        });
    }
}
