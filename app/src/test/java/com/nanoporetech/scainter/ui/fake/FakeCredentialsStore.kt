package com.nanoporetech.scainter.ui.fake

import com.nanoporetech.scainter.credentials.CredentialsStoreBase
import com.nanoporetech.scainter.credentials.RememberedCredentials

class FakeCredentialsStore() : CredentialsStoreBase {
    private var credentials: RememberedCredentials? = null

    override fun saveCredentials(username: String, password: String) {
        credentials = RememberedCredentials(
            username = username,
            password = password
        )
    }

    override fun clearCredentials() {
        credentials = null
    }

    override fun loadCredentials(): RememberedCredentials? {
        return credentials
    }
}
