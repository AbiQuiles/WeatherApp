package com.example.weather.screens.location.permission

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weather.ui.theme.WeatherTheme

@Composable
fun LocationPermissionPrompt(
    locationViewModel: LocationPermissionPromptViewModel = hiltViewModel(),
    onPermissionGrantedContext: @Composable () -> Unit
) {
    val context = LocalContext.current
    var permissionState by remember { mutableStateOf<PermissionStatus>(PermissionStatus.Undetermined) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            permissionState = if (isGranted) PermissionStatus.Granted else PermissionStatus.Denied
        }
    )

    // Handle the result of the permission request
    LaunchedEffect(permissionState) {
        if (permissionState == PermissionStatus.Granted) {
            // Permission granted! Tell the ViewModel to start fetching the location.
            locationViewModel.onPermissionGranted(context as Activity)
        }
    }

    // Request permission on first composition
    LaunchedEffect(Unit) {
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    // Pass the state down to the stateless content composable
    LocationPermissionContent(
        permissionStatus = permissionState,
        onPermissionGrantedContext = onPermissionGrantedContext,
        onRequestPermission = {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    )
}

@Composable
private fun LocationPermissionContent(
    permissionStatus: PermissionStatus,
    onRequestPermission: () -> Unit,
    onPermissionGrantedContext: @Composable () -> Unit
) {
    val context = LocalContext.current

    Scaffold { innerPadding ->
        when (permissionStatus) {
            PermissionStatus.Undetermined -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            }

            PermissionStatus.Denied -> {
                val shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )

                PermissionDeniedContext(
                    shouldShowRationale = shouldShowRationale,
                    onRequestPermission = onRequestPermission,
                    onOpenSettings = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", context.packageName, null)

                        intent.data = uri
                        context.startActivity(intent)
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            PermissionStatus.Granted -> {
                onPermissionGrantedContext()
            }
        }
    }
}

@Composable
private fun PermissionDeniedContext(
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        val permissionMsg = if (shouldShowRationale) {
            "Location permission is needed to provide weather data for your current area."
        } else {
            "Location permission was permanently denied. To use this feature, please enable it in the app settings."
        }
        Text(
            text = permissionMsg,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = {
                if (shouldShowRationale) {
                    onRequestPermission()
                } else {
                    onOpenSettings()
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            val buttonText = if (shouldShowRationale) "Grant Permission" else "Open Settings"
            Text(buttonText)
        }
    }
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "You can still search for location weather",
            textAlign = TextAlign.Center,
        )
    }
}


// --- Previews ---

@Preview(name = "Loading State", showBackground = true)
@Composable
fun LocationPermissionContentLoadingPreview() {
    WeatherTheme {
        LocationPermissionContent(
            permissionStatus = PermissionStatus.Undetermined,
            onRequestPermission = {},
            onPermissionGrantedContext = {}
        )
    }
}

@Preview(name = "Denied State - Rationale", showBackground = true)
@Composable
fun LocationPermissionContentDeniedPreview() {
    WeatherTheme {
        // This preview simulates the case where the user has denied once.
        LocationPermissionContent(
            permissionStatus = PermissionStatus.Denied,
            onRequestPermission = {},
            onPermissionGrantedContext = {}
        )
    }
}

@Preview(name = "Granted State", showBackground = true)
@Composable
fun LocationPermissionContentGrantedPreview() {
    WeatherTheme {
        LocationPermissionContent(
            permissionStatus = PermissionStatus.Granted,
            onRequestPermission = {},
            onPermissionGrantedContext = {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Permission Granted! Showing Main Content.")
                }
            }
        )
    }
}

