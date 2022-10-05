public class RandomForestClassifier {
    public int max_depth;
    public int min_samples_split;
    public int num_trees;
    public DecisionTree[] decisionTrees;
    public RandomForestClassifier(int max_depth, int min_samples_split, int num_trees){
        this.max_depth = max_depth;
        this.min_samples_split = min_samples_split;
        this.num_trees = num_trees;
        this.decisionTrees = new DecisionTree[num_trees];
    }

    public RandomForestClassifier(){
        this(100, 2, 10);
    }

    public void fit(double[][] X, int[] Y){

    }

}

