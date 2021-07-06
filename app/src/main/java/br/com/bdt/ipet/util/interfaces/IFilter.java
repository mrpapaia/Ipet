package br.com.bdt.ipet.util.interfaces;

import java.util.List;

import br.com.bdt.ipet.data.model.CasoComDoacao;

public interface IFilter {
    void onFilter(List<CasoComDoacao> casosFiltrados);
    void onClearFilter();
}
