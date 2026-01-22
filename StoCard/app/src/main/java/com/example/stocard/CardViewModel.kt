package com.example.stocard

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CardData(val id: String, val name: String, val ean: String)

class CardViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _cards = MutableStateFlow<List<CardData>>(emptyList())
    val cards: StateFlow<List<CardData>> = _cards.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    private var cardsListener: ListenerRegistration? = null

    init {
        if (auth.currentUser != null) { startListeningToCards() }
    }

    fun login(email: String, pass: String, onSuccess: () -> Unit) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener { _authError.value = null; startListeningToCards(); onSuccess() }
            .addOnFailureListener { e -> _authError.value = "Chyba: ${e.message}" }
    }

    fun register(email: String, pass: String, onSuccess: () -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener { _authError.value = null; startListeningToCards(); onSuccess() }
            .addOnFailureListener { e -> _authError.value = "Chyba: ${e.message}" }
    }

    fun logout() {
        auth.signOut()
        cardsListener?.remove()
        _cards.value = emptyList()
    }

    fun addCard(name: String, code: String) {
        val userId = auth.currentUser?.uid ?: return
        val cardMap = hashMapOf("name" to name, "ean" to code, "created" to System.currentTimeMillis())
        db.collection("users").document(userId).collection("cards").add(cardMap)
    }

    fun deleteCard(cardId: String) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId).collection("cards").document(cardId).delete()
    }

    fun updateCard(cardId: String, newName: String, newCode: String) {
        val userId = auth.currentUser?.uid ?: return
        val cardMap = hashMapOf("name" to newName, "ean" to newCode)
        db.collection("users").document(userId).collection("cards").document(cardId).update(cardMap as Map<String, Any>)
    }

    private fun startListeningToCards() {
        val userId = auth.currentUser?.uid ?: return
        cardsListener?.remove()
        cardsListener = db.collection("users").document(userId).collection("cards")
            .orderBy("created")
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null) {
                    val loadedCards = snapshot.documents.map { doc ->
                        CardData(
                            id = doc.id, // TADY získáme ID z Firestore
                            name = doc.getString("name") ?: "",
                            ean = doc.getString("ean") ?: ""
                        )
                    }
                    _cards.value = loadedCards
                }
            }
    }
}