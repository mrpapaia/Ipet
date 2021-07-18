package br.com.bdt.ipet.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.bdt.ipet.data.api.ConsumerData;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.Estado;
import br.com.bdt.ipet.singleton.EstadoSingleton;

import static android.util.Patterns.EMAIL_ADDRESS;

public class GeralUtils {

    public static AlertDialog.Builder builderDialog(Context context, int idIcon, String title, String message,
                                             String textBt1, DialogInterface.OnClickListener listenerBt1,
                                             String textBt2, DialogInterface.OnClickListener listenerBt2){
        return builderDialog(context, idIcon, title, message, textBt1, listenerBt1).setNegativeButton(textBt2, listenerBt2);
    }

    public static AlertDialog.Builder builderDialog(Context context, int idIcon, String title, String message, String textBt1, DialogInterface.OnClickListener listenerBt1){
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(textBt1, listenerBt1)
                .setIcon(idIcon);
    }

    public static boolean existeCidade(String cidade){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            List<String> cidades = EstadoSingleton.getEstadoSingleton().getCidades();
            if(cidades != null){
                return cidades.stream().anyMatch(x -> x.toLowerCase().equals(cidade.toLowerCase()));
            }
        }

        return true;
    }

    public static boolean existeEstado(String estado){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            List<Estado> estados = EstadoSingleton.getEstadoSingleton().getEstados();
            if(estados != null){
                return estados.stream().anyMatch(x -> x.getNome().toLowerCase().equals(estado.toLowerCase()));
            }
        }

        return true;
    }

    public static void setErrorInput(EditText et, String msg){
        et.setError(msg);
        et.requestFocus();
    }

    @SuppressLint("DefaultLocale")
    public static String formatarValor(double valor){
        return String.format("R$ %.2f", valor);
    }

    public static Map<String, Object> CasoToMap(Caso caso){

        Map<String, Object> docCaso = new HashMap<>();

        docCaso.put("id", caso.getId());
        docCaso.put("titulo", caso.getTitulo());
        docCaso.put("descricao", caso.getDescricao());
        docCaso.put("nomeAnimal", caso.getNomeAnimal());
        docCaso.put("especie", caso.getEspecie());
        docCaso.put("valor", caso.getValor());
        docCaso.put("arrecadado", caso.getArrecadado());
        docCaso.put("linkImg", caso.getLinkImg());

        return docCaso;
    }

    public static String getDataOfSp(Activity act, int idSpinner){
        Spinner sp = act.findViewById(idSpinner);
        Object selected = sp.getSelectedItem();
        return selected == null ? "" : selected.toString();
    }

    public static void initAutoCompletUfCity(Context context, AutoCompleteTextView acUf, AutoCompleteTextView acMunicipio){

        EstadoSingleton estadoSingleton = EstadoSingleton.getEstadoSingleton();

        if(estadoSingleton.getEstados() == null){
            new ConsumerData().getEstados(context, estados -> {
                estadoSingleton.setEstados(estados);
                setDataAcUf(context, acUf, estadoSingleton.getEstados());
            });
        }else{
            setDataAcUf(context, acUf, estadoSingleton.getEstados());
        }

        acUf.setOnItemClickListener((parent, view, position, id) -> {
            Estado estado = (Estado)parent.getItemAtPosition(position);
            loadMunicipios(context, estado.getUf(), acMunicipio);
        });

        acMunicipio.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                String textAcUf = acUf.getText().toString();
                if (textAcUf.isEmpty()) {
                    Toast.makeText(context, "Informe o Estado primeiro para carregar as cidades!", Toast.LENGTH_SHORT).show();
                } else if (!existeEstado(textAcUf)) {
                    Toast.makeText(context, "Informe um Estado válido!", Toast.LENGTH_SHORT).show();
                } else {
                    String uf = findUfByNomeEstado(textAcUf, estadoSingleton.getEstados());
                    loadMunicipios(context, uf, acMunicipio);
                }
            }
        });

    }

    private static String findUfByNomeEstado(String nome, List<Estado> estados){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Estado estado = estados.stream()
                    .filter(x -> x.getNome().toLowerCase().equals(nome.toLowerCase()))
                    .findAny()
                    .orElse(null);
            return estado != null ? estado.getUf() : "";
        }
        return "";
    }

    private static void loadMunicipios(Context context, String uf, AutoCompleteTextView acMunicipio){
        new ConsumerData().getCidades(context, uf, cidades -> {
            ArrayAdapter<String> adapterMunicipio = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, cidades);
            acMunicipio.setAdapter(adapterMunicipio);
            EstadoSingleton.getEstadoSingleton().setCidades(cidades);
        });
    }

    private static void setDataAcUf(Context context, AutoCompleteTextView acUf, List<Estado> estados){
        ArrayAdapter<Estado> adapterUF = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, estados);
        acUf.setAdapter(adapterUF);
    }

    public static boolean isValidInput(String str, String type){

        switch (type){
            case "text": return !str.equals("");
            case "email": return validateEmailFormat(str);
            case "number": return isInteger(str);
            case "whatsapp": return str.length() == 16;
            case "cnpj": return str.length() == 18;
            case "double": return isDouble(str);
        }

        return false;
    }

    public static boolean validateEmailFormat(String email) {
        return EMAIL_ADDRESS.matcher(email).matches();
    }

    public static Double getDouble(String value) {
        if(value.equals("")){
            return 0.0;
        }
        return Double.parseDouble(value);
    }

    public static boolean isDouble(String str) {

        try {
            Double.parseDouble(str.replace(',', '.'));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isInteger(String str){

        for(int i=0; i<str.length(); i++){
            if(!Character.isDigit(str.charAt(i))){
                return false;
            }
        }

        return true;
    }

    public static void toast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static int heightTela(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    @SuppressWarnings("deprecation")
    public static void setFullscreen(Activity act){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController insetsController = act.getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        } else {
            act.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }
    }
}
