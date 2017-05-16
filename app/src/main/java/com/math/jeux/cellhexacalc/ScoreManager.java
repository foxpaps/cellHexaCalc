package com.math.jeux.cellhexacalc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import static android.R.id.input;

/**
 * Created by fabrice on 14/03/17.
 */

public class ScoreManager {

    public static final String keyScore = "hexaCell_Score";
    private static final String keyHighScore = "hexaCell_High_Score";
    private static final String keyHighScoreName = "hexaCell_High_Score_Name";

    private static final int nbHighScore = 5;

    private int highScore[] = new int[nbHighScore];
    private String highScoreName[] = new String[nbHighScore];


    public void readHightScore(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        for (int i = 0; i < nbHighScore; i++) {
            highScore[i] = sharedPref.getInt(keyHighScore + String.valueOf(i), 0);
            highScoreName[i] = sharedPref.getString(keyHighScoreName + String.valueOf(i), "");
        }
    }

    public void saveHightScore(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPref.edit();

        for (int i = 0; i < nbHighScore; i++) {
            editor.putInt(keyHighScore + String.valueOf(i), highScore[i]);
            editor.putString(keyHighScoreName + String.valueOf(i), highScoreName[i]);
        }
        editor.commit();
    }

    public boolean isNewHightScore(int score) {
        return (score >= highScore[nbHighScore - 1]);
    }

    public void insertNewHightScore(String newName, int newScore) {
        String oldName=null;
        int oldScore=0;
        for (int i = 0; i<nbHighScore; i++) {
            if ( newScore>= highScore[i]) {
                if (oldName==null) {
                    oldName = highScoreName[i];
                    oldScore = highScore[i];
                    highScoreName[i] = newName;
                    highScore[i] = newScore;
                } else {
                    String tmpName = highScoreName[i];
                    int tmpScore = highScore[i];
                    highScoreName[i] = oldName;
                    highScore[i] = oldScore;
                    oldName = tmpName;
                    oldScore = tmpScore;
                }
            }
        }
    }

    public void createNewHighScore(final Context activity,final int score) {

        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle("Nouveau score");
        alertDialog.setMessage("Entre ton nom");

        final EditText input = new EditText(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        //alertDialog.setIcon(R.drawable.key);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                insertNewHightScore(input.getText().toString(),score);
                saveHightScore(activity);
             }
        });

        alertDialog.show();
    }

    public void showHighScore(Context activity) {

        readHightScore(activity);
        ContextThemeWrapper themeWrapper = android.os.Build.VERSION.SDK_INT >= 21 ?
                new ContextThemeWrapper(activity, android.R.style.Theme_DeviceDefault_Dialog_Alert) :
                new ContextThemeWrapper(activity, android.R.style.Theme_Dialog);
        AlertDialog alertDialog = new AlertDialog.Builder(themeWrapper).create();
        alertDialog.setTitle("Les Meilleurs scores");
        alertDialog.setMessage("Si tu n'y es pas continue !");

        TableLayout.LayoutParams lt = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        TableLayout table = new TableLayout(activity);

        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        for (int i = 0; i<nbHighScore; i++) {
            TableRow row = new TableRow(activity);

            TextView tv1 = new TextView(themeWrapper);
            tv1.setTextSize(20);
            tv1.setText(highScoreName[i]);
            tv1.setGravity(Gravity.CENTER);
            tv1.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );

            TextView tv2 = new TextView(themeWrapper);
            tv2.setTextSize(20);
            tv2.setText(String.valueOf(highScore[i]));
            tv2.setGravity(Gravity.LEFT);
            tv2.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );

            row.addView(tv1);
            row.addView(tv2);

            table.addView(row);

        }
        lt.width = WindowManager.LayoutParams.MATCH_PARENT;

        table.setLayoutParams(lt);
        alertDialog.setView(table);
        //alertDialog.setIcon(R.drawable.key);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();

    }
}
