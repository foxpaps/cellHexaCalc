package calcule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabrice on 17/02/17.
 */

public class ExpressionOp extends Expression {

    public enum operation  {plus, moins, fois, div, reste, egal };


    private List<Expression> expressionsParent = new ArrayList<Expression>();


    private operation mOperation;

    public ExpressionOp(operation mOperation) {
        this.mOperation = mOperation;
    }


    public operation getOperation() {
        return mOperation;
    }

    public String getOperationStr() {
        String operatorName = "?";
        switch (mOperation) {

            case plus:
                operatorName= "+";
                break;
            case moins:
                operatorName= "-";
                break;
            case fois:
                operatorName= "*";
                break;
            case div:
                operatorName= "/";
                break;
            case reste:
                operatorName= "%";
                break;
            case egal:
                operatorName= "=";
                break;
        }
        return operatorName;
    }

    public void ajoutExpr(Expression expr){
        expressionsParent.add(expr);
    }


    @Override
    public int calcul() {

        int result = 0;

        switch (mOperation) {

            case plus:
                result = calculPlus();
                break;
            case moins:
                result = calculMoins();
                break;
            case fois:
                break;
            case div:
                break;
            case reste:
                break;
            case egal:
                break;
        }

        return result;
    }

    public int score() {
        int result = calculPlus();

        return result;
    }


    private int calculPlus() {
        int res = 0;
        for (Expression expression : expressionsParent) {
            res += expression.calcul();
        }
        return res;
    }

    private int calculMoins() {
        int res = 0;
        if (expressionsParent.size() ==2) {
            res = expressionsParent.get(0).calcul() - expressionsParent.get(1).calcul();
        }
        return Math.abs(res);
    }


}
