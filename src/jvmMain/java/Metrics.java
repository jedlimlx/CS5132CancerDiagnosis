public class Metrics {
    int trueNegative, falsePositive, falseNegative, truePositive, size;
    public Metrics(int[] yTrue, int[] yPred) {
        assert (yTrue.length == yPred.length);
        trueNegative = 0;
        falsePositive = 0;
        falseNegative = 0;
        truePositive = 0;
        size = yPred.length;
        for (int i = 0; i < size; ++i) {
            if (yTrue[i] == 0 && yPred[i] == 0)
                trueNegative++;
            if (yTrue[i] == 0 && yPred[i] == 1)
                falsePositive++;
            if (yTrue[i] == 1 && yPred[i] == 0)
                falseNegative++;
            if (yTrue[i] == 1 && yPred[i] == 1)
                truePositive++;
        }
    }

    public int getTrueNegative() { return trueNegative; }

    public int getFalsePositive() { return falsePositive; }

    public int getFalseNegative() { return falseNegative; }

    public int getTruePositive() { return truePositive; }

    public double getAccuracy() { return (trueNegative + truePositive) / (double)size; }

    public double getPrecision() { return truePositive / (double)(truePositive + falsePositive); }

    public double getRecall() { return truePositive / (double)(truePositive + falseNegative); }

    public double getSpecificity() { return trueNegative / (double)(trueNegative + falsePositive); }

    public double getFScore(double beta) {
        return (1.0 + beta * beta) * getPrecision() * getRecall() /
            (beta * beta * getPrecision() + getRecall());
    }

    public double getBalancedAccuracy() { return (getRecall() + getSpecificity()) / 2.0; }

    @Override
    public String toString() {
        return String.format("True Negative: %d\n"
                                 + "False Positive: %d\n"
                                 + "False Negative: %d\n"
                                 + "True Positive: %d\n"
                                 + "Accuracy: %.2f%% \n"
                                 + "Precision: %.2f%% \n"
                                 + "Recall: %.2f%% \n"
                                 + "Specificity: %.2f%% \n"
                                 + "F1-Score: %.2f%% \n"
                                 + "Balanced Accuracy: %.2f%% \n",
                             trueNegative, falsePositive, falseNegative, truePositive,
                             getAccuracy() * 100, getPrecision() * 100, getRecall() * 100,
                             getSpecificity() * 100, getFScore(1.0) * 100,
                             getBalancedAccuracy() * 100);
    }
}
