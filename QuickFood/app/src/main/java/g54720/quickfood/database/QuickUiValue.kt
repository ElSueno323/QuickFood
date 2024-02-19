package g54720.quickfood.database

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class QuickUiValue(
    val tags:MutableList<String> = mutableListOf(
        "vegetarian","vegan","dessert","popular","cheap",
        "pescetarian","quinoa","gluten","apple","meat",
        "spicy","fast","asian","mexican","french",
        "breakfast","ketogenic","dairy-free","low-fat",
        "sugar-free"
    ),
    val tagSelect:MutableList<String> = mutableListOf(),
    val histories:MutableList<RecipeItem> = mutableListOf(),
    val favoritesList:MutableList<RecipeItem> = mutableListOf(),
    val recipeSelect:MutableState<RecipeItem> = mutableStateOf(RecipeItem()),
    val instruction:MutableState<AnalyzedInstructionItem> =
        mutableStateOf(AnalyzedInstructionItem(name="", recipeId = -1)),
    val stepsList:MutableList<StepItem> = mutableListOf()

)
