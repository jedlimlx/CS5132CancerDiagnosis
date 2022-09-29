import java.io.FileNotFoundException;

public class Tester {
    public static void main(String[] args) throws FileNotFoundException {
        DataFrame dataFrame = new DataFrame("PlayTennis.csv", 5, 14);
        System.out.println(dataFrame.getTargetEntropy());
        DecisionTree decisionTree = new DecisionTree();
        System.out.println(decisionTree.calculateInformationGain(dataFrame, "Outlook"));
    }
}
