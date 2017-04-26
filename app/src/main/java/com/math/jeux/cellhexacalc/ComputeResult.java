package com.math.jeux.cellhexacalc;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import calcule.Expression;
import calcule.ExpressionInt;
import calcule.ExpressionOp;

/**
 * Created by fabrice on 17/02/17.
 */

public class ComputeResult {

    private static final ComputeResult mCompute = new ComputeResult();

    private ExpressionOp mExpression;
    private  int resultCalc = Integer.MAX_VALUE;
    private List<HexaTextView> mHexaTextViewList = new ArrayList<HexaTextView>();


    private int score =0;

    private List<ExpressionInt> ListExpr;

    private void ComputeResult() {
    }

    public static ComputeResult getInstance() {
        return mCompute;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }

    public boolean isEmptyExpression() {
        return mExpression==null;
    }

    public void computeResult( ViewGroup grid) {

       int count = grid.getChildCount();
       for (int i = 0 ; i< count; i++) {
           View cell = grid.getChildAt(i);
           if (cell instanceof HexaTextView) {
               HexaTextView hexaCell = (HexaTextView)cell;
               if (hexaCell.isSelected()) {
                   String textCell = hexaCell.getText();

                   if (textCell != null && !textCell.isEmpty()) {
                       Expression expr = new ExpressionInt( Integer.parseInt(textCell));
                       mExpression.ajoutExpr(expr);
                   }
               }
           }
       }
       resultCalc = mExpression.calcul();
    }

    public void computeReset() {
        resultCalc = Integer.MAX_VALUE;
    }


    public void addOperator(ExpressionOp.operation operator) {
        mExpression = new ExpressionOp(operator);
        resultCalc = Integer.MAX_VALUE;
    }

    /**
     *
     * @param textView
     * @return true will reset operators
     */
    public boolean checkResult(HexaTextView textView) {
        boolean res = false;

        if (!textView.isSelected()) {
            return false;
        }

        if (mHexaTextViewList.size()>=2 && resultCalc == Integer.MAX_VALUE) {
            textView.setSelected(false);
            if (mExpression==null ) { // Aucun opérateur est selectioné
                ((MainActivity) textView.getContext()).showInfo("Deux termes maximum ...");

            } else { // on fait la selection de "="
                ((MainActivity)textView.getContext()).equal(textView);
            }
            return false;
        }

        // si le resultat n'est pas encore calculé, je conserve le textView
        if (resultCalc == Integer.MAX_VALUE) {
            mHexaTextViewList.add(textView);
        } else {
            String textCell = textView.getText();
            if (textCell != null &&  resultCalc ==Integer.parseInt(textCell)) {
                // WIN
                youWin(textView);
            } else {
                youLoose(textView);
            }
            res = true;
         }
        return res;
    }

    public void unselect(HexaTextView textView) {
        mHexaTextViewList.remove(textView);
    }

    private void youWin(HexaTextView textView) {
        textView.setText("");
        textView.invalidate();

        score += mExpression.score();

        for (HexaTextView hexaText : mHexaTextViewList  ) {
            hexaText.setText("");
            hexaText.invalidate();
        }

        resultCalc = Integer.MAX_VALUE;
        mExpression=null;

        mHexaTextViewList.clear();

        ((MainActivity)textView.getContext()).youWin(textView);
        ((MainActivity)textView.getContext()).showScore(String.valueOf(score));

    }

    private void youLoose(HexaTextView textView) {

        textView.setSelected(false);
        //textView.invalidate();
        ((MainActivity)textView.getContext()).youLoose(textView);
        textView.invalidate();

        resultCalc = Integer.MAX_VALUE;
        mExpression=null;
    }

    public String operationToString() {
        String operation = "? op ? = ?";
        String operator = mExpression==null ? "?" : mExpression.getOperationStr();

            if (mHexaTextViewList.size() == 0) {
                return "? " + operator + " ? = ?";
            }
            if (mHexaTextViewList.size() == 1) {
                return mHexaTextViewList.get(0).getText() + " " + operator + " ? = ?";
            }
            if (mHexaTextViewList.size() == 2) {
                if (mExpression!=null && mExpression.getOperation()==ExpressionOp.operation.moins) {
                    if ( Integer.parseInt( mHexaTextViewList.get(0).getText()) < Integer.parseInt( mHexaTextViewList.get(1).getText())) {
                        return mHexaTextViewList.get(1).getText() + " " + operator + " " + mHexaTextViewList.get(0).getText() + " = ?";
                    }
                }
                return mHexaTextViewList.get(0).getText() + " " + operator + " " + mHexaTextViewList.get(1).getText() + " = ?";
            }

        return operation;
    }
}
