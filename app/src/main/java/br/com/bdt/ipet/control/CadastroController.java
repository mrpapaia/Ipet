package br.com.bdt.ipet.control;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.view.CadastroInfoBanco;



public class CadastroController {
    private Ong ong;

    public void sendImg(Uri file) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference().child("images/"+file.getLastPathSegment());
        UploadTask uploadTask = ref.putFile(file);
        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return ref.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();


            } else {

            }
        });
        System.out.println(urlTask.getResult());
    }
}
