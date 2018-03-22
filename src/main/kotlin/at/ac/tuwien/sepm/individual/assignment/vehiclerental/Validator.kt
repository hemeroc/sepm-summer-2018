package at.ac.tuwien.sepm.individual.assignment.vehiclerental

import org.apache.commons.validator.routines.CreditCardValidator
import org.apache.commons.validator.routines.IBANValidator
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.reflect.KClass

@Constraint(validatedBy = [CreditCardNumberValidator::class])
@Target(FIELD)
@Retention(RUNTIME)
annotation class ValidCreditCardNumber(
    val message: String = "Invalid credit card number",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class CreditCardNumberValidator : ConstraintValidator<ValidCreditCardNumber, String> {

    lateinit var constraint: ValidCreditCardNumber

    override fun initialize(constraint: ValidCreditCardNumber) {
        this.constraint = constraint
    }

    override fun isValid(creditCardNumber: CreditCardNumber?, cxt: ConstraintValidatorContext): Boolean =
        creditCardNumber?.let { CreditCardValidator().isValid(creditCardNumber) } ?: true

}

@Constraint(validatedBy = [IbanValidator::class])
@Target(FIELD)
@Retention(RUNTIME)
annotation class ValidIban(
    val message: String = "Invalid IBAN",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class IbanValidator : ConstraintValidator<ValidIban, String> {

    lateinit var constraint: ValidIban

    override fun initialize(constraint: ValidIban) {
        this.constraint = constraint
    }

    override fun isValid(iban: Iban?, cxt: ConstraintValidatorContext): Boolean =
        iban?.let { IBANValidator().isValid(iban) } ?: true

}