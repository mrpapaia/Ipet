package br.com.bdt.ipet.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import br.com.bdt.ipet.data.api.DadosApi;
import br.com.bdt.ipet.R;
import br.com.bdt.ipet.util.ConfigureSpinner;
import br.com.bdt.ipet.util.NothingSelectedSpinnerAdapter;

import java.util.Collections;
import java.util.List;

public class SpinnerUtils {

    /*
     * Método para inicializar, inserir dados e configurar os spinners de UF e Cidade.
     * */
    public static void confSpinnersUfCity(final Context context, final Spinner spinnerUF,
                                          String titleSpUf, final Spinner spinnerCity,
                                          final String titleSpCity, Integer preIndexUf,
                                          final Integer preIndexCity) {

        final String urlStatic = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/";

        DadosApi dadosApiUF = new DadosApi(urlStatic, "sigla");

        initSpinner(spinnerCity, titleSpCity, context);
        initSpinner(spinnerUF, titleSpUf, context);

        new ConfigureSpinner(context, spinnerUF, titleSpUf,
                dadosApiUF, true,
                new ConfigureSpinner.Action() { //Define a ação ao selecionar um item no spinner UF
                    @Override
                    public void onSelect(String itemSelected) {
                        setDataSpCity(context, spinnerCity, titleSpCity, urlStatic, itemSelected,
                                preIndexCity);
                    }

                    @Override
                    public void onLoaded() {
                        if(preIndexCity != -1){
                            setDataSpCity(context, spinnerCity, titleSpCity, urlStatic,
                                    spinnerUF.getSelectedItem().toString(), preIndexCity);
                        }
                    }
                }, preIndexUf
        ).runConf();
    }

    /*
     * Método que irá inicializar um style_spinner, setando um conjunto de dados vazios, porém também
     * setará um titulo, adicionando um adaptador personalizado
     * */
    private static void initSpinner(Spinner spinner, String title, final Context context) {
        //spinner é desativado, e só é ativado na classe ConfigureSpinner
        spinner.setEnabled(false);
        setDataSpinner(spinner, context, title, Collections.singletonList(""));

        //se for o spinner de cidade, será necessáriod deixa-lo ativado
        //para que o evento de click seja possível, a fim de avisar o usuário
        //que deve-se selecionar primeiro a UF
        if(spinner.getId() == R.id.spFilterCidade){

            spinner.setEnabled(true);
            spinner.setOnTouchListener((v, event) -> {

                if (event.getAction() == MotionEvent.ACTION_UP){
                    GeralUtils.toast(context, "Informe o UF primeiro " +
                            "para carregar as cidades!");
                }

                return true;
            });
        }
    }

    /*
     * Irá setar os dados no style_spinner com ajuda do método runConf da classe ConfigureSpinner
     * */
    private static void setDataSpCity(Context context, Spinner spinnerCity, String titleSpCity,
                                      String urlStatic, String itemSelected, Integer preIndexCity) {

        //remove o aviso que aparecia alertando o usuário a escolher o UF primeiro.
        spinnerCity.setOnTouchListener(null);

        new ConfigureSpinner(context, spinnerCity, titleSpCity,
                new DadosApi(urlStatic + itemSelected + "/municipios", "nome"),
                false,
                new ConfigureSpinner.Action() {
                    @Override
                    public void onSelect(String itemSelected) {//Define a ação ao selecionar um item no spinner UF

                    }

                    @Override
                    public void onLoaded() {

                    }
                }, preIndexCity
        ).runConf();
    }

    /*
     * Método para setar dados dentro de um spinner, além de configurar o layout
     * */
    public static void setDataSpinner(Spinner spinner, Context context, String title, List<String> dados) {
        spinner.setAdapter(new NothingSelectedSpinnerAdapter(new ArrayAdapter<>(context,
                R.layout.spinner_row, dados), title, R.layout.spinner_row, context)
        );
    }

}
