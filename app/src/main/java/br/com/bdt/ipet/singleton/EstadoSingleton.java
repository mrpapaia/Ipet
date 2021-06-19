package br.com.bdt.ipet.singleton;

import java.util.List;

import br.com.bdt.ipet.data.model.Estado;

public class EstadoSingleton {

    private static EstadoSingleton estadoSingleton;
    private List<Estado> estados;

    private EstadoSingleton() {

    }

    public static EstadoSingleton getEstadoSingleton() {
        if (estadoSingleton == null) {
            estadoSingleton = new EstadoSingleton();
        }
        return estadoSingleton;
    }

    public List<Estado> getEstados() {
        return estados;
    }

    public void setEstados(List<Estado> estados) {
        this.estados = estados;
    }
}
