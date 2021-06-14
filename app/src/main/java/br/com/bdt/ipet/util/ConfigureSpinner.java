package br.com.bdt.ipet.util;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.api.ConsumerData;
import br.com.bdt.ipet.data.api.DadosApi;
import br.com.bdt.ipet.util.SpinnerUtils;

import java.util.Collections;
import java.util.List;

public class ConfigureSpinner {

    public interface Action {
        void onSelect(String itemSelected);
        void onLoaded();
    }

    protected Spinner spinner;
    protected DadosApi dadosApi;
    protected Action action;
    protected Context context;
    protected Boolean isChecked;
    protected Boolean isSorted;
    protected String title;
    protected Integer preIndex;

    public ConfigureSpinner(final Context context, Spinner spinner, String title, DadosApi dadosApi,
                            Boolean isSorted, Action action, Integer preIndex) {
        this.spinner = spinner;
        this.dadosApi = dadosApi;
        this.action = action;
        this.context = context;
        isChecked = false;
        this.isSorted = isSorted;
        this.title = title;
        this.preIndex = preIndex;
    }

    /*
    * Método que irá configurar um style_spinner quase por completo, definindo os dados do mesmo, além
    * de definir a ação de quando um item for clicado, ou quando o spinner for clicado
    * */
 /*   public void runConf(){

        setDataOnSpinner();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
                if(isChecked) {
                    Object obj = parent.getItemAtPosition(pos);
                    if(obj != null){
                        String selected_item = obj.toString();
                        action.onSelect(selected_item);
                    }
                }else{
                    isChecked = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }*/

    /*
    * Método que irá consumir a api com ajuda da classe ConsumerData, onde foi definido o corpo
    * do método setData, pois o mesmo é uma interface.
    * */

/*    private void setDataOnSpinner(){

        spinner.setEnabled(false); //desativa o style_spinner enquanto não carrega os dados das cidades

        new ConsumerData(context, dadosApi, new ConsumerData.DataSite() {
            *//*
            * Definição do corpo do método setData, pois o mesmo é de uma interface.
            * Terá acesso a uma lista de Strings, podendo ser ordenado dependendo do valor da
            * variável isSorted, por fim, o style_spinner terá seu adaptador alterado já com os dados,
            * além de personalizar o style_spinner com um título.
            * *//*
            @Override
            public void setData(List<String> dados) {

                if(isSorted) {
                    Collections.sort(dados);
                }

                SpinnerUtils.setDataSpinner(spinner, context, title, dados);

                if(preIndex != -1){
                    spinner.setSelection(preIndex);
                }

                spinner.setEnabled(true); //ativa quando os dados chegar
                action.onLoaded(); //avisa que os dados foram inseridos
            }
        }).getData();

    }*/
}
