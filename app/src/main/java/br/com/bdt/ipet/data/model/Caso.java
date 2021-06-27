package br.com.bdt.ipet.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Caso implements Serializable, Parcelable {

    private String id;
    private String titulo;
    private String descricao;
    private String nomeAnimal;
    private String especie;
    private Double valor;
    private Double arrecadado;
    private String linkImg;
    private Ong ong;

    public Caso(){
    }

    public Caso(String id, String titulo, String descricao, String nomeAnimal, String especie, Double valor, Double arrecadado, String linkImg, Ong ong) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.nomeAnimal = nomeAnimal;
        this.especie = especie;
        this.valor = valor;
        this.arrecadado = arrecadado;
        this.linkImg = linkImg;
        this.ong = ong;
    }

    protected Caso(Parcel in) {
        id = in.readString();
        titulo = in.readString();
        descricao = in.readString();
        nomeAnimal = in.readString();
        especie = in.readString();
        if (in.readByte() == 0) {
            valor = null;
        } else {
            valor = in.readDouble();
        }
        if (in.readByte() == 0) {
            arrecadado = null;
        } else {
            arrecadado = in.readDouble();
        }
        linkImg = in.readString();
        ong = in.readParcelable(Ong.class.getClassLoader());
    }

    public static final Creator<Caso> CREATOR = new Creator<Caso>() {
        @Override
        public Caso createFromParcel(Parcel in) {
            return new Caso(in);
        }

        @Override
        public Caso[] newArray(int size) {
            return new Caso[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNomeAnimal() {
        return nomeAnimal;
    }

    public void setNomeAnimal(String nomeAnimal) {
        this.nomeAnimal = nomeAnimal;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getArrecadado() {
        return arrecadado;
    }

    public void setArrecadado(Double arrecadado) {
        this.arrecadado = arrecadado;
    }

    public String getLinkImg() {
        return linkImg;
    }

    public void setLinkImg(String linkImg) {
        this.linkImg = linkImg;
    }

    public Ong getOng() {
        return ong;
    }

    public void setOng(Ong ong) {
        this.ong = ong;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Caso)) return false;

        Caso caso = (Caso) o;

        if (getId() != null ? !getId().equals(caso.getId()) : caso.getId() != null) return false;
        if (getTitulo() != null ? !getTitulo().equals(caso.getTitulo()) : caso.getTitulo() != null)
            return false;
        if (getDescricao() != null ? !getDescricao().equals(caso.getDescricao()) : caso.getDescricao() != null)
            return false;
        if (getNomeAnimal() != null ? !getNomeAnimal().equals(caso.getNomeAnimal()) : caso.getNomeAnimal() != null)
            return false;
        if (getEspecie() != null ? !getEspecie().equals(caso.getEspecie()) : caso.getEspecie() != null)
            return false;
        if (getValor() != null ? !getValor().equals(caso.getValor()) : caso.getValor() != null)
            return false;
        if (getArrecadado() != null ? !getArrecadado().equals(caso.getArrecadado()) : caso.getArrecadado() != null)
            return false;
        if (getLinkImg() != null ? !getLinkImg().equals(caso.getLinkImg()) : caso.getLinkImg() != null)
            return false;
        return getOng() != null ? getOng().equals(caso.getOng()) : caso.getOng() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getTitulo() != null ? getTitulo().hashCode() : 0);
        result = 31 * result + (getDescricao() != null ? getDescricao().hashCode() : 0);
        result = 31 * result + (getNomeAnimal() != null ? getNomeAnimal().hashCode() : 0);
        result = 31 * result + (getEspecie() != null ? getEspecie().hashCode() : 0);
        result = 31 * result + (getValor() != null ? getValor().hashCode() : 0);
        result = 31 * result + (getArrecadado() != null ? getArrecadado().hashCode() : 0);
        result = 31 * result + (getLinkImg() != null ? getLinkImg().hashCode() : 0);
        result = 31 * result + (getOng() != null ? getOng().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Caso{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", nomeAnimal='" + nomeAnimal + '\'' +
                ", especie='" + especie + '\'' +
                ", valor=" + valor +
                ", arrecadado=" + arrecadado +
                ", linkImg='" + linkImg + '\'' +
                ", ong=" + ong +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(titulo);
        dest.writeString(descricao);
        dest.writeString(nomeAnimal);
        dest.writeString(especie);
        if (valor == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(valor);
        }
        if (arrecadado == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(arrecadado);
        }
        dest.writeString(linkImg);
        dest.writeParcelable(ong, flags);
    }
}
