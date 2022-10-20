package com.gdsc.nitb.rudra.whatsappclone.modules.home.view.composables

import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gdsc.nitb.rudra.whatsappclone.modules.home.view.nav.NavigationItem
import com.gdsc.nitb.rudra.whatsappclone.modules.home.viewModel.HomeViewModel
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.Notification

/**
 * A bottom navigation for the home page
 */

@Composable
fun HomeBottomNavigation(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel()
) {
    val items = listOf(
        NavigationItem.Chat,
        NavigationItem.Search,
        NavigationItem.Status,
    )

    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Notification
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.size(22.dp)
                    )
                },
                selected = currentRoute == item.route,
                alwaysShowLabel = false,
                unselectedContentColor = Color.Gray,
                selectedContentColor = Notification,
                onClick = {
                    if (currentRoute != item.route) {
                        homeViewModel.updateTitle(item.title)
                        navController.navigate(item.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            // Avoid multiple copies of the same destination when
                            // re-selecting the same item
                            launchSingleTop = true
                            // Restore state when re-selecting a previously selected item
                            restoreState = true
                        }
                    }
                },
            )
        }
    }
}
