//Made by Aidan Parkhurst and Marcus San Antonio

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

public class Console {
    private static HashMap<String, String> definitions;

    private static Expression buildExpression(ArrayList<String> tokens) {
        String current = tokens.remove(tokens.size() - 1);
        Expression converted;

        //If the current token is an expression, parse it.
        if(current.charAt(0) == '(')
            converted = parse(current.substring(1, current.length() - 1));

        //If the current token is a function, make the parameter a variable and parse the right side
        else if(current.charAt(0) == '\\' || current.charAt(0) == 'λ') {
            int dot = current.indexOf('.');
            Variable param = new Variable(current.substring(1,dot).trim());
            Expression def = parse(current.substring(dot+1));

            converted = new Function(param, def);
        }

        //Otherwise the current token is just a variable
        else
            converted = new Variable(current);

        //If we took the last item off the list, we're done
        if(tokens.size() == 0)
            return converted;

        //If there're more items left in the list, recursively apply
        return (new Application(buildExpression(tokens), converted));
    }

    private static Expression populate(String in) {
        //Get the numbers out
        String numbers = in.replace("populate", "");
        ArrayList<String> splitNums = InputManager.split(numbers);
        int lowerBound = Integer.parseInt(splitNums.get(0));
        int upperBound = Integer.parseInt(splitNums.get(1));

        //Iterate up to the starting number
        String base = "(λf.λx.";
        for(int i = 0; i < lowerBound; i++) {
            base = base + "(f ";
        }

        String ending = "x)";
        for(int i = 0; i < lowerBound; i++) {
            ending = ending + ")";
        }

        //Save the numbers from the lower bound to the upper bound
        for (int j = lowerBound; j <= upperBound; j++) {
            //Save the current number
            definitions.put(Integer.toString(j), base + ending);

            //Advance to next number
            base = base + "(f ";
            ending = ending + ")";
        }

        //Return it as a variable, the toString prints
        return new Variable("Populated " + lowerBound + " to " + upperBound);
    }

    private static Expression parse(String in) {

        boolean populating = in.contains("populate");
        if(populating) {
            return populate(in);
        }

        //Is the user defining something?
        int equalIndex = in.indexOf('=');
        if(equalIndex != -1) {
            String name = in.substring(0,equalIndex).trim();
            String toParse = in.substring(equalIndex + 1);
            Expression parsed = parse(toParse);

            if(definitions.containsKey(name)) {
                return new Variable("Cannot redefine " + name);
            }

            definitions.put(name, parsed.toString());

            System.out.print("Defined " + name + " as ");
            return parsed;
        }

        //Replace any definitions we have
        for(String defined : definitions.keySet()) {
            in = InputManager.replace(in, defined, definitions.get(defined));
        }

        //Is the user trying to run something?
        boolean running = in.contains("run");
        if(running) {
            String toRun = in.replace("run", "");
            Expression parsed = parse(toRun);
            Expression result = parsed.alphaReduce(null, true);

            while(result.canRun()) {
                result = result.run();
            }

            return result;
        }

        //The user is just writing an expression
        else {
            ArrayList<String> tokens = InputManager.split(in);
            return buildExpression(tokens);
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        definitions = new HashMap<>();

        String input;
        while(true) {
            System.out.print(">");
            input = scan.nextLine();
            input = InputManager.trim(input);

            if(InputManager.isBlank(input))
                continue;
            if(input.equals("exit"))
                break;

            Expression result = parse(input);
            System.out.println(result);
        }
    }
}
