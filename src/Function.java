//Made by Aidan Parkhurst and Marcus San Antonio

import java.util.ArrayList;

public class Function implements Expression {
    private Variable parameter;
    private Expression definition;

    public Function(Variable parameter, Expression definition) {
        this.parameter = parameter;
        this.definition = definition;
    }

    @Override
    public Expression feed(Expression var, Expression value) {
        if(var != null) {
            if(var.toString().equals(parameter.toString()))
                return this;

            return new Function(parameter, definition.feed(var, value));
        }

        return definition.feed(parameter, value);
    }

    @Override
    public Expression run() {
        return new Function(parameter, definition.run());
    }

    @Override
    public boolean canRun() {
        return definition.canRun();
    }

    @Override
    public ArrayList<Variable> getFreeVars(ArrayList<Variable> bound) {
        bound.add(parameter);

        return definition.getFreeVars(bound);
    }

    @Override
    public Expression alphaReduce(Variable free, boolean first) {
        //If there are no potentially conflicting variables, just start alpha reduction on the definition
        if(free == null)
            return new Function(parameter, definition.alphaReduce(null,true));

        //If we conflict with the free variable
        if(parameter.toString().equals(free.toString())) {
            //Ignore the conflict if we're the first function its being applied to (The conflict goes away in running)
            if(first)
                return new Function(parameter, definition.alphaReduce(null, true));
            //Otherwise replace this function's parameter with a renamed version
            else {
                Expression fixedParam = parameter.feed(parameter, new Variable(parameter.toString() + "_"));
                Expression fixedDefinition = definition.feed(parameter, fixedParam);
                //Make sure to still do alpha reduction on the definition
                return new Function((Variable) fixedParam, fixedDefinition.alphaReduce(null,true));
            }
        }

        //If we don't conflict with this free variable, maybe an inner function will
        return new Function(parameter, definition.alphaReduce(free, false));
    }

    @Override
    public String toString() {
        return "(λ" + parameter + "." + definition + ")";
    }
}
