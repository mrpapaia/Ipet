package br.com.bdt.ipet.singleton;

import java.util.List;

import br.com.bdt.ipet.data.model.Estado;

public class EstadoSingleton {

    private static EstadoSingleton estadoSingleton;
    private List<Estado> estados;
    private List<String> cidades;

    private EstadoSingleton() {

    }

    public static EstadoSingleton getEstadoSingleton() {
        if (estadoSingleton == null) {
            estadoSingleton = new EstadoSingleton();
        }
        return estadoSingleton;
    }

    public List<String> getCidades() {
        return cidades;
    }

    public void setCidades(List<String> cidades) {
        this.cidades = cidades;
    }

    public List<Estado> getEstados() {
        return estados;
    }

    public void setEstados(List<Estado> estados) {
        this.estados = estados;
    }
}
