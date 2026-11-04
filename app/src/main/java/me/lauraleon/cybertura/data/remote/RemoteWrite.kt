package me.lauraleon.cybertura.data.remote

import android.util.Log
import com.google.android.gms.tasks.Task

/**
 * Queues a Firestore write without waiting for the server. The SDK stores it on the device and
 * sends it when there is a connection, even after the app restarts. Its task only completes once
 * the server answers, so awaiting it would block the game for as long as the phone is offline.
 */
internal fun queueRemoteWrite(description: String, write: () -> Task<Void>) {
    try {
        write().addOnFailureListener(Runnable::run) { e ->
            Log.e(TAG, "Firestore rejected: $description", e)
        }
    } catch (e: Exception) {
        Log.e(TAG, "Could not queue: $description", e)
    }
}

private const val TAG = "RemoteWrite"
