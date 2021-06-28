package br.com.bdt.ipet.control;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.bdt.ipet.data.model.Caso;

public class DetalhesCasoController {

    public DetalhesCasoController() {
    }

    public Intent sendEmail(Caso caso) {

        String msg = getMsgParaOng(caso);
        String destinatario = caso.getOng().getEmail();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{destinatario});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Quero ajudar um caso");
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        intent.setType("text/plain");

        return intent;
    }

    public Intent sendWhatsapp(Caso caso) {

        String msg = getMsgParaOng(caso);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=55" + caso.getOng().getWhatsapp() + "&text=" + msg));

        return intent;
    }

    @SuppressLint("DefaultLocale")
    public String getMsgParaOng(Caso caso) {
        return getMsgHoras() + " " + caso.getOng().getNome() + ", Gostaria de ajudar no caso " + caso.getTitulo() + ".";
    }

    public String getMsgHoras() {

        @SuppressLint("SimpleDateFormat")
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
