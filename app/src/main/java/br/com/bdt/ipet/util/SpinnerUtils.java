package br.com.bdt.ipet.util;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import br.com.bdt.ipet.R;

import java.util.List;

public class SpinnerUtils {

    public static void setDataSpinner(Spinner spinner, Context context, String title, List<String> dados) {
        spinner.setAdapter(new NothingSelectedSpinnerAdapter(new ArrayAdapter<>(context,
                R.layout.spinner_row, dados), title, R.layout.spinner_row, context)
        );
    }

}
