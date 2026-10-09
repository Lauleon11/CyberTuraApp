package me.lauraleon.cybertura.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import me.lauraleon.cybertura.ui.screens.screen1.Screen1
import me.lauraleon.cybertura.ui.screens.screen2.Screen2
import me.lauraleon.cybertura.ui.screens.screen3.Screen3
import me.lauraleon.cybertura.ui.screens.badges.HallOfBadgesScreen
import me.lauraleon.cybertura.ui.screens.world1.*
import me.lauraleon.cybertura.ui.screens.world2.*
import me.lauraleon.cybertura.ui.screens.world3.*
import me.lauraleon.cybertura.ui.screens.world4.*
import me.lauraleon.cybertura.ui.screens.world5.*
import me.lauraleon.cybertura.ui.theme.CyberCyan
import me.lauraleon.cybertura.ui.viewmodel.GameViewModel

@Composable
fun CyberturaNavGraph(
    viewModel: GameViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val playerProfile by viewModel.playerProfile.collectAsState()
    val unlockedWorldMax by viewModel.unlockedWorldMax.collectAsState()

    var isProfileLoaded by remember { mutableStateOf(false) }
    LaunchedEffect(playerProfile) {
        isProfileLoaded = true
    }

    if (!isProfileLoaded) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = CyberCyan)
        }
        return
    }

    val hasProfile = playerProfile != null && playerProfile?.nickname.isNullOrBlank().not()
    val startDestination = if (hasProfile) Screen3 else Screen1

    val userGender = playerProfile?.gender ?: "HOMBRE"
    val userCostumeId = playerProfile?.costumeId ?: "spider"
    val hasPlayedIntroNarrative = playerProfile?.hasPlayedIntroNarrative ?: false

    NavHost(navController = navController, startDestination = startDestination) {
        composable<Screen1> {
            Screen1 {
                navController.navigate(Screen2)
            }
        }
        composable<Screen2> {
            Screen2(
                onContinue = { alias, age, gender, costumeId ->
                    viewModel.savePlayer(alias, age, gender, costumeId)
                    navController.navigate(Screen3) {
                        popUpTo<Screen1> { inclusive = true }
                    }
                },
            )
        }
        composable<Screen3> {
            Screen3(
                unlockedWorldMax = unlockedWorldMax,
                hasPlayedIntroNarrative = hasPlayedIntroNarrative,
                onNarrativePlayed = { viewModel.setIntroNarrativePlayed(true) },
                onWorldClick = { world ->
                    if (world.id <= unlockedWorldMax) {
                        when (world.id) {
                            1 -> navController.navigate(World1ScreenA)
                            2 -> navController.navigate(World2ScreenA)
                            3 -> navController.navigate(World3ScreenA)
                            4 -> navController.navigate(World4ScreenA)
                            5 -> navController.navigate(World5ScreenA)
                        }
                    }
                },
                onBadgesClick = {
                    navController.navigate(HallOfBadgesRoute)
                }
            )
        }
        composable<HallOfBadgesRoute> {
            HallOfBadgesScreen(
                unlockedWorldMax = unlockedWorldMax,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        // World 1
        composable<World1ScreenA> {
            World1ScreenA(onNextClick = {
                navController.navigate(World1ScreenB)
            })
        }
        composable<World1ScreenB> {
            World1ScreenB(onNextClick = {
                navController.navigate(World1ScreenC)
            })
        }
        composable<World1ScreenC> {
            World1ScreenC(onNextClick = {
                navController.navigate(World1ScreenD)
            })
        }
        composable<World1ScreenD> {
            World1ScreenD(onNextClick = {
                navController.navigate(World1ScreenE(gender = userGender, costumeId = userCostumeId))
            })
        }
        composable<World1ScreenE> { backStackEntry ->
            val route: World1ScreenE = backStackEntry.toRoute()
            World1ScreenE(
                gender = route.gender,
                costumeId = route.costumeId,
                onSuccess = {
                    navController.navigate(World1ScreenF(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World1ScreenF> { backStackEntry ->
            val route: World1ScreenF = backStackEntry.toRoute()
            World1ScreenF(
                gender = route.gender,
                costumeId = route.costumeId,
                onSuccess = {
                    navController.navigate(World1ScreenG(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World1ScreenG> { backStackEntry ->
            val route: World1ScreenG = backStackEntry.toRoute()
            World1ScreenG(
                gender = route.gender,
                costumeId = route.costumeId,
                onSuccess = {
                    navController.navigate(World1ScreenH)
                }
            )
        }
        composable<World1ScreenH> {
            World1ScreenH(
                onWorld2Unlocked = {
                    viewModel.completeWorld(1)
                    navController.navigate(Screen3) {
                        popUpTo<Screen3> { inclusive = true }
                    }
                }
            )
        }

        // World 2
        composable<World2ScreenA> {
            World2ScreenA(onNextClick = {
                navController.navigate(World2ScreenB)
            })
        }
        composable<World2ScreenB> {
            World2ScreenB(onNextClick = {
                navController.navigate(World2ScreenC)
            })
        }
        composable<World2ScreenC> {
            World2ScreenC(onNextClick = {
                navController.navigate(World2ScreenD)
            })
        }
        composable<World2ScreenD> {
            World2ScreenD(onNextClick = {
                navController.navigate(World2ScreenE(gender = userGender, costumeId = userCostumeId))
            })
        }
        composable<World2ScreenE> { backStackEntry ->
            val route: World2ScreenE = backStackEntry.toRoute()
            World2ScreenE(
                gender = route.gender,
                costumeId = route.costumeId,
                onSuccess = {
                    navController.navigate(World2ScreenF(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World2ScreenF> { backStackEntry ->
            val route: World2ScreenF = backStackEntry.toRoute()
            World2ScreenF(
                gender = route.gender,
                costumeId = route.costumeId,
                onSuccess = {
                    navController.navigate(World2ScreenG(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World2ScreenG> { backStackEntry ->
            val route: World2ScreenG = backStackEntry.toRoute()
            World2ScreenG(
                gender = route.gender,
                costumeId = route.costumeId,
                onSuccess = {
                    navController.navigate(World2ScreenH)
                }
            )
        }
        composable<World2ScreenH> {
            World2ScreenH(
                onWorld3Unlocked = {
                    viewModel.completeWorld(2)
                    navController.navigate(Screen3) {
                        popUpTo<Screen3> { inclusive = true }
                    }
                }
            )
        }

        // World 3
        composable<World3ScreenA> {
            World3ScreenA(onNextClick = {
                navController.navigate(World3ScreenB)
            })
        }
        composable<World3ScreenB> {
            World3ScreenB(onNextClick = {
                navController.navigate(World3ScreenC)
            })
        }
        composable<World3ScreenC> {
            World3ScreenC(onNextClick = {
                navController.navigate(World3ScreenD)
            })
        }
        composable<World3ScreenD> {
            World3ScreenD(onNextClick = {
                navController.navigate(World3ScreenE(gender = userGender, costumeId = userCostumeId))
            })
        }
        composable<World3ScreenE> { backStackEntry ->
            val route: World3ScreenE = backStackEntry.toRoute()
            World3ScreenE(
                gender = route.gender,
                costumeId = route.costumeId,
                onSuccess = {
                    navController.navigate(World3ScreenF(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World3ScreenF> { backStackEntry ->
            val route: World3ScreenF = backStackEntry.toRoute()
            World3ScreenF(
                gender = route.gender,
                costumeId = route.costumeId,
                onSuccess = {
                    navController.navigate(World3ScreenG(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World3ScreenG> { backStackEntry ->
            val route: World3ScreenG = backStackEntry.toRoute()
            World3ScreenG(
                gender = route.gender,
                costumeId = route.costumeId,
                onSuccess = {
                    navController.navigate(World3ScreenH)
                }
            )
        }
        composable<World3ScreenH> {
            World3ScreenH(
                onWorld4Unlocked = {
                    viewModel.completeWorld(3)
                    navController.navigate(Screen3) {
                        popUpTo<Screen3> { inclusive = true }
                    }
                }
            )
        }

        // World 4
        composable<World4ScreenA> {
            World4ScreenA(onNextClick = {
                navController.navigate(World4ScreenB)
            })
        }
        composable<World4ScreenB> {
            World4ScreenB(onNextClick = {
                navController.navigate(World4ScreenC)
            })
        }
        composable<World4ScreenC> {
            World4ScreenC(onNextClick = {
                navController.navigate(World4ScreenD)
            })
        }
        composable<World4ScreenD> {
            World4ScreenD(onNextClick = {
                navController.navigate(World4ScreenE(gender = userGender, costumeId = userCostumeId))
            })
        }
        composable<World4ScreenE> { backStackEntry ->
            val route: World4ScreenE = backStackEntry.toRoute()
            World4ScreenE(
                gender = route.gender,
                costumeId = route.costumeId,
                onSuccess = {
                    navController.navigate(World4ScreenF(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World4ScreenF> { backStackEntry ->
            val route: World4ScreenF = backStackEntry.toRoute()
            World4ScreenF(
                gender = route.gender,
                costumeId = route.costumeId,
                onSuccess = {
                    navController.navigate(World4ScreenG(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World4ScreenG> { backStackEntry ->
            val route: World4ScreenG = backStackEntry.toRoute()
            World4ScreenG(
                gender = route.gender,
                costumeId = route.costumeId,
                onSuccess = {
                    navController.navigate(World4ScreenH)
                }
            )
        }
        composable<World4ScreenH> {
            World4ScreenH(
                onWorld5Unlocked = {
                    viewModel.completeWorld(4)
                    navController.navigate(Screen3) {
                        popUpTo<Screen3> { inclusive = true }
                    }
                }
            )
        }

        // World 5
        composable<World5ScreenA> {
            World5ScreenA(onNextClick = {
                navController.navigate(World5ScreenB)
            })
        }
        composable<World5ScreenB> {
            World5ScreenB(onNextClick = {
                navController.navigate(World5ScreenC)
            })
        }
        composable<World5ScreenC> {
            World5ScreenC(onNextClick = {
                navController.navigate(World5ScreenD)
            })
        }
        composable<World5ScreenD> {
            World5ScreenD(onNextClick = {
                navController.navigate(World5ScreenE(gender = userGender, costumeId = userCostumeId))
            })
        }
        composable<World5ScreenE> { backStackEntry ->
            val route: World5ScreenE = backStackEntry.toRoute()
            World5ScreenE(
                gender = route.gender,
                costumeId = route.costumeId,
                onSuccess = {
                    navController.navigate(World5ScreenF(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World5ScreenF> { backStackEntry ->
            val route: World5ScreenF = backStackEntry.toRoute()
            World5ScreenF(
                gender = route.gender,
                costumeId = route.costumeId,
                onSuccess = {
                    navController.navigate(World5ScreenG(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World5ScreenG> { backStackEntry ->
            val route: World5ScreenG = backStackEntry.toRoute()
            World5ScreenG(
                gender = route.gender,
                costumeId = route.costumeId,
                onSuccess = {
                    navController.navigate(World5ScreenH)
                }
            )
        }
        composable<World5ScreenH> {
            World5ScreenH(
                onHallOfBadges = {
                    viewModel.completeWorld(5)
                    navController.navigate(HallOfBadgesRoute) {
                        popUpTo<Screen3> { inclusive = false }
                    }
                }
            )
        }
    }
}
