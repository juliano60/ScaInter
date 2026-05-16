package com.nanoporetech.scainter.credentials

interface CredentialsStoreBase {
    fun saveCredentials(username: String, password: String)
    fun clearCredentials()
    fun loadCredentials(): RememberedCredentials?
}

data class RememberedCredentials(
    val username: String,
    val password: String
)