package hr.konjetic.fishy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hr.konjetic.fishy.database.entities.*

@Database(entities = [FavoriteFish::class, FavoriteFishFamily::class, FavoriteWaterType::class, FavoriteHabitat::class, Aquarium::class, AquariumFish::class], version = 3, exportSchema = false)
@TypeConverters(FishListConverter::class)
abstract class FishDatabase : RoomDatabase() {

    abstract fun getFishDao(): FishDao

    companion object {
        private var instance: FishDatabase? = null

        fun getDatabase(context: Context): FishDatabase? {
            if (instance == null) {
                instance = buildDatabase(context)
            }
            return instance
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                FishDatabase::class.java,
                "FishDatabase"
            )
                .fallbackToDestructiveMigration()
                .build()

    }
}
