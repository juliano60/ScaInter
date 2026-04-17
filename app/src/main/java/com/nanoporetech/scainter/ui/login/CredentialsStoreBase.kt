package com.nanoporetech.scainter.ui.login

interface CredentialsStoreBase {
    fun saveCredentials(username: String, password: String)
    fun clearCredentials()
    fun loadCredentials(): RememberedCredentials?
}

data class RememberedCredentials(
    val username: String,
    val password: String
)
