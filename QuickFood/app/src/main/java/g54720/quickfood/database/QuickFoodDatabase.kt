package g54720.quickfood.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities=[RecipeItem::class,AnalyzedInstructionItem::class,StepItem::class],
    version=1, exportSchema = false)
abstract class QuickFoodDatabase:RoomDatabase(){
    abstract fun recipeDao():RecipesDAO
    abstract fun analyzedInstructionsDao():AnalyzedInstructionDAO
    abstract fun stepDao():StepDAO

    companion object{
        private const val DATABASE_NAME="quickfood_db"
        private var sInstance:QuickFoodDatabase?=null

        fun getInstance(context:Context):QuickFoodDatabase{
            if (sInstance==null){

                val dbBuilder = Room.databaseBuilder(
                    context.applicationContext,
                    QuickFoodDatabase::class.java,
                    DATABASE_NAME
                )
                sInstance=dbBuilder.build()
            }
            return sInstance!!
        }
    }
}