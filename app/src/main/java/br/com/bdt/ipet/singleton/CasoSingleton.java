package br.com.bdt.ipet.singleton;

import java.util.List;

import br.com.bdt.ipet.data.model.Caso;

public class CasoSingleton {

    private static CasoSingleton casoSingleton;
    private List<Caso> casosAll;
    private List<Caso> casosOng;

    private CasoSingleton() {

    }

    public static CasoSingleton getCasoSingleton() {
        if (casoSingleton == null) {
            casoSingleton = new CasoSingleton();
        }
        return casoSingleton;
    }

    public List<Caso> getCasosAll() {
        return casosAll;
    }

    public void setCasosAll(List<Caso> casosAll) {
        this.casosAll = casosAll;
    }

    public List<Caso> getCasosOng() {
        return casosOng;
    }

    public void setCasosOng(List<Caso> casosOng) {
        this.casosOng = casosOng;
    }
}
