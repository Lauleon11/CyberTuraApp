package me.lauraleon.cybertura.data.remote

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch
import kotlinx.coroutines.test.runTest
import me.lauraleon.cybertura.data.local.dao.PlayerDao
import me.lauraleon.cybertura.data.local.dao.WorldProgressDao
import me.lauraleon.cybertura.data.repository.PlayerRepositoryImpl
import me.lauraleon.cybertura.data.repository.WorldRepositoryImpl
import me.lauraleon.cybertura.domain.model.PlayerProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import java.util.Collections

class PlayerDocumentsTest {

    /** One app installation: its own id storage and a Firestore that records every write. */
    private class Installation(
        val store: InMemoryStore = InMemoryStore(),
        private val serverAnswer: () -> Task<Void> = { Tasks.forResult(null) }
    ) {
        val writtenPaths = Collections.synchronizedList(mutableListOf<String>())
        val writtenData = Collections.synchronizedList(mutableListOf<Any?>())
        private val paths = FirestorePaths(PlayerIdProvider(store))
        private val firestore = recordingFirestore()

        val players = PlayerRepositoryImpl(mock(PlayerDao::class.java), firestore, paths)
        val worlds = WorldRepositoryImpl(mock(WorldProgressDao::class.java), firestore, paths)

        suspend fun playThroughEveryRemoteWrite() {
            players.savePlayer(PlayerProfile(nickname = "Alias", age = 10, costumeId = "a", gender = "b"))
            players.saveSurvey(stars = 5, comments = "", surveyAnswers = emptyMap())
            worlds.updateWorldScreen(1, "B")
            worlds.completeWorld(1)
            worlds.resetWorlds()
        }

        private fun recordingFirestore(): FirebaseFirestore {
            val firestore = mock(FirebaseFirestore::class.java)
            val batch = mock(WriteBatch::class.java)
            doAnswer { invocation ->
                writtenPaths += invocation.getArgument<DocumentReference>(0).path
                writtenData += invocation.getArgument<Any?>(1)
                batch
            }.`when`(batch).set(any<DocumentReference>(), any<Any>())
            doAnswer { serverAnswer() }.`when`(batch).commit()
            doAnswer { batch }.`when`(firestore).batch()
            doAnswer { invocation ->
                val path = invocation.getArgument<String>(0)
                val document = mock(DocumentReference::class.java)
                doAnswer { path }.`when`(document).path
                doAnswer { write ->
                    writtenPaths += path
                    writtenData += write.getArgument<Any?>(0)
                    serverAnswer()
                }.`when`(document).set(any<Any>())
                document
            }.`when`(firestore).document(anyString())
            return firestore
        }
    }

    private class InMemoryStore : PlayerIdStore {
        var value: String? = null
        var writes = 0

        override fun read(): String? = value
        override fun write(id: String) {
            value = id
            writes++
        }
    }

    @Test
    fun `two installations write to completely separate documents`() = runTest {
        val first = Installation().apply { playThroughEveryRemoteWrite() }
        val second = Installation().apply { playThroughEveryRemoteWrite() }

        val firstId = first.store.value!!
        val secondId = second.store.value!!
        assertNotEquals(firstId, secondId)

        assertEquals(
            setOf(
                "players/$firstId",
                "surveys/$firstId",
                "players/$firstId/survey/latest",
                "players/$firstId/worlds/1",
                "players/$firstId/worlds/2",
                "players/$firstId/worlds/3",
                "players/$firstId/worlds/4",
                "players/$firstId/worlds/5"
            ),
            first.writtenPaths.toSet()
        )
        assertEquals(
            "No document may be shared between installations",
            emptySet<String>(),
            first.writtenPaths.toSet() intersect second.writtenPaths.toSet()
        )
    }

    @Test
    fun `a new player on the same phone never writes over the previous one`() = runTest {
        val phone = Installation().apply { playThroughEveryRemoteWrite() }
        val firstPlayerId = phone.store.value!!
        val firstPlayerPaths = phone.writtenPaths.toSet()
        phone.writtenPaths.clear()

        phone.players.startNewPlayerDocument()
        phone.playThroughEveryRemoteWrite()

        assertNotEquals(firstPlayerId, phone.store.value)
        assertEquals(emptySet<String>(), firstPlayerPaths intersect phone.writtenPaths.toSet())
    }

    @Test
    fun `the new player keeps its documents after restarting`() = runTest {
        val store = InMemoryStore()
        Installation(store).apply {
            playThroughEveryRemoteWrite()
            players.startNewPlayerDocument()
        }
        val newPlayerId = store.value

        Installation(store).playThroughEveryRemoteWrite()

        assertEquals(newPlayerId, store.value)
    }

    @Test
    fun `saving does not wait for the server when the phone is offline`() = runTest {
        val neverAnswered = Installation(serverAnswer = { TaskCompletionSource<Void>().task })

        // Awaiting a task that never completes would hang this test until it times out.
        neverAnswered.playThroughEveryRemoteWrite()

        assertTrue(neverAnswered.writtenPaths.contains("surveys/${neverAnswered.store.value}"))
    }

    @Test
    fun `no write uses the shared legacy document`() = runTest {
        val installation = Installation().apply { playThroughEveryRemoteWrite() }

        assertFalse(installation.writtenPaths.any { it == "players/1" || it.startsWith("players/1/") })
        assertFalse(installation.writtenPaths.contains("surveys/1"))
    }

    @Test
    fun `survey is attributed to the player id`() = runTest {
        val installation = Installation().apply { playThroughEveryRemoteWrite() }

        val surveyIndex = installation.writtenPaths.indexOf("surveys/${installation.store.value}")
        val survey = installation.writtenData[surveyIndex] as Map<*, *>
        assertEquals(installation.store.value, survey["playerId"])
    }

    @Test
    fun `the same installation keeps its documents after restarting`() = runTest {
        val store = InMemoryStore()
        val beforeRestart = Installation(store).apply { playThroughEveryRemoteWrite() }
        val afterRestart = Installation(store).apply { playThroughEveryRemoteWrite() }

        assertEquals(beforeRestart.writtenPaths.toSet(), afterRestart.writtenPaths.toSet())
        assertEquals(1, store.writes)
    }

    @Test
    fun `player id is a random uuid with no player data`() {
        val id = PlayerIdProvider(InMemoryStore()).id

        assertTrue(id.matches(Regex("^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")))
    }

    @Test
    fun `a blank stored id is replaced instead of creating an invalid path`() {
        val store = InMemoryStore().apply { value = " " }

        val id = PlayerIdProvider(store) { "new-id" }.id

        assertEquals("new-id", id)
        assertEquals("new-id", store.value)
    }
}
