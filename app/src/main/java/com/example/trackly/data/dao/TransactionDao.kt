package com.example.trackly.data.dao

import androidx.room.*
import com.example.trackly.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: String): Transaction?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<Transaction>)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: String)

    @Query("SELECT SUM(amount) FROM transactions WHERE isExpense = 1 AND date BETWEEN :startDate AND :endDate")
    suspend fun getTotalExpenses(startDate: Long, endDate: Long): Double?

    @Query("SELECT SUM(amount) FROM transactions WHERE isExpense = 0 AND date BETWEEN :startDate AND :endDate")
    suspend fun getTotalIncome(startDate: Long, endDate: Long): Double?

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE category = :category AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getTransactionsByCategory(category: String, startDate: Long, endDate: Long): Flow<List<Transaction>>
} 