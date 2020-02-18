import java.util.ArrayList;

public class InputManager {
    private static final String DELIMS = "[|\\s|\\[\\]\\(\\)]";
    // Removes every character after semicolon, then removes white space at end and BOM
    public static String trim(String in) {
        for(int i = 0; i < in.length(); i++) {
            if(in.charAt(i) == ';')
                return in.substring(0,i).replaceAll("\uFEFF", "").trim();
        }
        return in.trim();
    }

    //Returns if a string is only whitespace
    public static boolean isBlank(String in) {
        return (in.trim().length() == 0);
    }

    //Splits string at spaces, ignores within parenthesis and functions
    public static ArrayList<String> split(String in) {
        ArrayList<String> tokens = new ArrayList<>();

        int openParens = 0;

        boolean inFunc = false;
        int funcParens = 0;

        int startIndex = 0;

        for(int i = 0; i < in.length(); i++) {
            char current = in.charAt(i);

            if(current == '(') {
                if(openParens == 0 && !inFunc) {
                    String toAdd = in.substring(startIndex, i);
                    if (!isBlank(toAdd))
                        tokens.add(toAdd);
                    startIndex = i;
                }

                openParens++;

                if(inFunc)
                    funcParens++;
            }
            else if(current == ')') {
                openParens--;

                //Only leave a function if every parenthesis opened within it has been closed
                if(inFunc) {
                    funcParens--;
                    if(funcParens < 0) {
                        inFunc = false;
                        funcParens = 0;
                    }
                }
            }

            if(current == '\\' || current == 'λ') {
                if(openParens == 0 && !inFunc) {
                    String toAdd = in.substring(startIndex, i);
                    if (!isBlank(toAdd))
                        tokens.add(toAdd);
                    startIndex = i;
                }

                inFunc = true;
            }

            if(openParens == 0 && !inFunc && current == ' ') {
                String toAdd = in.substring(startIndex, i);
                if(!isBlank(toAdd))
                    tokens.add(toAdd);

                startIndex = i+1;
            }
        }

        String toAdd = in.substring(startIndex);
        if(!isBlank(toAdd))
            tokens.add(toAdd);

        return tokens;
    }

    private static String sanitize(String in) {
        return in.replaceAll("[-.\\+*?\\[^\\]$(){}=!<>|:\\\\]", "\\\\$0");
    }

    public static String replace(String in, String key, String replacement) {
        //Fix functions not being saved correctly
        replacement = replacement.replace("\\","\\\\");
        key = sanitize(key);
        //Regex to match the specific key, as long as there's a delimiter or the line ends/start surrounding it
        key = "(?<=^|[|\\s|\\[|\\]|\\(|\\)])" + key + "(?=$|[\\s|\\[|\\]|\\(|\\)])";
        return in.replaceAll(key, replacement);
    }
}
