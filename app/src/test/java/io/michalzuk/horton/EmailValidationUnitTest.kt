package io.michalzuk.horton

import io.michalzuk.horton.utilities.EmailValidation
import org.junit.Assert.*
import org.junit.Test

class EmailValidationUnitTest {

    @Test
    fun emailValidation_CorrectSampleEmailAddress_True() {
        assertTrue(EmailValidation.isEmailValid("example@domail.com"))
    }

    @Test
    fun emailValidation_CorrectSubDomainEmailAddress_True() {
        assertTrue(EmailValidation.isEmailValid("example@domain.sub.com"))
    }

    @Test
    fun emailValidation_IncorrectEmailAddressShort_False() {
        assertFalse(EmailValidation.isEmailValid("example@domain"))
    }

    @Test
    fun emailValidation_IncorrectEmailDoubleDot_False() {
        assertFalse(EmailValidation.isEmailValid("name@email..pl"))
    }

    @Test
    fun emailValidation_IncorrectEmailNoUsername_False() {
        assertFalse(EmailValidation.isEmailValid("@email.pl"))
    }

    @Test
    fun emailValidation_IncorrectEmailNoDomain_False() {
        assertFalse(EmailValidation.isEmailValid("domain@.pl"))
    }

    @Test
    fun emailValidation_EmptyEmailAddress_False() {
        assertFalse(EmailValidation.isEmailValid(""))
    }

    @Test
    fun emailValidation_NullEmailAddress_False() {
        assertFalse(EmailValidation.isEmailValid(null))
    }
}