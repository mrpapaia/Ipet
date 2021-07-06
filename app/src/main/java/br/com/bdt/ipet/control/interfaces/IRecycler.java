package br.com.bdt.ipet.control.interfaces;

import java.util.List;

import br.com.bdt.ipet.data.model.CasoComDoacao;

public interface IRecycler {
    void init(List<CasoComDoacao> casos);
}
