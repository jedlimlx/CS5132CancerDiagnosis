import com.fuzzylite.Engine;
import com.fuzzylite.FuzzyLite;
import com.fuzzylite.Op;
import com.fuzzylite.norm.SNorm;
import com.fuzzylite.norm.TNorm;
import com.fuzzylite.rule.Expression;
import com.fuzzylite.rule.Operator;
import com.fuzzylite.rule.Proposition;
import com.fuzzylite.term.Function;
import com.fuzzylite.variable.OutputVariable;
import com.fuzzylite.variable.Variable;

import java.util.*;
import java.util.logging.Level;

public class Antecedent2 {

    private String text;
    private Expression expression;

    public Antecedent2() {
        this.text = "";
        this.expression = null;
    }

    /**
     Gets the antecedent in text
     */
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    /**
     Gets the expression tree of the antecedent
     */
    public Expression getExpression() {
        return this.expression;
    }

    /**
     Sets the expression tree of the antecedent
     */
    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    /**
     Indicates whether the antecedent is loaded
     */
    public boolean isLoaded() {
        return expression != null;
    }

    /**
     Computes the activation degree of the antecedent on the expression tree
     from the root node
     @param conjunction is the conjunction operator from the RuleBlock
     @param disjunction is the disjunction operator from the RuleBlock
     @return the activation degree of the antecedent
     */
    public double activationDegree(TNorm conjunction, SNorm disjunction) {
        return this.activationDegree(conjunction, disjunction, expression);
    }

    /**
     Computes the activation degree of the antecedent on the expression tree
     from the given node
     @param conjunction is the conjunction operator from the RuleBlock
     @param disjunction is the disjunction operator from the RuleBlock
     @param node is a node in the expression tree of the antecedent
     @return the activation degree of the antecedent
     */
    public double activationDegree(TNorm conjunction, SNorm disjunction, Expression node) {
        /* if (!isLoaded()) {
            throw new RuntimeException(String.format(
                    "[antecedent error] antecedent <%s> is not loaded", text));
        } */
        final Expression.Type expressionType = node.type();
        if (expressionType == Expression.Type.Proposition) {
            Proposition proposition = (Proposition) node;
            if (!proposition.getVariable().isEnabled()) {
                return 0.0;
            }

            Variable variable = proposition.getVariable();
            double result = Double.NaN;
            Variable.Type variableType = variable.type();
            if (variableType == Variable.Type.Input) {
                result = proposition.getTerm().membership(variable.getValue());
            } else if (variableType == Variable.Type.Output) {
                result = ((OutputVariable) variable).fuzzyOutput().activationDegree(proposition.getTerm());
            }

            return result;
        }

        if (expressionType == Expression.Type.Operator) {
            Operator operator = (Operator) node;
            if (operator.getLeft() == null || operator.getRight() == null) {
                throw new RuntimeException("[syntax error] left and right operators cannot be null");
            }
            if (Rule.FL_AND.equals(operator.getName())) {
                if (conjunction == null) {
                    throw new RuntimeException(String.format("[conjunction error] "
                            + "the following rule requires a conjunction operator:\n%s", text));
                }
                return conjunction.compute(
                        activationDegree(conjunction, disjunction, operator.getLeft()),
                        activationDegree(conjunction, disjunction, operator.getRight()));
            }
            throw new RuntimeException(String.format(
                    "[syntax error] operator <%s> not recognized",
                    operator.getName()));
        }
        else {
            throw new RuntimeException("[expression error] unknown instance of Expression");
        }
    }

    /**
     Unloads the antecedent
     */
    public void unload() {
        setExpression(null);
    }

    /**
     Loads the antecedent with the text obtained from Antecedent::getText() and
     uses the engine to identify and retrieve references to the input variables
     and output variables as required
     @param engine is the engine from which the rules are part of
     */

    public void load(Engine engine) {
        load(getText(), engine);
    }

    /**
     Loads the antecedent with the given text and uses the engine to identify
     and retrieve references to the input variables and output variables as
     required
     */

    public void load(String antecedent, Engine engine) {
        FuzzyLite.logger().log(Level.FINE, "Antecedent: {0}", antecedent);
        unload();
        setText(antecedent);
        if (antecedent.trim().isEmpty()) {
            throw new RuntimeException("[syntax error] antecedent is empty");
        }

        Function function = new Function();
        String postfix = function.toPostfix(antecedent);
        FuzzyLite.logger().log(Level.FINE, "Postfix {0}", postfix);

        final byte S_VARIABLE = 1, S_IS = 2, S_TERM = 8, S_AND_OR = 16;
        byte state = S_VARIABLE;
        Deque<Expression> expressionStack = new ArrayDeque<Expression>();
        Proposition proposition = null;

        StringTokenizer tokenizer = new StringTokenizer(postfix);
        String token = "";

        while (tokenizer.hasMoreTokens()) {
            token = tokenizer.nextToken();
            if ((state & S_VARIABLE) != 0) {
                Variable variable = null;
                if (engine.hasInputVariable(token)) {
                    variable = engine.getInputVariable(token);
                } else if (engine.hasOutputVariable(token)) {
                    variable = engine.getOutputVariable(token);
                }
                if (variable != null) {
                    proposition = new Proposition();
                    proposition.setVariable(variable);
                    expressionStack.push(proposition);

                    state = S_IS;
                    FuzzyLite.logger().log(Level.FINE, "Token <{0}> is variable", token);
                    continue;
                }
            }

            if ((state & S_TERM) != 0) {
                if (proposition.getVariable().hasTerm(token)) {
                    proposition.setTerm(proposition.getVariable().getTerm(token));
                    state = S_VARIABLE | S_AND_OR;
                    FuzzyLite.logger().log(Level.FINE, "Token <{0}> is term", token);
                    continue;
                }
            }

            if ((state & S_AND_OR) != 0) {
                if (Rule.FL_AND.equals(token) || Rule.FL_OR.equals(token)) {
                    if (expressionStack.size() < 2) {
                        throw new RuntimeException(String.format(
                                "[syntax error] logical operator <%s> expects at least two operands, but found <%d>",
                                token, expressionStack.size()));
                    }
                    Operator operator = new Operator();
                    operator.setName(token);
                    operator.setRight(expressionStack.pop());
                    operator.setLeft(expressionStack.pop());
                    expressionStack.push(operator);

                    state = S_VARIABLE | S_AND_OR;
                    FuzzyLite.logger().log(Level.FINE, "Subtree: ({0}) ({1})",
                            new Object[]{operator.getLeft(), operator.getRight()});
                    continue;
                }
            }

            //If reached this point, there was an error
            if ((state & S_VARIABLE) != 0 || (state & S_AND_OR) != 0) {
                throw new RuntimeException(String.format(
                        "[syntax error] expected variable or logical operator, but found <%s>",
                        token));
            }
            if ((state & S_IS) != 0) {
                throw new RuntimeException(String.format(
                        "[syntax error] expected keyword <%s>, but found <%s>",
                        Rule.FL_IS, token));
            }

            throw new RuntimeException(String.format(
                    "[syntax error] unexpected token <%s>",
                    token));
        }

        if (!((state & S_VARIABLE) != 0 || (state & S_AND_OR) != 0)) { //only acceptable final state
            if ((state & S_IS) != 0) {
                throw new RuntimeException(String.format(
                        "[syntax error] expected keyword <%s> after <%s>",
                        Rule.FL_IS, token));
            }

        }

        if (expressionStack.size() != 1) {
            List<String> errors = new LinkedList<String>();
            while (expressionStack.size() > 1) {
                Expression element = expressionStack.pop();
                errors.add(element.toString());
            }
            throw new RuntimeException(String.format(
                    "[syntax error] unable to parse the following expressions: <%s>",
                    Op.join(errors, " ")));
        }
        setExpression(expressionStack.pop());
    }
}