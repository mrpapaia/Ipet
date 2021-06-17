package br.com.bdt.ipet.control;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        return getCurrentUser().getEmail();
    }

    public Task<Void> recoveryPassword(String email){
        return mAuth.sendPasswordResetEmail(email);
    }

    public void logout(){
        mAuth.signOut();
    }
}
