package br.com.bdt.ipet.data.model;

import java.util.Arrays;

public class DadosFiltro {

    private String[] especies;
    private Double minValue;
    private Double maxValue;
    private String uf;
    private String cidade;
    private Integer idUf;
    private Integer idCity;

    public DadosFiltro(String[] especies, Double minValue, Double maxValue, String uf, String cidade, Integer idUf, Integer idCity) {
        this.especies = especies;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.uf = uf;
        this.cidade = cidade;
        this.idUf = idUf;
        this.idCity = idCity;
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

    public Integer getIdUf() {
        return idUf;
    }

    public void setIdUf(Integer idUf) {
        this.idUf = idUf;
    }

    public Integer getIdCity() {
        return idCity;
    }

    public void setIdCity(Integer idCity) {
        this.idCity = idCity;
    }

    @Override
    public String toString() {
        return "DadosFiltro{" +
                "especies=" + Arrays.toString(especies) +
                ", minValue=" + minValue +
                ", maxValue=" + maxValue +
                ", uf='" + uf + '\'' +
                ", cidade='" + cidade + '\'' +
                ", idUf=" + idUf +
                ", idCity=" + idCity +
                '}';
    }

    public boolean isClear() {

        DadosFiltro dadosVazios = new DadosFiltro(new String[0], 0.0, 0.0,
                "","", -1, -1);

        return Arrays.equals(especies, dadosVazios.especies) && minValue.equals(dadosVazios.getMinValue())
                && maxValue.equals(dadosVazios.getMaxValue()) && idUf.equals(dadosVazios.getIdUf())
                && idCity.equals(dadosVazios.getIdCity());
    }
}
