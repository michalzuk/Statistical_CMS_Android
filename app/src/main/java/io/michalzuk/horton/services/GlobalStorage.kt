package io.michalzuk.horton.services

import android.app.Application

object GlobalStorage : Application() {

    private var domain: String = ""
    private var user: String = ""
    private var apiKey: String = ""

    fun getDomain() : String {
        return domain
    }

    fun setDomain(domain : String) {
        this.domain = domain
    }

    fun getUser() : String {
        return user
    }

    fun setUser(user: String) {
        this.user = user
    }

    fun getApiKey(): String {
        return apiKey
    }

    fun setApiKey(apiKey: String) {
        this.apiKey = apiKey
    }

    fun isAnyMissing(): Boolean {
        return !(user.isEmpty() || domain.isEmpty() || apiKey.isEmpty())
    }

}