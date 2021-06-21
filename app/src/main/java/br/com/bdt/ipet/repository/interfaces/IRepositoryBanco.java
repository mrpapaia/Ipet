package br.com.bdt.ipet.repository.interfaces;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public interface IRepositoryBanco {
    Task<DocumentSnapshot> findAll();
}