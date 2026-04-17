package com.example.samsung_work_schedule.feature.setting

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.samsung_work_schedule.R
import com.example.samsung_work_schedule.theme.ScheduleTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    onBackClick: () -> Unit = {},
    viewModel: SettingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showPermissionDialog by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.toggleNotifications(true)
        } else {
            showPermissionDialog = true
        }
    }

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text(stringResource(R.string.notification_permission_title)) },
            text = { Text(stringResource(R.string.notification_permission_desc)) },
            confirmButton = {
                TextButton(onClick = {
                    showPermissionDialog = false
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }) {
                    Text(stringResource(R.string.go_to_settings))
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings),
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = ScheduleTheme.colors.textColor3
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = ScheduleTheme.colors.iconColor1
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ScheduleTheme.colors.background2)
            )
        },
        containerColor = ScheduleTheme.colors.background2
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                shape = RoundedCornerShape(28.dp),
                color = ScheduleTheme.colors.background1,
                modifier = Modifier.fillMaxWidth()
            ) {
                SettingItem(
                    icon = Icons.Outlined.DarkMode,
                    title = stringResource(R.string.dark_mode),
                    description = stringResource(R.string.dark_mode_desc),
                    trailingContent = {
                        Switch(
                            checked = uiState.isDarkModeEnabled,
                            onCheckedChange = { viewModel.toggleDarkMode(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ScheduleTheme.colors.iconColor1,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color.LightGray,
                                uncheckedBorderColor = Color.Transparent
                            )
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                shape = RoundedCornerShape(28.dp),
                color = ScheduleTheme.colors.background1,
                modifier = Modifier.fillMaxWidth()
            ) {
                SettingItem(
                    icon = Icons.Outlined.NotificationsNone,
                    title = stringResource(R.string.notifications),
                    description = stringResource(R.string.notifications_desc),
                    trailingContent = {
                        Switch(
                            checked = uiState.isNotificationsEnabled,
                            onCheckedChange = { enabled ->
                                if (enabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    val hasPermission = ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    ) == PackageManager.PERMISSION_GRANTED

                                    if (hasPermission) {
                                        viewModel.toggleNotifications(true)
                                    } else {
                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    }
                                } else {
                                    viewModel.toggleNotifications(enabled)
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ScheduleTheme.colors.iconColor1,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color.LightGray,
                                uncheckedBorderColor = Color.Transparent
                            )
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider(color = ScheduleTheme.colors.surfaceColor1)
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.version),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ScheduleTheme.colors.textColor3
                    )
                )

                Surface(
                    color = ScheduleTheme.colors.surfaceColor1,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.version_value),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = ScheduleTheme.colors.textColor4,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    description: String,
    trailingContent: @Composable () -> Unit,
    onClick: (() -> Unit)? = null
) {
    Surface(
        onClick = { onClick?.invoke() },
        color = Color.Transparent,
        enabled = onClick != null
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(16.dp),
                color = ScheduleTheme.colors.surfaceColor1
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = ScheduleTheme.colors.iconColor1,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ScheduleTheme.colors.textColor8
                    )
                )
                Text(
                    text = description,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = ScheduleTheme.colors.textColor4
                    )
                )
            }

            trailingContent()
        }
    }
}