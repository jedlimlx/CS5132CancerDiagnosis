import java.util.*;

public class Helper {
    // same as np.bincount
    public static int[] bincount(int[] array) {
        int maxNumber = -1;
        OptionalInt optionalMax = Arrays.stream(array).max();
        if (optionalMax.isPresent())
            maxNumber = optionalMax.getAsInt();

        int[] countArray = new int[maxNumber + 1];

        for (int j : array)
            countArray[j]++;

        return countArray;
    }
    public static double[] divideArray(int[] array, double dividend) {
        double[] newArray = new double[array.length];
        for(int i = 0; i < array.length; ++i)
            newArray[i] = array[i]/dividend;

        return newArray;
    }
    // same as y[idx] in python
    public static int[] getArrayFromIndices(int[] y, int[] idx) {
        ArrayList<Integer> rtn = new ArrayList<>();
        for (int i : idx)
            rtn.add(y[i]);

        return rtn.stream().mapToInt(i->i).toArray();
    }
    // same as X[idx, :]
    public static double[][] getArrayFromIndices(double[][] X, int[] idx) {
        double[][] rtn = new double[idx.length][];
        for (int i = 0; i < idx.length; ++i)
            rtn[i] = X[idx[i]];

        return rtn;
    }

    // same as X[:, column]
    public static double[] getColumn(double[][] X, int column) {
        ArrayList<Double> rtn = new ArrayList<>();
        for (double[] x : X)
            rtn.add(x[column]);

        return rtn.stream().mapToDouble(d -> d).toArray();
    }
    // same as np.argmax
    public static int getIndexOfMax(int[] a)
    {
        if (a == null || a.length == 0) return -1; // null or empty

        int maxIndex = 0;
        for (int i = 1; i < a.length; ++i)
            if (a[i] > a[maxIndex]) maxIndex = i;

        return maxIndex;
    }
    // same as np.random.choice
    public static int[] randomChoice(int upperBound, int size) { // replace is True
        int[] rtn = new int[size];
        Random random = new Random();
        for (int i = 1; i < rtn.length; ++i)
            rtn[i] = random.nextInt(upperBound);
        return rtn;
    }
    // 5 -> [3,0,2,4,1], np.random.choice(x,x,replace=False)
    public static Set<Integer> randomNumbersNoRepeat(int size) {
        Random random = new Random();
        Set<Integer> set = new LinkedHashSet<>();
        while (set.size() < size)
            set.add(random.nextInt(size));
        return set;
    }
    // same as sklearn's train_test_split, test_size < 1.0
    public static TrainTest train_test_split(double[][] X, int[] y, double test_size) {
        if (X.length != y.length) {
            System.out.println("X and y does not have the same size");
            return null;
        }
        int size = y.length;
        List<Integer> idx = new ArrayList<>(randomNumbersNoRepeat(size));
        int splitIndex = (int)(size * (1-test_size));
        List<Integer> idx_train = new ArrayList<>(idx.subList(0, splitIndex));
        List<Integer> idx_test = new ArrayList<>(idx.subList(splitIndex, size));

        double[][] X_train = new double[splitIndex][];
        double[][] X_test = new double[size-splitIndex][];
        int[] y_train = new int[splitIndex];
        int[] y_test = new int[size-splitIndex];

        for (int i = 0; i < splitIndex; ++i) {
            X_train[i] = X[idx_train.get(i)];
            y_train[i] = y[idx_train.get(i)];
        }
        for (int j = 0; j < size - splitIndex; ++j) {
            X_test[j] = X[idx_test.get(j)];
            y_test[j] = y[idx_test.get(j)];
        }

        return new TrainTest(X_train, X_test, y_train, y_test);
    }
    // same as sklearn's accuracy_score
    public static double accuracy_score(int[] actual, int[] pred) {
        int matchCount = 0;
        if (pred.length != actual.length) return -1.0;
        for (int i = 0; i < pred.length; ++i)
            if (pred[i] == actual[i])
                matchCount++;
        return matchCount / (double) actual.length;
    }
}