import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DecisionTree {
    public double calculateInformationGain(DataFrame dataFrame, String label){
        int dataColumn = dataFrame.getColumnFromLabel(label);
        String[] dataColumnClasses = getUniqueValues(dataFrame.getData()[dataColumn]);
        HashMap<String, Integer> classHashTable = getEmptyHash(dataColumnClasses);
        for(String i : dataFrame.getData()[dataColumn])
            classHashTable.put(i, classHashTable.get(i) + 1);
        double informationGain = dataFrame.getTargetEntropy();
        for(Map.Entry<String, Integer> i: classHashTable.entrySet())
            informationGain -= ((double) i.getValue())/dataFrame.getSampleCount() * calculateEntropy(dataFrame, i.getKey());
        return informationGain;
    }
    public double calculateEntropy(DataFrame dataFrame, String label){
        HashMap<String, Integer> hashTable = getEmptyHash(dataFrame.getTargetData());
        int dataColumn = dataFrame.getColumnFromLabel(label);
        int count = 0;
        for(int i = 0; i < dataFrame.getSampleCount(); ++i) {
            if (dataFrame.getData()[dataColumn][i].equals(label)) {
                hashTable.put(dataFrame.getTargetData()[i], hashTable.get(dataFrame.getTargetData()[i]) + 1);
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

    public String[] getUniqueValues(String[] array){
        return Arrays.stream(array).distinct().toArray(String[]::new);
    }

    public HashMap<String, Integer> getEmptyHash(String[] array){
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        for(String i : array)
            hashMap.put(i, 0);
        return hashMap;
    }
}
