import java.lang.Math;
import java.util.Map;

public class DecisionTree {

    public double getEntropy(DataColumn dataColumn){
        double entropy = 0;
        for(Map.Entry<String, Double> i : dataColumn.getCounts().entrySet())
            entropy -= i.getValue()/dataColumn.getSize() * Math.log(i.getValue()/dataColumn.getSize()) / Math.log(2);
        return entropy;
    }


}
