package com.math.jeux.cellhexacalc;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabrice on 29/01/17.
 */

public class ClickOnCellHexa implements View.OnClickListener {


    private List<View> selectetOperatorList = new ArrayList<>();

    private static ClickOnCellHexa intance = new ClickOnCellHexa ();

    public static ClickOnCellHexa getInstance () {
        return intance;
    }

    @Override
    public void onClick(View v) {

         HexaTextView textView = (HexaTextView) v;

        // verifie si la cellule n'est pas vide
        if (textView.getText().isEmpty()) {
            return;
        }

        if (!textView.isSelected()){
            textView.setSelected(true);
            if (ComputeResult.getInstance().checkResult(textView)) {
                resetOperator();
            }

        } else {
            textView.setSelected(false);
            ComputeResult.getInstance().unselect(textView);
        }

        textView.invalidate();
        ((MainActivity)textView.getContext()).showOperation();
    }

    public void addOperator(View pView) {
        selectetOperatorList.add(pView);
    }

    public void resetOperator() {
        for (View tView : selectetOperatorList) {
            tView.setSelected(false);
            tView.invalidate();
        }
        selectetOperatorList.clear();
    }
}
