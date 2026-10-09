package me.lauraleon.cybertura.ui.viewmodel

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
            val current = playerProfile.value
            val profile = PlayerProfile(
                id = 1,
                nickname = nickname,
                age = age,
                gender = gender,
                costumeId = costumeId,
                hasPlayedIntroNarrative = current?.hasPlayedIntroNarrative ?: false
            )
            playerRepository.savePlayer(profile)
        }
    }

    fun setIntroNarrativePlayed(played: Boolean) {
        viewModelScope.launch {
            playerProfile.value?.let { current ->
                playerRepository.savePlayer(current.copy(hasPlayedIntroNarrative = played))
            }
        }
    }

    fun completeWorld(worldId: Int) {
        viewModelScope.launch {
            worldRepository.completeWorld(worldId)
        }
    }
}
