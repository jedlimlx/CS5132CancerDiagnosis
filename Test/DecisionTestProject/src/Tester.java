import java.io.FileNotFoundException;

public class Tester {
    public static void main(String[] args) throws FileNotFoundException {
        Data data = new Data();
        System.out.println(data.getTargetEntropy());
        System.out.println(data.calculateInformationGain(0));
        System.out.println(data.calculateInformationGain(1));
        System.out.println(data.calculateInformationGain(2));
        System.out.println(data.calculateInformationGain(3));

    }
}
