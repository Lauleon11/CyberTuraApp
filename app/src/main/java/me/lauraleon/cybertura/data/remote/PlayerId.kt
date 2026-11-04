package me.lauraleon.cybertura.data.remote

import android.content.Context
import android.util.Log
import java.util.UUID

/** Where the current player's document id is kept between launches. */
interface PlayerIdStore {
    fun read(): String?
    fun write(id: String)
}

/**
 * Anonymous id of the Firestore documents being written: a random UUID that is not derived from
 * the device or the player. It lasts until [startNew] is called, so a replay or another student
 * on the same phone gets new documents and never overwrites earlier results.
 */
class PlayerIdProvider(
    private val store: PlayerIdStore,
    private val generate: () -> String = { UUID.randomUUID().toString() }
) {
    private var current: String? = null

    val id: String
        @Synchronized get() = current
            ?: (store.read()?.takeIf { it.isNotBlank() } ?: generate().also(store::write))
                .also { current = it }

    @Synchronized
    fun startNew() {
        current = generate().also(store::write)
    }
}

/**
 * The file is excluded from cloud backup and device transfer in res/xml/backup_rules.xml and
 * res/xml/data_extraction_rules.xml: a restored copy would make two devices share one document.
 */
class SharedPreferencesPlayerIdStore(context: Context) : PlayerIdStore {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun read(): String? = prefs.getString(KEY_ID, null)

    override fun write(id: String) {
        if (!prefs.edit().putString(KEY_ID, id).commit()) {
            Log.w(TAG, "Could not persist the player id")
        }
    }

    private companion object {
        const val PREFS_NAME = "cybertura_installation"
        const val KEY_ID = "installation_id"
        const val TAG = "PlayerId"
    }
}
