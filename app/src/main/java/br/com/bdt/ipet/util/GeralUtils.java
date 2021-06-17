package br.com.bdt.ipet.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import br.com.bdt.ipet.R;

import static android.util.Patterns.EMAIL_ADDRESS;

public class GeralUtils {

    public static void setDataSpinner(Spinner spinner, Context context, String title, List<String> dados) {
        spinner.setAdapter(new NothingSelectedSpinnerAdapter(new ArrayAdapter<>(context,
                R.layout.spinner_row, dados), title, R.layout.spinner_row, context)
        );
    }

    public static boolean isValidInput(String str, String type){

        switch (type){
            case "text": return !str.equals("");
            case "email": return validateEmailFormat(str);
            case "number": return isInteger(str);
            case "double": return isDouble(str);
        }

        return false;
    }

    public static boolean validateEmailFormat(String email) {
        return EMAIL_ADDRESS.matcher(email).matches();
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
