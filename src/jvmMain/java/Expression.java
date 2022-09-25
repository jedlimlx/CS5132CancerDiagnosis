public abstract class Expression
{
    public enum Type
    {
        Proposition, Operator
    }

    /**
     Returns the type of the expression
     @return the type of the expression
     */
    public abstract Type type();

    @Override
    public abstract String toString();
}