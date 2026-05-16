package com.nanoporetech.scainter.notification

import android.content.Context
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import java.io.IOException
import kotlin.coroutines.resume
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine

private const val TAG = "FcmTokenRegistrar"
private const val MAX_ATTEMPTS = 3
private const val INITIAL_RETRY_DELAY_MS = 1_000L

interface DeviceTokenRegistrar {
    suspend fun registerDeviceToken(userId: String)
}

class FirebaseDeviceTokenRegistrar(
    context: Context
) : DeviceTokenRegistrar {

    private val appContext = context.applicationContext

    override suspend fun registerDeviceToken(userId: String) {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val playServicesStatus = googleApiAvailability
            .isGooglePlayServicesAvailable(appContext)
        val firebaseOptions = FirebaseApp.getInstance().options

        Log.d(
            TAG,
            "Starting FCM token fetch for userId=$userId appId=${firebaseOptions.applicationId} projectId=${firebaseOptions.projectId} senderId=${firebaseOptions.gcmSenderId}"
        )

        if (playServicesStatus != ConnectionResult.SUCCESS) {
            Log.w(
                TAG,
                "Skipping FCM token fetch for userId=$userId because Google Play Services is unavailable: $playServicesStatus (${googleApiAvailability.getErrorString(playServicesStatus)})"
            )
            return
        }

        var retryDelayMs = INITIAL_RETRY_DELAY_MS
        repeat(MAX_ATTEMPTS) { attempt ->
            try {
                val token = awaitFcmToken()
                //Log.d(TAG, "FCM token retrieved for userId=$userId: $token")
                Log.d(TAG, "Token: $token")
                // TODO: send token to backend once the endpoint exists.
                return
            } catch (exception: IOException) {
                val isTransient = exception.message?.contains("SERVICE_NOT_AVAILABLE") == true
                val isLastAttempt = attempt == MAX_ATTEMPTS - 1

                if (!isTransient || isLastAttempt) {
                    Log.e(
                        TAG,
                        "FCM token fetch failed for userId=$userId after ${attempt + 1} attempt(s)",
                        exception
                    )
                    return
                }

                Log.w(
                    TAG,
                    "Transient FCM token fetch failure for userId=$userId, retrying in ${retryDelayMs}ms",
                    exception
                )
                delay(retryDelayMs)
                retryDelayMs *= 2
            } catch (exception: Exception) {
                Log.e(TAG, "Unexpected FCM token fetch failure for userId=$userId", exception)
                return
            }
        }
    }

    private suspend fun awaitFcmToken(): String = suspendCancellableCoroutine { continuation ->
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                when {
                    task.isSuccessful -> continuation.resume(task.result)
                    task.exception != null -> continuation.resumeWith(Result.failure(task.exception!!))
                    else -> continuation.resumeWith(
                        Result.failure(IOException("FCM token task finished without a result"))
                    )
                }
            }
    }
}
