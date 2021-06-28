package br.com.bdt.ipet.control;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.model.CasoComDoacao;
import br.com.bdt.ipet.data.model.DadosFiltro;
import br.com.bdt.ipet.data.model.Ong;
import br.com.bdt.ipet.repository.OngRepository;
import br.com.bdt.ipet.singleton.CasoSingleton;
import br.com.bdt.ipet.singleton.OngSingleton;
import br.com.bdt.ipet.util.FiltroUtils;
import br.com.bdt.ipet.util.interfaces.IFilter;

public class FilterController {

    private final OngSingleton ongSingleton;
    private final CasoSingleton casoSingleton;
    private HashMap<Integer, Boolean> especiesEscolhidas;

    public FilterController() {
        ongSingleton = OngSingleton.getOngSingleton();
        casoSingleton = CasoSingleton.getCasoSingleton();
    }

    public boolean haNomesOng() {
        return ongSingleton.getOngs() != null;
    }

    public Task<QuerySnapshot> requestNomeOngs() {

        OngRepository ongRepository = new OngRepository(FirebaseFirestore.getInstance());

        return ongRepository.findAll().addOnSuccessListener(doc -> {
            List<Ong> ongs = doc.toObjects(Ong.class);
            ongSingleton.setOngs(ongs);
        });
    }

    public List<Ong> getOngs(){
        return ongSingleton.getOngs();
    }

    public void setarOngEscolhida(AdapterView<?> parent, int position){
        CasoSingleton casoSingleton = CasoSingleton.getCasoSingleton();
        Ong ong = (Ong) parent.getItemAtPosition(position);
        casoSingleton.getDadosFiltro().setEmailOng(ong.getEmail());
    }

    public String getNomeByEmailOng(String email){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            Ong ong = ongSingleton.getOngs()
                    .stream()
                    .filter(x -> x.getEmail().equals(email))
                    .findAny()
                    .orElse(null);

            return (ong != null) ? ong.getNome() : "";
        }

        return null;
    }

    public void setDadosFiltro(String[] especies, Double minValue, Double maxValue, String uf, String cidade){
        DadosFiltro dadosFiltro = casoSingleton.getDadosFiltro();
        dadosFiltro.setEspecies(especies);
        dadosFiltro.setMinValue(minValue);
        dadosFiltro.setMaxValue(maxValue);
        dadosFiltro.setUf(uf);
        dadosFiltro.setCidade(cidade);
    }

    public DadosFiltro getDadosFiltro(){
        return casoSingleton.getDadosFiltro();
    }

    public void activeFilter(){
        List<CasoComDoacao> casos = casosFiltrados();
        System.out.println("Size casos: " + casos.size());
        casoSingleton.getiFilter().onFilter(casos);
    }

    public List<CasoComDoacao> casosFiltrados(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return casoSingleton.getCasos().stream().filter(this::isfiltrado).collect(Collectors.toList());
        }
        return null;
    }

    public boolean isfiltrado(CasoComDoacao caso){
        DadosFiltro dadosFiltro = casoSingleton.getDadosFiltro();
        return FiltroUtils.filterEspecie(caso.getCaso().getEspecie(), dadosFiltro.getEspecies()) &&
                FiltroUtils.filterValor(caso.getCaso().getValor(), dadosFiltro.getMinValue(), dadosFiltro.getMaxValue()) &&
                FiltroUtils.filterText(caso.getCaso().getOng().getUf(), dadosFiltro.getUf()) &&
                FiltroUtils.filterText(caso.getCaso().getOng().getCidade(), dadosFiltro.getCidade()) &&
                FiltroUtils.filterText(caso.getCaso().getOng().getEmail(), dadosFiltro.getEmailOng());
    }

    public void clearFilter(){
        casoSingleton.setDadosFiltro(null);
        casoSingleton.getiFilter().onClearFilter();
    }

    public void initValuesFilter(Activity act){

        DadosFiltro dados = getDadosFiltro();

        //Inicia os valores das espécies selecionadas com false(não selecionada)
        especiesEscolhidas = new HashMap<>();
        especiesEscolhidas.put(R.id.ivEspCao, false);
        especiesEscolhidas.put(R.id.ivEspGato, false);
        especiesEscolhidas.put(R.id.ivEspCoelho, false);

        //Verifica quais espécies estão ativas e seta true
        for(int i=0; i<dados.getEspecies().length; i++){
            switch (dados.getEspecies()[i]){
                case "Cachorro": especiesEscolhidas.put(R.id.ivEspCao, true); break;
                case "Gato": especiesEscolhidas.put(R.id.ivEspGato, true); break;
                case "Coelho": especiesEscolhidas.put(R.id.ivEspCoelho, true); break;
            }
        }

        changeImgEspecie(R.id.ivEspCao, especiesEscolhidas.get(R.id.ivEspCao), act);
        changeImgEspecie(R.id.ivEspGato, especiesEscolhidas.get(R.id.ivEspGato), act);
        changeImgEspecie(R.id.ivEspCoelho, especiesEscolhidas.get(R.id.ivEspCoelho), act);
    }

    @SuppressWarnings("ConstantConditions")
    public String[] especiesSelected(){

        List<String> especies = new ArrayList<>();

        Boolean espCao = especiesEscolhidas.get(R.id.ivEspCao);
        Boolean espGato = especiesEscolhidas.get(R.id.ivEspGato);
        Boolean espCoelho = especiesEscolhidas.get(R.id.ivEspCoelho);

        if(espCao) especies.add("Cachorro");
        if(espGato) especies.add("Gato");
        if(espCoelho) especies.add("Coelho");

        return especies.toArray(new String[0]);
    }

    @SuppressWarnings("ConstantConditions")
    public void selectEspecie(Integer id, Activity act){
        Boolean isSelected = !especiesEscolhidas.get(id);
        especiesEscolhidas.put(id, isSelected);
        changeImgEspecie(id, isSelected, act);
    }

    @SuppressLint("NonConstantResourceId")
    public void changeImgEspecie(Integer id, Boolean selected, Activity act){

        ImageView iv = act.findViewById(id);
        int backgroundId = -1;

        switch (id){
            case R.id.ivEspCao: backgroundId = selected ? R.drawable.icone_dog2 : R.drawable.icone_dog1; break;
            case R.id.ivEspGato: backgroundId = selected ? R.drawable.icone_cat2 : R.drawable.icone_cat1; break;
            case R.id.ivEspCoelho: backgroundId = selected ? R.drawable.icone_bunny2 : R.drawable.icone_bunny1; break;
        }

        iv.setBackgroundResource(backgroundId);
    }
}
