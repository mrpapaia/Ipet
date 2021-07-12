package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.IOException;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.CadastroController;
import br.com.bdt.ipet.control.CasoController;
import br.com.bdt.ipet.singleton.CadastroSingleton;
import br.com.bdt.ipet.singleton.CasoSingleton;

public class EnviarFoto extends AppCompatActivity {

    private static final int GET_FROM_GALLERY = 3;
    private Button btEnviarFoto;
    private CadastroSingleton cadastroSingleton;
    private boolean imgSelected;
    private boolean isCaso;
    private CasoSingleton casoSingleton;
    private CasoController casoController;

    @SuppressWarnings("ConstantConditions")
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_foto);
        imgSelected = false;
        Toolbar myToolbar = findViewById(R.id.tbNormal);
        TextView title = findViewById(R.id.toolbar_title);
        TextView title_extra = findViewById(R.id.toolbar_extra);
        TextView tvMsgImg = findViewById(R.id.tvMsgImg);
        btEnviarFoto = findViewById(R.id.btEnviarFoto);
        Button btPularEtapaImg = findViewById(R.id.btPularEtapaImg);
        title.setText("Foto");
        title_extra.setText("");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setNavigationOnClickListener(v -> onBackPressed());
        myToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        casoController = new CasoController();

        btEnviarFoto.setOnClickListener(this::pickImg);
        cadastroSingleton = CadastroSingleton.getCadastroSingleton();
        casoSingleton = CasoSingleton.getCasoSingleton();

        String nomeOng = "";
        String endText = "";

        isCaso = getIntent().getBooleanExtra("isCaso", false);

        if(isCaso){
            btPularEtapaImg.setText("Pular etapa e concluir");
            nomeOng = casoSingleton.getCaso().getOng().getNome();
            endText = "no seu caso ?";
        }else{
            nomeOng = cadastroSingleton.getOng().getNome();
            endText = "em sua Ong ?";
        }

        tvMsgImg.setText("Olá " + nomeOng + ", gostaria de inserir uma foto " + endText);
    }

    public void pickImg(View view) {
        if(imgSelected){
            if(isCaso){
                casoController.salvarCaso(this);
            }else {
                CadastroController cadastroController = new CadastroController();
                cadastroController.saveImgOng(this);
            }
        }else{
            Intent it = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(it, GET_FROM_GALLERY);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap;
            try {
                ImageView ivEnviar = findViewById(R.id.ivEnviar);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ivEnviar.setImageBitmap(bitmap);
                btEnviarFoto.setText(isCaso ? "Concluir" : "Enviar foto");
                if(isCaso){
                    casoSingleton.setUri(selectedImage);
                }else {
                    cadastroSingleton.setUri(selectedImage);
                }
                imgSelected = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void pularEtapa(View v){
        if(isCaso){
            casoController.salvarCaso(this);
        }else{
            startActivity(new Intent(this, CadastroInfoBanco.class));
        }
    }

}