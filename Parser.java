import java.util.*;

public class Parser {
    public static interface AST {
        public String derive(); 
    }

    public static class BinOp {
        AST left;
        AST right;

        public BinOp(AST left, AST right) {
            this.left = left;
            this.right = right;
        }
    }

    public static class Add extends BinOp implements AST {
        public Add(AST left, AST right) {
            super(left,right);
        }

        @Override
        public String derive() {
            return this.left.derive() + " + " + this.right.derive(); 
        }

        @Override
        public String toString() {
            return "Add(" + this.left + ", " + this.right + ")";
        }
    }

    public static class Subtract extends BinOp implements AST {
        public Subtract(AST left, AST right) {
            super(left,right);
        }

        @Override
        public String derive() {
            return this.left.derive() + " + " + this.right.derive(); 
        }

        @Override
        public String toString() {
            return "Subtract(" + this.left + ", " + this.right + ")";
        }
    }

    public static class Poly implements AST {
        double coeff;
        String var;
        double power;

        public Poly(double coeff, String var, double power) {
            this.coeff = coeff;
            this.var = var; 
            this.power = power;
        }

        @Override
        public String derive() {
            if (this.power == 1) {
                return this.coeff + "";
            } else {
                return this.power * this.coeff + this.var + "^" + (this.power - 1);
            }
        }

        @Override
        public String toString() {
            return "Poly(" + this.coeff + ", " + this.var + ", " + this.power + ")";
        }
    }

    public static class Constant implements AST {
        double value;

        public Constant(double value) {
            this.value = value;
        }

        @Override
        public String derive() {
            return "0";
        }

        @Override
        public String toString() {
            return "Constant(" + this.value + ")";
        }
    }

    Lexer lexer;
    ListIterator<Lexer.Token> itr;
    AST expr;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.lexer.scan();

        this.itr = this.lexer.tokens.listIterator();
    }

    public void parse() {
        while (this.itr.hasNext()) {
            Lexer.Token token = this.itr.next();

            if (token.type == Lexer.TokenType.PLUS) {
                this.expr = this.parseAdd(this.expr);
            } else if (token.type == Lexer.TokenType.MINUS) {
                this.expr = this.parseSubtract(this.expr);
            } else {
                this.expr = this.parseTerm(token);
            }
        }

        System.out.println(this.expr);
    }

    public AST parseTerm(Lexer.Token token) {
        if (token.type == Lexer.TokenType.NUMBER) {
            if (this.itr.hasNext()) {
                Lexer.Token nextToken = this.itr.next();

                if (nextToken.type == Lexer.TokenType.VARIABLE) {
                    return this.parsePoly(token, nextToken);
                } else {
                    this.itr.previous();
                    return new Constant(Double.parseDouble(token.value)); 
                }
            } else {
                return new Constant(Double.parseDouble(token.value));
            }
        } else if (token.type == Lexer.TokenType.VARIABLE) {
            if (this.itr.hasNext()) {
                Lexer.Token nextToken = this.itr.next();

                if (nextToken.type == Lexer.TokenType.POWER) {
                    return new Poly(1,token.value,Double.parseDouble(this.itr.next().value));
                } else {
                    this.itr.previous(); 
                    return new Poly(1,token.value,1);
                }
            } else {
                return new Poly(1,token.value,1);
            }
        }

        return new Constant(0.);
    }

    public Poly parsePoly(Lexer.Token token, Lexer.Token nextToken) {
        if (this.itr.hasNext()) {
            if (this.itr.next().type == Lexer.TokenType.POWER) {
                return new Poly(Double.parseDouble(token.value),nextToken.value,Double.parseDouble(this.itr.next().value));
            } else {
                this.itr.previous();
                return new Poly(Double.parseDouble(token.value),nextToken.value,1);
            }
        } else {
            return new Poly(Double.parseDouble(token.value),nextToken.value,1);
        }
    }

    public Add parseAdd(AST left) {
        AST right = this.parseTerm(this.itr.next());

        return new Add(left,right);
    }

    public Subtract parseSubtract(AST left) {
        AST right = this.parseTerm(this.itr.next());

        return new Subtract(left,right);
    }
}
