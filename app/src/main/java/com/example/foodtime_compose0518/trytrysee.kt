import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun TextFieldWithDropdown(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    setValue: (TextFieldValue) -> Unit,
    onDismissRequest: () -> Unit,
    dropDownExpanded: Boolean,
    list: List<String>,
    label: String = ""
) {
    Box(modifier) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        onDismissRequest()
                    }
                },
            value = value,
            onValueChange = setValue,
            label = { Text(label) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.DarkGray,
                focusedLabelColor = Color.DarkGray
            )
        )
        if (dropDownExpanded) {
            DropdownMenu(
                expanded = dropDownExpanded,
                properties = PopupProperties(
                    focusable = false,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                ),
                onDismissRequest = onDismissRequest
            ) {
                val filteredList = list.filter {
                    it.contains(value.text, ignoreCase = true)
                }
                if (filteredList.isEmpty()) {
//                    DropdownMenuItem(onClick = {}) {
//                        Text("No options available")
//                    }
                } else {
                    filteredList.forEach { text ->
                        DropdownMenuItem(onClick = {
                            setValue(
                                TextFieldValue(
                                    text,
                                    TextRange(text.length)
                                )
                            )
                        },) {
                            Text(text = text)
                        }
                    }
                }
            }
        }
    }
}




val all = listOf("aaa", "baa", "aab", "abb", "bab")

val dropDownOptions = mutableStateOf(listOf<String>())
val textFieldValue = mutableStateOf(TextFieldValue())
val dropDownExpanded = mutableStateOf(false)
fun onDropdownDismissRequest() {
    dropDownExpanded.value = false
}

fun onValueChanged(value: TextFieldValue) {
    dropDownExpanded.value = true
    textFieldValue.value = value
    dropDownOptions.value = all.filter { it.startsWith(value.text) && it != value.text }.take(3)
}




@Preview
@Composable
fun TextFieldWithDropdownUsage() {
    TextFieldWithDropdown(
        modifier = Modifier.fillMaxWidth(),
        value = textFieldValue.value,
        setValue = ::onValueChanged,
        onDismissRequest = ::onDropdownDismissRequest,
        dropDownExpanded = dropDownExpanded.value,
        list = dropDownOptions.value,
        label = "Label"
    )
}



fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}