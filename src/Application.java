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
        if(left instanceof Function) {
            return left.feed(null, right);
        }
        return this;
    }

    @Override
    public String toString() {
        return "(" + left + " " + right + ")";
    }
}
