package com.example.dreamscape.Views

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label

@Composable
fun MainBottomNavScreen(rootNavController: NavController) {
    val navController = rememberNavController()
    Scaffold (
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ){
            composable("home") { HomeScreen(rootNavController)  }
            composable("post") { PostScreen(rootNavController)  }
            composable("profile") { ProfileScreen(rootNavController)  }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("home", "Home", Icons.Filled.Home),
        BottomNavItem("post", "Post", Icons.Filled.AddCircle),
        BottomNavItem("profile", "Profile", Icons.Filled.Person)
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination


    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                          },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }

            )
        }
    }
}

data class BottomNavItem(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)