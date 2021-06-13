package br.com.bdt.ipet.data.api;

import android.os.Bundle;
import android.os.Parcelable;

import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.view.dialog.PagamentoDialog;

public class DadosApi {

    private String link;
    private String campo;

    public DadosApi(String link, String campo) {
        this.link = link;
        this.campo = campo;
    }
    public static DadosApi estados() {
        DadosApi dadosUrl = new DadosApi("https://servicodados.ibge.gov.br/api/v1/localidades/estados/","sigla");

        return dadosUrl;
    }
    public static DadosApi municipio(String estado) {
        DadosApi dadosUrl = new DadosApi("https://servicodados.ibge.gov.br/api/v1/localidades/estados/"+ estado + "/municipios", "nome");
        return dadosUrl;
    }
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    @Override
    public String toString() {
        return "DadosApi{" +
                "link='" + link + '\'' +
                ", campo='" + campo + '\'' +
                '}';
    }

}
