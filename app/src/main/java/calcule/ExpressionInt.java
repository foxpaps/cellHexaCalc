package calcule;

/**
 * Created by fabrice on 17/02/17.
 */
public class ExpressionInt  extends Expression {

    private int value=0;

    public ExpressionInt(int value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }


    @Override
    public int calcul() {
        return value;
    }
}
