package br.com.bdt.ipet.control;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import br.com.bdt.ipet.data.api.ConsumerData;
import br.com.bdt.ipet.data.api.ConsumerData.SendDataObject;
import br.com.bdt.ipet.data.model.Banco;
import br.com.bdt.ipet.data.model.DadosBancario;
import br.com.bdt.ipet.repository.BancoRepository;
import br.com.bdt.ipet.repository.OngRepository;
import br.com.bdt.ipet.repository.StorageRepository;
import br.com.bdt.ipet.repository.interfaces.IRepositoryOng;
import br.com.bdt.ipet.repository.interfaces.IStorage;
import br.com.bdt.ipet.singleton.CadastroSingleton;
import br.com.bdt.ipet.view.CadastroInfoBanco;
import br.com.bdt.ipet.view.EnviarFoto;
import br.com.bdt.ipet.view.FimCadastro;

import static android.app.Activity.RESULT_OK;

public class CadastroController {

    private static final String TAG = "CadastroController";
    private final CadastroSingleton cadastroSingleton;
    private final IRepositoryOng ongRepository;

    public CadastroController() {
        cadastroSingleton = CadastroSingleton.getCadastroSingleton();
        ongRepository = new OngRepository(FirebaseFirestore.getInstance());
    }

    public void isValidCNPJ(String CNPJ, Context context, SendDataObject sendDataObject) {
        String url = "https://www.receitaws.com.br/v1/cnpj/" + CNPJ;
        new ConsumerData().sendGetObject(context, url, sendDataObject);
    }

    public void saveUserOng(Activity act) {

        ProgressDialog progressDialog = ProgressDialog.show(act, "Aguarde um momento", "Estamos criando seu usuário...");
        AuthController authController = new AuthController();

        authController.create(cadastroSingleton.getOng().getEmail(), cadastroSingleton.getSenha())
                .addOnCompleteListener(act, task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Log.d(TAG, "Sucesso criarUserOng");
                        act.startActivity(new Intent(act, EnviarFoto.class));
                    } else {
                        Log.d(TAG, "Falha criarUserOng");
                    }
                });
    }

    public void saveImgOng(Activity act) {

        ProgressDialog progressDialog = ProgressDialog.show(act, "Aguarde um momento", "Estamos enviando sua foto...");

        if (cadastroSingleton.getUri() != null) {
            IStorage storageRepository = new StorageRepository(FirebaseStorage.getInstance());
            storageRepository.saveImg(cadastroSingleton.getOng().getEmail(), cadastroSingleton.getUri())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            cadastroSingleton.getOng().setImgPerfil(Objects.requireNonNull(task.getResult()).toString());
                            Log.d(TAG, "Sucesso save IMG");
                            progressDialog.dismiss();
                            act.startActivity(new Intent(act, CadastroInfoBanco.class));
                        } else {
                            Log.d(TAG, "Falha save IMG");
                        }
                    });
        }
    }

    public void updateDadosBancario(Activity act, String email, DadosBancario dadosBancario) {

        ongRepository.updateDocAddFild(email, dadosBancario).
                addOnCompleteListener(aVoid -> {
                    Intent intent = new Intent();

                    act.setResult(RESULT_OK
                            , intent);
                    act.onBackPressed();
                });
    }

    public void saveDadosOng(Activity act) {

        ProgressDialog progressDialog = ProgressDialog.show(act, "Aguarde um momento", "Estamos salvando todos seus dados...");

        ongRepository.save(cadastroSingleton.getOng())
                .addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    AuthController authController = new AuthController();
                    authController.logout();
                    Log.d(TAG, "Sucesso save ONG");
                    Intent intent = new Intent(act, FimCadastro.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    act.startActivity(intent);
                    act.finish();
                }).addOnFailureListener(aVoid -> Log.d(TAG, "Falha save ONG"));
    }

    public Task<DocumentSnapshot> buscarBancos(){

        BancoRepository bancoRepository = new BancoRepository(FirebaseFirestore.getInstance());

        return bancoRepository.findAll().addOnSuccessListener(task -> {

            @SuppressWarnings("unchecked")
            List<Map<String, String>> bancosMap = (List<Map<String, String>>) task.get("bancos");

            List<Banco> bancos = new ArrayList<>();

            assert bancosMap != null;
            for (Map<String, String> map : bancosMap) {
                bancos.add(new Banco(map.get("label"), map.get("value")));
            }

            cadastroSingleton.setBancos(bancos);
        });
    }

}
