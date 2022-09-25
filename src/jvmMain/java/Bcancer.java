import com.fuzzylite.Engine;
import com.fuzzylite.activation.General;
import com.fuzzylite.defuzzifier.Centroid;
import com.fuzzylite.norm.s.Maximum;
import com.fuzzylite.norm.t.Minimum;
import com.fuzzylite.rule.Rule;
import com.fuzzylite.rule.RuleBlock;
import com.fuzzylite.term.Trapezoid;
import com.fuzzylite.term.Triangle;
import com.fuzzylite.variable.InputVariable;
import com.fuzzylite.variable.OutputVariable;

import java.util.Scanner;

public class Bcancer
{
    public static void main(String[] args)
    {

        Engine engine = new Engine();
        engine.setName("Bcancer");
        engine.setDescription("");

        InputVariable Area = new InputVariable();
        Area.setName("Area");
        Area.setDescription("");
        Area.setEnabled(true); //Sets whether the rule is enabled. An enabled rule will be fired, but a disabled rule will not.
        Area.setRange(185, 4255); //Sets the range of the variable between [minimum, maximum]
        Area.setLockValueInRange(false);
        Area.addTerm(new Trapezoid("Smaller", 184.5, 185.0, 748.8, 1000, 1.0)); //creates a membership function
        Area.addTerm(new Trapezoid("Larger", 508.1, 2194, 4255, 4256, 1.0));

        Scanner s = new Scanner(System.in);
        System.out.print("Please enter the input value for Area: ");
        Area.setValue(10) ; //Double.parseDouble(s.nextLine()) );

        engine.addInputVariable(Area);

        InputVariable Perimeter = new InputVariable();
        Perimeter.setName("Perimeter");
        Perimeter.setDescription("");
        Perimeter.setEnabled(true); //Sets whether the rule is enabled. An enabled rule will be fired, but a disabled rule will not.
        Perimeter.setRange(50, 252); //Sets the range of the variable between [minimum, maximum]
        Perimeter.setLockValueInRange(false);
        Perimeter.addTerm(new Trapezoid("Smaller", 49.5, 50.1, 92.58, 103, 1.0)); //creates a membership function
        Perimeter.addTerm(new Trapezoid("Larger", 85.1, 159.8, 252, 252.5, 1.0));

        System.out.print("Please enter the input value for Perimeter: ");
        Perimeter.setValue(10); //Double.parseDouble(s.nextLine()) );

        engine.addInputVariable(Perimeter);

        InputVariable Homogeneity = new InputVariable();
        Homogeneity.setName("Homogeneity");
        Homogeneity.setDescription("");
        Homogeneity.setEnabled(true); //Sets whether the rule is enabled. An enabled rule will be fired, but a disabled rule will not.
        Homogeneity.setRange(0.01, 0.45); //Sets the range of the variable between [minimum, maximum]
        Homogeneity.setLockValueInRange(false);
        Homogeneity.addTerm(new Trapezoid("More", 0, 0.01, 0.1232, 0.19, 1.0)); //creates a membership function
        Homogeneity.addTerm(new Trapezoid("Less", 0.0295, 0.2168, 0.45, 0.5, 1.0));

        System.out.print("Please enter the input value for Homogeneity: ");
        Homogeneity.setValue(10); // Double.parseDouble(s.nextLine()) );

        engine.addInputVariable(Homogeneity);

        InputVariable Uniformity = new InputVariable();
        Uniformity.setName("Uniformity");
        Uniformity.setDescription("");
        Uniformity.setEnabled(true);
        Uniformity.setRange(0.000, 12.000);
        Uniformity.setLockValueInRange(false);
        Uniformity.addTerm(new Trapezoid("More", -0.5, 0, 1.669, 2.6, 1.0)); //creates a membership function
        Uniformity.addTerm(new Trapezoid("Less", 0.65, 6.205, 12, 12.5, 1.0));

        System.out.print("Please enter the input value for Uniformity: ");
        Uniformity.setValue(10); // Double.parseDouble(s.nextLine()) );

        engine.addInputVariable(Uniformity);

        OutputVariable Diagnosis = new OutputVariable();
        Diagnosis.setName("Diagnosis");
        Diagnosis.setDescription("");
        Diagnosis.setEnabled(true);
        Diagnosis.setRange(0, 1);
        Diagnosis.setLockValueInRange(false);
        Diagnosis.setAggregation(new Maximum());
        Diagnosis.setDefuzzifier(new Centroid(200));
        Diagnosis.setDefaultValue(Double.NaN);
        Diagnosis.setLockPreviousValue(false);
        Diagnosis.addTerm(new Trapezoid("Benign", -0.5, 0, 0.4, 0.5, 1.0)); //creates a membership function
        Diagnosis.addTerm(new Trapezoid("Malignant", 0.6, 0.7, 1, 1.5, 1.0));
        Diagnosis.addTerm(new Triangle("Undefined", 0.5, 0.55, 0.6, 1.0));

        engine.addOutputVariable(Diagnosis);

        RuleBlock ruleBlock = new RuleBlock();
        ruleBlock.setName("");
        ruleBlock.setDescription("");
        ruleBlock.setEnabled(true);
        ruleBlock.setConjunction(new Minimum());
        ruleBlock.setDisjunction(new Maximum());
        ruleBlock.setImplication(new Minimum());
        ruleBlock.setActivation(new General()); //the general class is a ruleBlock activation method that activates every rule following the order in which the rules were added to the rule block

        ruleBlock.addRule(Rule.parse("if (Area is Smaller and Perimeter is Smaller and Homogeneity is More and Uniformity is More) then Diagnosis is Benign", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Smaller and Perimeter is Smaller and Homogeneity is More and Uniformity is Less) then Diagnosis is Undefined", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Smaller and Perimeter is Smaller and Homogeneity is Less and Uniformity is More) then Diagnosis is Undefined", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Smaller and Perimeter is Smaller and Homogeneity is Less and Uniformity is Less) then Diagnosis is Undefined", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Smaller and Perimeter is Larger and Homogeneity is More and Uniformity is More) then Diagnosis is Undefined", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Smaller and Perimeter is Larger and Homogeneity is More and Uniformity is Less) then Diagnosis is Undefined", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Smaller and Perimeter is Larger and Homogeneity is Less and Uniformity is More) then Diagnosis is Undefined", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Smaller and Perimeter is Larger and Homogeneity is Less and Uniformity is Less) then Diagnosis is Undefined", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Larger and Perimeter is Smaller and Homogeneity is More and Uniformity is More) then Diagnosis is Undefined", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Larger and Perimeter is Smaller and Homogeneity is More and Uniformity is Less) then Diagnosis is Undefined", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Larger and Perimeter is Smaller and Homogeneity is Less and Uniformity is More) then Diagnosis is Undefined", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Larger and Perimeter is Smaller and Homogeneity is Less and Uniformity is Less) then Diagnosis is Undefined", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Larger and Perimeter is Larger and Homogeneity is More and Uniformity is More) then Diagnosis is Undefined", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Larger and Perimeter is Larger and Homogeneity is More and Uniformity is Less) then Diagnosis is Undefined", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Larger and Perimeter is Larger and Homogeneity is Less and Uniformity is More) then Diagnosis is Undefined", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Larger and Perimeter is Larger and Homogeneity is Less and Uniformity is Less) then Diagnosis is Malignant", engine));

        engine.addRuleBlock(ruleBlock);

        engine.process();

        int numOutputVariables = engine.numberOfOutputVariables();
        for(int i = 0; i < numOutputVariables; i++)
        {
            OutputVariable currOutputVariable = engine.getOutputVariable(i);
            System.out.printf("%s Output Variable's Value: %f\n", currOutputVariable.getName(), currOutputVariable.getValue());
        }
    }
}
