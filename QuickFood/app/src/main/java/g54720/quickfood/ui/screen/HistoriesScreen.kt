package g54720.quickfood.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import g54720.quickfood.R
import g54720.quickfood.database.QuickUiValue
import g54720.quickfood.database.RecipeItem
import g54720.quickfood.ui.ErrorUiState
import g54720.quickfood.ui.theme.QuickFoodTheme
import g54720.quickfood.ui.viewmodel.QuickFoodViewModel

@Composable
fun HistoriesScreen(
    quickFoodViewModel: QuickFoodViewModel,
    navigationRecipe:()->Unit,
    uiValue: QuickUiValue
){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when(quickFoodViewModel.errorUiState){
            is ErrorUiState.Success -> HistoriesView(
                navigationRecipe=navigationRecipe,
                uiValue=uiValue,
                quickFoodViewModel=quickFoodViewModel
            )
            is ErrorUiState.Loading -> ViewLoading()
            is ErrorUiState.Error -> ViewError(
                (quickFoodViewModel.errorUiState as ErrorUiState.Error).message
            )
        }

    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun HistoriesView(navigationRecipe: () -> Unit,
                  uiValue:QuickUiValue,
                  quickFoodViewModel: QuickFoodViewModel
){
    var histories by rememberSaveable {
        mutableStateOf(quickFoodViewModel.uiValue.value.histories)
    }

    LaunchedEffect(uiValue){
        histories=quickFoodViewModel.uiValue.value.histories
    }
    Text(
        text = stringResource(R.string.title_Histories),
        style = TextStyle(
            fontSize = dimensionResource(R.dimen.title_size).value.sp,
            fontWeight = FontWeight.ExtraBold,
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer),
        color = MaterialTheme.colorScheme.primary
    )
    HistoriesTable(
        navSearch = navigationRecipe,
        historiesList = histories,
        quickFoodViewModel=quickFoodViewModel
    )
    RowButton(
        quickFoodViewModel=quickFoodViewModel
    )
}

@Composable
fun HistoriesTable(
    navSearch:()->Unit,
    historiesList: List<RecipeItem>,
    quickFoodViewModel: QuickFoodViewModel
){

    if (historiesList.isEmpty()){
        Text(text = stringResource(R.string.history_emptied))
    }else{
        LazyColumn(modifier=Modifier.heightIn(
            max= LocalConfiguration.current.screenHeightDp.dp-
                    dimensionResource(R.dimen.histories_size) )){
            items(historiesList){ histories->
                Box(modifier = Modifier){
                    Button(
                        onClick = {
                            quickFoodViewModel.getRecipeById(histories.id)
                            navSearch()
                        },
                        modifier=Modifier.fillMaxHeight(),
                    ){
                        Text(
                            text = "${histories.title} : ${histories.createdAt}",
                            textAlign=TextAlign.Center,
                        )
                    }
                }
            }
        }

    }

}
@Composable
fun RowButton(
    quickFoodViewModel: QuickFoodViewModel
){
    Row(
        modifier= Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ){
        Spacer(modifier=Modifier.weight(0.5f))
        IconButton(
            onClick = {
            quickFoodViewModel.removeAllFavoritesInDB()
            }
        ){
            Icon(
                Icons.Filled.Star,
                contentDescription = stringResource(R.string.button_emptied_favorites_description),
                tint = MaterialTheme.colorScheme.error
                )
        }
        Spacer(modifier=Modifier.weight(0.5f))
        IconButton(
            onClick = {
                quickFoodViewModel.removeHistoriesInDB()
            }
        ){
            Icon(
                Icons.Filled.Delete,
                contentDescription = stringResource(R.string.button_emptied_histories_description),
                tint = MaterialTheme.colorScheme.error
            )
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }
}

@Preview(showBackground = true)
@Composable
fun HistoriesPreview(){
    QuickFoodTheme {
       HistoriesScreen(
           quickFoodViewModel = QuickFoodViewModel(),
           navigationRecipe = {},
           uiValue = QuickUiValue()
       )
    }
}