package com.nanoporetech.scainter.ui.qrcode

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.nanoporetech.scainter.BuildConfig
import com.nanoporetech.scainter.R
import java.util.concurrent.Executors

sealed interface BarScanState {
    data object Idle : BarScanState
    data class ScanSuccess(val value: String) : BarScanState
    data class Error(val error: String) : BarScanState
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CodeScannerScreen(
    modifier: Modifier = Modifier,
    onScanResult: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var scanState by remember { mutableStateOf<BarScanState>(BarScanState.Idle) }
    var lastValue by remember { mutableStateOf<String?>(null) }
    var testBarcode by remember { mutableStateOf("") }
    val permission = rememberPermissionState(Manifest.permission.CAMERA)

    fun handleBarcode(value: String) {
        if (value != lastValue) {
            lastValue = value
            onScanResult(value)
        }
    }

    LaunchedEffect(Unit) {
        if (!permission.status.isGranted) permission.launchPermissionRequest()
    }

    Box(modifier) {
        // CAMERA PREVIEW

        if (permission.status.isGranted) {
            CameraPreview(
                context = context,
                lifecycleOwner = lifecycleOwner,
                onBarcode = { value ->
                    // de-dupe so it doesn't fire 30 times
                    if (value != lastValue) {
                        lastValue = value
                        scanState = BarScanState.ScanSuccess(value)
                        onScanResult(value)
                    }
                },
                onError = { msg ->
                    scanState = BarScanState.Error(msg)
                }
            )
            ScanWindowOverlay()
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.camera_permissions_prompt))
                Spacer(Modifier.height(12.dp))
                Button(onClick = { permission.launchPermissionRequest() }) {
                    Text(stringResource(R.string.camera_permissions_grant))
                }
            }
        }

        // DEBUG INPUT FOR EMULATOR TESTING

        /*if (BuildConfig.DEBUG) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = testBarcode,
                        onValueChange = {
                            testBarcode = it
                        },
                        label = {
                            Text("Test QR code")
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.8f),
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.6f),
                            cursorColor = Color.White,
                            ),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(Modifier.width(8.dp))

                    Button(
                        onClick = {
                            handleBarcode(testBarcode)
                        },
                        enabled = testBarcode.isNotBlank()
                    ) {
                        Text("Scan")
                    }
                }
            }*/
        //}

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
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
        Surface(
            //tonalElevation = 6.dp,
            color = Color.Black.copy(alpha = 0.25f),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(24.dp)
        ) {
            Text(text,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(12.dp))
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

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun ScanWindowOverlay(
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()
        val scanWidth = width * 0.8f
        val scanHeight = height * 0.5f
        val left = (width - scanWidth) / 2f
        val top = (height - scanHeight) / 2f
        val right = left + scanWidth
        val bottom = top + scanHeight

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val overlayColor = Color.Black.copy(alpha = 0.6f)

            // TOP
            drawRect(
                color = overlayColor,
                topLeft = Offset(0f, 0f),
                size = Size(width, top)
            )

            // LEFT
            drawRect(
                color = overlayColor,
                topLeft = Offset(0f, top),
                size = Size(left, scanHeight)
            )

            // RIGHT
            drawRect(
                color = overlayColor,
                topLeft = Offset(right, top),
                size = Size(width - right, scanHeight)
            )

            // BOTTOM
            drawRect(
                color = overlayColor,
                topLeft = Offset(0f, bottom),
                size = Size(width, height - bottom)
            )

            // Scan window border

            drawRoundRect(
                color = Color.White,
                topLeft = Offset(left, top),
                size = Size(scanWidth, scanHeight),
                cornerRadius = CornerRadius(24f, 24f),
                style = Stroke(width = 4.dp.toPx())
            )
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
    val analysisExecutor = remember { Executors.newSingleThreadExecutor() }
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