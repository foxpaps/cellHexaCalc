package com.math.jeux.cellhexacalc;

import android.content.Context;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import calcule.ExpressionOp;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String keyHexaText = "hexaCell_text";
    private static final String keyHexaSelected = "hexaCell_selected";

    private int maxValueCell = 21;
    private int nbHexaCell = 68;
    private HexaTextView[] hexaTextViewTab = new HexaTextView[nbHexaCell];


    private ScoreManager scoreMgr = new ScoreManager();

    private AttributeSet mAttributs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            XmlPullParser parser =  this.getResources().getXml(R.layout.circular_text_view);
            // Seek to the first tag.
            int type = 0;
            while (type != XmlPullParser.END_DOCUMENT && type != XmlPullParser.START_TAG) {
                type = parser.next();
            }

            mAttributs = Xml.asAttributeSet(parser);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TypedArray a = this.obtainStyledAttributes(attrs, R.styleable.Hexgrid_max_value);
        //maxValueCell = a.getInt(R.styleable.Hexgrid_max_value,2);

        if (savedInstanceState==null) {
            generateNewGame();
        } else {
            restoreGame(savedInstanceState);
        }
        ViewGroup grid = (ViewGroup) findViewById(R.id.hexGrid1);
        for (int i = 0; i < nbHexaCell; i++) {
            grid.addView(hexaTextViewTab[i]);
        }

        scoreMgr.readHightScore(MainActivity.this);
        //attrs.recycle();

        TextView opeViewPlus = (TextView)findViewById(R.id.operatorPlus);
        opeViewPlus.setOnClickListener(this);
        TextView opeViewMoins = (TextView)findViewById(R.id.operatorMoins);
        opeViewMoins.setOnClickListener(this);
        TextView opeViewEqual = (TextView)findViewById(R.id.operatorEqual);
        opeViewEqual.setOnClickListener(this);

        /*opeViewPlus.setTextSize(10);
        opeViewMoins.setTextSize(10);
        opeViewEqual.setTextSize(10);*/
    }



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

         ArrayList<CharSequence> hexaTextList= new ArrayList<>();
        boolean[] hexaSelectedList = new boolean[nbHexaCell];

        for (int i = 0;i<nbHexaCell;i++) {
            HexaTextView cell = hexaTextViewTab[i];
            hexaTextList.add(cell.getText());
            hexaSelectedList[i] = cell.isSelected();
        }

        savedInstanceState.putCharSequenceArrayList(keyHexaText, hexaTextList);
        savedInstanceState.putBooleanArray(keyHexaSelected, hexaSelectedList);
        savedInstanceState.putInt(ScoreManager.keyScore,ComputeResult.getInstance().getScore());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_replay) {
            newGame();
            return true;
        }
        if (id == R.id.action_show_scores) {
            scoreMgr.showHighScore(MainActivity.this);
            return true;
        }

        if (id == R.id.action_settings) {
            changeSettingValue();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    public void handleClick(View view) {
        int idv = view.getId();
        if (view.getId() == R.id.operatorPlus) {
            int idv1 = view.getId();
            CircularTextView textView = (CircularTextView) view;

            if (!textView.isSelected()) {
                textView.setSelected(true);
                //deselectioner les autres opérateurs
                CircularTextView textView2 = (CircularTextView)this.findViewById(R.id.operatorMoins);
                textView2.setSelected(false);
                ClickOnCellHexa.getInstance().addOperator(textView);
                ComputeResult.getInstance().addOperator(ExpressionOp.operation.plus);
            } else {
                textView.setSelected(false);
            }
            showOperation();
        }

        if (view.getId() == R.id.operatorMoins) {
            int idv2 = view.getId();
            CircularTextView textView = (CircularTextView) view;

            if (!textView.isSelected()) {
                textView.setSelected(true);
                CircularTextView textView2 = (CircularTextView) this.findViewById(R.id.operatorPlus);
                textView2.setSelected(false);

                ClickOnCellHexa.getInstance().addOperator(textView);
                ComputeResult.getInstance().addOperator(ExpressionOp.operation.moins);
            } else {
                textView.setSelected(false);
            }
            showOperation();
        }

        if (view.getId() == R.id.operatorEqual) {
            if (ComputeResult.getInstance().isEmptyExpression()) {
                showInfo("Selectionne un opérateur");
                return;
            }

            CircularTextView textView = (CircularTextView) view;

            if (!textView.isSelected()) {
                textView.setSelected(true);
                ClickOnCellHexa.getInstance().addOperator(textView);
                ViewGroup grid = (ViewGroup) findViewById(R.id.hexGrid1);
                ComputeResult.getInstance().computeResult(grid);
            } else {
                textView.setSelected(false);
                ComputeResult.getInstance().computeReset();
            }
        }
    }

    public void youWin(HexaTextView textView) {
        showInfo("Tu as gagné !.");
    }

    public void youLoose(HexaTextView textView) {
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
        showInfo("Tu as perdu !");
        for (int i = 0; i<5; i++) {
            v.vibrate(5000);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void showInfo(final String message) {

        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    public void showOperation() {
        TextView opeView =   (TextView)findViewById(R.id.showOperation);

        String ope = ComputeResult.getInstance().operationToString();
       opeView.setText(ope);

    }

    public void showScore(String score) {
        Toolbar toolBar = (Toolbar)findViewById(R.id.toolbar);
        String newTitle = "CellHexaCalc - Score : " + score;
        toolBar.setTitle(newTitle);
    }

    public void equal(HexaTextView textView) {
        CircularTextView equal = (CircularTextView)findViewById(R.id.operatorEqual);
        equal.setSelected(true);
        ClickOnCellHexa.getInstance().addOperator(equal);
        ViewGroup grid = (ViewGroup) findViewById(R.id.hexGrid1);
        ComputeResult.getInstance().computeResult(grid);
        textView.setSelected(true);
        if (ComputeResult.getInstance().checkResult(textView)) {
            ClickOnCellHexa.getInstance().resetOperator();
        }
    }

    private void generateNewGame() {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        maxValueCell = sharedPref.getInt("hexaCell_maxValueCell", 21);

        Random rangene = new Random();
        for (int i = 0; i < nbHexaCell; i++) {
            int val = rangene.nextInt(maxValueCell);
            hexaTextViewTab[i] = new HexaTextView(this, mAttributs, String.valueOf(val));
        }
        ComputeResult.getInstance().setScore(0);
    }

    private void restoreGame(Bundle savedInstanceState) {
        ArrayList<CharSequence> hexaTextList = savedInstanceState.getCharSequenceArrayList(keyHexaText);
        boolean[] hexaSelectedList = savedInstanceState.getBooleanArray(keyHexaSelected);
        for (int i = 0; i < nbHexaCell; i++) {
            //int val = rangene.nextInt(maxValueCell);
            hexaTextViewTab[i] = new HexaTextView(this, mAttributs, (String) hexaTextList.get(i));
            hexaTextViewTab[i].setSelected(hexaSelectedList[i]);
        }

        ComputeResult.getInstance().setScore(savedInstanceState.getInt(ScoreManager.keyScore));
    }


    public void newGame() {
        checkScore();
        generateNewGame();
        recreate();
    }

    private void checkScore() {

        final int score = ComputeResult.getInstance().getScore();
        if (scoreMgr.isNewHightScore(score)) {
            scoreMgr.createNewHighScore(MainActivity.this ,score);
        }

    }

    public void changeSettingValue() {
        SettingManager.getInstance().modifySetting(this);
    }

    @Override
    public void onClick(View view) {
        handleClick(view);
    }
}
