import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Data {
    private final String[][] attributeData;
    private final String[] attributeLabels, targetData, targetClasses;
    private final String targetLabel;

    private double targetEntropy;

    public Data() throws FileNotFoundException {
        attributeData = new String[4][14];
        attributeLabels = new String[4];
        targetData = new String[14];
        Scanner sc = new Scanner(new File("PlayTennis.csv"));
        String[] stringVars = sc.nextLine().split(",");
        targetLabel = stringVars[4];
        System.arraycopy(stringVars, 0, attributeLabels, 0, 4);
        for (int i = 0; i < 14; ++i) {
            stringVars = sc.nextLine().split(",");
            attributeData[0][i] = stringVars[0];
            attributeData[1][i] = stringVars[1];
            attributeData[2][i] = stringVars[2];
            attributeData[3][i] = stringVars[3];
            targetData[i] = stringVars[4];
        }
        targetClasses = getUniqueValues(targetData);
        targetEntropy = 0.0;
        HashMap<String, Integer> hashTable = getEmptyHash(targetClasses);
        for(String i : targetData)
            hashTable.put(i, hashTable.get(i) + 1);
        for(Map.Entry<String, Integer> i: hashTable.entrySet()){
            double prob = ((double) i.getValue())/targetData.length;
            targetEntropy -= prob * Math.log(prob)/Math.log(2);
        }
    }

    public double calculateEntropy(int dataColumn, String label){
        HashMap<String, Integer> hashTable = getEmptyHash(targetClasses);

        int count = 0;
        for(int i = 0; i < attributeData[dataColumn].length; ++i) {
            if (attributeData[dataColumn][i].equals(label)) {
                hashTable.put(targetData[i], hashTable.get(targetData[i]) + 1);
                count++;
            }
        }

        double entropy = 0;
        for(Map.Entry<String, Integer> i: hashTable.entrySet()){
            if(i.getValue() != 0) {
                double prob = ((double) i.getValue()) / count;
                entropy -= prob * Math.log(prob) / Math.log(2);
            }
        }
        return entropy;
    }

    public double calculateInformationGain(int dataColumn){
        String[] dataColumnClasses = getUniqueValues(attributeData[dataColumn]);
        HashMap<String, Integer> classHashTable = getEmptyHash(dataColumnClasses);
        for(String i : attributeData[dataColumn])
            classHashTable.put(i, classHashTable.get(i) + 1);
        double informationGain = targetEntropy;
        for(Map.Entry<String, Integer> i: classHashTable.entrySet())
            informationGain -= ((double) i.getValue())/attributeData[dataColumn].length * calculateEntropy(dataColumn, i.getKey());
        return informationGain;
    }

    public String[] getUniqueValues(String[] array){
        return Arrays.stream(array).distinct().toArray(String[]::new);
    }

    public HashMap<String, Integer> getEmptyHash(String[] array){
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        for(String i : array)
            hashMap.put(i, 0);
        return hashMap;
    }

    public String[][] getAttributeData() {
        return attributeData;
    }

    public String[] getAttributeLabels() {
        return attributeLabels;
    }

    public String getTargetLabel() {
        return targetLabel;
    }

    public String[] getTargetClasses() {
        return targetClasses;
    }

    public String[] getTargetData() {
        return targetData;
    }

    public double getTargetEntropy() {
        return targetEntropy;
    }
}
