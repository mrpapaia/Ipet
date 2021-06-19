package br.com.bdt.ipet.util.interfaces;

import java.util.List;

import br.com.bdt.ipet.data.model.Caso;

public interface IFilter {
    void onFilter(List<Caso> casosFiltrados);
    void onClearFilter();
}
