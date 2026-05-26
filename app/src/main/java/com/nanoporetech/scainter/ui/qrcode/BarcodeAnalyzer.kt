package com.nanoporetech.scainter.ui.qrcode

import android.graphics.Rect
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer(
    private val onBarcode: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private var isProcessing = false
    private var lastScanned: String? = null

    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_CODE_128,
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_EAN_8
            )
            .build()
    )

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        if (isProcessing) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image ?: run {
            imageProxy.close()
            return
        }

        isProcessing = true

        val scanArea = Rect(
            (imageProxy.width * 0.1f).toInt(),
            (imageProxy.height * 0.25f).toInt(),
            (imageProxy.width * 0.9f).toInt(),
            (imageProxy.height * 0.75f).toInt()
        )

        val image = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                val barcode = barcodes.firstOrNull { barcode ->
                    val box = barcode.boundingBox
                    box != null && Rect.intersects(scanArea, box)
                }

                val raw = barcode?.rawValue

                if (!raw.isNullOrBlank() && raw != lastScanned) {
                    lastScanned = raw
                    onBarcode(raw)
                }
            }
            .addOnFailureListener {
                // optionally report error
            }
            .addOnCompleteListener {
                isProcessing = false
                imageProxy.close()
            }
    }

    fun shutdown() {
        scanner.close()
    }
}