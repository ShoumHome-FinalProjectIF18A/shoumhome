package id.shoumhome.android.ustadz.preferences

import android.content.Context
import id.shoumhome.android.ustadz.models.Credential

class CredentialPreference(context: Context) {

    companion object {
        private const val PREFERENCE_NAME = "credential_preference"
        private const val USERNAME = "username"
        private const val PASSWORD = "password"
    }

    private val sharedPreference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getCredential(): Credential {
        val credential = Credential()
        credential.username = sharedPreference.getString(USERNAME, "admin")
        credential.password = sharedPreference.getString(PASSWORD, "admin")
        return credential
    }

    fun setCredential(credential: Credential) {
        val editor = sharedPreference.edit()
        with (editor) {
            putString(USERNAME, credential.username)
            putString(PASSWORD, credential.password)
            apply()
        }
    }

}