package br.com.bdt.ipet.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.view.dialog.MetodoPagamentoDialog;
import br.com.bdt.ipet.view.dialog.PagamentoDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetalhesCasoActivity extends AppCompatActivity {

    Caso caso;
    Button btEmail, btWhatsapp;
    ImageView ivLogoAnimal, ivIconAnimal;
    TextView tvLocalizacao;
    Button btn;
    private Ong ong;
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

        setarInformacoes();


    }

    @Override
    protected void onStart() {
        super.onStart();
        btn = (Button) findViewById(R.id.btDoar);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = MetodoPagamentoDialog.newInstance(ong);

                dialog.show(getSupportFragmentManager(), "MetodoPagamento");
                getSupportFragmentManager().executePendingTransactions();
                dialog.getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);


            }
        };


        btn.setOnClickListener(listener);
    }

    /*
     * Pega todos os dados do caso e seta nos TextView's
     * */
    public void setarInformacoes() {

     ong = caso.getOng(); //Pega a ong do caso

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

        //Ajustando a largura dos botões
        btEmail.getLayoutParams().width = sizeButton();
        btWhatsapp.getLayoutParams().width = sizeButton();
    }

    /*
     * Recebe id de um TextView e uma string, instanciando um TextView e setando o texto.
     * */
    public void setTextTv(int idTextView, String text) {
        TextView tv = findViewById(idTextView);
        tv.setText(text);
    }

    /*
     * Primeiramente é necessário fazer o carregamento das conexões que estão no bd,
     * após isso é incrementado o contador qtd para atualizar o número de conexões feitas.
     * */
    public void setConexoes() {
        FirebaseFirestore.getInstance()
                .collection("conexoes")
                .document("counter")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Integer qtd = document.get("quantidade", Integer.class);
                            qtd++;
                            Map<String, Object> qtdConexoes = new HashMap<>();
                            qtdConexoes.put("quantidade", qtd);
                            FirebaseFirestore.getInstance()
                                    .collection("conexoes")
                                    .document("counter")
                                    .set(qtdConexoes);
                        }
                    }
                });
    }

    /*
     * Chamado no onClick da setinha na parte superior da interface de detalhes de caso
     * fazendo com que apenas simule o clique no botão de voltar
     * */
    public void voltar(View view) {
        onBackPressed();
    }

    /*
     * Abre uma lista de aplicativos para mandar um email com o propósito de avisar a ong
     * que a pessoa quer ajudar no caso.
     * */
    public void email(View view) {

        String msg = getMsgParaOng();
        String destinatario = caso.getOng().getEmail();

        Intent EnviarEmail = new Intent(Intent.ACTION_SEND);
        EnviarEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{destinatario});
        EnviarEmail.putExtra(Intent.EXTRA_SUBJECT, "Quero ajudar um caso");
        EnviarEmail.putExtra(Intent.EXTRA_TEXT, msg);
        EnviarEmail.setType("text/plain");
        startActivity(Intent.createChooser(EnviarEmail, "Escolha o cliente de e-mail"));

        setConexoes();
    }

    /*
     * Abre o whatsapp ja na conversa com o número da ong, mandando uma mensagem informando
     * o interesse no caso.
     * */
    public void whatsapp(View view) {

        String msg = getMsgParaOng();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=55" +
                caso.getOng().getWhatsapp() +
                "&text=" +
                msg));

        startActivity(intent);
        setConexoes();
    }

    /*
     * Método que define a mensagem que será enviada no email ou whatsapp
     * */
    public String getMsgParaOng() {
        return getMsgHoras() + " " + caso.getOng().getNome() + ", Gostaria de ajudar no caso " +
                caso.getTitulo() + ", com o valor de " + String.format("R$ %.2f", caso.getValor());
    }

    /*
     * Coleta a hora atual e retorna uma mensagem de saudação
     * */
    public String getMsgHoras() {

        SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH");
        int horas = Integer.parseInt(dateFormat_hora.format(new Date()));

        if (horas >= 0 && horas < 12) {
            return "Bom Dia";
        } else if (horas < 18) {
            return "Boa tarde";
        } else {
            return "Boa Noite";
        }

    }

}