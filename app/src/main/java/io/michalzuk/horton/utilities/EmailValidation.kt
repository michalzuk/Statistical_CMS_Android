package io.michalzuk.horton.utilities

import android.util.Patterns
import java.util.regex.Pattern

class EmailValidation {

    companion object {
        val EMAIL_PATTERN = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        )

        fun isEmailValid(email: String?): Boolean {
            return email != null && EMAIL_PATTERN.matcher(email).matches()
        }
    }
}