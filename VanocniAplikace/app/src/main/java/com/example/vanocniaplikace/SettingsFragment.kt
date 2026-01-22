package com.example.vanocniaplikace

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val budgetManager = BudgetManager(requireContext())

        val etBudgetInput = view.findViewById<EditText>(R.id.etBudgetInput)
        val btnSave = view.findViewById<Button>(R.id.btnSaveBudget)
        val btnReset = view.findViewById<Button>(R.id.btnResetAll)

        btnSave.setOnClickListener {
            val text = etBudgetInput.text.toString()

            if (text.isNotEmpty()) {
                val amount = text.toInt()

                lifecycleScope.launch {
                    budgetManager.saveBudget(amount)

                    Toast.makeText(context, "Limit $amount Kč uložen! 🎄", Toast.LENGTH_SHORT).show()

                    etBudgetInput.text.clear()
                }
            } else {
                Toast.makeText(context, "Zadej prosím částku", Toast.LENGTH_SHORT).show()
            }
        }

        btnReset.setOnClickListener {
            lifecycleScope.launch {
                budgetManager.resetAll()
                Toast.makeText(context, "Všechna data vymazána", Toast.LENGTH_SHORT).show()
            }
        }
    }
}