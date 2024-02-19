package g54720.quickfood.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import g54720.quickfood.database.AnalyzedInstructionItem
import g54720.quickfood.database.QuickUiValue
import g54720.quickfood.database.RecipeItem
import g54720.quickfood.database.StepItem
import g54720.quickfood.model.Repository
import g54720.quickfood.network.SpoonacularApi
import g54720.quickfood.ui.ErrorUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuickFoodViewModel:ViewModel() {
    private val _uiValue= MutableStateFlow(QuickUiValue())
    val uiValue: StateFlow<QuickUiValue> = _uiValue.asStateFlow()
    var errorUiState: ErrorUiState by mutableStateOf(ErrorUiState.Loading)
        private set

    init {
        getRecipesFavorites()
        getHistories()
    }
    fun getRecipeById(id:Int){
        viewModelScope.launch {
            errorUiState=try {
                _uiValue.update { currentState->
                    currentState.copy(
                        recipeSelect = mutableStateOf(Repository.getRecipeById(id)),
                        instruction = mutableStateOf(Repository.getAnalyzedInstructionByRecipeIdInDB(id)),
                        stepsList = Repository.getStepsByInstructionId(
                            Repository.getAnalyzedInstructionByRecipeIdInDB(id).id).toMutableList()
                    )
                }
                ErrorUiState.Success
            }catch (e:Exception){
                ErrorUiState.Error("Error:${e.message}")
            }
        }
    }
    fun getRecipeByIngredient(ingredients:List<String>){
        viewModelScope.launch {
            errorUiState=try {
                var tags=""
                ingredients.forEach { ingredient->
                    tags="${ingredient},${tags}"
                }
                val search= SpoonacularApi.retrofitService.getRecipeRandom(tags=tags)
                var recipeItem=Repository.getRecipeById(search.recipes[0].id)
                if(recipeItem.id==-1){
                    recipeItem= RecipeItem(search.recipes[0].id,
                        search.recipes[0].pricePerServing,
                        search.recipes[0].title,search.recipes[0].readyInMinutes,
                        search.recipes[0].image)
                }
                val instructionItem= AnalyzedInstructionItem(
                    name = search.recipes[0].analyzedInstructions[0].name,
                    recipeId = recipeItem.id,
                )
                val stepItems= mutableListOf<StepItem>()
                search.recipes[0].analyzedInstructions[0].steps.forEach { step->
                    stepItems.add(
                        StepItem(number = step.number, info = step.step,
                        instructionId = instructionItem.id)
                    )
                }
                _uiValue.update { currentState->
                    currentState.copy(
                        recipeSelect = mutableStateOf(recipeItem),
                        instruction = mutableStateOf(instructionItem),
                        stepsList = stepItems.toMutableList(),
                        tagSelect = ingredients.toMutableList()
                    )
                }
                createRecipe()
                ErrorUiState.Success
            }catch (e:Exception){
                ErrorUiState.Error("Error:${e.message}")
            }
        }
    }

    private fun createRecipe(){
        viewModelScope.launch {
            errorUiState=try {
                val recipe=_uiValue.value.recipeSelect
                val instruction=_uiValue.value.instruction
                Repository.insertRecipeInDB(
                    id = recipe.value.id,
                    title = recipe.value.title,
                    price = recipe.value.pricePerServing,
                    timeMinute = recipe.value.readyInMinutes,
                    image = recipe.value.imageSrc,
                )
                Repository.insertAnalyzedInstructionInDB(
                    id= instruction.value.id,
                    name= instruction.value.name,
                    recipeId= recipe.value.id
                )
                instruction.value=Repository.getAnalyzedInstructionByRecipeIdInDB(recipe.value.id)
                _uiValue.value.stepsList.forEach {  step->
                    Repository.insertStepsInDB(
                        step.id,step.number,step.info,instruction.value.id)
                }
                ErrorUiState.Success
            }catch (e:Exception){
                ErrorUiState.Error("Error:${e.message}")
            }
        }
    }
    fun updateFavorites(){
        viewModelScope.launch {
            errorUiState=try {
                val recipe=_uiValue.value.recipeSelect
                Repository.changeRecipeFavoriteInDB(recipe.value.id, !recipe.value.favorite)
                _uiValue.update { currentState->
                    currentState.copy(
                        recipeSelect = mutableStateOf(Repository.getRecipeById(recipe.value.id)),
                        favoritesList = Repository.getAllRecipesFavoritesInDb().toMutableList()
                    )
                }
                ErrorUiState.Success
            }catch (e:Exception){
                ErrorUiState.Error("Error:${e.message}")
            }
        }
    }
    fun refreshRecipe(){
        getRecipeByIngredient(_uiValue.value.tagSelect)
    }

    fun getHistories(){
        viewModelScope.launch {
            errorUiState=try {
                val histories=Repository.getAllRecipesInDB()
                _uiValue.update { currentState->
                    currentState.copy(
                        histories = histories.toMutableList()
                    )
                }
                ErrorUiState.Success
            }catch (e:Exception){
                ErrorUiState.Error("Error:${e.message}")
            }
        }
    }

    fun removeHistoriesInDB(){
        viewModelScope.launch {
            errorUiState=try {
                Repository.removeHistoriesInDB()
                Repository.removeAnalyzedInstructionInDB()
                Repository.removeStepInDB()
                getHistories()
                ErrorUiState.Success
            }catch (e:Exception){
                ErrorUiState.Error("Error:${e.message}")
            }
        }
    }

    fun removeAllFavoritesInDB(){
        viewModelScope.launch {
            errorUiState=try {
                Repository.removeFavoritesFromRecipesInDB()
                _uiValue.update { currentState->
                    currentState.copy(
                        histories = Repository.getAllRecipesInDB().toMutableList()
                    )
                }
                ErrorUiState.Success
            }catch (e:Exception){
                ErrorUiState.Error("Error:${e.message}")
            }
        }
    }

    fun getRecipesFavorites(){
        viewModelScope.launch {
            errorUiState=try {
                _uiValue.update { currentState->
                    currentState.copy(
                        favoritesList = Repository.getAllRecipesFavoritesInDb().toMutableList()
                    )
                }
                ErrorUiState.Success
            }catch (e:Exception){
                ErrorUiState.Error("${e.message}")
            }
        }
    }

    fun addTagSelection(tag:String){
        val listSelect:MutableList<String> = mutableListOf()
        listSelect.addAll(_uiValue.value.tagSelect)
        if(!_uiValue.value.tagSelect.contains(tag)){
            listSelect.add(tag)
        }
        _uiValue.update { currentState->
            currentState.copy(
                tagSelect = listSelect
            )
        }
    }

    fun removeTagSelection(tag:String){
        val listSelect:MutableList<String> = mutableListOf()
        listSelect.addAll(_uiValue.value.tagSelect)
        if(_uiValue.value.tagSelect.contains(tag)){
            listSelect.remove(tag)
        }
        _uiValue.update { currentState->
            currentState.copy(
                tagSelect = listSelect
            )
        }
    }
}