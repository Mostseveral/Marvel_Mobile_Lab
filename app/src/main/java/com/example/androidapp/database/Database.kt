package com.example.androidapp.database

import android.content.Context
import androidx.room.*

@Entity(tableName = "heroes")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val image: String
)
@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE) // Игнорировать вставку, если id уже существует
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Query("SELECT * FROM heroes WHERE id = :id")
    suspend fun getCharacterById(id: Int): CharacterEntity?

    @Query("SELECT * FROM heroes")
    suspend fun getAllHeroes(): List<CharacterEntity>

    @Query("DELETE FROM heroes")
    suspend fun clearAllHeroes()

}
@Database(entities = [CharacterEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "heroes_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}