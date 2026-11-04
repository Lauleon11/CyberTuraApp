package me.lauraleon.cybertura.ui.viewmodel

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.lauraleon.cybertura.domain.model.PlayerProfile
import me.lauraleon.cybertura.domain.model.WorldState
import me.lauraleon.cybertura.domain.model.WorldStatus
import me.lauraleon.cybertura.domain.repository.PlayerRepository
import me.lauraleon.cybertura.domain.repository.WorldRepository
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val worldRepository: WorldRepository
) : ViewModel() {

    private var backgroundMediaPlayer: MediaPlayer? = null
    private var currentPlayingWorld: Int? = null

    // Serializes every read-modify-write on the player profile. Without this,
    // concurrent updates (e.g. addScore + recordAttempt) read the same snapshot
    // and the last write wins, discarding the score change.
    private val playerMutex = Mutex()

    // Session-level flag so the intro narrative never replays while navigating,
    // even before the Room write is observed back.
    private val _introNarrativeConsumed = MutableStateFlow(false)
    val introNarrativeConsumed: StateFlow<Boolean> = _introNarrativeConsumed.asStateFlow()

    val playerProfile: StateFlow<PlayerProfile?> = playerRepository.observePlayer()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val worlds: StateFlow<List<WorldState>> = worldRepository.observeWorlds()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unlockedWorldMax: StateFlow<Int> = worlds.map { worldStates ->
        val completedOrUnlocked = worldStates.filter {
            it.progress.status == WorldStatus.COMPLETED ||
                    it.progress.status == WorldStatus.UNLOCKED
        }
        completedOrUnlocked.maxOfOrNull { it.world.id } ?: 1
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    private suspend fun updatePlayer(transform: (PlayerProfile?) -> PlayerProfile) {
        playerMutex.withLock {
            val current = playerRepository.observePlayer().firstOrNull()
            playerRepository.savePlayer(transform(current))
        }
    }

    private suspend fun updateExistingPlayer(transform: (PlayerProfile) -> PlayerProfile) {
        playerMutex.withLock {
            val current = playerRepository.observePlayer().firstOrNull() ?: return@withLock
            playerRepository.savePlayer(transform(current))
        }
    }

    fun savePlayer(nickname: String, age: Int, gender: String, costumeId: String) {
        viewModelScope.launch {
            updatePlayer { current ->
                (current ?: PlayerProfile(
                    nickname = nickname,
                    age = age,
                    gender = gender,
                    costumeId = costumeId
                )).copy(
                    nickname = nickname,
                    age = age,
                    gender = gender,
                    costumeId = costumeId
                )
            }
        }
    }

    fun setIntroNarrativePlayed(played: Boolean) {
        if (played) {
            _introNarrativeConsumed.value = true
        }
        viewModelScope.launch {
            updateExistingPlayer { it.copy(hasPlayedIntroNarrative = played) }
        }
    }

    fun saveRating(stars: Int, comments: String) {
        viewModelScope.launch {
            updatePlayer { current ->
                current?.copy(
                    ratingStars = stars,
                    ratingComments = comments,
                    hasRated = true
                ) ?: PlayerProfile(
                    id = 1,
                    nickname = "Jugador",
                    age = 10,
                    costumeId = "spider",
                    gender = "HOMBRE",
                    ratingStars = stars,
                    ratingComments = comments,
                    hasRated = true
                )
            }
        }
    }

    fun saveSurvey(stars: Int, comments: String, surveyAnswers: Map<Int, Int>) {
        viewModelScope.launch {
            playerMutex.withLock {
                val current = playerRepository.observePlayer().firstOrNull()
                val updated = (current ?: PlayerProfile(
                    id = 1,
                    nickname = "Jugador",
                    age = 10,
                    costumeId = "spider",
                    gender = "HOMBRE"
                )).copy(
                    ratingStars = stars,
                    ratingComments = comments,
                    hasRated = true
                )
                playerRepository.savePlayer(updated)
            }
            playerRepository.saveSurvey(stars, comments, surveyAnswers)
        }
    }

    fun addScore(points: Int) {
        viewModelScope.launch {
            updatePlayer { current ->
                val currentScore = current?.score ?: 0
                val newScore = maxOf(0, currentScore + points)
                (current ?: PlayerProfile(
                    id = 1,
                    nickname = "Jugador",
                    age = 10,
                    costumeId = "spider",
                    gender = "HOMBRE"
                )).copy(score = newScore)
            }
        }
    }

    fun recordAttempt(worldNumber: Int, screen: Char) {
        viewModelScope.launch {
            updateExistingPlayer { current ->
                when (worldNumber) {
                    1 -> when (screen) {
                        'E' -> current.copy(attemptsW1E = current.attemptsW1E + 1)
                        'F' -> current.copy(attemptsW1F = current.attemptsW1F + 1)
                        'G' -> current.copy(attemptsW1G = current.attemptsW1G + 1)
                        else -> current
                    }
                    2 -> when (screen) {
                        'E' -> current.copy(attemptsW2E = current.attemptsW2E + 1)
                        'F' -> current.copy(attemptsW2F = current.attemptsW2F + 1)
                        'G' -> current.copy(attemptsW2G = current.attemptsW2G + 1)
                        else -> current
                    }
                    3 -> when (screen) {
                        'E' -> current.copy(attemptsW3E = current.attemptsW3E + 1)
                        'F' -> current.copy(attemptsW3F = current.attemptsW3F + 1)
                        'G' -> current.copy(attemptsW3G = current.attemptsW3G + 1)
                        else -> current
                    }
                    4 -> when (screen) {
                        'E' -> current.copy(attemptsW4E = current.attemptsW4E + 1)
                        'F' -> current.copy(attemptsW4F = current.attemptsW4F + 1)
                        'G' -> current.copy(attemptsW4G = current.attemptsW4G + 1)
                        else -> current
                    }
                    5 -> when (screen) {
                        'E' -> current.copy(attemptsW5E = current.attemptsW5E + 1)
                        'F' -> current.copy(attemptsW5F = current.attemptsW5F + 1)
                        'G' -> current.copy(attemptsW5G = current.attemptsW5G + 1)
                        else -> current
                    }
                    else -> current
                }
            }
        }
    }

    fun recordActivityAMetrics(worldNumber: Int, timeSeconds: Int, errors: Int, attempts: Int) {
        viewModelScope.launch {
            updateExistingPlayer { current ->
                when (worldNumber) {
                    1 -> current.copy(timeW1A = timeSeconds, errorsW1A = errors, attemptsW1A = attempts)
                    2 -> current.copy(timeW2A = timeSeconds, errorsW2A = errors, attemptsW2A = attempts)
                    3 -> current.copy(timeW3A = timeSeconds, errorsW3A = errors, attemptsW3A = attempts)
                    4 -> current.copy(timeW4A = timeSeconds, errorsW4A = errors, attemptsW4A = attempts)
                    5 -> current.copy(timeW5A = timeSeconds, errorsW5A = errors, attemptsW5A = attempts)
                    else -> current
                }
            }
        }
    }

    fun recordActivityBMetrics(worldNumber: Int, timeSeconds: Int, errors: Int, attempts: Int) {
        viewModelScope.launch {
            updateExistingPlayer { current ->
                when (worldNumber) {
                    1 -> current.copy(timeW1B = timeSeconds, errorsW1B = errors, attemptsW1B = attempts)
                    2 -> current.copy(timeW2B = timeSeconds, errorsW2B = errors, attemptsW2B = attempts)
                    3 -> current.copy(timeW3B = timeSeconds, errorsW3B = errors, attemptsW3B = attempts)
                    4 -> current.copy(timeW4B = timeSeconds, errorsW4B = errors, attemptsW4B = attempts)
                    5 -> current.copy(timeW5B = timeSeconds, errorsW5B = errors, attemptsW5B = attempts)
                    else -> current
                }
            }
        }
    }

    fun recordActivityCMetrics(worldNumber: Int, timeSeconds: Int, moves: Int) {
        viewModelScope.launch {
            updateExistingPlayer { current ->
                when (worldNumber) {
                    1 -> current.copy(timeW1C = timeSeconds, movesW1C = moves)
                    2 -> current.copy(timeW2C = timeSeconds, movesW2C = moves)
                    3 -> current.copy(timeW3C = timeSeconds, movesW3C = moves)
                    4 -> current.copy(timeW4C = timeSeconds, movesW4C = moves)
                    5 -> current.copy(timeW5C = timeSeconds, movesW5C = moves)
                    else -> current
                }
            }
        }
    }

    fun recordQuestionMetrics(worldNumber: Int, screen: Char, timeSeconds: Int, errors: Int, attempts: Int) {
        viewModelScope.launch {
            updateExistingPlayer { current ->
                when (worldNumber) {
                    1 -> when (screen) {
                        'E' -> current.copy(timeW1E = timeSeconds, errorsW1E = errors, attemptsW1E = attempts)
                        'F' -> current.copy(timeW1F = timeSeconds, errorsW1F = errors, attemptsW1F = attempts)
                        'G' -> current.copy(timeW1G = timeSeconds, errorsW1G = errors, attemptsW1G = attempts)
                        else -> current
                    }
                    2 -> when (screen) {
                        'E' -> current.copy(timeW2E = timeSeconds, errorsW2E = errors, attemptsW2E = attempts)
                        'F' -> current.copy(timeW2F = timeSeconds, errorsW2F = errors, attemptsW2F = attempts)
                        'G' -> current.copy(timeW2G = timeSeconds, errorsW2G = errors, attemptsW2G = attempts)
                        else -> current
                    }
                    3 -> when (screen) {
                        'E' -> current.copy(timeW3E = timeSeconds, errorsW3E = errors, attemptsW3E = attempts)
                        'F' -> current.copy(timeW3F = timeSeconds, errorsW3F = errors, attemptsW3F = attempts)
                        'G' -> current.copy(timeW3G = timeSeconds, errorsW3G = errors, attemptsW3G = attempts)
                        else -> current
                    }
                    4 -> when (screen) {
                        'E' -> current.copy(timeW4E = timeSeconds, errorsW4E = errors, attemptsW4E = attempts)
                        'F' -> current.copy(timeW4F = timeSeconds, errorsW4F = errors, attemptsW4F = attempts)
                        'G' -> current.copy(timeW4G = timeSeconds, errorsW4G = errors, attemptsW4G = attempts)
                        else -> current
                    }
                    5 -> when (screen) {
                        'E' -> current.copy(timeW5E = timeSeconds, errorsW5E = errors, attemptsW5E = attempts)
                        'F' -> current.copy(timeW5F = timeSeconds, errorsW5F = errors, attemptsW5F = attempts)
                        'G' -> current.copy(timeW5G = timeSeconds, errorsW5G = errors, attemptsW5G = attempts)
                        else -> current
                    }
                    else -> current
                }
            }
        }
    }

    fun completeWorld(worldId: Int) {
        viewModelScope.launch {
            worldRepository.completeWorld(worldId)
        }
    }

    fun updateWorldScreen(worldId: Int, screen: String) {
        viewModelScope.launch {
            worldRepository.updateWorldScreen(worldId, screen)
        }
    }

    fun resetGame() {
        _introNarrativeConsumed.value = false
        viewModelScope.launch {
            worldRepository.resetWorlds()
            playerMutex.withLock {
                val current = playerRepository.observePlayer().firstOrNull()
                val updated = current?.copy(
                    score = 0,
                    hasPlayedIntroNarrative = false,
                    gamesPlayedCount = current.gamesPlayedCount + 1,
                    attemptsW1E = 0, attemptsW1F = 0, attemptsW1G = 0, errorsW1E = 0, errorsW1F = 0, errorsW1G = 0, timeW1E = 0, timeW1F = 0, timeW1G = 0,
                    attemptsW2E = 0, attemptsW2F = 0, attemptsW2G = 0, errorsW2E = 0, errorsW2F = 0, errorsW2G = 0, timeW2E = 0, timeW2F = 0, timeW2G = 0,
                    attemptsW3E = 0, attemptsW3F = 0, attemptsW3G = 0, errorsW3E = 0, errorsW3F = 0, errorsW3G = 0, timeW3E = 0, timeW3F = 0, timeW3G = 0,
                    attemptsW4E = 0, attemptsW4F = 0, attemptsW4G = 0, errorsW4E = 0, errorsW4F = 0, errorsW4G = 0, timeW4E = 0, timeW4F = 0, timeW4G = 0,
                    attemptsW5E = 0, attemptsW5F = 0, attemptsW5G = 0, errorsW5E = 0, errorsW5F = 0, errorsW5G = 0, timeW5E = 0, timeW5F = 0, timeW5G = 0,
                    timeW1A = 0, errorsW1A = 0, attemptsW1A = 0, timeW1B = 0, errorsW1B = 0, attemptsW1B = 0, timeW1C = 0, movesW1C = 0,
                    timeW2A = 0, errorsW2A = 0, attemptsW2A = 0, timeW2B = 0, errorsW2B = 0, attemptsW2B = 0, timeW2C = 0, movesW2C = 0,
                    timeW3A = 0, errorsW3A = 0, attemptsW3A = 0, timeW3B = 0, errorsW3B = 0, attemptsW3B = 0, timeW3C = 0, movesW3C = 0,
                    timeW4A = 0, errorsW4A = 0, attemptsW4A = 0, timeW4B = 0, errorsW4B = 0, attemptsW4B = 0, timeW4C = 0, movesW4C = 0,
                    timeW5A = 0, errorsW5A = 0, attemptsW5A = 0, timeW5B = 0, errorsW5B = 0, attemptsW5B = 0, timeW5C = 0, movesW5C = 0
                ) ?: PlayerProfile(
                    id = 1,
                    nickname = "Jugador",
                    age = 10,
                    costumeId = "spider",
                    gender = "HOMBRE",
                    gamesPlayedCount = 1
                )
                playerRepository.savePlayer(updated)
            }
        }
    }

    fun startWorldMusic(context: Context, worldNumber: Int) {
        if (currentPlayingWorld == worldNumber && backgroundMediaPlayer != null) {
            if (backgroundMediaPlayer?.isPlaying == false) {
                backgroundMediaPlayer?.start()
            }
            return
        }

        stopWorldMusic()
        currentPlayingWorld = worldNumber

        val resName = "world${worldNumber}_music"
        val resId = context.resources.getIdentifier(resName, "raw", context.packageName)
        if (resId != 0) {
            try {
                backgroundMediaPlayer = MediaPlayer.create(context.applicationContext, resId)?.apply {
                    isLooping = true
                    start()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun stopWorldMusic() {
        try {
            backgroundMediaPlayer?.stop()
            backgroundMediaPlayer?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            backgroundMediaPlayer = null
            currentPlayingWorld = null
        }
    }

    fun pauseWorldMusic() {
        stopWorldMusic()
    }

    fun onAppBackground() {
        stopWorldMusic()
    }

    fun onAppForeground() {
        // no-op
    }

    override fun onCleared() {
        super.onCleared()
        stopWorldMusic()
    }
}
