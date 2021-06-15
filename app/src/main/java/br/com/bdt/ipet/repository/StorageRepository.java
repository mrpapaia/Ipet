package br.com.bdt.ipet.repository;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import br.com.bdt.ipet.repository.interfaces.IStorage;

public class StorageRepository implements IStorage {

    private FirebaseStorage storage;

    public StorageRepository(FirebaseStorage storage) {
        this.storage = storage;
    }

    @Override
    public Task<Uri> saveImg(String path, Uri uri) {

        StorageReference ref = storage.getReference().child("images/"+path+"/"+uri.getLastPathSegment());
        UploadTask uploadTask = ref.putFile(uri);
        return uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return ref.getDownloadUrl();
        });
    }
}
