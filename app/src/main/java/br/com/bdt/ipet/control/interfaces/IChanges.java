package br.com.bdt.ipet.control.interfaces;

import com.google.firebase.firestore.DocumentChange.Type;

public interface IChanges {
    void onChange(int sizeList);
}
