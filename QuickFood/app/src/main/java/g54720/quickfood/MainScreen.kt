package g54720.quickfood

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import g54720.quickfood.ui.screen.BottomBar
import g54720.quickfood.ui.screen.FavoritesScreen
import g54720.quickfood.ui.screen.HistoriesScreen
import g54720.quickfood.ui.screen.RecipeScreen
import g54720.quickfood.ui.screen.SearchScreen
import g54720.quickfood.ui.screen.TopBar
import g54720.quickfood.ui.theme.QuickFoodTheme
import g54720.quickfood.ui.viewmodel.QuickFoodViewModel

enum class MainScreen{
    Search,
    Favorites,
    Histories,
    Recipe
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    modifier:Modifier=Modifier,
    quickFoodViewModel: QuickFoodViewModel=viewModel()
){
    val navController= rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen=backStackEntry?.destination?.route?:MainScreen.Search.name

    val uiValue by quickFoodViewModel.uiValue.collectAsState()
    Scaffold (
        bottomBar = {
            BottomBar(
                onHistoryButton = { navController.navigate(MainScreen.Histories.name)},
                onSearchBottom = { navController.navigate(MainScreen.Search.name) },
                onFavoritesButton = {navController.navigate(MainScreen.Favorites.name)},
                currentScreen = currentScreen,
                quickFoodViewModel = quickFoodViewModel
             )
        },
        topBar = {
            TopBar(
                navigateUp = {navController.navigateUp()},
                canNavigateUp = navController.previousBackStackEntry!=null)
        }
    ){innerPadding->
        NavHost(
            navController = navController,
            startDestination = MainScreen.Search.name,
            modifier=modifier.padding(innerPadding)
        ){
            composable(
                route=MainScreen.Search.name
            ){
                SearchScreen(
                    navigationRecipe={
                        navController.navigate(MainScreen.Recipe.name)
                    },
                    quickFoodViewModel = quickFoodViewModel,
                    uiValue = uiValue
                )
            }
            composable(
                route=MainScreen.Histories.name
            ){
                HistoriesScreen(
                    quickFoodViewModel = quickFoodViewModel,
                    navigationRecipe = {
                        navController.navigate(MainScreen.Recipe.name)
                    },
                    uiValue=uiValue
                )
            }
            composable(
                route=MainScreen.Favorites.name
            ){
                FavoritesScreen(
                    navigationRecipe={
                        navController.navigate(MainScreen.Recipe.name)
                    },
                    quickFoodViewModel = quickFoodViewModel,
                    uiValue = uiValue
                )
            }
            composable(
                route=MainScreen.Recipe.name
            ){
                RecipeScreen(
                    quickFoodViewModel=quickFoodViewModel,
                    uiValue=uiValue,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview(){
    QuickFoodTheme {
        MainApp()
    }
}