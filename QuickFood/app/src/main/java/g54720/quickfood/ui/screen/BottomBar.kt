package g54720.quickfood.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import g54720.quickfood.MainScreen
import g54720.quickfood.R
import g54720.quickfood.ui.theme.QuickFoodTheme
import g54720.quickfood.ui.viewmodel.QuickFoodViewModel

@Composable
fun BottomBar(
    onHistoryButton:()->Unit,
    onSearchBottom:()->Unit,
    onFavoritesButton:()->Unit,
    currentScreen:String,
    quickFoodViewModel: QuickFoodViewModel
){
    BottomAppBar (
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary
    ){
        Row(
            modifier= Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ){
            Spacer(modifier=Modifier.weight(0.5f))
            IconButton(
                onClick = {
                    if(currentScreen!=MainScreen.Histories.name){
                        quickFoodViewModel.getHistories()
                        onHistoryButton()
                    }
                }
            ){
                Icon(Icons.Filled.DateRange,
                    contentDescription = stringResource(R.string.button_history_description))
            }
            Spacer(modifier = Modifier.weight(0.5f))
            IconButton(
                onClick = {
                    if(currentScreen!=MainScreen.Search.name){
                        onSearchBottom()
                    }
                }
            ){
                Icon(Icons.Filled.Search,
                    contentDescription = stringResource(R.string.button_search_description))
            }
            Spacer(modifier=Modifier.weight(0.5f))
            IconButton(
                onClick = {
                    if(currentScreen!=MainScreen.Favorites.name){
                        quickFoodViewModel.getRecipesFavorites()
                        onFavoritesButton()
                    }
                }
            ){
                Icon(Icons.Filled.Star,
                    contentDescription = stringResource(R.string.button_favorites_description))
            }
            Spacer(modifier = Modifier.weight(0.5f))
        }
    }
}


@Preview(showBackground=true)
@Composable
fun BarPreview(){
    QuickFoodTheme {
        BottomBar(
            onHistoryButton = {},
            onSearchBottom = {},
            onFavoritesButton = {},
            currentScreen = "",
            quickFoodViewModel = QuickFoodViewModel()
        )
    }
}