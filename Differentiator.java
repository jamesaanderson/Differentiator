import java.util.*;

public class Differentiator {
    public static void main(String[] args) {
        System.out.println("Types supported: 5x + 2, x + 5, 5x^2 + 32x - 9, 8, 8x");

        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Insert Equation: ");
            String in = sc.nextLine();
        
            System.out.println("=> " + derive(in));
        }
    }

    public static String derive(String in) {
        // tokenize
        Lexer l = new Lexer(in);
        // parse
        Parser p = new Parser(l);
        p.parse();

        // interpret
        return p.expr.derive();
    }
}
