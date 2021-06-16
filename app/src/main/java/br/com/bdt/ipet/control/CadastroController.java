package br.com.bdt.ipet.control;

import android.content.Context;
import br.com.bdt.ipet.data.api.ConsumerData;
import br.com.bdt.ipet.data.api.ConsumerData.SendDataObject;

public class CadastroController {

    public void isValidCNPJ(String CNPJ, Context context, SendDataObject sendDataObject){
        String url = "https://www.receitaws.com.br/v1/cnpj/" + CNPJ;
        new ConsumerData().sendGetObject(context, url, sendDataObject);
    }
}
