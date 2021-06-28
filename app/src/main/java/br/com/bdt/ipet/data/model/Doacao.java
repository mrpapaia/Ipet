package br.com.bdt.ipet.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.util.Date;

public class Doacao implements Serializable, Parcelable {
    private String banco;
    private String tipo;
    private Double valor;
    private Date data;
    private DocumentReference id;

    public Doacao() {
    }

    public Doacao(String banco, String tipo, Double valor, Date data) {
        this.banco = banco;
        this.tipo = tipo;
        this.valor = valor;
        this.data = data;
    }

    public DocumentReference getId() {
        return id;
    }

    public void setId(DocumentReference id) {
        this.id = id;
    }

    protected Doacao(Parcel in) {
        banco = in.readString();
        tipo = in.readString();
        if (in.readByte() == 0) {
            valor = null;
        } else {
            valor = in.readDouble();
        }
    }

    public static final Creator<Doacao> CREATOR = new Creator<Doacao>() {
        @Override
        public Doacao createFromParcel(Parcel in) {
            return new Doacao(in);
        }

        @Override
        public Doacao[] newArray(int size) {
            return new Doacao[size];
        }
    };

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Doacao{" +
                "banco='" + banco + '\'' +
                ", tipo='" + tipo + '\'' +
                ", valor=" + valor +
                ", data=" + data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Doacao doacao = (Doacao) o;

        if (getBanco() != null ? !getBanco().equals(doacao.getBanco()) : doacao.getBanco() != null)
            return false;
        if (getTipo() != null ? !getTipo().equals(doacao.getTipo()) : doacao.getTipo() != null)
            return false;
        if (getValor() != null ? !getValor().equals(doacao.getValor()) : doacao.getValor() != null)
            return false;
        return getData() != null ? getData().equals(doacao.getData()) : doacao.getData() == null;
    }

    @Override
    public int hashCode() {
        int result = getBanco() != null ? getBanco().hashCode() : 0;
        result = 31 * result + (getTipo() != null ? getTipo().hashCode() : 0);
        result = 31 * result + (getValor() != null ? getValor().hashCode() : 0);
        result = 31 * result + (getData() != null ? getData().hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(banco);
        dest.writeString(tipo);
        if (valor == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(valor);
        }
    }
}
