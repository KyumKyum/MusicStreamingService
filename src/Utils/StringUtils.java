package Utils;

public class StringUtils {
    public static int parse(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Input Error!: Press Only Number.");
        }
        return -1;
    }

    public static String stringTrim(String input){
        return stringReplace(input).trim();
    }

    private static String stringReplace(String input){
        return input.replace("'","").replace("\"","");
    }
}
