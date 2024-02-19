package g54720.quickfood.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import g54720.quickfood.R
import g54720.quickfood.database.QuickUiValue
import g54720.quickfood.database.RecipeItem
import g54720.quickfood.ui.ErrorUiState
import g54720.quickfood.ui.theme.QuickFoodTheme
import g54720.quickfood.ui.viewmodel.QuickFoodViewModel

@Composable
fun FavoritesScreen(
    quickFoodViewModel: QuickFoodViewModel,
    navigationRecipe:()->Unit,
    uiValue: QuickUiValue
){
    when(quickFoodViewModel.errorUiState){
        is ErrorUiState.Success -> FavoritesView(
            quickFoodViewModel=quickFoodViewModel
            ,uiValue=uiValue
            ,navigationRecipe=navigationRecipe
        )
        is ErrorUiState.Loading -> ViewLoading()
        is ErrorUiState.Error -> ViewError((
                quickFoodViewModel.errorUiState
                        as ErrorUiState.Error).message)
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun FavoritesView(
    quickFoodViewModel: QuickFoodViewModel,
                  uiValue: QuickUiValue,
                  navigationRecipe: () -> Unit
){
    var favoritesList by remember {
        mutableStateOf(quickFoodViewModel.uiValue.value.favoritesList)
    }
    LaunchedEffect(uiValue){
        favoritesList=quickFoodViewModel.uiValue.value.favoritesList
    }
    Column {
        Text(
            text= stringResource(R.string.title_Favorites),
            style = TextStyle(
                fontSize = dimensionResource(R.dimen.title_size).value.sp,
                fontWeight = FontWeight.Bold,
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer),
            color = MaterialTheme.colorScheme.primary
        )
        FavoritesTable(
            favoritesList= favoritesList,
            onDetailRecipeClick = {
                quickFoodViewModel.getRecipeById(it)
                navigationRecipe()
            }
        )
    }

}

@Composable
fun FavoritesTable( favoritesList:List<RecipeItem>,
                    onDetailRecipeClick:(Int)->Unit
){
    if(favoritesList.isEmpty()){
        Text(text = stringResource(R.string.description_empty_favorites))
    }else {
        LazyColumn(modifier = Modifier) {
            items(favoritesList) { favorite->
                Row {
                        Button(
                            onClick = {
                                onDetailRecipeClick(favorite.id)
                            },
                            modifier = Modifier.fillMaxHeight(),
                        ) {
                            Text(
                                text = favorite.title,
                                textAlign = TextAlign.Center,
                            )
                        }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenPreview(){
    QuickFoodTheme {
        FavoritesScreen(
            navigationRecipe = {},
            uiValue = QuickUiValue(),
            quickFoodViewModel = QuickFoodViewModel()
        )
    }
}
