package br.com.bdt.ipet.util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import static android.util.Patterns.EMAIL_ADDRESS;

public class GeralUtils {

    /*
     * Lança um simples toast, criado apenas para agilizar o uso do toast, pois aqui já estará
     * configurado.
     * */
    public static void toast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /*
     * Método utilizando uma lógica pronta para verificar se o email é válido ou não
     * */
    public static boolean validateEmailFormat(String email) {
        return EMAIL_ADDRESS.matcher(email).matches();
    }

    /*
     * Recebe uma string e verifica se é double, aceita tanto ',' como '.'
     * */
    public static boolean isDouble(String str) {

        try {
            Double.parseDouble(str.replace(',', '.'));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /*
    * Recebe uma string e verifica se é um número
    * */
    public static boolean isInteger(String str){

        for(int i=0; i<str.length(); i++){
            if(!Character.isDigit(str.charAt(i))){
                return false;
            }
        }

        return true;
    }

    /*
    * Recebe uma string e o tipo dela, verificando se é ou não é o tipo desejado
    * */
    public static boolean isValidInput(String str, String type){

        switch (type){
            case "text": return !str.equals("");
            case "email": return validateEmailFormat(str);
            case "number": return isInteger(str);
            case "double": return isDouble(str);
        }

        return false;
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
}
