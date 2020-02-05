public class Function implements Expression {
    private Variable parameter;
    private Expression definition;

    public Function(Variable parameter, Expression definition) {
        this.parameter = parameter;
        this.definition = definition;
    }

    @Override
    public Expression feed(Expression var, Expression value) {
        return definition.feed(parameter, value).run();
    }

    @Override
    public Expression run() {
        return this;
    }

    @Override
    public String toString() {
        return "(λ" + parameter + "." + definition + ")";
    }
}
