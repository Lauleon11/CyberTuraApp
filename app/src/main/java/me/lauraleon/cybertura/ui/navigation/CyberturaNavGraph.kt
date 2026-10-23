package me.lauraleon.cybertura.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import me.lauraleon.cybertura.domain.model.WorldStatus
import me.lauraleon.cybertura.ui.screens.screen1.Screen1
import me.lauraleon.cybertura.ui.screens.screen2.Screen2
import me.lauraleon.cybertura.ui.screens.screen3.Screen3
import me.lauraleon.cybertura.ui.screens.badges.HallOfBadgesScreen
import me.lauraleon.cybertura.ui.screens.rating.RatingScreen
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
    val worlds by viewModel.worlds.collectAsState()
    val unlockedWorldMax by viewModel.unlockedWorldMax.collectAsState()
    val completedWorldIds = worlds.filter { it.progress.status == WorldStatus.COMPLETED }.map { it.world.id }.toSet()

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

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> viewModel.onAppBackground()
                Lifecycle.Event.ON_START -> viewModel.onAppForeground()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

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
            LaunchedEffect(Unit) {
                viewModel.stopWorldMusic()
            }
            Screen3(
                score = playerProfile?.score ?: 0,
                unlockedWorldMax = unlockedWorldMax,
                completedWorldIds = completedWorldIds,
                hasPlayedIntroNarrative = hasPlayedIntroNarrative,
                onNarrativePlayed = { viewModel.setIntroNarrativePlayed(true) },
                onWorldClick = { world ->
                    val isCompleted = world.id in completedWorldIds || world.id < unlockedWorldMax
                    if (world.id <= unlockedWorldMax && !isCompleted) {
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
                completedWorldIds = completedWorldIds,
                onPlayAgain = {
                    viewModel.resetGame()
                    navController.navigate(Screen3) {
                        popUpTo<Screen1> { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable<RatingScreenRoute> {
            RatingScreen(
                onRatingSubmitted = { stars, comment ->
                    viewModel.saveRating(stars, comment)
                },
                onNavigateToBadges = {
                    navController.navigate(HallOfBadgesRoute) {
                        popUpTo<Screen3> { inclusive = false }
                    }
                }
            )
        }
        
        // World 1
        composable<World1ScreenA> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
            World1ScreenA(onNextClick = {
                navController.navigate(World1ScreenAActivityRoute)
            })
        }
        composable<World1ScreenAActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 1)
            }
            World1ScreenAActivity(onNextClick = {
                navController.navigate(World1ScreenB)
            })
        }
        composable<World1ScreenB> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
            World1ScreenB(onNextClick = {
                navController.navigate(World1ScreenBActivityRoute)
            })
        }
        composable<World1ScreenBActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 1)
            }
            World1ScreenBActivity(onNextClick = {
                navController.navigate(World1ScreenC)
            })
        }
        composable<World1ScreenC> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
            World1ScreenC(onNextClick = {
                navController.navigate(World1ScreenCActivityRoute)
            })
        }
        composable<World1ScreenCActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 1)
            }
            World1ScreenCActivity(onNextClick = {
                navController.navigate(World1ScreenD)
            })
        }
        composable<World1ScreenD> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
            World1ScreenD(onNextClick = {
                navController.navigate(World1ScreenE(gender = userGender, costumeId = userCostumeId))
            })
        }
        composable<World1ScreenE> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 1)
            }
            val route: World1ScreenE = backStackEntry.toRoute()
            World1ScreenE(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.recordAttempt(1, 'E') },
                onSuccess = {
                    navController.navigate(World1ScreenF(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World1ScreenF> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 1)
            }
            val route: World1ScreenF = backStackEntry.toRoute()
            World1ScreenF(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.recordAttempt(1, 'F') },
                onSuccess = {
                    navController.navigate(World1ScreenG(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World1ScreenG> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 1)
            }
            val route: World1ScreenG = backStackEntry.toRoute()
            World1ScreenG(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.recordAttempt(1, 'G') },
                onSuccess = {
                    navController.navigate(World1ScreenH)
                }
            )
        }
        composable<World1ScreenH> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
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
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
            World2ScreenA(onNextClick = {
                navController.navigate(World2ScreenAActivityRoute)
            })
        }
        composable<World2ScreenAActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 2)
            }
            World2ScreenAActivity(onNextClick = {
                navController.navigate(World2ScreenB)
            })
        }
        composable<World2ScreenB> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
            World2ScreenB(onNextClick = {
                navController.navigate(World2ScreenBActivityRoute)
            })
        }
        composable<World2ScreenBActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 2)
            }
            World2ScreenBActivity(onNextClick = {
                navController.navigate(World2ScreenC)
            })
        }
        composable<World2ScreenC> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
            World2ScreenC(onNextClick = {
                navController.navigate(World2ScreenCActivityRoute)
            })
        }
        composable<World2ScreenCActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 2)
            }
            World2ScreenCActivity(onNextClick = {
                navController.navigate(World2ScreenD)
            })
        }
        composable<World2ScreenD> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
            World2ScreenD(onNextClick = {
                navController.navigate(World2ScreenE(gender = userGender, costumeId = userCostumeId))
            })
        }
        composable<World2ScreenE> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 2)
            }
            val route: World2ScreenE = backStackEntry.toRoute()
            World2ScreenE(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.recordAttempt(2, 'E') },
                onSuccess = {
                    navController.navigate(World2ScreenF(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World2ScreenF> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 2)
            }
            val route: World2ScreenF = backStackEntry.toRoute()
            World2ScreenF(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.recordAttempt(2, 'F') },
                onSuccess = {
                    navController.navigate(World2ScreenG(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World2ScreenG> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 2)
            }
            val route: World2ScreenG = backStackEntry.toRoute()
            World2ScreenG(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.recordAttempt(2, 'G') },
                onSuccess = {
                    navController.navigate(World2ScreenH)
                }
            )
        }
        composable<World2ScreenH> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
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
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
            World3ScreenA(onNextClick = {
                navController.navigate(World3ScreenAActivityRoute)
            })
        }
        composable<World3ScreenAActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 3)
            }
            World3ScreenAActivity(onNextClick = {
                navController.navigate(World3ScreenB)
            })
        }
        composable<World3ScreenB> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
            World3ScreenB(onNextClick = {
                navController.navigate(World3ScreenBActivityRoute)
            })
        }
        composable<World3ScreenBActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 3)
            }
            World3ScreenBActivity(onNextClick = {
                navController.navigate(World3ScreenC)
            })
        }
        composable<World3ScreenC> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
            World3ScreenC(onNextClick = {
                navController.navigate(World3ScreenCActivityRoute)
            })
        }
        composable<World3ScreenCActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 3)
            }
            World3ScreenCActivity(onNextClick = {
                navController.navigate(World3ScreenD)
            })
        }
        composable<World3ScreenD> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
            World3ScreenD(onNextClick = {
                navController.navigate(World3ScreenE(gender = userGender, costumeId = userCostumeId))
            })
        }
        composable<World3ScreenE> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 3)
            }
            val route: World3ScreenE = backStackEntry.toRoute()
            World3ScreenE(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.recordAttempt(3, 'E') },
                onSuccess = {
                    navController.navigate(World3ScreenF(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World3ScreenF> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 3)
            }
            val route: World3ScreenF = backStackEntry.toRoute()
            World3ScreenF(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.recordAttempt(3, 'F') },
                onSuccess = {
                    navController.navigate(World3ScreenG(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World3ScreenG> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 3)
            }
            val route: World3ScreenG = backStackEntry.toRoute()
            World3ScreenG(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.recordAttempt(3, 'G') },
                onSuccess = {
                    navController.navigate(World3ScreenH)
                }
            )
        }
        composable<World3ScreenH> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
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
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
            World4ScreenA(onNextClick = {
                navController.navigate(World4ScreenAActivityRoute)
            })
        }
        composable<World4ScreenAActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 4)
            }
            World4ScreenAActivity(onNextClick = {
                navController.navigate(World4ScreenB)
            })
        }
        composable<World4ScreenB> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
            World4ScreenB(onNextClick = {
                navController.navigate(World4ScreenBActivityRoute)
            })
        }
        composable<World4ScreenBActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 4)
            }
            World4ScreenBActivity(onNextClick = {
                navController.navigate(World4ScreenC)
            })
        }
        composable<World4ScreenC> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
            World4ScreenC(onNextClick = {
                navController.navigate(World4ScreenCActivityRoute)
            })
        }
        composable<World4ScreenCActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 4)
            }
            World4ScreenCActivity(onNextClick = {
                navController.navigate(World4ScreenD)
            })
        }
        composable<World4ScreenD> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
            World4ScreenD(onNextClick = {
                navController.navigate(World4ScreenE(gender = userGender, costumeId = userCostumeId))
            })
        }
        composable<World4ScreenE> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 4)
            }
            val route: World4ScreenE = backStackEntry.toRoute()
            World4ScreenE(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.recordAttempt(4, 'E') },
                onSuccess = {
                    navController.navigate(World4ScreenF(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World4ScreenF> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 4)
            }
            val route: World4ScreenF = backStackEntry.toRoute()
            World4ScreenF(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.recordAttempt(4, 'F') },
                onSuccess = {
                    navController.navigate(World4ScreenG(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World4ScreenG> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 4)
            }
            val route: World4ScreenG = backStackEntry.toRoute()
            World4ScreenG(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.recordAttempt(4, 'G') },
                onSuccess = {
                    navController.navigate(World4ScreenH)
                }
            )
        }
        composable<World4ScreenH> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
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
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
            World5ScreenA(onNextClick = {
                navController.navigate(World5ScreenAActivityRoute)
            })
        }
        composable<World5ScreenAActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 5)
            }
            World5ScreenAActivity(onNextClick = {
                navController.navigate(World5ScreenB)
            })
        }
        composable<World5ScreenB> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
            World5ScreenB(onNextClick = {
                navController.navigate(World5ScreenBActivityRoute)
            })
        }
        composable<World5ScreenBActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 5)
            }
            World5ScreenBActivity(onNextClick = {
                navController.navigate(World5ScreenC)
            })
        }
        composable<World5ScreenC> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
            World5ScreenC(onNextClick = {
                navController.navigate(World5ScreenCActivityRoute)
            })
        }
        composable<World5ScreenCActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 5)
            }
            World5ScreenCActivity(onNextClick = {
                navController.navigate(World5ScreenD)
            })
        }
        composable<World5ScreenD> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
            World5ScreenD(onNextClick = {
                navController.navigate(World5ScreenE(gender = userGender, costumeId = userCostumeId))
            })
        }
        composable<World5ScreenE> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 5)
            }
            val route: World5ScreenE = backStackEntry.toRoute()
            World5ScreenE(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.recordAttempt(5, 'E') },
                onSuccess = {
                    navController.navigate(World5ScreenF(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World5ScreenF> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 5)
            }
            val route: World5ScreenF = backStackEntry.toRoute()
            World5ScreenF(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.recordAttempt(5, 'F') },
                onSuccess = {
                    navController.navigate(World5ScreenG(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World5ScreenG> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 5)
            }
            val route: World5ScreenG = backStackEntry.toRoute()
            World5ScreenG(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.recordAttempt(5, 'G') },
                onSuccess = {
                    navController.navigate(World5ScreenH)
                }
            )
        }
        composable<World5ScreenH> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
            }
            World5ScreenH(
                onRating = {
                    viewModel.completeWorld(5)
                    if (playerProfile?.hasRated == true) {
                        navController.navigate(HallOfBadgesRoute) {
                            popUpTo<Screen3> { inclusive = false }
                        }
                    } else {
                        navController.navigate(RatingScreenRoute) {
                            popUpTo<Screen3> { inclusive = false }
                        }
                    }
                }
            )
        }
    }
}
