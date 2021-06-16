package br.com.bdt.ipet.singleton;



import br.com.bdt.ipet.data.model.Ong;

public class OngSingleton {


    private final String TAG = "OngSingleton.class";

    private static OngSingleton ongSingleton;
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
}
