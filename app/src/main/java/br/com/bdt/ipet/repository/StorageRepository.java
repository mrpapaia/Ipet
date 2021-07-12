package br.com.bdt.ipet.repository;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import br.com.bdt.ipet.repository.interfaces.IStorage;

public class StorageRepository implements IStorage {

    private final FirebaseStorage storage;

    public StorageRepository(FirebaseStorage storage) {
        this.storage = storage;
    }

    @Override
    public Task<Uri> saveImg(String path, Uri uri,String id) {

        StorageReference ref = storage.getReference().child("images/"+path+"/"+id);
        UploadTask uploadTask = ref.putFile(uri);
        return uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw Objects.requireNonNull(task.getException());
            }

            return ref.getDownloadUrl();
        });
    }


    @Override
    public Task<Void> deleteImg(String path,String id) {
        StorageReference desertRef = storage.getReference().child("images/"+path+"/"+ id);

// Delete the file
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
        return null ;
    }
}
