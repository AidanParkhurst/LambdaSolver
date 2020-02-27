import java.util.ArrayList;

public class Application implements Expression {
    private Expression left;
    private Expression right;

    public Application(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Expression feed(Expression var, Expression value) {
        return new Application(left.feed(var, value), right.feed(var, value));
    }

    @Override
    public Expression run() {
        //Simplest case we are directly applying something to a function
        if(left instanceof Function) {
            return left.feed(null, right);
        }
        //Otherwise the left or right may be able to run individually
        else if(this.canRun()) {
            return new Application(left.run(),right.run());
        }

        //If neither of those are true, we can't run anything
        return this;
    }

    @Override
    public ArrayList<Variable> getFreeVars(ArrayList<Variable> bound) {
        ArrayList<Variable> total = left.getFreeVars(bound == null ? new ArrayList<>() : bound);
        total.addAll(right.getFreeVars(bound == null ? new ArrayList<>() : bound));
        return total;
    }

    @Override
    public Expression alphaReduce(Variable free, boolean first) {
        Application toReduce = this;
        if(canRun() && (free == null)) {
            ArrayList<Variable> freeVars = toReduce.getFreeVars(null);

            for(Variable v : freeVars) {
                toReduce = new Application(toReduce.left.alphaReduce(v, true), toReduce.right);
            }
        }
        return toReduce;
    }

    @Override
    public boolean canRun() {
        if(left instanceof Function)
            return true;

        return left.canRun() || right.canRun();
    }

    @Override
    public String toString() {
        return "(" + left + " " + right + ")";
    }
}
