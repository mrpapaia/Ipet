package br.com.bdt.ipet.singleton;

import android.net.Uri;

import java.util.List;

import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.CasoComDoacao;
import br.com.bdt.ipet.data.model.DadosFiltro;
import br.com.bdt.ipet.util.interfaces.IFilter;

public class CasoSingleton {

    private static CasoSingleton casoSingleton;
    private List<CasoComDoacao> casos;
    private DadosFiltro dadosFiltro;
    private IFilter iFilter;
    private Caso caso;
    private Uri uri;

    private CasoSingleton() {

    }

    public static CasoSingleton getCasoSingleton() {
        if (casoSingleton == null) {
            casoSingleton = new CasoSingleton();
        }
        return casoSingleton;
    }

    public List<CasoComDoacao> getCasos() {
        return casos;
    }

    public String textSizeCasos(){
        return String.valueOf(casos.size());
    }

    public void setCasos(List<CasoComDoacao> casos) {
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

    public Caso getCaso() {
        return caso;
    }

    public void setCaso(Caso caso) {
        this.caso = caso;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
