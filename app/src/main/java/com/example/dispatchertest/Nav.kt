package com.example.dispatchertest

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class Route {
    Q, W, E, R, T, Y;

    companion object {
        fun getRoute(str: String?): Route {
            return entries.firstOrNull { it.name == str } ?: Y
        }
    }
}

@Composable
fun NavigationPage() {
    val coroutineScope = rememberCoroutineScope()

    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    val mainRoute by remember(currentDestination) {
        derivedStateOf {
            Route.getRoute(currentDestination)
        }
    }

    fun popUpNav(route: Route) {
        navController.navigate(route.name) {
            // 清除堆栈，确保没有回退栈
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            // 避免重新创建同一视图
            launchSingleTop = true
            restoreState = false
        }
    }


    Row(Modifier.imePadding()) {
        NavigationRail(
            modifier = Modifier.width(intrinsicSize = IntrinsicSize.Min),
            containerColor = MaterialTheme.colorScheme.primary,

            ) {
            Spacer(modifier = Modifier.weight(1f))
            Route.entries.forEach {
                NavigationRailItem(
                    modifier = Modifier.padding(vertical = 8.dp),
                    selected = mainRoute == it,
                    onClick = { popUpNav(it) },
                    icon = {
                        Text(text = it.name)
                    },
                )
            }
            FloatingActionButton(modifier = Modifier,
//                .size(56.dp)
//                .clip(CircleShape)
//                .background(Color.White),
//                .clickable {
//
//                },
                onClick = {
                coroutineScope.launch {
                    repeat(20) {
                        popUpNav(Route.entries.random())
                        delay(150)
                    }
                    popUpNav(Route.Q)
                }
            }) { Text ("Jump")}
            Spacer(
                modifier = Modifier
                    .navigationBarsPadding()
                    .weight(1f)
            )
        }

        NavHost(
            navController = navController, startDestination = Route.Q.name,
            modifier = Modifier
                .statusBarsPadding()
                .background(MaterialTheme.colorScheme.primary)
                .clip(RoundedCornerShape(topStart = 8.dp))
                .background(MaterialTheme.colorScheme.surface),

            contentAlignment = Alignment.TopStart
        ) {
            Route.entries.forEach { route ->
                composable(
                    route.name,
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() },
                ) {
                    Surface {
                        when (route) {
                            Route.Q -> {
                                DispatcherTest()
                            }

                            else -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) { Text("Blank Page ${route.name}") }
                            }
                        }
                    }
                }
            }
        }
    }
}