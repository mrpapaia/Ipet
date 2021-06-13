package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import br.com.bdt.ipet.R;

public class CadastroInfoBanco extends AppCompatActivity {
    private Toolbar myToolbar;
    private TextView title;
    private CheckBox cbHablitapix;
    private EditText etChavePix;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_info_banco);
        myToolbar = (Toolbar) findViewById(R.id.tbNormal);
        title = (TextView) findViewById(R.id.toolbar_title);
        cbHablitapix=findViewById(R.id.cbHabilitaPix);
        etChavePix=findViewById(R.id.etChavePix);
        title.setText("Dados Bancarios");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setNavigationOnClickListener(v -> onBackPressed());
        etChavePix.setVisibility(View.INVISIBLE);
    }


    public void hablitaPix(View v){
        if(cbHablitapix.isChecked()){
            etChavePix.setVisibility(View.VISIBLE);
            return;
        }
        etChavePix.setVisibility(View.INVISIBLE);
    }


    public void end(View v){
        startActivity(new Intent(getBaseContext(), FimCadastro.class));
        finish();
    }
}