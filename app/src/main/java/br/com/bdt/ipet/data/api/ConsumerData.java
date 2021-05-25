package br.com.bdt.ipet.data.api;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConsumerData {

    public interface DataSite {
        void setData(List<String> dados);
    }

    Context context;
    DataSite dataSite;
    DadosApi dadosApi;

    public ConsumerData(Context context, DadosApi dadosApi, DataSite dataSite) {
        this.context = context;
        this.dadosApi = dadosApi;
        this.dataSite = dataSite;
    }

    /*
    * Método que irá realizar requisição GET a uma API, salvando os dados em uma lista de String.
    * Quando obter resposta, o método setData será chamado, onde o corpo deste será definido por
    * quem instânciar ele por fora, pois nesta classe ele é apenas uma interface.
    * */
    public void getData(){

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(dadosApi.getLink(),
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                List<String> datas = new ArrayList<>();

                for(int i=0; i<response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String data = jsonObject.getString(dadosApi.getCampo());
                        datas.add(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                dataSite.setData(datas);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

}
