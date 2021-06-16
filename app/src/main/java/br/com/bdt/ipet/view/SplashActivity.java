package br.com.bdt.ipet.view;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;


import br.com.bdt.ipet.R;
import br.com.bdt.ipet.util.UserUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        new Handler().postDelayed(() -> {
            //Se a ong estiver logada, já inicia na tela de gerenciamento da ong
            if (UserUtils.getUser() != null) {
                startActivity(new Intent(getBaseContext(), OngMain.class));
                finish();
            } else { //senão, inicia a tela main
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                finish();
            }
        }, 2000);
    }

}
