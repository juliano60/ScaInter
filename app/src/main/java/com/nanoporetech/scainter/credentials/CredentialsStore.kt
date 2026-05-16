package com.nanoporetech.scainter.credentials

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class CredentialsStore(context: Context): CredentialsStoreBase {
    private val appContext = context.applicationContext

    private val masterKey = MasterKey.Builder(appContext)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        appContext,
        "auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private object Keys {
        const val USERNAME = "username"
        const val PASSWORD = "password"
        const val REMEMBER_ME = "rememberMe"
    }

    override fun saveCredentials(username: String, password: String) {
        prefs.edit()
            .putString(Keys.USERNAME, username)
            .putString(Keys.PASSWORD, password)
            .putBoolean(Keys.REMEMBER_ME, true)
            .commit() // safer for credentials
    }

    override fun clearCredentials() {
        prefs.edit()
            .remove(Keys.USERNAME)
            .remove(Keys.PASSWORD)
            .putBoolean(Keys.REMEMBER_ME, false)
            .commit()
    }

    override fun loadCredentials(): RememberedCredentials? {
        val username = prefs.getString(Keys.USERNAME, null) ?: return null
        val password = prefs.getString(Keys.PASSWORD, null) ?: return null
        val remember = prefs.getBoolean(Keys.REMEMBER_ME, false)

        return if (remember) RememberedCredentials(username, password) else null
    }
}