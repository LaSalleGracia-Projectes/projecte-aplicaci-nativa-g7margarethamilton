package com.example.projecte_aplicaci_nativa_g7margarethamilton.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.R
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.UserViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.setLocale

@Composable
fun Terms() {
    var showTermsModal by remember { mutableStateOf(false) }
    val viewModel: UserViewModel = viewModel()
    val context = LocalContext.current
    val lang = viewModel.getSavedLanguage(context)
    val localizedContext = context.setLocale(lang)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = localizedContext.getString(R.string.terms_and_conditions_view_title),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 8.dp)
        )
        Text(
            text = localizedContext.getString(R.string.terms_and_conditions_view_accept),
            modifier = Modifier.clickable { showTermsModal = true }
        )
    }

    if (showTermsModal) {
        TermsModal(onDismiss = { showTermsModal = false }, viewModel = viewModel)
    }
}

@Composable
fun TermsModal(onDismiss: () -> Unit, viewModel: UserViewModel) {
    val context = LocalContext.current
    val lang = viewModel.getSavedLanguage(context)
    val localizedContext = context.setLocale(lang)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = localizedContext.getString(R.string.terms_and_conditions_view_title),
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
                    text = localizedContext.getString(R.string.terms_and_conditions_view_content),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(localizedContext.getString(R.string.terms_and_conditions_view_accept))
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}
