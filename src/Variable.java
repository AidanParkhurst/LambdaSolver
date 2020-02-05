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
}
