package br.com.bdt.ipet.data.api;

public class DadosApi {

    private String link;
    private String campo;

    public DadosApi(String link, String campo) {
        this.link = link;
        this.campo = campo;
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
