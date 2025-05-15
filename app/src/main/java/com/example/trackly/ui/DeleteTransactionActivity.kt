package com.example.trackly.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trackly.R
import com.example.trackly.data.TransactionRepository
import com.example.trackly.databinding.ActivityDeleteTransactionBinding
import com.example.trackly.notification.NotificationManager
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class DeleteTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeleteTransactionBinding
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var notificationManager: NotificationManager

    private var transactionId: String = ""
    private var transactionTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transactionRepository = TransactionRepository(this)
        notificationManager = NotificationManager(this)

        transactionId = intent.getStringExtra("transaction_id") ?: ""
        transactionTitle = intent.getStringExtra("transaction_title") ?: ""

        binding.tvDeleteConfirmation.text = getString(
            R.string.delete_transaction_confirmation, transactionTitle
        )

        binding.btnDelete.setOnClickListener {
            deleteTransaction()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun deleteTransaction() {
        lifecycleScope.launch {
            val transaction = transactionRepository.getTransactionById(transactionId)
            if (transaction != null) {
                transactionRepository.deleteTransaction(transaction)
                notificationManager.checkBudgetAndNotify()
                Toast.makeText(
                    this@DeleteTransactionActivity,
                    getString(R.string.transaction_deleted),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } else {
                Toast.makeText(
                    this@DeleteTransactionActivity,
                    "Transaction not found",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}