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
        Area.addTerm(new Trapezoid("Within_Smaller_Trapezoid", 184.5, 185.0, 748.8, 1000, 1.0)); //creates a membership function
        Area.addTerm(new Trapezoid("Within_Larger_Trapezoid", 508.1, 2194, 4255, 4256, 1.0));

        Scanner s = new Scanner(System.in);
        System.out.print("Please enter the input value for Area: ");
        Area.setValue( Double.parseDouble(s.nextLine()) );

        engine.addInputVariable(Area);

        InputVariable Perimeter = new InputVariable();
        Perimeter.setName("Perimeter");
        Perimeter.setDescription("");
        Perimeter.setEnabled(true); //Sets whether the rule is enabled. An enabled rule will be fired, but a disabled rule will not.
        Perimeter.setRange(50, 252); //Sets the range of the variable between [minimum, maximum]
        Perimeter.setLockValueInRange(false);
        Perimeter.addTerm(new Trapezoid("Within_Smaller_Trapezoid", 49.5, 50.1, 92.58, 103, 1.0)); //creates a membership function
        Perimeter.addTerm(new Trapezoid("Within_Larger_Trapezoid", 85.1, 159.8, 252, 252.5, 1.0));

        System.out.print("Please enter the input value for Perimeter: ");
        Perimeter.setValue( Double.parseDouble(s.nextLine()) );

        engine.addInputVariable(Perimeter);

        InputVariable Homogeneity = new InputVariable();
        Homogeneity.setName("Homogeneity");
        Homogeneity.setDescription("");
        Homogeneity.setEnabled(true); //Sets whether the rule is enabled. An enabled rule will be fired, but a disabled rule will not.
        Homogeneity.setRange(0.01, 0.45); //Sets the range of the variable between [minimum, maximum]
        Homogeneity.setLockValueInRange(false);
        Homogeneity.addTerm(new Trapezoid("Within_More_Trapezoid", 0, 0.01, 0.1232, 0.19, 1.0)); //creates a membership function
        Homogeneity.addTerm(new Trapezoid("Within_Less_Trapezoid", 0.0295, 0.2168, 0.45, 0.5, 1.0));

        System.out.print("Please enter the input value for Homogeneity: ");
        Homogeneity.setValue( Double.parseDouble(s.nextLine()) );

        engine.addInputVariable(Homogeneity);

        InputVariable Uniformity = new InputVariable();
        Uniformity.setName("Uniformity");
        Uniformity.setDescription("");
        Uniformity.setEnabled(true);
        Uniformity.setRange(0.000, 12.000);
        Uniformity.setLockValueInRange(false);
        Uniformity.addTerm(new Trapezoid("Within_More_Trapezoid", -0.5, 0, 1.669, 2.6, 1.0)); //creates a membership function
        Uniformity.addTerm(new Trapezoid("Within_Less_Trapezoid", 0.65, 6.205, 12, 12.5, 1.0));

        System.out.print("Please enter the input value for Uniformity: ");
        Uniformity.setValue( Double.parseDouble(s.nextLine()) );

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
        Diagnosis.addTerm(new Trapezoid("Within_Benign_Triangle", -0.5, 0, 0.4, 0.5, 1.0)); //creates a membership function
        Diagnosis.addTerm(new Trapezoid("Within_Malignant_Triangle", 0.6, 0.7, 1, 1.5, 1.0));
        Diagnosis.addTerm(new Triangle("Within_Undefined_Triangle", 0.5, 0.55, 0.6, 1.0));

        engine.addOutputVariable(Diagnosis);

        RuleBlock ruleBlock = new RuleBlock();
        ruleBlock.setName("");
        ruleBlock.setDescription("");
        ruleBlock.setEnabled(true);
        ruleBlock.setConjunction(new Minimum());
        ruleBlock.setDisjunction(new Maximum());
        ruleBlock.setImplication(new Minimum());
        ruleBlock.setActivation(new General()); //the general class is a ruleBlock activation method that activates every rule following the order in which the rules were added to the rule block


        /** AS THE RULES ARE FUZZY, A PATIENT WILL TRIGGER MORE THAN 1 RULE.
            IN THE WORST CASE, A PATIENT CAN TRIGGER ALL 16 RULES.
            HENCE, A FUZZY LOGIC SYSTEM IS NEEDED.
        */
        ruleBlock.addRule(Rule.parse("if (Area is Within_Smaller_Trapezoid and Perimeter is Within_Smaller_Trapezoid and Homogeneity is Within_More_Trapezoid and Uniformity is Within_More_Trapezoid) then Diagnosis is Within_Benign_Triangle", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Within_Smaller_Trapezoid and Perimeter is Within_Smaller_Trapezoid and Homogeneity is Within_More_Trapezoid and Uniformity is Within_Less_Trapezoid) then Diagnosis is Within_Undefined_Triangle", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Within_Smaller_Trapezoid and Perimeter is Within_Smaller_Trapezoid and Homogeneity is Within_Less_Trapezoid and Uniformity is Within_More_Trapezoid) then Diagnosis is Within_Undefined_Triangle", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Within_Smaller_Trapezoid and Perimeter is Within_Smaller_Trapezoid and Homogeneity is Within_Less_Trapezoid and Uniformity is Within_Less_Trapezoid) then Diagnosis is Within_Undefined_Triangle", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Within_Smaller_Trapezoid and Perimeter is Within_Larger_Trapezoid and Homogeneity is Within_More_Trapezoid and Uniformity is Within_More_Trapezoid) then Diagnosis is Within_Undefined_Triangle", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Within_Smaller_Trapezoid and Perimeter is Within_Larger_Trapezoid and Homogeneity is Within_More_Trapezoid and Uniformity is Within_Less_Trapezoid) then Diagnosis is Within_Undefined_Triangle", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Within_Smaller_Trapezoid and Perimeter is Within_Larger_Trapezoid and Homogeneity is Within_Less_Trapezoid and Uniformity is Within_More_Trapezoid) then Diagnosis is Within_Undefined_Triangle", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Within_Smaller_Trapezoid and Perimeter is Within_Larger_Trapezoid and Homogeneity is Within_Less_Trapezoid and Uniformity is Within_Less_Trapezoid) then Diagnosis is Within_Undefined_Triangle", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Within_Larger_Trapezoid and Perimeter is Within_Smaller_Trapezoid and Homogeneity is Within_More_Trapezoid and Uniformity is Within_More_Trapezoid) then Diagnosis is Within_Undefined_Triangle", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Within_Larger_Trapezoid and Perimeter is Within_Smaller_Trapezoid and Homogeneity is Within_More_Trapezoid and Uniformity is Within_Less_Trapezoid) then Diagnosis is Within_Undefined_Triangle", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Within_Larger_Trapezoid and Perimeter is Within_Smaller_Trapezoid and Homogeneity is Within_Less_Trapezoid and Uniformity is Within_More_Trapezoid) then Diagnosis is Within_Undefined_Triangle", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Within_Larger_Trapezoid and Perimeter is Within_Smaller_Trapezoid and Homogeneity is Within_Less_Trapezoid and Uniformity is Within_Less_Trapezoid) then Diagnosis is Within_Undefined_Triangle", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Within_Larger_Trapezoid and Perimeter is Within_Larger_Trapezoid and Homogeneity is Within_More_Trapezoid and Uniformity is Within_More_Trapezoid) then Diagnosis is Within_Undefined_Triangle", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Within_Larger_Trapezoid and Perimeter is Within_Larger_Trapezoid and Homogeneity is Within_More_Trapezoid and Uniformity is Within_Less_Trapezoid) then Diagnosis is Within_Undefined_Triangle", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Within_Larger_Trapezoid and Perimeter is Within_Larger_Trapezoid and Homogeneity is Within_Less_Trapezoid and Uniformity is Within_More_Trapezoid) then Diagnosis is Within_Undefined_Triangle", engine));
        ruleBlock.addRule(Rule.parse("if (Area is Within_Larger_Trapezoid and Perimeter is Within_Larger_Trapezoid and Homogeneity is Within_Less_Trapezoid and Uniformity is Within_Less_Trapezoid) then Diagnosis is Within_Malignant_Triangle", engine));

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