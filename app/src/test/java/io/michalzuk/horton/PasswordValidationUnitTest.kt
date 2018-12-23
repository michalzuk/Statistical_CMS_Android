package io.michalzuk.horton

import io.michalzuk.horton.utilities.EmailValidation
import org.junit.Assert.*
import org.junit.Test

class PasswordValidationUnitTest {

    @Test
    fun emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertTrue(EmailValidation.isEmailValid("@email.pl"))
    }
}