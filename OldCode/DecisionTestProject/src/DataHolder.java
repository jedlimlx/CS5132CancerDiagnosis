import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DataHolder {
    ArrayList<BreastCancerPatient> array = new ArrayList<>();

    public DataHolder() {
        Scanner sc = new Scanner("breast_cancer.csv");

        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(",");
            array.add(
                new BreastCancerPatient(line[1], line[2], line[3], line[4], line[5], line[6]));
        }
    }
}

class BreastCancerPatient {
    int id;
    int diagnosis;
    char area_mean;
    double perimeter_mean;
    double homogeneity;
    double uniformity;

    public BreastCancerPatient(String id, String diagnosis, String area_mean, String perimeter_mean,
                               String homogeneity, String uniformity) {
        this.id = Integer.parseInt(id);
        this.diagnosis = Integer.parseInt(diagnosis);
        this.area_mean = area_mean.charAt(0);
        this.perimeter_mean = Double.parseDouble(perimeter_mean);
        this.homogeneity = Double.parseDouble(homogeneity);
        this.uniformity = Double.parseDouble(uniformity);
    }
}
