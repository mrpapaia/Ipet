package br.com.bdt.ipet.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.AuthController;
import br.com.bdt.ipet.util.GeralUtils;

public class SplashActivity extends AppCompatActivity {

    private AuthController authController;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        GeralUtils.setFullscreen(this);


        authController = new AuthController();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            FirebaseUser ongUser = authController.getCurrentUser();

            if(ongUser != null) {
                verificarOng(ongUser);
            }else{
                initMain();
            }

        }, 3000);
    }

    public void verificarOng(FirebaseUser ongUser){
        authController.verifyUser(ongUser.getEmail()).addOnSuccessListener(docOng -> {
            if(docOng.exists()){
                initOngMain();
            }else{
                ongUser.delete().addOnCompleteListener(task -> initMain());
            }
        });
    }

    public void initMain(){
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void initOngMain(){
        Intent intent = new Intent(getBaseContext(), OngMain.class);
        startActivity(intent);
        finish();
    }

}
