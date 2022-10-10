import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// DataFrame Class to hold data
public class DataFrame {
    private final String[][] data;
    private final String[] labels;
    private final HashMap<String, Integer> labelToColumnHashMap;
    private final int attributeCount, sampleCount;
    private double targetEntropy;

    public DataFrame(String[] labels, HashMap<String, Integer> labelToColumnHashMap,
                     int attributeCount, int sampleCount) {
        this.data = new String[attributeCount][sampleCount];
        this.labels = labels;
        this.labelToColumnHashMap = labelToColumnHashMap;
        this.attributeCount = attributeCount;
        this.sampleCount = sampleCount;
    }
    public DataFrame(String[][] data, String[] labels,
                     HashMap<String, Integer> labelToColumnHashMap, int attributeCount,
                     int sampleCount) {
        this.data = data;
        this.labels = labels;
        this.labelToColumnHashMap = labelToColumnHashMap;
        this.attributeCount = attributeCount;
        this.sampleCount = sampleCount;
    }

    public DataFrame(String csvName, int attributeCount, int sampleCount)
        throws FileNotFoundException {
        this.data = new String[attributeCount][sampleCount];
        this.labels = new String[attributeCount];
        this.labelToColumnHashMap = new HashMap<String, Integer>();
        Scanner sc = new Scanner(new File("PlayTennis.csv"));
        String[] stringVars = sc.nextLine().split(",");
        for (int i = 0; i < attributeCount; ++i) {
            labels[i] = stringVars[i];
            System.out.println(labels[i]);
            labelToColumnHashMap.put(labels[i], i);
        }
        for (int i = 0; i < sampleCount; ++i) {
            stringVars = sc.nextLine().split(",");
            for (int j = 0; j < attributeCount; ++j)
                data[j][i] = stringVars[j];
        }

        targetEntropy = 0.0;
        HashMap<String, Integer> hashTable = new HashMap<String, Integer>();
        for (String i : data[attributeCount - 1]) {
            if (hashTable.containsKey(i))
                hashTable.put(i, hashTable.get(i) + 1);
            else
                hashTable.put(i, 1);
        }
        for (Map.Entry<String, Integer> i : hashTable.entrySet()) {
            double prob = ((double)i.getValue()) / ((double)sampleCount);
            targetEntropy -= prob * Math.log(prob) / Math.log(2);
        }

        this.attributeCount = attributeCount;
        this.sampleCount = sampleCount;
    }

    public String[][] getData() { return data; }

    public int getAttributeCount() { return attributeCount; }

    public String[] getLabels() { return labels; }

    public HashMap<String, Integer> getLabelToColumnHashMap() { return labelToColumnHashMap; }

    public int getSampleCount() { return sampleCount; }

    public double getTargetEntropy() { return targetEntropy; }

    public String[] getTargetData() { return data[attributeCount - 1]; }

    public int getColumnFromLabel(String label) { return labelToColumnHashMap.get(label); }
}
