import java.util.ArrayList;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/*
A Decision Tree is a supervised machine learning algorithm for classification tasks.
There are 2 main steps:
1. Building the decision tree
 This involving binary recursive splitting, evaluating each possible split at the current stage,
 and continuing to grow the tree until a stopping criterion is satisfied.
2. Making a prediction
 This involves traversing the tree recursively and  returning the most-common class label as a
 response value.
*/
public class DecisionTree {
    public int maxDepth;
    public int minSamplesSplit;
    public TreeNode<Integer> root;
    public int nClassLabels;
    public int nSamples;
    public int nFeatures;

    public DecisionTree(int maxDepth, int minSamplesSplit) {
        this.maxDepth = maxDepth;
        this.minSamplesSplit = minSamplesSplit;
        this.root = null;
    }
    public DecisionTree(int maxDepth) {
        this(maxDepth, 2);
    }
    public DecisionTree() {
        this(100, 2);
    }

    /*
    isFinished is used to evaluate the stopping criteria.
    The building process will be stopped at that current branch if the predefined max depth
    has been reached, or if there is only 1 nClassLabel, or if there are fewer samples than
    the minimum required samples per split remaining.
     */
    private boolean isFinished(int depth) {
        return depth >= this.maxDepth || this.nClassLabels == 1 || this.nSamples < this.minSamplesSplit;
    }

    /*
    Entropy is the level of uncertainty and is defined as follows: For each unique value in y,
    target variable of 0 for benign and 1 for malignant, the proportion of the values in y that
    is equal to that unique value is calculated. For each proportion p, -(p * Math.log2(p)) is
    calculated and added to sum. Sum will be returned to give entropy.
     */
    private double entropy(int[] y) {
        double[] proportions = Helper.divideArray(Helper.bincount(y), y.length);
        double sum = 0;
        for (double p : proportions) {
            if (p > 0) {
                sum += -(p * Math.log(p) / Math.log(2));
            }
        }
        return sum;
    }

    private Pair<int[], int[]> createSplit(double[] X, double thresh) {
        ArrayList<Integer> leftIdx = new ArrayList<>();
        ArrayList<Integer> rightIdx = new ArrayList<>();
        for (int i = 0; i < X.length; ++i) {
            if (X[i] <= thresh) leftIdx.add(i);
            else rightIdx.add(i);
        }
        int[] leftIdxArr = leftIdx.stream().mapToInt(i->i).toArray();
        int[] rightIdxArr = rightIdx.stream().mapToInt(i->i).toArray();
        return new Pair<>(leftIdxArr, rightIdxArr);
    }

    /*
    To calculate the split’s information gain, compute the sum of weighted entropy of the
    children and subtract it from the parent’s entropy.
    */
    private double informationGain(double[] X, int[] y, double thresh) {
        double parentLoss = entropy(y);
        Pair<int[], int[]> splitPair = createSplit(X, thresh);
        int[] leftIdx = splitPair.getA();
        int[] rightIdx = splitPair.getB();
        int n = y.length;
        int nLeft = leftIdx.length;
        int nRight = rightIdx.length;

        if (nLeft == 0 || nRight == 0) {
            return 0;
        }

        double child_loss = (nLeft / (double)n) * entropy(Helper.getArrayFromIndices(y, leftIdx)) +
                (nRight / (double)n) * entropy(Helper.getArrayFromIndices(y, rightIdx));

        return parentLoss - child_loss;
    }

    /*
    To get the best split, loop through all the feature indices and unique threshold values
    to calculate the information gain. Once the information gain is obtained for the specific
    feature-threshold combination, the result is compared to previous iterations. If a better
    split is found, the associated parameters are stored. After looping through all combinations,
    the best feature and threshold are returned.
     */
    private Pair<Integer, Double> bestSplit(double[][] X, int[] y, int[] features) {
        double splitScore = -1.0;
        int splitFeat = 0;
        double splitThresh = 0.0;

        for (int feat : features) {
            double[] X_feat = Helper.getColumn(X, feat);
            double[] thresholds = DoubleStream.of(X_feat).distinct().sorted().toArray(); // unique
            for (double thresh : thresholds) {
                double score = informationGain(X_feat, y, thresh);

                if (score > splitScore) {
                    splitScore = score;
                    splitFeat = feat;
                    splitThresh = thresh;
                }
            }
        }

        return new Pair<>(splitFeat, splitThresh);
    }

    private TreeNode<Integer> buildTree(double[][] X, int[] y, int depth) {
        this.nSamples = X.length;
        if (X.length > 0) this.nFeatures = X[0].length;
        else this.nFeatures = 0;
        this.nClassLabels = IntStream.of(y).distinct().sorted().toArray().length;

        // stopping criteria
        /*
        If the building process has finished, compute the most common class label and
        save that value in a leaf node.
         */
        if (isFinished(depth)) {
            int mostCommonLabel = Helper.getIndexOfMax(Helper.bincount(y));
            return new TreeNode<Integer>(mostCommonLabel);
        }

        // get best split
        int[] rndFeats = Helper.randomNumbersNoRepeat(nFeatures).stream().mapToInt(i->i).toArray();
        Pair<Integer, Double> bestSplit = bestSplit(X, y, rndFeats);
        int bestFeat = bestSplit.getA();
        double bestThresh = bestSplit.getB();

        // grow children recursively
        Pair<int[], int[]> bestSplitPair = createSplit(Helper.getColumn(X, bestFeat), bestThresh);
        int[] leftIdx = bestSplitPair.getA();
        int[] rightIdx = bestSplitPair.getB();
        TreeNode<Integer> leftChild = buildTree(Helper.getArrayFromIndices(X, leftIdx), Helper.getArrayFromIndices(y, leftIdx), depth+1);
        TreeNode<Integer> rightChild = buildTree(Helper.getArrayFromIndices(X, rightIdx), Helper.getArrayFromIndices(y, rightIdx), depth+1);
        return new TreeNode<>(bestFeat, bestThresh, leftChild, rightChild);
    }
    private TreeNode<Integer> buildTree(double[][] X, int[] y) {
        return buildTree(X, y, 0);
    }

    private Integer traverseTree(double[] x, TreeNode<Integer> node) {
        if (node.is_leaf()) {
            return node.getItem();
        }
        if (x[node.getFeature()] <= node.getThreshold()) {
            return traverseTree(x, node.getLeft());
        }
        return traverseTree(x, node.getRight());
    }

    public void fit(double[][] X, int[] y) {
        this.root = buildTree(X, y);
    }

    // Making a prediction by recursively traversing the tree until a leaf note is reached
    public int[] predict(double[][] X) {
        int[] predictions = new int[X.length];
        for (int i = 0; i < X.length; ++i) {
            predictions[i] = traverseTree(X[i], root);
        }
        return predictions;
    }
}