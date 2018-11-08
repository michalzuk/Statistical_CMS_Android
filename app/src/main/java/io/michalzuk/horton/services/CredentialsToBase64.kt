package io.michalzuk.horton.services

import android.util.Base64


class CredentialsToBase64(private val username: String, private val apiKey: String) {

    val authorizationHeader: String get() {
            val base = "$username $apiKey"

            return "Basic " + Base64.encodeToString(base.toByteArray(), Base64.NO_WRAP)
        }
}
