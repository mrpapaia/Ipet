package br.com.bdt.ipet.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Objects;

public class DadosBancario implements Serializable, Parcelable {
    private String banco;
    private String conta;
    private String agencia;
    private String chavePix;
    private String cpfCNPJ;

    public DadosBancario() {

    }

    public DadosBancario(String banco, String conta, String agencia, String chavePix, String cpfCNPJ) {
        this.banco = banco;
        this.conta = conta;
        this.agencia = agencia;
        this.chavePix = chavePix;
        this.cpfCNPJ = cpfCNPJ;
    }

    protected DadosBancario(Parcel in) {
        banco = in.readString();
        conta = in.readString();
        agencia = in.readString();
        chavePix = in.readString();
        cpfCNPJ = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(banco);
        dest.writeString(conta);
        dest.writeString(agencia);
        dest.writeString(chavePix);
        dest.writeString(cpfCNPJ);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DadosBancario> CREATOR = new Creator<DadosBancario>() {
        @Override
        public DadosBancario createFromParcel(Parcel in) {
            return new DadosBancario(in);
        }

        @Override
        public DadosBancario[] newArray(int size) {
            return new DadosBancario[size];
        }
    };

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getChavePix() {
        return chavePix;
    }

    public void setChavePix(String chavePix) {
        this.chavePix = chavePix;
    }

    public String getCpfCNPJ() {
        return cpfCNPJ;
    }

    public void setCpfCNPJ(String cpfCNPJ) {
        this.cpfCNPJ = cpfCNPJ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DadosBancario)) return false;
        DadosBancario that = (DadosBancario) o;
        return getBanco().equals(that.getBanco()) &&
                getConta().equals(that.getConta()) &&
                getAgencia().equals(that.getAgencia()) &&
                getChavePix().equals(that.getChavePix()) &&
                getCpfCNPJ().equals(that.getCpfCNPJ());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBanco(), getConta(), getAgencia(), getChavePix(), getCpfCNPJ());
    }
}
