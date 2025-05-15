package com.example.trackly.data

import android.content.Context
import android.net.Uri
import com.example.trackly.model.Transaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.Date

class TransactionRepository(context: Context) {
    private val transactionDao = AppDatabase.getDatabase(context).transactionDao()
    private val categoryDao = AppDatabase.getDatabase(context).categoryDao()
    private val budgetDao = AppDatabase.getDatabase(context).budgetDao()

    // Transaction operations
    fun getAllTransactions(): Flow<List<Transaction>> = transactionDao.getAllTransactions()

    suspend fun getTransactionById(id: String): Transaction? = transactionDao.getTransactionById(id)

    suspend fun insertTransaction(transaction: Transaction) = transactionDao.insertTransaction(transaction)

    suspend fun insertTransactions(transactions: List<Transaction>) = transactionDao.insertTransactions(transactions)

    suspend fun updateTransaction(transaction: Transaction) = transactionDao.updateTransaction(transaction)

    suspend fun deleteTransaction(transaction: Transaction) = transactionDao.deleteTransaction(transaction)

    suspend fun deleteTransactionById(id: String) = transactionDao.deleteTransactionById(id)

    // Category operations
    fun getAllCategories() = categoryDao.getAllCategories()

    suspend fun getCategoryByName(name: String) = categoryDao.getCategoryByName(name)

    suspend fun insertCategory(category: com.example.trackly.model.Category) = categoryDao.insertCategory(category)

    suspend fun insertCategories(categories: List<com.example.trackly.model.Category>) = categoryDao.insertCategories(categories)

    suspend fun updateCategory(category: com.example.trackly.model.Category) = categoryDao.updateCategory(category)

    suspend fun deleteCategory(category: com.example.trackly.model.Category) = categoryDao.deleteCategory(category)

    // Budget operations
    fun getAllBudgets() = budgetDao.getAllBudgets()

    suspend fun getBudgetByMonthAndYear(month: Int, year: Int) = budgetDao.getBudgetByMonthAndYear(month, year)

    suspend fun insertBudget(budget: com.example.trackly.model.Budget) = budgetDao.insertBudget(budget)

    suspend fun updateBudget(budget: com.example.trackly.model.Budget) = budgetDao.updateBudget(budget)

    suspend fun deleteBudget(budget: com.example.trackly.model.Budget) = budgetDao.deleteBudget(budget)

    // Additional helper methods
    suspend fun getTotalExpenses(startDate: Date, endDate: Date): Double {
        return transactionDao.getTotalExpenses(startDate.time, endDate.time) ?: 0.0
    }

    suspend fun getTotalIncome(startDate: Date, endDate: Date): Double {
        return transactionDao.getTotalIncome(startDate.time, endDate.time) ?: 0.0
    }

    fun getTransactionsByDateRange(startDate: Date, endDate: Date): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByDateRange(startDate.time, endDate.time)
    }

    fun getTransactionsByCategory(category: String, startDate: Date, endDate: Date): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByCategory(category, startDate.time, endDate.time)
    }

    // Backup all transactions to a given URI
    suspend fun backupToUri(context: Context, uri: Uri): Boolean {
        return try {
            val transactions = getAllTransactions().first()
            val json = Gson().toJson(transactions)
            context.contentResolver.openOutputStream(uri)?.use { it.write(json.toByteArray()) }
            true
        } catch (e: Exception) {
            false
        }
    }

    // Restore all transactions from a given URI
    suspend fun restoreFromUri(context: Context, uri: Uri): Boolean {
        return try {
            val json = context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
            if (json.isNullOrBlank()) return false
            val type = object : TypeToken<List<com.example.trackly.model.Transaction>>() {}.type
            val transactions: List<com.example.trackly.model.Transaction> = Gson().fromJson(json, type)
            insertTransactions(transactions)
            true
        } catch (e: Exception) {
            false
        }
    }
}