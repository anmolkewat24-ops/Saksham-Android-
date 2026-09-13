package com.example.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "saved_schemes")
data class SavedSchemeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val department: String,
    val category: String,
    val maxLoan: String,
    val interestRate: String,
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_plans")
data class SavedPlanEntity(
    @PrimaryKey val id: String,
    val businessType: String,
    val summary: String,
    val totalInvestment: Long,
    val loanRequired: Long,
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_partners")
data class SavedPartnerEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String,
    val phone: String,
    val address: String,
    val distance: Double,
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val fullName: String = "",
    val phone: String = "",
    val email: String = "",
    val age: Int = 0,
    val gender: String = "",
    val state: String = "",
    val district: String = "",
    val socialCategory: String = "",
    val familyIncome: String = "",
    val selectedLanguage: String = "English",
    val photoUri: String? = null,
    val activeBusinessTarget: String = "",
    val isLoggedIn: Boolean = false
)

@Dao
interface SakshamDao {
    // Schemes
    @Query("SELECT * FROM saved_schemes ORDER BY savedAt DESC")
    fun getSavedSchemes(): Flow<List<SavedSchemeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveScheme(scheme: SavedSchemeEntity)

    @Query("DELETE FROM saved_schemes WHERE id = :schemeId")
    suspend fun removeScheme(schemeId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_schemes WHERE id = :schemeId)")
    fun isSchemeSaved(schemeId: String): Flow<Boolean>

    // Business Plans
    @Query("SELECT * FROM saved_plans ORDER BY savedAt DESC")
    fun getSavedPlans(): Flow<List<SavedPlanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePlan(plan: SavedPlanEntity)

    @Query("DELETE FROM saved_plans WHERE id = :planId")
    suspend fun deletePlan(planId: String)

    // Partners
    @Query("SELECT * FROM saved_partners ORDER BY savedAt DESC")
    fun getSavedPartners(): Flow<List<SavedPartnerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePartner(partner: SavedPartnerEntity)

    @Query("DELETE FROM saved_partners WHERE id = :partnerId")
    suspend fun deletePartner(partnerId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_partners WHERE id = :partnerId)")
    fun isPartnerSaved(partnerId: String): Flow<Boolean>

    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUserProfile(profile: UserProfileEntity)
}

@Database(
    entities = [
        SavedSchemeEntity::class,
        SavedPlanEntity::class,
        SavedPartnerEntity::class,
        UserProfileEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class SakshamDatabase : RoomDatabase() {
    abstract fun sakshamDao(): SakshamDao

    companion object {
        @Volatile
        private var INSTANCE: SakshamDatabase? = null

        fun getDatabase(context: Context): SakshamDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SakshamDatabase::class.java,
                    "saksham_gov_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
