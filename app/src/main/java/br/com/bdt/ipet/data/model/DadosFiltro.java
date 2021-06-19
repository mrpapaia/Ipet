package br.com.bdt.ipet.data.model;

import java.util.Arrays;

public class DadosFiltro {

    private String[] especies;
    private Double minValue;
    private Double maxValue;
    private String uf;
    private String cidade;
    private String emailOng;

    public DadosFiltro(){

    }

    public DadosFiltro(String[] especies, Double minValue, Double maxValue, String uf, String cidade, String emailOng) {
        this.especies = especies;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.uf = uf;
        this.cidade = cidade;
        this.emailOng = emailOng;
    }

    public String[] getEspecies() {
        return especies;
    }

    public void setEspecies(String[] especies) {
        this.especies = especies;
    }

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEmailOng() {
        return emailOng;
    }

    public void setEmailOng(String emailOng) {
        this.emailOng = emailOng;
    }

    public boolean isClear() {
        return Arrays.equals(especies, new String[0]) &&
                minValue.equals(0.0) &&
                maxValue.equals(0.0) &&
                uf.equals("") &&
                cidade.equals("") &&
                emailOng.equals("");
    }

    @Override
    public String toString() {
        return "DadosFiltro{" +
                "especies=" + Arrays.toString(especies) +
                ", minValue=" + minValue +
                ", maxValue=" + maxValue +
                ", uf='" + uf + '\'' +
                ", cidade='" + cidade + '\'' +
                ", emailOng='" + emailOng + '\'' +
                '}';
    }
}
