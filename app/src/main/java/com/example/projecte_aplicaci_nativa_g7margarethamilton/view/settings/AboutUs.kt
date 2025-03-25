package com.example.projecte_aplicaci_nativa_g7margarethamilton.view.settings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.projecte_aplicaci_nativa_g7margarethamilton.R
import kotlinx.serialization.Serializable



@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AboutUsView(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val backgroundColor = Color(0xFFF8F8F8)

    // Contenido del carrusel
    val images = listOf(
        R.drawable.profile_avatar_placeholder_large,
        R.drawable.profile_avatar_placeholder_large,
        R.drawable.profile_avatar_placeholder_large,
        R.drawable.profile_avatar_placeholder_large
    )

    val texts = listOf(
        "Flow2Day es una aplicación innovadora diseñada para ayudarte a gestionar tu tiempo de manera eficiente.",
        "Nuestra misión es simplificar tu día a día con herramientas intuitivas y efectivas.",
        "Un equipo apasionado por la tecnología y la productividad.",
        "Comprometidos con la excelencia y la satisfacción del usuario."
    )

    val pagerState = rememberPagerState(pageCount = { images.size })
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Sobre nosaltres",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor,
                    titleContentColor = Color.Black
                ),
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Flow2Day!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E3B4E),
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Carrusel de imágenes
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) { page ->
                Image(
                    painter = painterResource(id = images[page]),
                    contentDescription = "Imagen ${page + 1}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
            }

            // Indicadores de página
            Row(
                Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(images.size) { iteration ->
                    val color = if (currentPage == iteration) Color(0xFF2E3B4E) else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)
                    )
                }
            }

            // Texto descriptivo
            Text(
                text = texts[currentPage],
                fontSize = 16.sp,
                color = Color(0xFF2E3B4E),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AboutUsPreview() {
    val navController = rememberNavController()
    AboutUsView(navController)
}