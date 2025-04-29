package com.example.projecte_aplicaci_nativa_g7margarethamilton.view.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactUsView(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val backgroundColor = Color(0xFFF8F8F8)

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(top = 40.dp),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Contáctanos",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onBackground                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                scrollBehavior = scrollBehavior
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "¿Tienes algún problema?",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque sollicitudin lacus nec lectus auctor, eu mattis lectus ultricies. Integer aliquam ligula nibh, vel imperdiet nisl dignissim et. Morbi varius ultricies mauris, et auctor diam tincidunt eu. Nullam molestie odio risus, vitae efficitur ante malesuada a.",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.padding(8.dp))

            var text by remember { mutableStateOf("") }
            var maxChars = 140
            var isTextValid = text.length <= maxChars && text.isNotEmpty()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = Color.Black),
                    maxLines = 5,
                    singleLine = false,
                    decorationBox = { innerTextField ->
                        if (text.isEmpty()) {
                            Text("Escribe aquí...", color = Color.Gray)
                        }
                        innerTextField()
                    }
                )
                Text(
                    text = "${text.length}/$maxChars",
                    color = if (text.length > maxChars) Color.Red else Color.Gray,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.padding(8.dp))

            Button(
                onClick = {/*TODO: Enviar el mensaje */ },
                modifier = Modifier
                    .height(50.dp)
                .align(Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E3B4E)
                ),
                shape = MaterialTheme.shapes.small,
                enabled = isTextValid
            ) {
                Text("Enviar")
            }
        }

        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ContactUsViewPreview() {
    val navController = rememberNavController()
    ContactUsView(navController)
}


