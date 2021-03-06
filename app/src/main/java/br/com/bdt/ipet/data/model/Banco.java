package br.com.bdt.ipet.data.model;

import java.io.Serializable;

public class Banco implements Serializable {

    private String label;
    private String value;

    public Banco(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return label;
    }
}
