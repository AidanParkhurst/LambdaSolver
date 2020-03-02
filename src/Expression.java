import java.util.ArrayList;

interface Expression {
    Expression run();
    //Replace occurrences of a variable within an expression with the value (Beta Reduction)
    Expression feed(Expression var, Expression value);
    //Checks if a bound variable will conflict, if the conflict will resolve itself we don't care
    Expression alphaReduce(Variable free, boolean first);
    //Get all of the free variables in an expression
    ArrayList<Variable> getFreeVars(ArrayList<Variable> bound);
    //True if running the expression will not necessarily equal the expression
    boolean canRun();
}
