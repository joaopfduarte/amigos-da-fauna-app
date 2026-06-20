package com.app.amigos_da_fauna.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenStorage @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val prefs = EncryptedSharedPreferences.create(
        PREFS_NAME,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    suspend fun getToken(): String? = prefs.getString(TOKEN_KEY, null)

    suspend fun saveToken(token: String) {
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    suspend fun deleteToken() {
        prefs.edit().remove(TOKEN_KEY).apply()
    }

    companion object {
        private const val PREFS_NAME = "auth_secure_prefs"
        private const val TOKEN_KEY = "auth_token"
    }
}
