package com.example.projecte_aplicaci_nativa_g7margarethamilton.view

import TermsModal
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.path
import com.example.projecte_aplicaci_nativa_g7margarethamilton.Routes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var showTermsModal by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(top = 40.dp),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Ajustes",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onSecondary,
                ),
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = { BottomNavBar(navController) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            SettingOption(
                title = "Terminis i condicions",
                icon = Article,
                onClick = { showTermsModal = true }
            )
            if (showTermsModal) {
                TermsModal(onDismiss = { showTermsModal = false })
            }
            
            SettingOption(
                title = "Sobre nosaltres",
                icon = Icons.Default.Info,
                onClick = { navController.navigate(Routes.AboutUs.route) }
            )
            
            SettingOption(
                title = "Contacta",
                icon = Icons.Default.Email,
                onClick = { /* TODO: Navigate to Contact */ }
            )
            
            SettingOption(
                title = "Config. Perfil",
                icon = Icons.Default.Settings,
                onClick = { /* TODO: Navigate to Profile Config */ }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingOption(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSecondary,
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Ir a $title",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsViewPreview() {
    val navController = rememberNavController()
    SettingsView(navController)
}


public val Article: ImageVector
    get() {
        if (_Article != null) {
            return _Article!!
        }
        _Article = ImageVector.Builder(
            name = "Article",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(280f, 680f)
                horizontalLineToRelative(280f)
                verticalLineToRelative(-80f)
                horizontalLineTo(280f)
                close()
                moveToRelative(0f, -160f)
                horizontalLineToRelative(400f)
                verticalLineToRelative(-80f)
                horizontalLineTo(280f)
                close()
                moveToRelative(0f, -160f)
                horizontalLineToRelative(400f)
                verticalLineToRelative(-80f)
                horizontalLineTo(280f)
                close()
                moveToRelative(-80f, 480f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(120f, 760f)
                verticalLineToRelative(-560f)
                quadToRelative(0f, -33f, 23.5f, -56.5f)
                reflectiveQuadTo(200f, 120f)
                horizontalLineToRelative(560f)
                quadToRelative(33f, 0f, 56.5f, 23.5f)
                reflectiveQuadTo(840f, 200f)
                verticalLineToRelative(560f)
                quadToRelative(0f, 33f, -23.5f, 56.5f)
                reflectiveQuadTo(760f, 840f)
                close()
                moveToRelative(0f, -80f)
                horizontalLineToRelative(560f)
                verticalLineToRelative(-560f)
                horizontalLineTo(200f)
                close()
                moveToRelative(0f, -560f)
                verticalLineToRelative(560f)
                close()
            }
        }.build()
        return _Article!!
    }

private var _Article: ImageVector? = null
