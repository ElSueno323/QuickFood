package g54720.quickfood.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import g54720.quickfood.R
import g54720.quickfood.database.QuickUiValue
import g54720.quickfood.database.RecipeItem
import g54720.quickfood.database.StepItem
import g54720.quickfood.ui.ErrorUiState
import g54720.quickfood.ui.theme.QuickFoodTheme
import g54720.quickfood.ui.viewmodel.QuickFoodViewModel

@Composable
fun RecipeScreen(
    uiValue: QuickUiValue,
    quickFoodViewModel: QuickFoodViewModel
){
    when(quickFoodViewModel.errorUiState){
            is ErrorUiState.Success -> RecipeValid(
                quickFoodViewModel =quickFoodViewModel
                ,uiValue)
            is ErrorUiState.Loading -> ViewLoading()
            is ErrorUiState.Error -> ViewError(
                (quickFoodViewModel.errorUiState as ErrorUiState.Error).message)
        }
}

@Composable
fun RecipeValid(
    quickFoodViewModel: QuickFoodViewModel,
                uiValue: QuickUiValue){
    var recipe by remember {
        mutableStateOf(
            quickFoodViewModel.uiValue.value.recipeSelect
        )
    }
    var steps by remember { mutableStateOf(
        quickFoodViewModel.uiValue.value.stepsList
    ) }

    LaunchedEffect(uiValue.recipeSelect ) {
        recipe=quickFoodViewModel.uiValue.value.recipeSelect
        steps=quickFoodViewModel.uiValue.value.stepsList
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = recipe.value.title,
            style = TextStyle(
                fontSize = dimensionResource(R.dimen.title_size).value.sp,
                fontWeight = FontWeight.ExtraBold,
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary
        )
        RecipeBoxImage(
            onAddFavoritesButton = {
                                   quickFoodViewModel.updateFavorites()
                                   },
            onRefreshButton = {
                              quickFoodViewModel.refreshRecipe()
                              },
            recipeItem = recipe.value,
        )
        BoxDetail(recipeItem = recipe.value)
        BoxSteps(
            stepsItem = steps
        )
    }
}
@Composable
fun BoxSteps(
    stepsItem: List<StepItem>
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.title_subtitle_Steps),
            style = TextStyle(
                fontSize = dimensionResource(R.dimen.subtitle_size).value.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            ),
        )
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onPrimaryContainer)
                .fillMaxSize()
        ) {
            stepsItem.forEach { step ->
                Row {
                    Text(
                        text = "${step.number}.",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ),
                    )
                    Text(
                        text = step.info,
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun BoxDetail(
    recipeItem: RecipeItem
){
    Column(
        modifier=Modifier
    ) {
        Row (
            modifier=Modifier
        ){
            Icon(
                painter = painterResource(R.drawable.baseline_alarm_24),
                contentDescription = "Time to cook",
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${recipeItem.readyInMinutes} ${stringResource(R.string.minute_name)}",
                color = MaterialTheme.colorScheme.primary
            )

        }
    }
}

@Composable
fun RecipeBoxImage(
    onRefreshButton:()->Unit,
    onAddFavoritesButton:(Int)->Unit,
    recipeItem: RecipeItem,
){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        AsyncImage(model = recipeItem.imageSrc,
        contentDescription =recipeItem.title )
        Row (
            horizontalArrangement = Arrangement.Center ,
            modifier = Modifier.fillMaxWidth()
        )
        {
                IconButton(
                    onClick = {onRefreshButton()}
                ){
                    Icon(Icons.Filled.Refresh,
                        contentDescription = stringResource(R.string.button_refresh_description),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            IconButton(
                onClick = {
                    onAddFavoritesButton(recipeItem.id)
                    recipeItem.favorite
                },
            ){
                if(recipeItem.favorite){
                    Icon(Icons.Filled.Star,
                        stringResource(R.string.button_remove_favorites_description),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }else{
                    Icon(Icons.TwoTone.Star,
                        stringResource(R.string.button_add_favorites_description),
                        tint = MaterialTheme.colorScheme.primaryContainer)
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun RecipeScreenPreview(){
    QuickFoodTheme {
        RecipeScreen(
            quickFoodViewModel = QuickFoodViewModel(),
            uiValue = QuickUiValue()
        )
    }
}