package com.example.trackly.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var amount: Double,
    var month: Int = Calendar.getInstance().get(Calendar.MONTH),
    var year: Int = Calendar.getInstance().get(Calendar.YEAR)
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "amount" to amount,
            "month" to month,
            "year" to year
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>): Budget {
            return Budget(
                id = (map["id"] as? Number)?.toInt() ?: 0,
                amount = (map["amount"] as Number).toDouble(),
                month = (map["month"] as Number).toInt(),
                year = (map["year"] as Number).toInt()
            )
        }
    }
}
