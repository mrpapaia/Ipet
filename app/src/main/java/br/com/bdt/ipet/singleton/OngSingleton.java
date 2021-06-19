package br.com.bdt.ipet.singleton;

import java.util.List;

import br.com.bdt.ipet.data.model.Ong;

public class OngSingleton {

    private static OngSingleton ongSingleton;
    private List<Ong> ongs;
    private Ong ong;

    private OngSingleton() {
    }

    public static OngSingleton getOngSingleton() {
        if (ongSingleton == null) {
            ongSingleton = new OngSingleton();
        }
        return ongSingleton;
    }

    public Ong getOng() {
        return ong;
    }

    public void setOng(Ong ong) {
        this.ong = ong;
    }

    public List<Ong> getOngs() {
        return ongs;
    }

    public void setOngs(List<Ong> ongs) {
        this.ongs = ongs;
    }
}
