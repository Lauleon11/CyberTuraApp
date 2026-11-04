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
import androidx.navigation.NavController
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
    val introNarrativeConsumed by viewModel.introNarrativeConsumed.collectAsState()
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
    // The Room write is asynchronous, so combine it with the in-memory session
    // flag to make sure the intro never replays while navigating between worlds.
    val hasPlayedIntroNarrative =
        (playerProfile?.hasPlayedIntroNarrative ?: false) || introNarrativeConsumed

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
                    if (world.id <= unlockedWorldMax) {
                        val worldState = worlds.find { it.world.id == world.id }
                        val currentScreen = worldState?.progress?.currentScreen ?: "A"
                        navigateWorld(navController, world.id, currentScreen, userGender, userCostumeId)
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
                onNewPlayer = {
                    viewModel.startNewPlayer()
                    navController.navigate(Screen1) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable<RatingScreenRoute> {
            RatingScreen(
                onRatingSubmitted = { stars, comment, answers ->
                    viewModel.saveSurvey(stars, comment, answers)
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
                viewModel.updateWorldScreen(1, "A")
            }
            World1ScreenA(onNextClick = {
                navController.navigate(World1ScreenAActivityRoute)
            })
        }
        composable<World1ScreenAActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 1)
                viewModel.updateWorldScreen(1, "AActivity")
            }
            World1ScreenAActivity(
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.addScore(-5) },
                onComplete = { time, errors, attempts ->
                    viewModel.recordActivityAMetrics(1, time, errors, attempts)
                },
                onNextClick = {
                    navController.navigate(World1ScreenB)
                }
            )
        }
        composable<World1ScreenB> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
                viewModel.updateWorldScreen(1, "B")
            }
            World1ScreenB(onNextClick = {
                navController.navigate(World1ScreenBActivityRoute)
            })
        }
        composable<World1ScreenBActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 1)
                viewModel.updateWorldScreen(1, "BActivity")
            }
            World1ScreenBActivity(
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.addScore(-5) },
                onComplete = { time, errors, attempts ->
                    viewModel.recordActivityBMetrics(1, time, errors, attempts)
                },
                onNextClick = {
                    navController.navigate(World1ScreenC)
                }
            )
        }
        composable<World1ScreenC> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
                viewModel.updateWorldScreen(1, "C")
            }
            World1ScreenC(onNextClick = {
                navController.navigate(World1ScreenCActivityRoute)
            })
        }
        composable<World1ScreenCActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 1)
                viewModel.updateWorldScreen(1, "CActivity")
            }
            World1ScreenCActivity(
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onComplete = { time, moves ->
                    viewModel.recordActivityCMetrics(1, time, moves)
                },
                onNextClick = {
                    navController.navigate(World1ScreenD)
                }
            )
        }
        composable<World1ScreenD> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
                viewModel.updateWorldScreen(1, "D")
            }
            World1ScreenD(onNextClick = {
                navController.navigate(World1ScreenE(gender = userGender, costumeId = userCostumeId))
            })
        }
        composable<World1ScreenE> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 1)
                viewModel.updateWorldScreen(1, "E")
            }
            val metrics = rememberQuestionMetrics(viewModel, 1, 'E')
            val route: World1ScreenE = backStackEntry.toRoute()
            World1ScreenE(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = {
                    viewModel.addScore(10)
                    metrics.onCorrect()
                },
                onIncorrectAttempt = {
                    viewModel.addScore(-5)
                    metrics.onIncorrect()
                },
                onSuccess = {
                    navController.navigate(World1ScreenF(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World1ScreenF> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 1)
                viewModel.updateWorldScreen(1, "F")
            }
            val metrics = rememberQuestionMetrics(viewModel, 1, 'F')
            val route: World1ScreenF = backStackEntry.toRoute()
            World1ScreenF(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = {
                    viewModel.addScore(10)
                    metrics.onCorrect()
                },
                onIncorrectAttempt = {
                    viewModel.addScore(-5)
                    metrics.onIncorrect()
                },
                onSuccess = {
                    navController.navigate(World1ScreenG(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World1ScreenG> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 1)
                viewModel.updateWorldScreen(1, "G")
            }
            val metrics = rememberQuestionMetrics(viewModel, 1, 'G')
            val route: World1ScreenG = backStackEntry.toRoute()
            World1ScreenG(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = {
                    viewModel.addScore(10)
                    metrics.onCorrect()
                },
                onIncorrectAttempt = {
                    viewModel.addScore(-5)
                    metrics.onIncorrect()
                },
                onSuccess = {
                    navController.navigate(World1ScreenH)
                }
            )
        }
        composable<World1ScreenH> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
                viewModel.updateWorldScreen(1, "H")
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
                viewModel.updateWorldScreen(2, "A")
            }
            World2ScreenA(onNextClick = {
                navController.navigate(World2ScreenAActivityRoute)
            })
        }
        composable<World2ScreenAActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 2)
                viewModel.updateWorldScreen(2, "AActivity")
            }
            World2ScreenAActivity(
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.addScore(-5) },
                onComplete = { time, errors, attempts ->
                    viewModel.recordActivityAMetrics(2, time, errors, attempts)
                },
                onNextClick = {
                    navController.navigate(World2ScreenB)
                }
            )
        }
        composable<World2ScreenB> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
                viewModel.updateWorldScreen(2, "B")
            }
            World2ScreenB(onNextClick = {
                navController.navigate(World2ScreenBActivityRoute)
            })
        }
        composable<World2ScreenBActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 2)
                viewModel.updateWorldScreen(2, "BActivity")
            }
            World2ScreenBActivity(
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.addScore(-5) },
                onComplete = { time, errors, attempts ->
                    viewModel.recordActivityBMetrics(2, time, errors, attempts)
                },
                onNextClick = {
                    navController.navigate(World2ScreenC)
                }
            )
        }
        composable<World2ScreenC> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
                viewModel.updateWorldScreen(2, "C")
            }
            World2ScreenC(onNextClick = {
                navController.navigate(World2ScreenCActivityRoute)
            })
        }
        composable<World2ScreenCActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 2)
                viewModel.updateWorldScreen(2, "CActivity")
            }
            World2ScreenCActivity(
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onComplete = { time, moves ->
                    viewModel.recordActivityCMetrics(2, time, moves)
                },
                onNextClick = {
                    navController.navigate(World2ScreenD)
                }
            )
        }
        composable<World2ScreenD> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
                viewModel.updateWorldScreen(2, "D")
            }
            World2ScreenD(onNextClick = {
                navController.navigate(World2ScreenE(gender = userGender, costumeId = userCostumeId))
            })
        }
        composable<World2ScreenE> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 2)
                viewModel.updateWorldScreen(2, "E")
            }
            val metrics = rememberQuestionMetrics(viewModel, 2, 'E')
            val route: World2ScreenE = backStackEntry.toRoute()
            World2ScreenE(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = {
                    viewModel.addScore(10)
                    metrics.onCorrect()
                },
                onIncorrectAttempt = {
                    viewModel.addScore(-5)
                    metrics.onIncorrect()
                },
                onSuccess = {
                    navController.navigate(World2ScreenF(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World2ScreenF> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 2)
                viewModel.updateWorldScreen(2, "F")
            }
            val metrics = rememberQuestionMetrics(viewModel, 2, 'F')
            val route: World2ScreenF = backStackEntry.toRoute()
            World2ScreenF(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = {
                    viewModel.addScore(10)
                    metrics.onCorrect()
                },
                onIncorrectAttempt = {
                    viewModel.addScore(-5)
                    metrics.onIncorrect()
                },
                onSuccess = {
                    navController.navigate(World2ScreenG(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World2ScreenG> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 2)
                viewModel.updateWorldScreen(2, "G")
            }
            val metrics = rememberQuestionMetrics(viewModel, 2, 'G')
            val route: World2ScreenG = backStackEntry.toRoute()
            World2ScreenG(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = {
                    viewModel.addScore(10)
                    metrics.onCorrect()
                },
                onIncorrectAttempt = {
                    viewModel.addScore(-5)
                    metrics.onIncorrect()
                },
                onSuccess = {
                    navController.navigate(World2ScreenH)
                }
            )
        }
        composable<World2ScreenH> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
                viewModel.updateWorldScreen(2, "H")
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
                viewModel.updateWorldScreen(3, "A")
            }
            World3ScreenA(onNextClick = {
                navController.navigate(World3ScreenAActivityRoute)
            })
        }
        composable<World3ScreenAActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 3)
                viewModel.updateWorldScreen(3, "AActivity")
            }
            World3ScreenAActivity(
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.addScore(-5) },
                onComplete = { time, errors, attempts ->
                    viewModel.recordActivityAMetrics(3, time, errors, attempts)
                },
                onNextClick = {
                    navController.navigate(World3ScreenB)
                }
            )
        }
        composable<World3ScreenB> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
                viewModel.updateWorldScreen(3, "B")
            }
            World3ScreenB(onNextClick = {
                navController.navigate(World3ScreenBActivityRoute)
            })
        }
        composable<World3ScreenBActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 3)
                viewModel.updateWorldScreen(3, "BActivity")
            }
            World3ScreenBActivity(
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.addScore(-5) },
                onComplete = { time, errors, attempts ->
                    viewModel.recordActivityBMetrics(3, time, errors, attempts)
                },
                onNextClick = {
                    navController.navigate(World3ScreenC)
                }
            )
        }
        composable<World3ScreenC> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
                viewModel.updateWorldScreen(3, "C")
            }
            World3ScreenC(onNextClick = {
                navController.navigate(World3ScreenCActivityRoute)
            })
        }
        composable<World3ScreenCActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 3)
                viewModel.updateWorldScreen(3, "CActivity")
            }
            World3ScreenCActivity(
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onComplete = { time, moves ->
                    viewModel.recordActivityCMetrics(3, time, moves)
                },
                onNextClick = {
                    navController.navigate(World3ScreenD)
                }
            )
        }
        composable<World3ScreenD> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
                viewModel.updateWorldScreen(3, "D")
            }
            World3ScreenD(onNextClick = {
                navController.navigate(World3ScreenE(gender = userGender, costumeId = userCostumeId))
            })
        }
        composable<World3ScreenE> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 3)
                viewModel.updateWorldScreen(3, "E")
            }
            val metrics = rememberQuestionMetrics(viewModel, 3, 'E')
            val route: World3ScreenE = backStackEntry.toRoute()
            World3ScreenE(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = {
                    viewModel.addScore(10)
                    metrics.onCorrect()
                },
                onIncorrectAttempt = {
                    viewModel.addScore(-5)
                    metrics.onIncorrect()
                },
                onSuccess = {
                    navController.navigate(World3ScreenF(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World3ScreenF> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 3)
                viewModel.updateWorldScreen(3, "F")
            }
            val metrics = rememberQuestionMetrics(viewModel, 3, 'F')
            val route: World3ScreenF = backStackEntry.toRoute()
            World3ScreenF(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = {
                    viewModel.addScore(10)
                    metrics.onCorrect()
                },
                onIncorrectAttempt = {
                    viewModel.addScore(-5)
                    metrics.onIncorrect()
                },
                onSuccess = {
                    navController.navigate(World3ScreenG(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World3ScreenG> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 3)
                viewModel.updateWorldScreen(3, "G")
            }
            val metrics = rememberQuestionMetrics(viewModel, 3, 'G')
            val route: World3ScreenG = backStackEntry.toRoute()
            World3ScreenG(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = {
                    viewModel.addScore(10)
                    metrics.onCorrect()
                },
                onIncorrectAttempt = {
                    viewModel.addScore(-5)
                    metrics.onIncorrect()
                },
                onSuccess = {
                    navController.navigate(World3ScreenH)
                }
            )
        }
        composable<World3ScreenH> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
                viewModel.updateWorldScreen(3, "H")
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
                viewModel.updateWorldScreen(4, "A")
            }
            World4ScreenA(onNextClick = {
                navController.navigate(World4ScreenAActivityRoute)
            })
        }
        composable<World4ScreenAActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 4)
                viewModel.updateWorldScreen(4, "AActivity")
            }
            World4ScreenAActivity(
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.addScore(-5) },
                onComplete = { time, errors, attempts ->
                    viewModel.recordActivityAMetrics(4, time, errors, attempts)
                },
                onNextClick = {
                    navController.navigate(World4ScreenB)
                }
            )
        }
        composable<World4ScreenB> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
                viewModel.updateWorldScreen(4, "B")
            }
            World4ScreenB(onNextClick = {
                navController.navigate(World4ScreenBActivityRoute)
            })
        }
        composable<World4ScreenBActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 4)
                viewModel.updateWorldScreen(4, "BActivity")
            }
            World4ScreenBActivity(
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.addScore(-5) },
                onComplete = { time, errors, attempts ->
                    viewModel.recordActivityBMetrics(4, time, errors, attempts)
                },
                onNextClick = {
                    navController.navigate(World4ScreenC)
                }
            )
        }
        composable<World4ScreenC> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
                viewModel.updateWorldScreen(4, "C")
            }
            World4ScreenC(onNextClick = {
                navController.navigate(World4ScreenCActivityRoute)
            })
        }
        composable<World4ScreenCActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 4)
                viewModel.updateWorldScreen(4, "CActivity")
            }
            World4ScreenCActivity(
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onComplete = { time, moves ->
                    viewModel.recordActivityCMetrics(4, time, moves)
                },
                onNextClick = {
                    navController.navigate(World4ScreenD)
                }
            )
        }
        composable<World4ScreenD> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
                viewModel.updateWorldScreen(4, "D")
            }
            World4ScreenD(onNextClick = {
                navController.navigate(World4ScreenE(gender = userGender, costumeId = userCostumeId))
            })
        }
        composable<World4ScreenE> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 4)
                viewModel.updateWorldScreen(4, "E")
            }
            val metrics = rememberQuestionMetrics(viewModel, 4, 'E')
            val route: World4ScreenE = backStackEntry.toRoute()
            World4ScreenE(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = {
                    viewModel.addScore(10)
                    metrics.onCorrect()
                },
                onIncorrectAttempt = {
                    viewModel.addScore(-5)
                    metrics.onIncorrect()
                },
                onSuccess = {
                    navController.navigate(World4ScreenF(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World4ScreenF> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 4)
                viewModel.updateWorldScreen(4, "F")
            }
            val metrics = rememberQuestionMetrics(viewModel, 4, 'F')
            val route: World4ScreenF = backStackEntry.toRoute()
            World4ScreenF(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = {
                    viewModel.addScore(10)
                    metrics.onCorrect()
                },
                onIncorrectAttempt = {
                    viewModel.addScore(-5)
                    metrics.onIncorrect()
                },
                onSuccess = {
                    navController.navigate(World4ScreenG(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World4ScreenG> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 4)
                viewModel.updateWorldScreen(4, "G")
            }
            val metrics = rememberQuestionMetrics(viewModel, 4, 'G')
            val route: World4ScreenG = backStackEntry.toRoute()
            World4ScreenG(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = {
                    viewModel.addScore(10)
                    metrics.onCorrect()
                },
                onIncorrectAttempt = {
                    viewModel.addScore(-5)
                    metrics.onIncorrect()
                },
                onSuccess = {
                    navController.navigate(World4ScreenH)
                }
            )
        }
        composable<World4ScreenH> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
                viewModel.updateWorldScreen(4, "H")
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
                viewModel.updateWorldScreen(5, "A")
            }
            World5ScreenA(onNextClick = {
                navController.navigate(World5ScreenAActivityRoute)
            })
        }
        composable<World5ScreenAActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 5)
                viewModel.updateWorldScreen(5, "AActivity")
            }
            World5ScreenAActivity(
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.addScore(-5) },
                onComplete = { time, errors, attempts ->
                    viewModel.recordActivityAMetrics(5, time, errors, attempts)
                },
                onNextClick = {
                    navController.navigate(World5ScreenB)
                }
            )
        }
        composable<World5ScreenB> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
                viewModel.updateWorldScreen(5, "B")
            }
            World5ScreenB(onNextClick = {
                navController.navigate(World5ScreenBActivityRoute)
            })
        }
        composable<World5ScreenBActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 5)
                viewModel.updateWorldScreen(5, "BActivity")
            }
            World5ScreenBActivity(
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onIncorrectAttempt = { viewModel.addScore(-5) },
                onComplete = { time, errors, attempts ->
                    viewModel.recordActivityBMetrics(5, time, errors, attempts)
                },
                onNextClick = {
                    navController.navigate(World5ScreenC)
                }
            )
        }
        composable<World5ScreenC> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
                viewModel.updateWorldScreen(5, "C")
            }
            World5ScreenC(onNextClick = {
                navController.navigate(World5ScreenCActivityRoute)
            })
        }
        composable<World5ScreenCActivityRoute> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 5)
                viewModel.updateWorldScreen(5, "CActivity")
            }
            World5ScreenCActivity(
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = { viewModel.addScore(10) },
                onComplete = { time, moves ->
                    viewModel.recordActivityCMetrics(5, time, moves)
                },
                onNextClick = {
                    navController.navigate(World5ScreenD)
                }
            )
        }
        composable<World5ScreenD> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
                viewModel.updateWorldScreen(5, "D")
            }
            World5ScreenD(onNextClick = {
                navController.navigate(World5ScreenE(gender = userGender, costumeId = userCostumeId))
            })
        }
        composable<World5ScreenE> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 5)
                viewModel.updateWorldScreen(5, "E")
            }
            val metrics = rememberQuestionMetrics(viewModel, 5, 'E')
            val route: World5ScreenE = backStackEntry.toRoute()
            World5ScreenE(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = {
                    viewModel.addScore(10)
                    metrics.onCorrect()
                },
                onIncorrectAttempt = {
                    viewModel.addScore(-5)
                    metrics.onIncorrect()
                },
                onSuccess = {
                    navController.navigate(World5ScreenF(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World5ScreenF> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 5)
                viewModel.updateWorldScreen(5, "F")
            }
            val metrics = rememberQuestionMetrics(viewModel, 5, 'F')
            val route: World5ScreenF = backStackEntry.toRoute()
            World5ScreenF(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = {
                    viewModel.addScore(10)
                    metrics.onCorrect()
                },
                onIncorrectAttempt = {
                    viewModel.addScore(-5)
                    metrics.onIncorrect()
                },
                onSuccess = {
                    navController.navigate(World5ScreenG(gender = route.gender, costumeId = route.costumeId))
                }
            )
        }
        composable<World5ScreenG> { backStackEntry ->
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.startWorldMusic(context, 5)
                viewModel.updateWorldScreen(5, "G")
            }
            val metrics = rememberQuestionMetrics(viewModel, 5, 'G')
            val route: World5ScreenG = backStackEntry.toRoute()
            World5ScreenG(
                gender = route.gender,
                costumeId = route.costumeId,
                score = playerProfile?.score ?: 0,
                onCorrectAnswer = {
                    viewModel.addScore(10)
                    metrics.onCorrect()
                },
                onIncorrectAttempt = {
                    viewModel.addScore(-5)
                    metrics.onIncorrect()
                },
                onSuccess = {
                    navController.navigate(World5ScreenH)
                }
            )
        }
        composable<World5ScreenH> {
            LaunchedEffect(Unit) {
                viewModel.pauseWorldMusic()
                viewModel.updateWorldScreen(5, "H")
            }
            World5ScreenH(
                onRating = {
                    viewModel.completeWorld(5)
                    if (playerProfile?.hasRated == true) {
                        navController.navigate(HallOfBadgesRoute) {
                            popUpTo<Screen3> { inclusive = false }
                        }
                    } else {
                        navController.navigate(RatingScreenRoute)
                    }
                }
            )
        }
    }
}

/** Measures one question screen, counting attempts the same way as activities A and B. */
private class QuestionMetrics(private val record: (seconds: Int, errors: Int, attempts: Int) -> Unit) {
    private val startTime = System.currentTimeMillis()
    private var errors = 0

    fun onIncorrect() {
        errors++
    }

    fun onCorrect() {
        val seconds = ((System.currentTimeMillis() - startTime) / 1000).toInt()
        record(seconds, errors, errors + 1)
    }
}

@Composable
private fun rememberQuestionMetrics(viewModel: GameViewModel, worldId: Int, screen: Char) = remember {
    QuestionMetrics { seconds, errors, attempts ->
        viewModel.recordQuestionMetrics(worldId, screen, seconds, errors, attempts)
    }
}

private fun navigateWorld(
    navController: NavController,
    worldId: Int,
    currentScreen: String,
    gender: String,
    costumeId: String
) {
    when (worldId) {
        1 -> when (currentScreen) {
            "A" -> navController.navigate(World1ScreenA)
            "AActivity" -> navController.navigate(World1ScreenAActivityRoute)
            "B" -> navController.navigate(World1ScreenB)
            "BActivity" -> navController.navigate(World1ScreenBActivityRoute)
            "C" -> navController.navigate(World1ScreenC)
            "CActivity" -> navController.navigate(World1ScreenCActivityRoute)
            "D" -> navController.navigate(World1ScreenD)
            "E" -> navController.navigate(World1ScreenE(gender = gender, costumeId = costumeId))
            "F" -> navController.navigate(World1ScreenF(gender = gender, costumeId = costumeId))
            "G" -> navController.navigate(World1ScreenG(gender = gender, costumeId = costumeId))
            "H" -> navController.navigate(World1ScreenH)
            else -> navController.navigate(World1ScreenA)
        }
        2 -> when (currentScreen) {
            "A" -> navController.navigate(World2ScreenA)
            "AActivity" -> navController.navigate(World2ScreenAActivityRoute)
            "B" -> navController.navigate(World2ScreenB)
            "BActivity" -> navController.navigate(World2ScreenBActivityRoute)
            "C" -> navController.navigate(World2ScreenC)
            "CActivity" -> navController.navigate(World2ScreenCActivityRoute)
            "D" -> navController.navigate(World2ScreenD)
            "E" -> navController.navigate(World2ScreenE(gender = gender, costumeId = costumeId))
            "F" -> navController.navigate(World2ScreenF(gender = gender, costumeId = costumeId))
            "G" -> navController.navigate(World2ScreenG(gender = gender, costumeId = costumeId))
            "H" -> navController.navigate(World2ScreenH)
            else -> navController.navigate(World2ScreenA)
        }
        3 -> when (currentScreen) {
            "A" -> navController.navigate(World3ScreenA)
            "AActivity" -> navController.navigate(World3ScreenAActivityRoute)
            "B" -> navController.navigate(World3ScreenB)
            "BActivity" -> navController.navigate(World3ScreenBActivityRoute)
            "C" -> navController.navigate(World3ScreenC)
            "CActivity" -> navController.navigate(World3ScreenCActivityRoute)
            "D" -> navController.navigate(World3ScreenD)
            "E" -> navController.navigate(World3ScreenE(gender = gender, costumeId = costumeId))
            "F" -> navController.navigate(World3ScreenF(gender = gender, costumeId = costumeId))
            "G" -> navController.navigate(World3ScreenG(gender = gender, costumeId = costumeId))
            "H" -> navController.navigate(World3ScreenH)
            else -> navController.navigate(World3ScreenA)
        }
        4 -> when (currentScreen) {
            "A" -> navController.navigate(World4ScreenA)
            "AActivity" -> navController.navigate(World4ScreenAActivityRoute)
            "B" -> navController.navigate(World4ScreenB)
            "BActivity" -> navController.navigate(World4ScreenBActivityRoute)
            "C" -> navController.navigate(World4ScreenC)
            "CActivity" -> navController.navigate(World4ScreenCActivityRoute)
            "D" -> navController.navigate(World4ScreenD)
            "E" -> navController.navigate(World4ScreenE(gender = gender, costumeId = costumeId))
            "F" -> navController.navigate(World4ScreenF(gender = gender, costumeId = costumeId))
            "G" -> navController.navigate(World4ScreenG(gender = gender, costumeId = costumeId))
            "H" -> navController.navigate(World4ScreenH)
            else -> navController.navigate(World4ScreenA)
        }
        5 -> when (currentScreen) {
            "A" -> navController.navigate(World5ScreenA)
            "AActivity" -> navController.navigate(World5ScreenAActivityRoute)
            "B" -> navController.navigate(World5ScreenB)
            "BActivity" -> navController.navigate(World5ScreenBActivityRoute)
            "C" -> navController.navigate(World5ScreenC)
            "CActivity" -> navController.navigate(World5ScreenCActivityRoute)
            "D" -> navController.navigate(World5ScreenD)
            "E" -> navController.navigate(World5ScreenE(gender = gender, costumeId = costumeId))
            "F" -> navController.navigate(World5ScreenF(gender = gender, costumeId = costumeId))
            "G" -> navController.navigate(World5ScreenG(gender = gender, costumeId = costumeId))
            "H" -> navController.navigate(World5ScreenH)
            else -> navController.navigate(World5ScreenA)
        }
    }
}
