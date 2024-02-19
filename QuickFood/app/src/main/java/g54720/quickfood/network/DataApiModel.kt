package g54720.quickfood.network

import kotlinx.serialization.Serializable

@Serializable
data class RecipeList(
    val recipes:List<RecipeDetail>
)

@Serializable
data class RecipeDetail(
    val id:Int,
    //val vegan:Boolean,
    //val vegetarian:Boolean,
    val pricePerServing:Double,
    val title:String,
    val readyInMinutes:Int,
    val image:String,
    val analyzedInstructions:List<AnalyzedInstruction>,
)

@Serializable
data class AnalyzedInstruction(
    val name:String,
    val steps:List<Step>
)

@Serializable
data class Step(
    val number:Int,
    val step:String
)
