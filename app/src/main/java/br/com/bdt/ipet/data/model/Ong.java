package br.com.bdt.ipet.data.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

public class Ong implements Serializable, Parcelable {

    private String nome;
    private String email;
    private String whatsapp;
    private String uf;
    private String cidade;
    private List<DadosBancario> dadosBancarios;
    private String imgPerfil;

    public Ong() {
    }

    public Ong(String nome, String email, String whatsapp, String uf, String cidade) {
        this.nome = nome;
        this.email = email;
        this.whatsapp = whatsapp;
        this.uf = uf;
        this.cidade = cidade;

    }
    public Ong(String nome, String email, String whatsapp, String uf, String cidade, List<DadosBancario> dadosBancarios) {
        this.nome = nome;
        this.email = email;
        this.whatsapp = whatsapp;
        this.uf = uf;
        this.cidade = cidade;
        this.dadosBancarios = dadosBancarios;
    }

    protected Ong(Parcel in) {
        nome = in.readString();
        email = in.readString();
        whatsapp = in.readString();
        uf = in.readString();
        cidade = in.readString();
        dadosBancarios = in.createTypedArrayList(DadosBancario.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(email);
        dest.writeString(whatsapp);
        dest.writeString(uf);
        dest.writeString(cidade);
        dest.writeTypedList(dadosBancarios);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ong> CREATOR = new Creator<Ong>() {
        @Override
        public Ong createFromParcel(Parcel in) {
            return new Ong(in);
        }

        @Override
        public Ong[] newArray(int size) {
            return new Ong[size];
        }
    };

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public List<DadosBancario> getDadosBancarios() {
        return dadosBancarios;
    }

    public void setDadosBancarios(List<DadosBancario> dadosBancarios) {
        this.dadosBancarios = dadosBancarios;
    }

    public String getImgPerfil() {
        return imgPerfil;
    }

    public void setImgPerfil(String imgPerfil) {
        this.imgPerfil = imgPerfil;

    }

    @Override
    public String toString() {
        return "Ong{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", whatsapp='" + whatsapp + '\'' +
                ", uf='" + uf + '\'' +
                ", cidade='" + cidade + '\'' +
                ", dadosBancarios=" + dadosBancarios +
                ", imgPerfil=" + imgPerfil +
                '}';
    }
}
