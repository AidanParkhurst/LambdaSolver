import java.util.ArrayList;

public class Variable implements Expression {
    private String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public Expression run() {
        return this;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Expression feed(Expression var, Expression value) {
        if(this.name.equals(var.toString()))
            return value;

        return this;
    }

    @Override
    public Expression alphaReduce(Variable free, boolean first) {
        return this;
    }

    @Override
    public ArrayList<Variable> getFreeVars(ArrayList<Variable> bound) {
        for(Variable v : bound) {
            if(v.toString().equals(this.toString()))
                return new ArrayList<>();
        }

        ArrayList<Variable> container = new ArrayList<>();
        container.add(this);
        return container;
    }

    @Override
    public boolean canRun() {
        return false;
    }
}
