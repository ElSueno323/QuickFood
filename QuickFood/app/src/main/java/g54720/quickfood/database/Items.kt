package g54720.quickfood.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Entity(tableName = "recipes")
data class RecipeItem(
    @PrimaryKey
    val id:Int=-1,
    //val vegan:Boolean=false,
    //val vegetarian:Boolean=false,
    val pricePerServing:Double=0.0,
    val title:String="",
    val readyInMinutes:Int=-1,
    val imageSrc:String="",
    val createdAt: String =
        LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        ),
    val favorite:Boolean=false
)

@Entity(tableName = "analyzed_instruction_table")
data class AnalyzedInstructionItem(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val name:String,
    val recipeId:Int,
)

@Entity(tableName = "step_table")
data class StepItem(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val number:Int,
    val info:String,
    val instructionId:Int
)
