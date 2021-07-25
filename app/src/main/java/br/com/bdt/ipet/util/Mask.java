package br.com.bdt.ipet.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.text.NumberFormat;
import java.util.Locale;

public abstract class Mask {

    public static String CPF_MASK       = "###.###.###-##";
    public static String CELULAR_MASK   = "(##) # #### ####";
    public static String CNPJ_MASK      = "##.###.###/####-##";

    public static String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "")
                .replaceAll("[/]", "").replaceAll("[(]", "")
                .replaceAll("[)]", "").replaceAll(" ", "")
                .replaceAll(",", "");
    }

    public static boolean isASign(char c) {
        if (c == '.' || c == '-' || c == '/' || c == '(' || c == ')' || c == ',' || c == ' ') {
            return true;
        } else {
            return false;
        }
    }

    public static TextWatcher insert(final String mask1, final EditText ediTxt) {

        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = Mask.unmask(s.toString());
                String mask = mask1;

                if(mask.equals(Mask.CPF_MASK) && str.length() > 11){
                    mask = Mask.CNPJ_MASK;
                }


                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }

                int index = 0;
                for (int i = 0; i < mask.length(); i++) {
                    char m = mask.charAt(i);
                    if (m != '#') {
                        if (index == str.length() && str.length() < old.length()) {
                            continue;
                        }
                        mascara += m;
                        continue;
                    }

                    try {
                        mascara += str.charAt(index);
                    } catch (Exception e) {
                        break;
                    }

                    index++;
                }

                if (mascara.length() > 0) {
                    char last_char = mascara.charAt(mascara.length() - 1);
                    boolean hadSign = false;
                    while (isASign(last_char) && str.length() == old.length()) {
                        mascara = mascara.substring(0, mascara.length() - 1);
                        last_char = mascara.charAt(mascara.length() - 1);
                        hadSign = true;
                    }

                    if (mascara.length() > 0 && hadSign) {
                        mascara = mascara.substring(0, mascara.length() - 1);
                    }
                }

                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void afterTextChanged(Editable s) {}
        };
    }

    public static TextWatcher insertCurrency( final EditText ediTxt) {

        return new TextWatcher() {

            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)) {
                    ediTxt.removeTextChangedListener(this);
                    String cleanString = s.toString().replaceAll("[R$,.]", "").replaceAll("\\s","");
                    double value = Double.parseDouble(cleanString);
                    String formatted = doubleToStrBRL(value, true);
                    if(value == 0.0) formatted = "";
                    current = formatted;
                    ediTxt.setText(formatted);
                    ediTxt.setSelection(formatted.length());
                    ediTxt.addTextChangedListener(this);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void afterTextChanged(Editable s) {}
        };
    }

    public static String doubleToStrBRL(double value, boolean divider){
        return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(divider ? (value / 100) : value);
    }

    public static String unMaskBRL(String brl){
        return brl.replaceAll("[R$.]", "")
                  .replaceAll("\\s","")
                  .replaceAll("[,]", ".");
    }

}