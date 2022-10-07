import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.FileDialog
import java.io.File
import java.io.FileInputStream
import java.io.IOException

@Composable
@Preview
fun App(tree: RandomForestClassifier) {
    val perimeter = remember { mutableStateOf("") }
    val area = remember { mutableStateOf("") }
    val uniformity = remember { mutableStateOf("") }
    val homogenity = remember { mutableStateOf("") }

    val prediction = remember { mutableStateOf("") }

    val inputFilePath = remember { mutableStateOf("Path to input file") }
    val outputFilePath = remember { mutableStateOf("Path to output file") }

    MaterialTheme {
        Row(modifier = Modifier.padding(10.dp)) {
            Column(modifier = Modifier.padding(10.dp)) {
                OutlinedTextField(
                    label = { Text("Enter Worst Perimeter of Nucleus") },
                    value = perimeter.value,
                    onValueChange = { perimeter.value = it },
                    modifier = Modifier.padding(10.dp)
                )

                OutlinedTextField(
                    label = { Text("Enter Worst Area of Nucleus") },
                    value = area.value,
                    onValueChange = { area.value = it },
                    modifier = Modifier.padding(10.dp)
                )

                OutlinedTextField(
                    label = { Text("Enter Uniformity of Nuclei") },
                    value = uniformity.value,
                    onValueChange = { uniformity.value = it },
                    modifier = Modifier.padding(10.dp)
                )

                OutlinedTextField(
                    label = { Text("Enter Homogenity of Nuclei") },
                    value = homogenity.value,
                    onValueChange = { homogenity.value = it },
                    modifier = Modifier.padding(10.dp)
                )

                Button(
                    onClick = {
                        prediction.value = if (tree.predict(arrayOf(doubleArrayOf(
                                perimeter.value.toDouble(),
                                area.value.toDouble(),
                                uniformity.value.toDouble(),
                                homogenity.value.toDouble()
                        )))[0] == 0) "Benign" else "Malignant"
                    }
                ) {
                    Text("Diagnose")
                }

                Text(text=prediction.value)
            }

            Divider(
                modifier = Modifier.fillMaxHeight().width(2.dp)
            )

            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row {
                    Button(
                        onClick = {
                            val dialog = FileDialog(ComposeWindow(), "Open CSV", FileDialog.LOAD)
                            dialog.isVisible = true
                            inputFilePath.value = dialog.directory + dialog.file
                        }
                    ) {
                        Text("Load CSV")
                    }

                    Text(inputFilePath.value, modifier = Modifier.padding(15.dp))
                }

                Row {
                    Button(
                        onClick = {
                            val dialog = FileDialog(ComposeWindow(), "Save CSV", FileDialog.SAVE)
                            dialog.isVisible = true
                            outputFilePath.value = dialog.directory + dialog.file
                        }
                    ) {
                        Text("Save CSV")
                    }

                    Text(outputFilePath.value, modifier = Modifier.padding(15.dp))
                }

                Button(
                    onClick = {
                        try {
                            val reader = CSVReader(null, arrayOf(), FileInputStream(File(inputFilePath.value)))

                            // Generating predictions
                            val predictions = tree.predict(reader.readCsvToXy().a)

                            // Writing the predictions
                            val outputFile = File(outputFilePath.value)
                            outputFile.writeText(predictions.joinToString("\n"))
                        } catch (exception: IOException) { }
                    }
                ) {
                    Text("Diagnose")
                }
            }
        }
    }
}

fun trainTree(): RandomForestClassifier? {
    // write your code here
    val targetColName = "diagnosis"
    val colsToSkip = arrayOf("id", "id2")
    val reader = CSVReader(targetColName, colsToSkip, object {}.javaClass.getResourceAsStream("data.csv"))
    reader.readCsvToXy()

    val results = Helper.train_test_split(reader.X, reader.y, 0.2)
    if (results == null) {
        println("Train Test Split is null")
        return null
    }

    val clf = DecisionTree(100)
    clf.fit(results.X_train, results.y_train)

    var y_pred = clf.predict(results.X_test)
    println("Accuracy: " + Helper.accuracy_score(results.y_test, y_pred))

    val rClf = RandomForestClassifier()
    rClf.fit(results.X_train, results.y_train)
    println("fit done")

    y_pred = rClf.predict(results.X_test)
    println("Accuracy: " + Helper.accuracy_score(results.y_test, y_pred))
    return rClf
}

fun main() {
    val tree = trainTree()!!
    return application {
       Window(title="Breast Cancer Diagnosis", onCloseRequest = ::exitApplication) {
           App(tree)
       }
    }
}
