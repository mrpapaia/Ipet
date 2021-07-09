package br.com.bdt.ipet.control;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.bdt.ipet.repository.OngRepository;

public class AuthController {

    private final FirebaseAuth mAuth;

    public AuthController() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> login(String email, String senha){
        return mAuth.signInWithEmailAndPassword(email, senha);
    }

    public Task<AuthResult> create(String email, String senha){
        return mAuth.createUserWithEmailAndPassword(email, senha);
    }

    public FirebaseUser getCurrentUser(){
        return mAuth.getCurrentUser();
    }

    public String getCurrentEmail(){
        FirebaseUser user = getCurrentUser();
        if(user != null){
            return user.getEmail();
        }
        return null;
    }

    public Task<Void> recoveryPassword(String email){
        return mAuth.sendPasswordResetEmail(email);
    }

    public void logout(){
        mAuth.signOut();
    }

    public Task<DocumentSnapshot> verifyUser(String email){
        return new OngRepository(FirebaseFirestore.getInstance()).findById(email);
    }
}
