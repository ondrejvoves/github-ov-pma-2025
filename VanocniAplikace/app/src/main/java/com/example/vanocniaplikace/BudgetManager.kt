package com.example.vanocniaplikace

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
private val Context.dataStore by preferencesDataStore(name = "settings")

class BudgetManager(private val context: Context) {

    companion object {
        val BUDGET_KEY = intPreferencesKey("budget_key")
        val SPENT_KEY = intPreferencesKey("spent_key")
    }

    val budgetFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[BUDGET_KEY] ?: 0
        }

    val spentFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[SPENT_KEY] ?: 0
        }

    suspend fun saveBudget(amount: Int) {
        context.dataStore.edit { preferences ->
            preferences[BUDGET_KEY] = amount
        }
    }

    suspend fun addExpense(amount: Int) {
        context.dataStore.edit { preferences ->
            val currentSpent = preferences[SPENT_KEY] ?: 0
            preferences[SPENT_KEY] = currentSpent + amount
        }
    }

    suspend fun resetAll() {
        context.dataStore.edit { preferences ->
            preferences[BUDGET_KEY] = 0
            preferences[SPENT_KEY] = 0
        }
    }
}