public class TrainTest {
    public double[][] xTrain;
    public double[][] xTest;
    public int[] yTrain;
    public int[] yTest;

    public TrainTest(double[][] X_train, double[][] X_test, int[] y_train, int[] y_test) {
        this.xTrain = X_train;
        this.xTest = X_test;
        this.yTrain = y_train;
        this.yTest = y_test;
    }
}
