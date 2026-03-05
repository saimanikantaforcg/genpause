package com.genpause.app.ui.screens

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.genpause.app.data.entity.TargetAppEntity
import com.genpause.app.ui.components.GradientButton
import com.genpause.app.ui.theme.*

data class InstalledApp(
    val packageName: String,
    val label: String,
    val isSystem: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppPickerScreen(
    onDone: (List<TargetAppEntity>) -> Unit
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    val selectedApps = remember { mutableStateMapOf<String, Boolean>() }

    // Load installed apps
    val installedApps = remember {
        val pm = context.packageManager
        pm.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { app ->
                // Only show launchable, non-system apps
                pm.getLaunchIntentForPackage(app.packageName) != null &&
                app.packageName != context.packageName
            }
            .map { app ->
                InstalledApp(
                    packageName = app.packageName,
                    label = app.loadLabel(pm).toString(),
                    isSystem = (app.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                )
            }
            .sortedBy { it.label.lowercase() }
    }

    // Pre-select popular distracting apps
    LaunchedEffect(Unit) {
        val popularApps = listOf(
            "com.instagram.android",
            "com.google.android.youtube",
            "com.twitter.android",
            "com.zhiliaoapp.musically",  // TikTok
            "com.snapchat.android",
            "com.facebook.katana",
            "com.reddit.frontpage"
        )
        installedApps.forEach { app ->
            if (app.packageName in popularApps) {
                selectedApps[app.packageName] = true
            }
        }
    }

    val filteredApps = if (searchQuery.isBlank()) {
        installedApps
    } else {
        installedApps.filter {
            it.label.contains(searchQuery, ignoreCase = true) ||
            it.packageName.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ZenBackground)
    ) {
        // Header
        Column(
            modifier = Modifier.padding(24.dp, 32.dp, 24.dp, 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Kaunsi apps rokni hai? 🎯",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = ZenTextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${selectedApps.count { it.value }} apps selected",
                color = ZenPrimary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Search
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search apps…", color = ZenTextSecondary) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = ZenTextSecondary)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ZenPrimary,
                    unfocusedBorderColor = ZenDivider,
                    focusedTextColor = ZenTextPrimary,
                    unfocusedTextColor = ZenTextPrimary,
                    cursorColor = ZenPrimary,
                    focusedContainerColor = ZenSurface,
                    unfocusedContainerColor = ZenSurface
                ),
                singleLine = true
            )
        }

        // App list
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(filteredApps, key = { it.packageName }) { app ->
                val isSelected = selectedApps[app.packageName] == true
                AppPickerItem(
                    app = app,
                    isSelected = isSelected,
                    onToggle = {
                        selectedApps[app.packageName] = !isSelected
                    }
                )
            }
        }

        // Done button
        Box(modifier = Modifier.padding(24.dp)) {
            GradientButton(
                text = "Done ✅",
                onClick = {
                    val selected = selectedApps
                        .filter { it.value }
                        .map { (pkg, _) ->
                            val label = installedApps.find { it.packageName == pkg }?.label ?: pkg
                            TargetAppEntity(
                                packageName = pkg,
                                displayName = label
                            )
                        }
                    onDone(selected)
                },
                enabled = selectedApps.any { it.value }
            )
        }
    }
}

@Composable
private fun AppPickerItem(
    app: InstalledApp,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) ZenElevatedSurface else ZenSurface.copy(alpha = 0.5f))
            .clickable(onClick = onToggle)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // App icon placeholder
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = if (isSelected) ZenPrimary.copy(alpha = 0.2f) else ZenDivider,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = app.label.first().uppercase(),
                color = if (isSelected) ZenPrimary else ZenTextSecondary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        // App info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = app.label,
                color = ZenTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = app.packageName,
                color = ZenTextSecondary,
                fontSize = 11.sp
            )
        }

        // Checkbox
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(
                checkedColor = ZenPrimary,
                uncheckedColor = ZenDivider,
                checkmarkColor = ZenBackground
            )
        )
    }
}
