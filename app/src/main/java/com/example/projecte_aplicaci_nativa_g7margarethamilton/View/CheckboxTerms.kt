import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton

@Composable
fun Terms() {
    var showTermsModal by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        Text(
            text = "Al registrar-te estàs acceptant els ",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 8.dp)
        )
        Text(
            text = "Terminis i Condicions",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.clickable { showTermsModal = true }
        )
    }

    if (showTermsModal) {
        TermsModal(onDismiss = { showTermsModal = false })
    }
}

@Composable
fun TermsModal(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Terminis i Condicions",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = """
                        1. Aceptación de los términos
                        Al acceder y utilizar esta aplicación, usted acepta estar sujeto a estos términos y condiciones.

                        2. Uso de la aplicación
                        Esta aplicación está destinada únicamente a fines de registro y uso personal.

                        3. Privacidad
                        Nos comprometemos a proteger su privacidad y datos personales de acuerdo con nuestra política de privacidad.

                        4. Modificaciones
                        Nos reservamos el derecho de modificar estos términos en cualquier momento.

                        5. Limitación de responsabilidad
                        No nos hacemos responsables de cualquier daño directo o indirecto que pueda surgir del uso de esta aplicación.
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Aceptar")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}
