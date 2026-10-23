package me.lauraleon.cybertura.ui.viewmodel

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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

    fun savePlayer(nickname: String, age: Int, gender: String, costumeId: String) {
        viewModelScope.launch {
            val current = playerRepository.observePlayer().firstOrNull()
            val profile = (current ?: PlayerProfile(
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
            playerRepository.savePlayer(profile)
        }
    }

    fun setIntroNarrativePlayed(played: Boolean) {
        viewModelScope.launch {
            val current = playerRepository.observePlayer().firstOrNull()
            current?.let {
                playerRepository.savePlayer(it.copy(hasPlayedIntroNarrative = played))
            }
        }
    }

    fun saveRating(stars: Int, comments: String) {
        viewModelScope.launch {
            val current = playerRepository.observePlayer().firstOrNull()
            val updated = current?.copy(
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
            playerRepository.savePlayer(updated)
        }
    }

    fun addScore(points: Int) {
        viewModelScope.launch {
            val current = playerRepository.observePlayer().firstOrNull()
            val currentScore = current?.score ?: 0
            val newScore = currentScore + points
            val updated = current?.copy(score = newScore) ?: PlayerProfile(
                id = 1,
                nickname = "Jugador",
                age = 10,
                costumeId = "spider",
                gender = "HOMBRE",
                score = newScore
            )
            playerRepository.savePlayer(updated)
        }
    }

    fun recordAttempt(worldNumber: Int, screen: Char) {
        viewModelScope.launch {
            val current = playerRepository.observePlayer().firstOrNull() ?: return@launch
            val updated = when (worldNumber) {
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
            playerRepository.savePlayer(updated)
        }
    }

    fun completeWorld(worldId: Int) {
        viewModelScope.launch {
            worldRepository.completeWorld(worldId)
        }
    }

    fun resetGame() {
        viewModelScope.launch {
            worldRepository.resetWorlds()
            val current = playerRepository.observePlayer().firstOrNull()
            val updated = current?.copy(
                score = 0,
                gamesPlayedCount = current.gamesPlayedCount + 1,
                attemptsW1E = 0, attemptsW1F = 0, attemptsW1G = 0,
                attemptsW2E = 0, attemptsW2F = 0, attemptsW2G = 0,
                attemptsW3E = 0, attemptsW3F = 0, attemptsW3G = 0,
                attemptsW4E = 0, attemptsW4F = 0, attemptsW4G = 0,
                attemptsW5E = 0, attemptsW5F = 0, attemptsW5G = 0
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
