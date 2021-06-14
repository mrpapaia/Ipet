package br.com.bdt.ipet.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.singleton.CadastroSingleton;

public class EnviarFoto extends AppCompatActivity {
    private static final int GET_FROM_GALLERY = 3;
    private Toolbar myToolbar;
    private TextView title;
    private ImageView ivEnviar;
    private Button btEnviarFoto;
    private Ong ong;
    private String senha;
    private CadastroSingleton cadastroSingleton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_foto);
        myToolbar = (Toolbar) findViewById(R.id.tbNormal);
        title = (TextView) findViewById(R.id.toolbar_title);
        btEnviarFoto=findViewById(R.id.btEnviarFoto);
        title.setText("Foto");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setNavigationOnClickListener(v -> onBackPressed());
        btEnviarFoto.setOnClickListener(v->pickImg());
        cadastroSingleton=CadastroSingleton.getCadastroSingleton();


    }

    public void pickImg() {
        Intent it = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(it, GET_FROM_GALLERY);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;

            try {
                ivEnviar=findViewById(R.id.ivEnviar);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ivEnviar.setImageBitmap(bitmap);
                btEnviarFoto.setText("Enviar foto");
                cadastroSingleton.setUri(selectedImage);
                btEnviarFoto.setOnClickListener(v->pularEtapa(btEnviarFoto));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public void pularEtapa(View v){
        Intent it =new Intent(this, CadastroInfoBanco.class);
        startActivity(it);
    }
}