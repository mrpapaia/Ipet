package br.com.bdt.ipet.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Ong implements Serializable, Parcelable {

    private String nome;
    private String email;
    private String whatsapp;
    private String uf;
    private String cidade;

    public Ong(){
    }

    public Ong(String nome, String email, String whatsapp, String uf, String cidade) {
        this.nome = nome;
        this.email = email;
        this.whatsapp = whatsapp;
        this.uf = uf;
        this.cidade = cidade;
    }

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

    @Override
    public String toString() {
        return "Ong{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", whatsapp='" + whatsapp + '\'' +
                ", uf='" + uf + '\'' +
                ", cidade='" + cidade + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(email);
        dest.writeString(whatsapp);
        dest.writeString(uf);
        dest.writeString(cidade);
    }

    public Ong(Parcel in){
        nome = in.readString();
        email = in.readString();
        whatsapp = in.readString();
        uf = in.readString();
        cidade = in.readString();
    }

    public static final Parcelable.Creator<Ong> CREATOR = new Parcelable.Creator<Ong>() {
        public Ong createFromParcel(Parcel in) {
            return new Ong(in);
        }

        public Ong[] newArray(int size) {
            return new Ong[size];
        }
    };
}
