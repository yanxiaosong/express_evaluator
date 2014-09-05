package calculator;

import java.util.*;


public class Expression {

    HashMap<Integer, String> postions;
    private Map<String, Function> functions = new HashMap<String, Function>();
    private String expression;
    private Stack<String> operands;
    private Stack<String> commands;
    private List<HashMap<String, String>> variables;
    private int defCount;
    private int removeCount;   // variables need to remove

    /**
     * constructor
     *
     * @param expression
     */
    public Expression(String expression) {

        this.expression = expression;

        addFunction(new Function("ADD", 2) {
            @Override
            public int eval(List<Integer> params) {
                this.checkParams(params);
                return params.get(0) + params.get(1);
            }
        });


        addFunction(new Function("MULT", 2) {
            @Override
            public int eval(List<Integer> params) {
                this.checkParams(params);
                return params.get(0) * params.get(1);
            }
        });

        addFunction(new Function("DIV", 2) {
            @Override
            public int eval(List<Integer> params) {
                this.checkParams(params);
                return params.get(0) / params.get(1);
            }
        });

        addFunction(new Function("SUB", 2) {
            @Override
            public int eval(List<Integer> params) {
                this.checkParams(params);
                return params.get(0) - params.get(1);
            }
        });

    }

    public Function addFunction(Function function) {
        return this.functions.put(function.getName(), function);
    }

    /**
     * Evaluates the expression.
     *
     * @return The result of the expression.
     */
    public int eval() {

        int result = 0;

        operands = new Stack<String>();
        commands = new Stack<String>();
        variables = new ArrayList<HashMap<String, String>>();
        defCount = 0;
        removeCount = 0;
        postions = new HashMap<Integer, String>();
        boolean needToPush = false;

        Tokenizer tokenizer = new Tokenizer(expression);
        while (tokenizer.hasNext()) {

            String token = tokenizer.next();

            if (functions.containsKey(token.toUpperCase())) {

                commands.push(token);
                operands.push(tokenizer.next());        // push  "("

            } else if ("LET".equals(token.toUpperCase())) {

                commands.push(token);
                operands.push(tokenizer.next());        // push  "("
                String varName = tokenizer.next();
                operands.push(varName);        // push  variable name

                defCount++;

                // remember position in stack
                postions.put(operands.size() - 1, varName);

            } else if (this.isInteger(token)) {

                if (this.isPreviousPositionVariableDefinition()) {
                    retrieveVariable(token);
                } else {
                    operands.push(token);
                }

            } else if (this.isVariable(token)) {

                for (int i = variables.size() - 1; i >= 0; i--) {

                    String varValue = variables.get(i).get(token);
                    if (varValue != null) {
                        operands.push(varValue);
                        break;
                    }

                }

            } else if ("(".equals(token)) {
                operands.push(token);

            } else if (")".equals(token)) {

                if ("(".equals(operands.peek())) {

                    operands.pop();

                    if (!operands.isEmpty() && !"(".equals(operands.peek()) && needToPush) {

                        if (this.isPreviousPositionVariableDefinition()) {
                            retrieveVariable(String.valueOf(result));
                        } else {
                            operands.push(String.valueOf(result));
                        }

                        needToPush = false;
                    }

                    continue;
                }

                List<Integer> args = new ArrayList<Integer>();

                while (!operands.isEmpty() && !"(".equals(operands.peek())) {
                    args.add(Integer.valueOf(operands.pop()));
                }

                if (operands.isEmpty()) {
                    throw new RuntimeException("Mismatched parentheses");
                }
                operands.pop();

                Function f = this.functions.get(commands.pop().toUpperCase());
                Collections.reverse(args);
                result = f.eval(args);
                needToPush = true;

                if (!operands.isEmpty()) {
                    if (!"(".equals(operands.peek())) {

                        if (this.isPreviousPositionVariableDefinition()) {
                            retrieveVariable(String.valueOf(result));
                        } else {
                            operands.push(String.valueOf(result));
                        }

                        needToPush = false;

                    }
                }

            } else if(",".equals(token)) {
                if ("(".equals(operands.peek()) && needToPush) {
                    operands.push(String.valueOf(result));
                }

            } else {
                // do nothing
            }
        }

        return result;
    }

    private boolean isInteger(String s) {

        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;

    }

    private boolean isVariable(String s) {
        return this.isAlpha(s) && !this.functions.containsKey(s.toUpperCase()) && !"LET".equals(s.toUpperCase());
    }

    /**
     * retrieve variable and remove variable not in the scope
     * @param varValue
     */
    private void retrieveVariable(String varValue) {

        // remove the position
        postions.remove(operands.size() - 1);

        String varName = operands.pop();
        HashMap<String, String> v = new HashMap<String, String>();
        v.put(varName, String.valueOf(varValue));

        if (!variables.isEmpty() && removeCount > 0) {
            variables.remove(0);
            removeCount--;
        }

        if (defCount > 1) {
            removeCount++;
        }

        defCount--;
        variables.add(v);
        commands.pop();
    }

    private boolean isPreviousPositionVariableDefinition() {
        return !postions.isEmpty() && postions.get(operands.size() - 1) != null;
    }

    private boolean isAlpha(String s) {
        return s.matches("[a-zA-Z]+");
    }

    /**
     * Arithmetic functions
     */
    public abstract class Function {

        private String name;
        private int numParams;

        public Function(String name, int numParams) {

            this.name = name;

            this.numParams = numParams;

        }

        public String getName() {
            return name;
        }

        public int getNumParams() {
            return numParams;
        }

        public abstract int eval(List<Integer> params);

        public void checkParams(List<Integer> params) {

            if (params.size() != this.numParams) {
                throw new ExpressionException("Params number of function '" + this.name + "' is not correct.");
            }
        }
    }

    public class Tokenizer implements Iterator<String> {

        private final char minusSign = '-';
        private String input;
        private int pos = 0;

        public Tokenizer(String input) {
            this.input = input;
        }

        @Override
        public boolean hasNext() {
            return this.pos < this.input.length();
        }

        @Override
        public String next() {

            StringBuffer token = new StringBuffer();

            // get current character, ignore spaces
            char ch = input.charAt(pos);
            while (Character.isWhitespace(ch) && pos < input.length()) {
                ch = input.charAt(++pos);
            }

            if (Character.isDigit(ch)) {
                while (Character.isDigit(ch) && pos < input.length()) {
                    token.append(input.charAt(pos++));
                    if (pos < input.length()) {
                        ch = input.charAt(pos);
                    }
                }
            } else if (ch == minusSign
                    && Character.isDigit(peekNextChar())) {
                token.append(minusSign);
                pos++;
                token.append(next());
            } else if (Character.isLetter(ch)) {
                while (Character.isLetter(ch) && (pos < input.length())) {
                    token.append(input.charAt(pos++));
                    ch = pos == input.length() ? 0 : input.charAt(pos);
                }
            } else if (ch == '(' || ch == ')' || ch == ',') {
                token.append(ch);
                pos++;
            } else {
                while (!Character.isLetter(ch) && !Character.isDigit(ch)
                        && !Character.isWhitespace(ch) && ch != '('
                        && ch != ')' && ch != ',' && (pos < input.length())) {
                    token.append(input.charAt(pos));
                    pos++;
                    ch = pos == input.length() ? 0 : input.charAt(pos);
                    if (ch == minusSign) {
                        break;
                    }
                }

                throw new ExpressionException("Unknown character '" + token + "' at position " + (pos - token.length() + 1));

            }
            return token.toString();

        }

        @Override
        public void remove() {
            throw new RuntimeException("Not implemented.");
        }

        private char peekNextChar() {
            if (pos < (input.length() - 1)) {
                return input.charAt(pos + 1);
            } else {
                return 0;
            }
        }
    }

    public class ExpressionException extends RuntimeException {

        public ExpressionException(String s) {
            super(s);
        }
    }


}
