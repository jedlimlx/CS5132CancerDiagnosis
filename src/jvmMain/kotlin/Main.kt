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

@Composable
@Preview
fun App() {
    val text = remember { mutableStateOf("") }
    val inputFilePath = remember { mutableStateOf("Path to input file") }
    val outputFilePath = remember { mutableStateOf("Path to output file") }

    MaterialTheme {
        Row(modifier = Modifier.padding(10.dp)) {
            Column(modifier = Modifier.padding(10.dp)) {
                OutlinedTextField(
                    label = { Text("Enter Radius of Nucleus") },
                    value = text.value,
                    onValueChange = { text.value = it },
                    modifier = Modifier.padding(10.dp)
                )

                Button(
                    onClick = {
                        println("placeholder :D")
                    }
                ) {
                    Text("Diagnose")
                }
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
                        println("placeholder :D")
                    }
                ) {
                    Text("Diagnose")
                }
            }
        }
    }
}

fun main() {
    Main.main(arrayOf(""))
    // return application {
    //    Window(title="Breast Cancer Diagnosis", onCloseRequest = ::exitApplication) {
    //        App()
    //    }
    // }
}
