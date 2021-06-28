package br.com.bdt.ipet.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.control.DetalhesCasoController;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.view.dialog.ValorPagamentoDialog;

public class DetalhesCasoActivity extends AppCompatActivity {

    private Caso caso;
    private Button btEmail, btWhatsapp;
    private ImageView ivLogoAnimal, ivIconAnimal;
    private TextView tvLocalizacao;
    private DetalhesCasoController detalhesCasoController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_caso);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        caso = getIntent().getParcelableExtra("casoOng");
        btEmail = findViewById(R.id.btEmailOngCase);
        btWhatsapp = findViewById(R.id.btWhatsappOngCase);
        ivLogoAnimal = findViewById(R.id.ivLogoAnimal);
        ivIconAnimal = findViewById(R.id.ivIconAnimal);
        tvLocalizacao = findViewById(R.id.tvLocalizacao);

        detalhesCasoController = new DetalhesCasoController();

        setarInformacoes();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onStart() {
        super.onStart();
        Button btn = (Button) findViewById(R.id.btDoar);

        View.OnClickListener listener = v -> {
            DialogFragment dialog = ValorPagamentoDialog.newInstance(caso);
            dialog.show(getSupportFragmentManager(), "ValorPagamento");
            getSupportFragmentManager().executePendingTransactions();
            dialog.getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        };

        btn.setOnClickListener(listener);
    }

    public void setarInformacoes() {

        Ong ong = caso.getOng(); //Pega a ong do caso

        setImgAndColorAnimal(); //Seta logo e icone do animal do caso e muda cor dos botões

        setTextTv(R.id.tvNomeAnimal, caso.getNomeAnimal()); //Nome animal
        setTextTv(R.id.tvRaca, caso.getEspecie()); //Espécie animal

        String localizacao = ong.getCidade() + '/' + ong.getUf();
        setTextTv(R.id.tvLocalizacao, localizacao); //City/Uf ong

        //Mudanças no tamanho da fonte de acordo com o tamanho da string
        if (localizacao.length() > 15) {
            tvLocalizacao.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        }
        if (localizacao.length() > 25) {
            tvLocalizacao.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        }

        setTextTv(R.id.tvOngData, ong.getNome()); //Nome ong
        setTextTv(R.id.tvTitleData, caso.getTitulo()); //Nome caso
        setTextTv(R.id.tvValorData, String.valueOf(caso.getValor())); //Valor caso
        setTextTv(R.id.tvDescricaoData, caso.getDescricao()); //Descrição caso
        setTextTv(R.id.tvValorArrecadado, String.valueOf(caso.getArrecadado()));

        //Lógica para pegar o height atual e dar um espaçamento no bottom, alterando seu height
        ConstraintLayout main3 = findViewById(R.id.main3);
        int wrapSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        main3.measure(wrapSpec, wrapSpec);
        main3.getLayoutParams().height = main3.getMeasuredHeight() + 80;
    }

    //Retorna 10% da largura do celular
    public int sizeIcon() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return (int) (displayMetrics.heightPixels * 0.1);
    }

    //Retorna 38% da altura do celular
    public int sizeButton() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return (int) (displayMetrics.widthPixels * 0.38);
    }

    public void setImgAndColorAnimal() {

        int logo = 0, icon = 0, color = 0;

        switch (caso.getEspecie()) {
            case "Cachorro":
                logo = R.drawable.logo_dog;
                icon = R.drawable.icon_dog;
                color = getResources().getColor(R.color.colorCachorro);
                break;
            case "Gato":
                logo = R.drawable.logo_cat;
                icon = R.drawable.icon_cat;
                color = getResources().getColor(R.color.colorGato);
                break;
            case "Coelho":
                logo = R.drawable.logo_bunny;
                icon = R.drawable.icon_bunny;
                color = getResources().getColor(R.color.colorCoelho);
                break;
        }

        //altera as imagens da logo e icon
        ivLogoAnimal.setBackgroundResource(logo);
        ivIconAnimal.setBackgroundResource(icon);

        Picasso.get().load(caso.getLinkImg()).into(ivLogoAnimal);

        //seta tamanho no icone do animal
        int size = sizeIcon();
        ivIconAnimal.getLayoutParams().width = size;
        ivIconAnimal.getLayoutParams().height = size;

        //define o tema dos botões
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        gd.setCornerRadius(35);
        btEmail.setBackground(gd);
        btWhatsapp.setBackground(gd);

        Button btDoar = findViewById(R.id.btDoar);
        btDoar.setBackground(gd);

        //Ajustando a largura dos botões
        btEmail.getLayoutParams().width = sizeButton();
        btWhatsapp.getLayoutParams().width = sizeButton();
    }

    public void setTextTv(int idTextView, String text) {
        TextView tv = findViewById(idTextView);
        tv.setText(text);
    }

    public void voltar(View view) {
        onBackPressed();
    }

    public void email(View view) {
        Intent intent = detalhesCasoController.sendEmail(caso);
        startActivity(Intent.createChooser(intent, "Escolha o cliente de e-mail"));
    }

    public void whatsapp(View view) {
        Intent intent = detalhesCasoController.sendWhatsapp(caso);
        startActivity(intent);
    }
}