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

    public DecisionTree(int max_depth, int min_samples_split) {
        this.maxDepth = max_depth;
        this.minSamplesSplit = min_samples_split;
        this.root = null;
    }
    public DecisionTree(int max_depth) {
        this(max_depth, 2);
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

    private Pair<int[], int[]> create_split(double[] X, double thresh) {
        ArrayList<Integer> left_idx = new ArrayList<>();
        ArrayList<Integer> right_idx = new ArrayList<>();
        for (int i = 0; i < X.length; ++i) {
            if (X[i] <= thresh) left_idx.add(i);
            else right_idx.add(i);
        }
        int[] left_idx_arr = left_idx.stream().mapToInt(i->i).toArray();
        int[] right_idx_arr = right_idx.stream().mapToInt(i->i).toArray();
        return new Pair<>(left_idx_arr, right_idx_arr);
    }

    /*
    To calculate the split’s information gain, compute the sum of weighted entropy of the
    children and subtract it from the parent’s entropy.
    */
    private double information_gain(double[] X, int[] y, double thresh) {
        double parent_loss = entropy(y);
        Pair<int[], int[]> splitPair = create_split(X, thresh);
        int[] left_idx = splitPair.getA();
        int[] right_idx = splitPair.getB();
        int n = y.length;
        int n_left = left_idx.length;
        int n_right = right_idx.length;

        if (n_left == 0 || n_right == 0) {
            return 0;
        }

        double child_loss = (n_left / (double)n) * entropy(Helper.getArrayFromIndices(y, left_idx)) +
                (n_right / (double)n) * entropy(Helper.getArrayFromIndices(y, right_idx));

        return parent_loss - child_loss;
    }

    /*
    To get the best split, loop through all the feature indices and unique threshold values
    to calculate the information gain. Once the information gain is obtained for the specific
    feature-threshold combination, the result is compared to previous iterations. If a better
    split is found, the associated parameters are stored. After looping through all combinations,
    the best feature and threshold are returned.
     */
    private Pair<Integer, Double> best_split(double[][] X, int[] y, int[] features) {
        double splitScore = -1.0;
        int splitFeat = 0;
        double splitThresh = 0.0;

        for (int feat : features) {
            double[] X_feat = Helper.getColumn(X, feat);
            double[] thresholds = DoubleStream.of(X_feat).distinct().sorted().toArray(); // unique
            for (double thresh : thresholds) {
                double score = information_gain(X_feat, y, thresh);

                if (score > splitScore) {
                    splitScore = score;
                    splitFeat = feat;
                    splitThresh = thresh;
                }
            }
        }

        return new Pair<>(splitFeat, splitThresh);
    }

    private TreeNode<Integer> build_tree(double[][] X, int[] y, int depth) {
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
            int most_common_Label = Helper.getIndexOfMax(Helper.bincount(y));
            return new TreeNode<Integer>(most_common_Label);
        }

        // get best split
        int[] rnd_feats = Helper.randomNumbersNoRepeat(nFeatures).stream().mapToInt(i->i).toArray();
        Pair<Integer, Double> bestSplit = best_split(X, y, rnd_feats);
        int best_feat = bestSplit.getA();
        double best_thresh = bestSplit.getB();

        // grow children recursively
        Pair<int[], int[]> bestSplitPair = create_split(Helper.getColumn(X, best_feat), best_thresh);
        int[] left_idx = bestSplitPair.getA();
        int[] right_idx = bestSplitPair.getB();
        TreeNode<Integer> left_child = build_tree(Helper.getArrayFromIndices(X, left_idx), Helper.getArrayFromIndices(y, left_idx), depth+1);
        TreeNode<Integer> right_child = build_tree(Helper.getArrayFromIndices(X, right_idx), Helper.getArrayFromIndices(y, right_idx), depth+1);
        return new TreeNode<>(best_feat, best_thresh, left_child, right_child);
    }
    private TreeNode<Integer> build_tree(double[][] X, int[] y) {
        return build_tree(X, y, 0);
    }

    private Integer traverse_tree(double[] x, TreeNode<Integer> node) {
        if (node.is_leaf()) {
            return node.getItem();
        }
        if (x[node.getFeature()] <= node.getThreshhold()) {
            return traverse_tree(x, node.getLeft());
        }
        return traverse_tree(x, node.getRight());
    }

    public void fit(double[][] X, int[] y) {
        this.root = build_tree(X, y);
    }

    // Making a prediction by recursively traversing the tree until a leaf note is reached
    public int[] predict(double[][] X) {
        int[] predictions = new int[X.length];
        for (int i = 0; i < X.length; ++i) {
            predictions[i] = traverse_tree(X[i], root);
        }
        return predictions;
    }
}