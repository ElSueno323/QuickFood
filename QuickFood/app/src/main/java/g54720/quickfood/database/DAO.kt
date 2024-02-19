package g54720.quickfood.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipesDAO {
    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe:RecipeItem)

    @Query("SELECT * FROM Recipes ORDER BY createdAt DESC")
    suspend fun getAllRecipes():List<RecipeItem>

    @Query("SELECT * FROM Recipes WHERE favorite=1")
    suspend fun getAllRecipesFavorite():List<RecipeItem>

    @Query("SELECT * FROM Recipes WHERE id = :id")
    suspend fun getRecipeById(id:Int):List<RecipeItem>

    @Query("UPDATE Recipes SET favorite = :isFavorite WHERE id= :id")
    suspend fun updateFavoriteStatus(id:Int,isFavorite:Boolean )

    @Query("UPDATE Recipes SET favorite = 0")
    suspend fun removeFavoritesFromRecipes()

    @Query("DELETE FROM Recipes")
    suspend fun deleteAllRecipes()

}

@Dao
interface AnalyzedInstructionDAO{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInstruction(instruction: AnalyzedInstructionItem)

    @Query("SELECT * FROM analyzed_instruction_table WHERE recipeId = :recipeId")
    suspend fun getInstructionByRecipeId(recipeId:Int):List<AnalyzedInstructionItem>

    @Query("DELETE FROM analyzed_instruction_table")
    suspend fun deleteAllAnalyzedInstruction()
}

@Dao
interface StepDAO{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(stepItem:StepItem)

    @Query("SELECT * FROM step_table WHERE instructionId = :instructionId")
    suspend fun getStepsByIdInstruction(instructionId:Int):List<StepItem>

    @Query("DELETE FROM step_table")
    suspend fun deleteAllStep()
}