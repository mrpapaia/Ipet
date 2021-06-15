package br.com.bdt.ipet.control;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthController {

    private FirebaseAuth mAuth;

    public AuthController() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> login(String email, String senha){
        return mAuth.signInWithEmailAndPassword(email, senha);
    }

    public Task<AuthResult> create(String email, String senha){
        return mAuth.createUserWithEmailAndPassword(email, senha);
    }

    public String getCurrentEmail(){
        return mAuth.getCurrentUser().getEmail();
    }

    public void logout(){
        mAuth.signOut();
    }
}
