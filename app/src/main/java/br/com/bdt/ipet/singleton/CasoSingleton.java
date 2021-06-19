package br.com.bdt.ipet.singleton;

import java.util.List;

import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.DadosFiltro;
import br.com.bdt.ipet.util.interfaces.IFilter;

public class CasoSingleton {

    private static CasoSingleton casoSingleton;
    private List<Caso> casos;
    private DadosFiltro dadosFiltro;
    private IFilter iFilter;

    private CasoSingleton() {

    }

    public static CasoSingleton getCasoSingleton() {
        if (casoSingleton == null) {
            casoSingleton = new CasoSingleton();
        }
        return casoSingleton;
    }

    public List<Caso> getCasos() {
        return casos;
    }

    public String textSizeCasos(){
        return String.valueOf(casos.size());
    }

    public void setCasos(List<Caso> casos) {
        this.casos = casos;
    }

    public DadosFiltro getDadosFiltro() {
        if(dadosFiltro == null){
            setDadosFiltro(null);
        }
        return dadosFiltro;
    }

    public void setDadosFiltro(DadosFiltro dadosFiltro) {

        if(dadosFiltro == null){ //se for null = clear nele

            if(this.dadosFiltro == null){
                this.dadosFiltro = new DadosFiltro();
            }

            this.dadosFiltro.setEspecies(new String[0]);
            this.dadosFiltro.setMinValue(0.0);
            this.dadosFiltro.setMaxValue(0.0);
            this.dadosFiltro.setUf("");
            this.dadosFiltro.setCidade("");
            this.dadosFiltro.setEmailOng("");

            return;
        }

        this.dadosFiltro = dadosFiltro;
    }

    public IFilter getiFilter() {
        return iFilter;
    }

    public void setiFilter(IFilter iFilter) {
        this.iFilter = iFilter;
    }
}
