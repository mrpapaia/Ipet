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
    public static String CEP_MASK       = "#####-###";
    public static String CNPJ_MASK      = "##.###.###/####-##";
    //public static String DINHEIRO_MASK1      = "#,###";
    public static String DINHEIRO_MASK1     = "0,0#";
    public static String DINHEIRO_MASK2    = "0,##";
    public static String DINHEIRO_MASK3      = "#,##";

    public static String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "")
                .replaceAll("[/]", "").replaceAll("[(]", "")
                .replaceAll("[)]", "").replaceAll(" ", "")
                .replaceAll(",", "");
    }
    public static String unmask2(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "")
                .replaceAll("[/]", "").replaceAll("[(]", "")
                .replaceAll("[)]", "").replaceAll(" ", "")
                .replaceAll(",", "").replaceAll("0", "");
    }

    public static boolean isASign(char c) {
        if (c == '.' || c == '-' || c == '/' || c == '(' || c == ')' || c == ',' || c == ' ') {
            return true;
        } else {
            return false;
        }
    }

    public static String mask(String mask, String text) {
        int i = 0;
        String mascara = "";
        for (char m : mask.toCharArray()) {
            if (m != '#') {
                mascara += m;
                continue;
            }
            try {
                mascara += text.charAt(i);
            } catch (Exception e) {
                break;
            }
            i++;
        }

        return mascara;
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
            boolean isUpdating;
            private String current = "";


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)) {
                    Locale myLocale = new Locale("pt", "BR");
                    //Nesse bloco ele monta a maskara para money
                    ediTxt.removeTextChangedListener(this);
                    String cleanString = s.toString().replaceAll("[R$,.]", "").replaceAll("\\s","");
                    Double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance(myLocale).format((parsed / 100));
                    current = formatted;
                    ediTxt.setText(formatted);
                    ediTxt.setSelection(formatted.length());

                    //Nesse bloco ele faz a conta do total (Caso a qtde esteja preenchida)
                   // String qtde = txtQtdeLitros.getText().toString();

                    ediTxt.addTextChangedListener(this);
                }
            }




            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void afterTextChanged(Editable s) {}
        };
    }

}