package br.com.bdt.ipet.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CasoComDoacao implements Serializable, Parcelable {
    private Caso caso;
    private List<Doacao> doacaoList;
    private Integer qtdDoacaoPendente;

    public CasoComDoacao(Caso caso) {
        this.caso = caso;
        this.doacaoList= new ArrayList<>();
        this.qtdDoacaoPendente=0;
    }

    public CasoComDoacao(Caso caso, List<Doacao> doacaoList, Integer qtdDoacaoPendente) {
        this.caso = caso;
        this.doacaoList = doacaoList;
        this.qtdDoacaoPendente = qtdDoacaoPendente;
    }

    protected CasoComDoacao(Parcel in) {
        caso = in.readParcelable(Caso.class.getClassLoader());
        doacaoList = in.createTypedArrayList(Doacao.CREATOR);
        if (in.readByte() == 0) {
            qtdDoacaoPendente = null;
        } else {
            qtdDoacaoPendente = in.readInt();
        }
    }

    public static final Creator<CasoComDoacao> CREATOR = new Creator<CasoComDoacao>() {
        @Override
        public CasoComDoacao createFromParcel(Parcel in) {
            return new CasoComDoacao(in);
        }

        @Override
        public CasoComDoacao[] newArray(int size) {
            return new CasoComDoacao[size];
        }
    };

    public Caso getCaso() {
        return caso;
    }

    public void setCaso(Caso caso) {
        this.caso = caso;
    }

    public List<Doacao> getDoacaoList() {
        return doacaoList;
    }

    public void setDoacaoList(List<Doacao> doacaoList) {
        this.doacaoList = doacaoList;
    }

    public Integer getQtdDoacaoPendente() {
        return qtdDoacaoPendente;
    }

    public void setQtdDoacaoPendente(Integer qtdDoacaoPendente) {
        this.qtdDoacaoPendente = qtdDoacaoPendente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CasoComDoacao)) return false;

        CasoComDoacao that = (CasoComDoacao) o;

        if (getCaso() != null ? !getCaso().equals(that.getCaso()) : that.getCaso() != null)
            return false;
        if (getDoacaoList() != null ? !getDoacaoList().equals(that.getDoacaoList()) : that.getDoacaoList() != null)
            return false;
        return getQtdDoacaoPendente() != null ? getQtdDoacaoPendente().equals(that.getQtdDoacaoPendente()) : that.getQtdDoacaoPendente() == null;
    }

    @Override
    public int hashCode() {
        int result = getCaso() != null ? getCaso().hashCode() : 0;
        result = 31 * result + (getDoacaoList() != null ? getDoacaoList().hashCode() : 0);
        result = 31 * result + (getQtdDoacaoPendente() != null ? getQtdDoacaoPendente().hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(caso, flags);
        dest.writeTypedList(doacaoList);
        if (qtdDoacaoPendente == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(qtdDoacaoPendente);
        }
    }
}
