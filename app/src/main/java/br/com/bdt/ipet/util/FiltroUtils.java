package br.com.bdt.ipet.util;

public class FiltroUtils {

    public static boolean filterEspecie(String especieCaso, String[] especiesEscolhidas){

        if(especiesEscolhidas.length == 0){
            return true;
        }

        for(String especieEscolhida : especiesEscolhidas){
            if(especieCaso.equals(especieEscolhida)){
                return true;
            }
        }

        return false;
    }

    public static boolean filterValor(Double valorCaso, Double valorMin, Double valorMax){

        //sem filtro de valor
        if(valorMin == 0.0 && valorMax == 0.0){
            return true;
        }

        //filtro de apenas valor mínimo
        if(valorMin != 0.0 && valorMax == 0.0){
            return valorCaso >= valorMin;
        }

        //filtro de apenas valor máximo
        if(valorMin == 0.0 && valorMax != 0.0){
            return valorCaso <= valorMax;
        }

        //filtro usando mínimo e máximo
        if(valorMin != 0.0 && valorMax != 0.0){
            return valorCaso >= valorMin && valorCaso <= valorMax;
        }

        //valor não apto, não passou por nenhuma condição
        return false;
    }

    public static boolean filterText(String textCaso, String textFiltro){

        if(textFiltro.equals("")){ //sem valor no filtro
            return true;
        }

        return textCaso.equals(textFiltro);
    }

}
