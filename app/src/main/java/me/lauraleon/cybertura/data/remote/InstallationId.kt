package me.lauraleon.cybertura.data.remote

import android.content.Context
import android.util.Log
import java.util.UUID

/** Where an installation keeps its identifier between launches. */
interface InstallationIdStore {
    fun read(): String?
    fun write(id: String)
}

/**
 * Anonymous identifier for this app installation: a random UUID that is not derived from the
 * device or the player, created on first use and kept until the app data is cleared.
 */
class InstallationIdProvider(
    private val store: InstallationIdStore,
    private val generate: () -> String = { UUID.randomUUID().toString() }
) {
    val id: String by lazy {
        store.read()?.takeIf { it.isNotBlank() } ?: generate().also(store::write)
    }
}

/**
 * The file is excluded from cloud backup and device transfer in res/xml/backup_rules.xml and
 * res/xml/data_extraction_rules.xml: a restored copy would make two devices share one document.
 */
class SharedPreferencesInstallationIdStore(context: Context) : InstallationIdStore {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun read(): String? = prefs.getString(KEY_ID, null)

    override fun write(id: String) {
        if (!prefs.edit().putString(KEY_ID, id).commit()) {
            Log.w(TAG, "Could not persist the installation id")
        }
    }

    private companion object {
        const val PREFS_NAME = "cybertura_installation"
        const val KEY_ID = "installation_id"
        const val TAG = "InstallationId"
    }
}
