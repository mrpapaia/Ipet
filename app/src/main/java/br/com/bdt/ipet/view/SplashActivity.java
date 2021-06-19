package br.com.bdt.ipet.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.AuthController;
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
            AuthController authController = new AuthController();
            boolean isLogged = authController.getCurrentUser() != null;
            Intent intent = new Intent(getBaseContext(), isLogged ? OngMain.class : MainActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }

}
