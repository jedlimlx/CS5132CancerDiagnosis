public class TrainTest {
    public double[][] X_train;
    public double[][] X_test;
    public int[] y_train;
    public int[] y_test;

    public TrainTest(double[][] X_train, double[][] X_test, int[] y_train, int[] y_test) {
        this.X_train = X_train;
        this.X_test = X_test;
        this.y_train = y_train;
        this.y_test = y_test;
    }
}
