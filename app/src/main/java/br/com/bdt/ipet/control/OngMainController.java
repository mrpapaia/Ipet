package br.com.bdt.ipet.control;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.bdt.ipet.data.model.DadosBancario;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.repository.OngRepository;
import br.com.bdt.ipet.repository.interfaces.IRepositoryOng;
import br.com.bdt.ipet.singleton.OngSingleton;
import br.com.bdt.ipet.util.GeralUtils;

public class OngMainController {

    private final OngSingleton ongSingleton;
    private final IRepositoryOng ongRepository;

    public OngMainController() {
        this.ongSingleton = OngSingleton.getOngSingleton();
        ongRepository = new OngRepository(FirebaseFirestore.getInstance());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Task<DocumentSnapshot> initOng() {

        AuthController authController = new AuthController();
        String email = authController.getCurrentEmail();
        return ongRepository.findById(email).addOnSuccessListener(doc -> {
            Ong ong = doc.toObject(Ong.class);
            // String imgPerfil = doc.getString("imgPerfil");
            // ong.setImgPerfil(imgPerfil != null ? imgPerfil : "");
            ongSingleton.setOng(ong);
        });
    }

    public void listenner() {
        AuthController authController = new AuthController();
        String email = authController.getCurrentEmail();
        ongRepository.listennerDoc(email).addSnapshotListener((documentSnapshot, error) -> {
            ongSingleton.setOng(documentSnapshot.toObject(Ong.class));
            //Log.d("SaidaListenner", documentSnapshot.toString());
        });
    }

    public void updateDocRemoveFild(Activity act, String email, DadosBancario dadosBancario) {
        ongRepository.updateDocRemoveFild(email, dadosBancario).addOnCompleteListener(aVoid -> GeralUtils.toast(act, "Banco Removido."));
    }

}
