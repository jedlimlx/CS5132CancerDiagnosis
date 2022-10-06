public class Main {
    public static void main(String[] args) {
	    // write your code here
        String targetColName = "diagnosis";
        String[] colsToSkip = {"id"};

        CSVReader reader = new CSVReader(targetColName, colsToSkip);
        reader.readCsvToXy();
        TrainTest results = Helper.train_test_split(reader.X, reader.y, 0.2);
        if (results == null) {
            System.out.println("Train Test Split is null");
            return;
        }

        DecisionTree clf = new DecisionTree(100);
        clf.fit(results.X_train, results.y_train);
        int[] y_pred = clf.predict(results.X_test);
        System.out.println("Decision Tree Accuracy: " + Helper.accuracy_score(results.y_test, y_pred));

        RandomForestClassifier rClf = new RandomForestClassifier();
        rClf.fit(results.X_train, results.y_train);
        System.out.println("fit done");
        y_pred = rClf.predict(results.X_test);
        System.out.println("Random Forest Classifier Accuracy: " + Helper.accuracy_score(results.y_test, y_pred));
    }
}
