package g54720.quickfood.ui.screen


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import g54720.quickfood.R
import g54720.quickfood.database.QuickUiValue
import g54720.quickfood.ui.theme.QuickFoodTheme
import g54720.quickfood.ui.viewmodel.QuickFoodViewModel

@SuppressLint("MutableCollectionMutableState")
@Composable
fun SearchScreen(
    uiValue: QuickUiValue,
    navigationRecipe:()->Unit,
    quickFoodViewModel: QuickFoodViewModel,
){
    var tagsSelect by rememberSaveable {
        mutableStateOf(quickFoodViewModel.uiValue.value.tagSelect)
    }
    var tags by rememberSaveable {
        mutableStateOf(quickFoodViewModel.uiValue.value.tags)
    }

    LaunchedEffect(uiValue){
        tagsSelect=quickFoodViewModel.uiValue.value.tagSelect
        tags=quickFoodViewModel.uiValue.value.tags
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.title_Search),
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
        Column (
            modifier= Modifier
                .fillMaxWidth()
                .weight(0.5f)
        ){
            TagSelectBox(
                modifier=Modifier.weight(1f),
                navSearch = navigationRecipe,
                onTagRemoveClicked = {
                                     quickFoodViewModel.removeTagSelection(it)
                                     },
                quickFoodViewModel = quickFoodViewModel,
                tagsSelect = tagsSelect
            )
            TagBox(
                Modifier.weight(1f),
                onTagAddClicked = {
                                  quickFoodViewModel.addTagSelection(it)
                                  },
                tags = tags
            )
        }
    }
}

@Composable
fun TagSelectBox(
    modifier: Modifier=Modifier,
    navSearch:()->Unit,
    onTagRemoveClicked:(String)->Unit,
    tagsSelect:List<String>,
    quickFoodViewModel: QuickFoodViewModel
){
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        if(tagsSelect.isNotEmpty()){
            val chunkedTags = tagsSelect.chunked(2)
            LazyColumn(
                modifier=modifier.fillMaxWidth(),
            ){
                items(chunkedTags){ tagList->
                    Row {
                        tagList.forEach { tag->
                            Button(
                                onClick = {
                                    onTagRemoveClicked(tag)
                                },
                                colors=ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ){
                                Text(text = tag)
                            }
                        }
                    }
                }
            }
        }else{
            Text(text= stringResource(R.string.description_empty_selected))
        }
        IconButton(onClick = {
            quickFoodViewModel.getRecipeByIngredient(
                quickFoodViewModel.uiValue.value.tagSelect)
            navSearch()
        }) {
            Icon(Icons.Filled.Search,
                contentDescription = stringResource(
                    R.string.button_recipe_description)
            )
        }
    }
}

@Composable
fun TagBox(
    modifier: Modifier=Modifier,
    onTagAddClicked:(String)->Unit,
    tags:List<String>
){
    if(tags.isEmpty()){
        Text(text = stringResource(R.string.description_empty_tags))
    }else{
        LazyColumn(
            modifier=modifier.fillMaxWidth(),
        ){
            val chunkedListsTags= tags.chunked(3)
            items(chunkedListsTags){ tagList->
                Row{
                    tagList.forEach{tag->
                        Button(
                            onClick = {
                                onTagAddClicked(tag)
                            }
                        ){
                            Text(text = tag)
                        }
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview(){
    QuickFoodTheme {
        SearchScreen(navigationRecipe = {},
            uiValue = QuickUiValue(),
            quickFoodViewModel = QuickFoodViewModel(),
        )
    }
}