public class RandomForestClassifier {
    public int maxDepth;
    public int minSamplesSplit;
    public int numTrees;
    public DecisionTree[] decisionTrees;
    public RandomForestClassifier(int max_depth, int min_samples_split, int num_trees){
        this.maxDepth = max_depth;
        this.minSamplesSplit = min_samples_split;
        this.numTrees = num_trees;
        this.decisionTrees = new DecisionTree[num_trees];
    }

    public RandomForestClassifier(){
        this(100, 2, 11);
    }

    public void fit(double[][] X, int[] Y){
        int currSampleIndex = 0;
        double[][] tempX = new double[(int)Math.ceil((double)X.length/(double)numTrees)][X[0].length];
        int[] tempY = new int[(int)Math.ceil((double)X.length/(double)numTrees)];
        for(int i = 0; i < numTrees; ++i){
            for(int j = 0; j < tempX.length; ++j){
                tempX[j] = X[currSampleIndex];
                tempY[j] = Y[currSampleIndex];
                currSampleIndex++;
            }
            decisionTrees[i] = new DecisionTree(this.maxDepth, this.minSamplesSplit);
            decisionTrees[i].fit(tempX, tempY);
        }
    }

    public int[] predict(double X[][]){
        return decisionTrees[0].predict(X);
    }
}

