package br.com.bdt.ipet.data.api;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.bdt.ipet.data.model.Estado;

public class ConsumerData {

    public interface SendDataArray {
        void onData(JSONArray response);
    }

    public interface SendDataObject {
        void onData(JSONObject response) throws JSONException;
    }

    public interface DataSiteEstado {
        void setDataEstado(List<Estado> estados);
    }

    public interface DataSiteCidade {
        void setDataCidade(List<String> cidades);
    }

    public void sendGetArray(Context context, String url, SendDataArray sendDataArray){

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                response -> sendDataArray.onData(response),
                error -> Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show());

        requestQueue.add(jsonArrayRequest);
    }

    public void sendGetObject(Context context, String url, SendDataObject sendDataObject){

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                response -> {
                    try {
                        sendDataObject.onData(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show());

        requestQueue.add(jsonObjectRequest);

    }


    public void getEstados(Context context, DataSiteEstado dataSiteEstado){

        String url = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/";

        sendGetArray(context, url,  response -> {

            List<Estado> estados = new ArrayList<>();

            for(int i=0; i<response.length(); i++){
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    String uf = jsonObject.getString("sigla");
                    String nome = jsonObject.getString("nome");
                    Estado estado = new Estado(uf, nome);
                    estados.add(estado);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            dataSiteEstado.setDataEstado(estados);
        });
    }

    public void getCidades(Context context, String uf, DataSiteCidade dataSiteCidade){

        String url = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/"+uf+"/municipios/";

        sendGetArray(context, url,  response -> {

            List<String> cidades = new ArrayList<>();

            for(int i=0; i<response.length(); i++){
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    String nome = jsonObject.getString("nome");
                    cidades.add(nome);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            dataSiteCidade.setDataCidade(cidades);
        });
    }

}
