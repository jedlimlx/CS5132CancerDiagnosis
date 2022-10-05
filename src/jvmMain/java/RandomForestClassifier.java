public class RandomForestClassifier {
    public int max_depth;
    public int min_samples_split;

    DecisionTree[] decisionTrees;
    public RandomForestClassifier(int max_depth, int min_samples_split){
        this.max_depth = max_depth;
        this.min_samples_split = min_samples_split;
    }

    public RandomForestClassifier(int max_depth){
        this(max_depth, 2);
    }

    public RandomForestClassifier(){
        this(100, 2);
    }
}

