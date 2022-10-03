import java.util.ArrayList;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class DecisionTree {
    public int max_depth;
    public int min_samples_split;
    public TreeNode<Integer> root;
    public int n_class_labels;
    public int n_samples;
    public int n_features;

    // constructors
    public DecisionTree(int max_depth, int min_samples_split) {
        this.max_depth = max_depth;
        this.min_samples_split = min_samples_split;
        this.root = null;
    }
    public DecisionTree(int max_depth) {
        this(max_depth, 2);
    }
    public DecisionTree() {
        this(100, 2);
    }

    // private methods
    private boolean isFinished(int depth) {
        return depth >= this.max_depth || this.n_class_labels == 1 || this.n_samples < this.min_samples_split;
    }
    private double entropy(int[] y) {
        double[] proportions = Helper.divideArray(Helper.bincount(y), y.length);
        double sum = 0;
        for (double p : proportions) {
            if (p > 0) {
                sum += p * Math.log(p) / Math.log(2);
            }
        }
        return -sum;
    }
    // return two arrays of idx
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
        this.n_samples = X.length;
        if (X.length > 0) this.n_features = X[0].length;
        else this.n_features = 0;
        this.n_class_labels = IntStream.of(y).distinct().sorted().toArray().length;

        // stopping criteria
        if (isFinished(depth)) {
            int most_common_Label = Helper.getIndexOfMax(Helper.bincount(y));
            return new TreeNode<Integer>(most_common_Label);
        }

        // get best split
        int[] rnd_feats = Helper.randomNumbersNoRepeat(n_features).stream().mapToInt(i->i).toArray();
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

    // public methods
    public void fit(double[][] X, int[] y) {
        System.out.println("Fitting data into Decision Tree...");
        this.root = build_tree(X, y);
    }

    public int[] predict(double[][] X) {
        int[] predictions = new int[X.length];
        for (int i = 0; i < X.length; ++i) {
            predictions[i] = traverse_tree(X[i], root);
        }
        return predictions;
    }
}