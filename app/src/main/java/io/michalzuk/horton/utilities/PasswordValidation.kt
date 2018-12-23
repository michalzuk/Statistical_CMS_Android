package io.michalzuk.horton.utilities

import java.util.regex.Pattern

class PasswordValidation {
    companion object {
        /**
         * Email validation pattern.
         */
        val EMAIL_PATTERN = Pattern.compile(
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@*#!.\$%^&+=])(?=\\S+\$).{8,}\$"
        )

        fun isPasswordValid(password: String?): Boolean {
            return password != null && EMAIL_PATTERN.matcher(password).matches()
        }
    }

}