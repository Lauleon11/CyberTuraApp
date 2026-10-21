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
            val profile = PlayerProfile(
                id = 1,
                nickname = nickname,
                age = age,
                gender = gender,
                costumeId = costumeId,
                hasPlayedIntroNarrative = current?.hasPlayedIntroNarrative ?: false,
                score = current?.score ?: 0
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

    fun completeWorld(worldId: Int) {
        viewModelScope.launch {
            worldRepository.completeWorld(worldId)
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

    override fun onCleared() {
        super.onCleared()
        stopWorldMusic()
    }
}
