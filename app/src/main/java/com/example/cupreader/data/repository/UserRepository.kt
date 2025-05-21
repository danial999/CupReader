package com.example.cupreader.data.repository

import com.example.cupreader.data.model.UserInfo
import com.example.cupreader.data.model.AuthUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db   = FirebaseFirestore.getInstance()

    fun getAuthUser(): AuthUser? =
        auth.currentUser?.let { AuthUser(it.displayName, it.email, it.photoUrl?.toString()) }

    suspend fun saveUserInfo(uid: String, info: UserInfo) {
        db.collection("UserInfo").document(uid).set(info).await()
    }

    suspend fun fetchUserInfo(uid: String): UserInfo? {
        return db.collection("UserInfo").document(uid).get().await().toObject(UserInfo::class.java)
    }
}
