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

// Admin Managed Scheme Entity
@Entity(tableName = "managed_schemes")
data class ManagedSchemeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val shortName: String,
    val department: String,
    val category: String,
    val maxLoanAmountDisplay: String,
    val maxLoanNumber: Long,
    val interestRateDisplay: String,
    val subsidyPercent: String,
    val moratoriumMonths: Int,
    val eligibilitySummary: String,
    val description: String,
    val officialApplyUrl: String = "https://nsfdc.nic.in",
    val officialPortalName: String = "NSFDC Official Portal (nsfdc.nic.in)",
    val isActive: Boolean = true,
    val updatedAt: Long = System.currentTimeMillis()
)

// Admin Registered Users Database Entity
@Entity(tableName = "user_records")
data class UserRecordEntity(
    @PrimaryKey val id: String,
    val fullName: String,
    val phone: String,
    val email: String,
    val age: Int,
    val gender: String,
    val state: String,
    val district: String,
    val socialCategory: String,
    val familyIncome: String,
    val businessTarget: String,
    val registeredAt: Long = System.currentTimeMillis(),
    val status: String = "Active", // Active, Suspended, Verified
    val lastActive: Long = System.currentTimeMillis()
)

// Admin User Applications / Guidance Requests
@Entity(tableName = "user_applications")
data class UserApplicationEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val userName: String,
    val userPhone: String,
    val schemeId: String,
    val schemeName: String,
    val businessType: String,
    val totalProjectCost: Long,
    val loanAmountRequired: Long,
    val ownCapital: Long,
    val state: String,
    val district: String,
    val status: String = "Pending", // Pending, Under Review, Sanctioned, Rejected
    val adminRemarks: String = "",
    val assignedBankPartner: String = "State Bank of India (Lead Bank)",
    val submittedAt: Long = System.currentTimeMillis()
)

// Admin AI Assistant Query Log
@Entity(tableName = "ai_query_logs")
data class AiQueryLogEntity(
    @PrimaryKey val id: String,
    val userName: String,
    val userPhone: String,
    val userPrompt: String,
    val aiResponseSummary: String,
    val language: String,
    val timestamp: Long = System.currentTimeMillis()
)

// Admin Channel Partners Management
@Entity(tableName = "managed_partners")
data class ManagedPartnerEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String,
    val state: String,
    val district: String,
    val phone: String,
    val address: String,
    val nodalOfficer: String = "District Nodal Officer",
    val isActive: Boolean = true,
    val addedAt: Long = System.currentTimeMillis()
)

// Support Tickets / Contact Requests
@Entity(tableName = "support_tickets")
data class SupportTicketEntity(
    @PrimaryKey val id: String,
    val userName: String,
    val userPhone: String,
    val userEmail: String,
    val category: String, // Scheme Eligibility, Loan Processing, App Issue, General
    val message: String,
    val priority: String = "Medium", // High, Medium, Low
    val status: String = "Open", // Open, In Progress, Resolved
    val adminReply: String = "",
    val submittedAt: Long = System.currentTimeMillis()
)

// User Feedback Management
@Entity(tableName = "user_feedbacks")
data class UserFeedbackEntity(
    @PrimaryKey val id: String,
    val userName: String,
    val userPhone: String,
    val rating: Int, // 1 to 5
    val category: String, // App Ease, Scheme Guidance, Bank Process, AI Assistance
    val comment: String,
    val submittedAt: Long = System.currentTimeMillis()
)

// Admin Settings Entity
@Entity(tableName = "admin_settings")
data class AdminSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val adminName: String = "Govt. Officer - NSFDC",
    val adminEmail: String = "admin@saksham.gov.in",
    val department: String = "National Scheduled Castes Finance & Development Corporation",
    val maintenanceMode: Boolean = false,
    val aiAdvisorEnabled: Boolean = true,
    val systemAnnouncement: String = "Saksham Portal v2.5 - All NSFDC Direct Schemes Active",
    val lastBackupDate: String = "2026-09-13"
)

@Dao
interface SakshamDao {
    // Schemes (User)
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

    // Partners (User)
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

    // === ADMIN DAO QUERIES ===

    // Admin Managed Schemes
    @Query("SELECT * FROM managed_schemes ORDER BY updatedAt DESC")
    fun getManagedSchemes(): Flow<List<ManagedSchemeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertManagedScheme(scheme: ManagedSchemeEntity)

    @Query("DELETE FROM managed_schemes WHERE id = :schemeId")
    suspend fun deleteManagedScheme(schemeId: String)

    // Admin User Records
    @Query("SELECT * FROM user_records ORDER BY registeredAt DESC")
    fun getAllUserRecords(): Flow<List<UserRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserRecord(userRecord: UserRecordEntity)

    @Query("UPDATE user_records SET status = :newStatus WHERE id = :userId")
    suspend fun updateUserStatus(userId: String, newStatus: String)

    // Admin User Applications
    @Query("SELECT * FROM user_applications ORDER BY submittedAt DESC")
    fun getAllApplications(): Flow<List<UserApplicationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(app: UserApplicationEntity)

    @Query("UPDATE user_applications SET status = :status, adminRemarks = :remarks, assignedBankPartner = :partner WHERE id = :appId")
    suspend fun updateApplicationStatus(appId: String, status: String, remarks: String, partner: String)

    // Admin AI Query Logs
    @Query("SELECT * FROM ai_query_logs ORDER BY timestamp DESC")
    fun getAiQueryLogs(): Flow<List<AiQueryLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAiQueryLog(log: AiQueryLogEntity)

    // Admin Managed Channel Partners
    @Query("SELECT * FROM managed_partners ORDER BY addedAt DESC")
    fun getManagedPartners(): Flow<List<ManagedPartnerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertManagedPartner(partner: ManagedPartnerEntity)

    @Query("DELETE FROM managed_partners WHERE id = :partnerId")
    suspend fun deleteManagedPartner(partnerId: String)

    // Admin Support Tickets
    @Query("SELECT * FROM support_tickets ORDER BY submittedAt DESC")
    fun getSupportTickets(): Flow<List<SupportTicketEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupportTicket(ticket: SupportTicketEntity)

    @Query("UPDATE support_tickets SET status = :status, adminReply = :reply WHERE id = :ticketId")
    suspend fun updateSupportTicket(ticketId: String, status: String, reply: String)

    // Admin User Feedbacks
    @Query("SELECT * FROM user_feedbacks ORDER BY submittedAt DESC")
    fun getUserFeedbacks(): Flow<List<UserFeedbackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserFeedback(feedback: UserFeedbackEntity)

    // Admin Settings
    @Query("SELECT * FROM admin_settings WHERE id = 1")
    fun getAdminSettings(): Flow<AdminSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAdminSettings(settings: AdminSettingsEntity)
}

@Database(
    entities = [
        SavedSchemeEntity::class,
        SavedPlanEntity::class,
        SavedPartnerEntity::class,
        UserProfileEntity::class,
        ManagedSchemeEntity::class,
        UserRecordEntity::class,
        UserApplicationEntity::class,
        AiQueryLogEntity::class,
        ManagedPartnerEntity::class,
        SupportTicketEntity::class,
        UserFeedbackEntity::class,
        AdminSettingsEntity::class
    ],
    version = 5,
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

