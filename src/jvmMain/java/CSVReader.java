import java.io.*;
import java.util.*;
import java.util.stream.Stream;

public class CSVReader {
    public String targetColName;
    public String[] colsToSkip;
    public double[][] X;
    public int[] y;

    private int targetIdx;
    private ArrayList<Integer> skipColsIdx;

    public CSVReader(String targetColName, String[] colsToSkip) {
        this.targetColName = targetColName;
        this.colsToSkip = colsToSkip;
        targetIdx = -1;
        skipColsIdx = new ArrayList<>();
        y = null;
        X = null;
    }

    public Pair<double[][], int[]> readCsvToXy()
    {
        String line;
        ArrayList<ArrayList<Double>> X_list = new ArrayList<>();
        ArrayList<String> y_list = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(Main.class.getResource("data.csv").openStream()));

            // read headings
            String firstLine = br.readLine().replaceAll("\"", "");
            if (firstLine.isEmpty()) {
                System.out.println("Error: No content");
                return null;
            }
            String[] headings = firstLine.split(",");

            // find the columns to skip and the target column indices
            targetIdx = Arrays.asList(headings).indexOf(targetColName);
            if (targetIdx == -1) {
                System.out.println("Error: Target column is not found");
                return null;
            }
            for (String colName : colsToSkip) {
                int skipIdx = Arrays.asList(headings).indexOf(colName);
                if (skipIdx == -1) {
                    System.out.println("Error: A column to skip ("+ colName +") is not found");
                    return null;
                }
                skipColsIdx.add(skipIdx);
            }

            // read in data, build X and y
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                ArrayList<Double> X_rowData = new ArrayList<>();
                for (int i = 0; i < values.length; ++i) {
                    if (skipColsIdx.contains(i)) continue;
                    if (i == targetIdx) {
                        y_list.add(values[i]);
                    }
                    else {
                        X_rowData.add(Double.parseDouble(values[i]));
                    }
                }
                if (!X_rowData.isEmpty()) {
                    X_list.add(X_rowData);
                }
            }

            // convert y_list to y
            y = new int[y_list.size()];
            Set<String> uniqueTargetSet = new HashSet<>(y_list);
            List<String> uniqueTargetArr = new ArrayList<>(uniqueTargetSet);
            for (int i = 0; i < uniqueTargetArr.size(); ++i) {
                System.out.println("Mapping " + uniqueTargetArr.get(i) + " as " + i);
            }
            for (int j = 0; j < y_list.size(); ++j) {
                y[j] = uniqueTargetArr.indexOf(y_list.get(j));
            }

            // convert X_list to X
            X = new double[X_list.size()][];
            Double[] blankArray = new Double[0];
            for (int i = 0; i < X_list.size(); ++i) {
                Double[] temp = X_list.get(i).toArray(blankArray);
                X[i] = Stream.of(temp).mapToDouble(Double::doubleValue).toArray();
            }

        } catch (FileNotFoundException e) {
            System.out.println("File No Found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO Exception");
            e.printStackTrace();
        }
        return new Pair<>(X, y);
    }
}
