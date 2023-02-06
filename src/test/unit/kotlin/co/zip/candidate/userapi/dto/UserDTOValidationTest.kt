package co.zip.candidate.userapi.dto

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory


class UserDTOValidationTest {

    private val factory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
    private val validator: Validator = factory.validator

    @Test
    fun `should forbid empty or incorrect values`() {
        val violations = validator.validate(UserDTO(null, "", "", 0, 0))

        assertEquals(4, violations.size)
        assertTrue(containsViolationWithPath(violations, "monthlyExpenses", "must be a positive number"))
        assertTrue(containsViolationWithPath(violations, "monthlySalary", "must be a positive number"))
        assertTrue(containsViolationWithPath(violations, "email", "must not be blank"))
        assertTrue(containsViolationWithPath(violations, "name", "must not be blank"))
    }

    @Test
    fun `should forbid invalid emails`() {
        val violations = validator.validate(UserDTO(null, "john doe", "abc", 1, 1))

        assertEquals(1, violations.size)
        assertTrue(containsViolationWithPath(violations, "email", "must be a well-formed email address"))
    }

    private fun containsViolationWithPath(violations: Set<ConstraintViolation<*>>, path: String, message: String): Boolean {
        for (violation in violations) {
            if (violation.propertyPath.toString() == path && violation.message == message) {
                return true
            }
        }
        return false
    }


}