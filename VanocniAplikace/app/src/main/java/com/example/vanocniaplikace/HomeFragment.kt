package com.example.vanocniaplikace

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var currentBudget = 0
    private var currentSpent = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val budgetManager = BudgetManager(requireContext())


        val tvRemaining = view.findViewById<TextView>(R.id.tvRemainingBudget)
        val tvSpent = view.findViewById<TextView>(R.id.tvSpent)
        val btnAdd = view.findViewById<Button>(R.id.btnAddExpense)


        fun updateUI() {
            val remaining = currentBudget - currentSpent


            tvRemaining.text = "$remaining Kč"
            tvSpent.text = "Utraceno: $currentSpent Kč"


            if (remaining < 0) {
                tvRemaining.setTextColor(Color.RED)
            } else {
                tvRemaining.setTextColor(Color.parseColor("#2E7D32"))
            }
        }




        lifecycleScope.launch {
            budgetManager.budgetFlow.collect { budget ->
                currentBudget = budget
                updateUI()
            }
        }


        lifecycleScope.launch {
            budgetManager.spentFlow.collect { spent ->
                currentSpent = spent
                updateUI()
            }
        }

        btnAdd.setOnClickListener {
            showAddExpenseDialog(budgetManager)
        }
    }

    private fun showAddExpenseDialog(manager: BudgetManager) {
        val input = EditText(requireContext())
        input.hint = "Částka (Kč)"
        input.inputType = InputType.TYPE_CLASS_NUMBER

        AlertDialog.Builder(requireContext())
            .setTitle("🎄 Nová útrata")
            .setMessage("Kolik jsi utratil za dárek?")
            .setView(input)
            .setPositiveButton("Přidat") { _, _ ->
                val text = input.text.toString()
                if (text.isNotEmpty()) {
                    val amount = text.toInt()
                    lifecycleScope.launch {
                        manager.addExpense(amount)
                    }
                }
            }
            .setNegativeButton("Zrušit", null)
            .show()
    }
}