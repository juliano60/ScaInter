package com.nanoporetech.scainter.ui.consultation

import android.Manifest
import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.ui.utils.BarcodeAnalyzer

sealed interface BarScanState {
    data object Idle : BarScanState
    data class ScanSuccess(val value: String) : BarScanState
    data class Error(val error: String) : BarScanState
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CodeScannerScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var scanState by remember { mutableStateOf<BarScanState>(BarScanState.Idle) }
    var lastValue by remember { mutableStateOf<String?>(null) }
    val permission = rememberPermissionState(Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        if (!permission.status.isGranted) permission.launchPermissionRequest()
    }

    Box(modifier) {
        if (permission.status.isGranted) {
            CameraPreview(
                context = context,
                lifecycleOwner = lifecycleOwner,
                onBarcode = { value ->
                    // de-dupe so it doesn't fire 30 times
                    if (value != lastValue) {
                        lastValue = value
                        scanState = BarScanState.ScanSuccess(value)
                    }
                },
                onError = { msg ->
                    scanState = BarScanState.Error(msg)
                }
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Camera permission is required to scan barcodes.")
                Spacer(Modifier.height(12.dp))
                Button(onClick = { permission.launchPermissionRequest() }) {
                    Text("Grant permission")
                }
            }
        }

        // Simple overlay UI based on scan state
        when (val s = scanState) {
            BarScanState.Idle -> OverlayHint(stringResource(R.string.camera_overlay_hint))
            is BarScanState.ScanSuccess -> OverlaySuccess(
                value = s.value,
                onDone = {
                    // reset and keep scanning
                    scanState = BarScanState.Idle
                    lastValue = null
                }
            )
            is BarScanState.Error -> OverlayError(s.error)
        }
    }
}

@Composable
private fun OverlayHint(text: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Surface(tonalElevation = 6.dp, modifier = Modifier.padding(16.dp)) {
            Text(text, modifier = Modifier.padding(12.dp))
        }
    }
}

@Composable
private fun OverlaySuccess(value: String, onDone: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Surface(tonalElevation = 6.dp, modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Scanned: $value", modifier = Modifier.weight(1f))
                Spacer(Modifier.width(12.dp))
                Button(onClick = onDone) { Text(stringResource(R.string.close_button)) }
            }
        }
    }
}

@Composable
private fun OverlayError(msg: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Surface(color = MaterialTheme.colorScheme.errorContainer) {
            Text("Error: $msg", modifier = Modifier.padding(12.dp))
        }
    }
}

@Composable
private fun CameraPreview(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    onBarcode: (String) -> Unit,
    onError: (String) -> Unit,
) {
    val cameraExecutor = remember { ContextCompat.getMainExecutor(context) }
    val analysisExecutor = remember { java.util.concurrent.Executors.newSingleThreadExecutor() }
    val analyzer = remember {
        BarcodeAnalyzer(
            onBarcode = onBarcode
        )
    }
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            cameraProvider?.unbindAll()
            analyzer.shutdown()
            analysisExecutor.shutdown()
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            PreviewView(ctx).apply {
                scaleType = PreviewView.ScaleType.FIT_CENTER
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
        },
        update = { previewView ->
            val providerFuture = ProcessCameraProvider.getInstance(context)

            providerFuture.addListener({
                val provider = providerFuture.get()
                cameraProvider = provider

                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                val analysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(analysisExecutor, analyzer)
                    }

                try {
                    provider.unbindAll()
                    provider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        analysis
                    )
                } catch (t: Throwable) {
                    onError(t.message ?: "Failed to bind camera use cases")
                }
            }, cameraExecutor)
        }
    )
}