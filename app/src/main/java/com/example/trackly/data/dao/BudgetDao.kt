package com.example.trackly.data.dao

import androidx.room.*
import com.example.trackly.model.Budget
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgets ORDER BY year DESC, month DESC")
    fun getAllBudgets(): Flow<List<Budget>>

    @Query("SELECT * FROM budgets WHERE month = :month AND year = :year")
    suspend fun getBudgetByMonthAndYear(month: Int, year: Int): Budget?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: Budget)

    @Update
    suspend fun updateBudget(budget: Budget)

    @Delete
    suspend fun deleteBudget(budget: Budget)

    @Query("DELETE FROM budgets WHERE month = :month AND year = :year")
    suspend fun deleteBudgetByMonthAndYear(month: Int, year: Int)
} 