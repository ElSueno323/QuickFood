package g54720.quickfood.model

import android.content.Context
import g54720.quickfood.database.AnalyzedInstructionItem
import g54720.quickfood.database.QuickFoodDatabase
import g54720.quickfood.database.RecipeItem
import g54720.quickfood.database.StepItem

object Repository {
    private var database : QuickFoodDatabase?=null

    fun initDatabase(context:Context){
        if(database==null){
            database= QuickFoodDatabase.getInstance(context)
        }
    }

    suspend fun insertRecipeInDB(id:Int,title:String,
                                 price:Double,timeMinute:Int, image:String){
        database?.let { theDB->
            val newRecipe=RecipeItem(id, price, title, timeMinute, image)

            theDB.recipeDao().insertRecipe(newRecipe)
        }
    }

    suspend fun changeRecipeFavoriteInDB(id:Int,isFavorite:Boolean){
        database?.recipeDao()?.updateFavoriteStatus(id=id,isFavorite=isFavorite)
    }


    suspend fun getAllRecipesInDB():List<RecipeItem>{
        database?.let{ theDB->
            return theDB.recipeDao().getAllRecipes()
        }
        return listOf()
    }

    suspend fun getAllRecipesFavoritesInDb():List<RecipeItem>{
        database?.let { theDB->
            return theDB.recipeDao().getAllRecipesFavorite()
        }
        return listOf()
    }

    suspend fun getRecipeById(id:Int):RecipeItem{
        database?.let{ theDB->
            if(theDB.recipeDao().getRecipeById(id).isEmpty()){
                return RecipeItem()
            }
            return theDB.recipeDao().getRecipeById(id).first()
        }
        return RecipeItem()
    }



    suspend fun insertAnalyzedInstructionInDB(id:Int,name:String,recipeId:Int){
        database?.let { theDB->
            val newInstruction=AnalyzedInstructionItem(
                id,name,recipeId
            )
            theDB.analyzedInstructionsDao().insertInstruction(newInstruction)
        }

    }

    suspend fun getAnalyzedInstructionByRecipeIdInDB(idRecipe:Int):AnalyzedInstructionItem{
        database?.let{theDB->
            if(theDB.analyzedInstructionsDao().getInstructionByRecipeId(idRecipe).isEmpty()){
                return AnalyzedInstructionItem(-1,"",-1)
            }
            return theDB.analyzedInstructionsDao().getInstructionByRecipeId(idRecipe).first()
        }
        return AnalyzedInstructionItem(-1,"",-1)
    }

    suspend fun insertStepsInDB(id:Int,number:Int,info:String,instructionId:Int){
        database?.let { theDB->
            val newStep=StepItem(
                id,number, info , instructionId
            )
            theDB.stepDao().insertSteps(newStep)
        }
    }

    suspend fun getStepsByInstructionId(instructionId:Int):List<StepItem>{
        database?.let{theDB->
            if(theDB.stepDao().getStepsByIdInstruction(instructionId).isEmpty()){
                return listOf()
            }
            return theDB.stepDao().getStepsByIdInstruction(instructionId)
        }
        return listOf()
    }

    suspend fun removeHistoriesInDB(){
        database?.recipeDao()?.deleteAllRecipes()
    }

    suspend fun removeAnalyzedInstructionInDB(){
        database?.analyzedInstructionsDao()?.deleteAllAnalyzedInstruction()
    }

    suspend fun removeStepInDB(){
        database?.stepDao()?.deleteAllStep()
    }

    suspend fun removeFavoritesFromRecipesInDB(){
        database?.recipeDao()?.removeFavoritesFromRecipes()
    }
}