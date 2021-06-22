package br.com.bdt.ipet.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.AuthController;
import br.com.bdt.ipet.repository.OngRepository;
import br.com.bdt.ipet.util.GeralUtils;

public class SplashActivity extends AppCompatActivity {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        GeralUtils.setFullscreen(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            FirebaseUser ongUser = new AuthController().getCurrentUser();

            if(ongUser != null) {
                verificarOng(ongUser);
            }else{
                initMain();
            }

        }, 3000);
    }

    public void verificarOng(FirebaseUser ongUser){

        new OngRepository(FirebaseFirestore.getInstance())
                .findById(ongUser.getEmail())
                .addOnSuccessListener(docOng -> {
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
