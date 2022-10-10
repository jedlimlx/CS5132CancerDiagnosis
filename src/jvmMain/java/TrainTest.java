public class TrainTest {
    public double[][] xTrain;
    public double[][] xTest;
    public int[] yTrain;
    public int[] yTest;

    public TrainTest(double[][] xTrain, double[][] xTest, int[] yTrain, int[] yTest) {
        this.xTrain = xTrain;
        this.xTest = xTest;
        this.yTrain = yTrain;
        this.yTest = yTest;
    }
}
