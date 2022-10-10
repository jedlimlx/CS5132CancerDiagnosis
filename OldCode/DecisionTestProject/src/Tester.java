import java.io.FileNotFoundException;

public class Tester {
    public static void main(String[] args) throws FileNotFoundException {
        DataFrame dataFrame = new DataFrame("PlayTennis.csv", 5, 14);
        System.out.println("Entropy: " + dataFrame.getTargetEntropy());
        DecisionTree decisionTree = new DecisionTree();
        System.out.println("Information Gain: " +
                           decisionTree.calculateInformationGain(dataFrame, "Outlook"));

        System.out.println("Dataframe: " + dataFrame);
    }
}
