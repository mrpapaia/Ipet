package br.com.bdt.ipet.control;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.bdt.ipet.control.interfaces.IChanges;
import br.com.bdt.ipet.data.model.DadosBancario;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.repository.OngRepository;
import br.com.bdt.ipet.repository.interfaces.IRepository;
import br.com.bdt.ipet.singleton.OngSingleton;

public class OngMainController {

    private final OngSingleton ongSingleton;
    private IChanges iChanges;

    public OngMainController() {
        this.ongSingleton = OngSingleton.getOngSingleton();
    }
    public void setiChanges(IChanges iChanges) {
        this.iChanges = iChanges;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Task<DocumentSnapshot> initOng(){

        AuthController authController = new AuthController();
        String email = authController.getCurrentEmail();
        IRepository<Ong,DadosBancario> ongRepository = new OngRepository(FirebaseFirestore.getInstance());

        return ongRepository.findById(email).addOnSuccessListener(doc -> {
            Ong ong =  doc.toObject(Ong.class);

           // String imgPerfil = doc.getString("imgPerfil");
           // ong.setImgPerfil(imgPerfil != null ? imgPerfil : "");

            ongSingleton.setOng(ong);
        });
    }



}
