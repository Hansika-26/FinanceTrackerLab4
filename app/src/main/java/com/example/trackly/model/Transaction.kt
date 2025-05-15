package com.example.trackly.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey
    val id: String,
    var title: String,
    var amount: Double,
    var category: String,
    var date: Long, // Store as timestamp for Room
    var isExpense: Boolean = true
) {
    constructor(title: String, amount: Double, category: String, date: Date, isExpense: Boolean = true) :
            this(
                id = java.util.UUID.randomUUID().toString(),
                title = title,
                amount = amount,
                category = category,
                date = date.time, // Convert Date to timestamp
                isExpense = isExpense
            )

    // Convert timestamp to Date object
    fun toDate(): Date = Date(date)
    
    // Format the date with a specific pattern
    fun getFormattedDate(pattern: String = "dd MMM yyyy"): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        return formatter.format(toDate())
    }

    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "title" to title,
            "amount" to amount,
            "category" to category,
            "date" to date,
            "isExpense" to isExpense
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>): Transaction {
            // Handle the case where date might be a Double instead of Long
            val dateValue = when (val rawDate = map["date"]) {
                is Long -> rawDate
                is Double -> rawDate.toLong()
                is Date -> (rawDate as Date).time
                else -> throw IllegalArgumentException("Date value is not a valid type: ${rawDate?.javaClass?.name}")
            }

            return Transaction(
                id = map["id"] as String,
                title = map["title"] as String,
                amount = (map["amount"] as Number).toDouble(),
                category = map["category"] as String,
                date = dateValue,
                isExpense = map["isExpense"] as Boolean
            )
        }
    }
}
