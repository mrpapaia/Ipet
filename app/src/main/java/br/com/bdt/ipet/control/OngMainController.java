package br.com.bdt.ipet.control;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.bdt.ipet.control.interfaces.IChanges;
import br.com.bdt.ipet.data.model.DadosBancario;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.repository.OngRepository;
import br.com.bdt.ipet.repository.interfaces.IRepository;
import br.com.bdt.ipet.singleton.OngSingleton;

import static android.app.Activity.RESULT_OK;

public class OngMainController {

    private final OngSingleton ongSingleton;
    private IChanges iChanges;
    private IRepository<Ong, DadosBancario> ongRepository;


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
    public void setChanges(IChanges iChanges) {
        this.iChanges = iChanges;
    }
    public void listenner() {
        AuthController authController = new AuthController();
        String email = authController.getCurrentEmail();
        ongRepository.listennerDoc(email).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot documentSnapshot, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                ongSingleton.setOng(documentSnapshot.toObject(Ong.class));
                Log.d("SaidaListenner",documentSnapshot.toString());
            }
        });
    }

    public void updateDocRemoveFild(Activity act, String email, DadosBancario dadosBancario) {

        ongRepository.updateDocRemoveFild(email, dadosBancario).
                addOnCompleteListener(aVoid -> {
                    //ongSingleton.getOng().getDadosBancarios().remove(dadosBancario);
                });
    }

}
