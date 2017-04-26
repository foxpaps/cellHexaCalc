package com.math.jeux.cellhexacalc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by fabrice on 07/04/17.
 */

public class SettingManager {

    int maxValueCell = 21;

    public static SettingManager setting = new SettingManager();

    static public SettingManager getInstance() {
        return setting;
    }

    public void modifySetting(final Context activity) {

        ContextThemeWrapper themeWrapper = new ContextThemeWrapper(activity, android.R.style.Theme_Dialog);
        //ContextThemeWrapper themeWrapper = new ContextThemeWrapper(activity, android.R.style.Theme_DeviceDefault_Dialog_Alert);

        AlertDialog alertDialog = new AlertDialog.Builder(themeWrapper).create();
        alertDialog.setTitle("Les Paramètres");

        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);


        //alertDialog.setMessage("Définition des paramètres");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        maxValueCell = sharedPref.getInt("hexaCell_maxValueCell", 21);

        TextView tv1 = new TextView(themeWrapper);
        tv1.setTextSize(20);
        tv1.setText("Nouvelle valeur max :");
        layout.addView(tv1);

        final EditText maxVal = new EditText(themeWrapper);
        maxVal.setInputType(InputType.TYPE_CLASS_NUMBER);
        maxVal.setHint(String.valueOf(maxValueCell));
        layout.addView(maxVal);

         //final EditText input = new EditText(activity);

        alertDialog.setView(layout);


        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String text = maxVal.getText().toString();
                int val = Integer.parseInt(text);
                val++;

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("hexaCell_maxValueCell", val);
                editor.commit();
            }
        });

        alertDialog.setButton2("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();

    }
}
