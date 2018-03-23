package at.ac.tuwien.sepm.individual.assignment.vehiclerental.util

import at.ac.tuwien.sepm.individual.assignment.vehiclerental.model.CreditCardNumber
import at.ac.tuwien.sepm.individual.assignment.vehiclerental.model.Iban
import org.apache.commons.validator.routines.CreditCardValidator
import org.apache.commons.validator.routines.IBANValidator
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass


@Target(FUNCTION, PROPERTY_SETTER, PROPERTY_GETTER, FIELD, VALUE_PARAMETER)
@Retention(RUNTIME)
@Constraint(validatedBy = [CreditCardNumberValidator::class])
annotation class ValidCreditCardNumber(
    val issuer: Array<CreditCardNumberValidator.CreditCardIssuer> = [],
    val message: String = "Invalid credit card number",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class CreditCardNumberValidator : ConstraintValidator<ValidCreditCardNumber, String> {

    lateinit var issuer: Array<CreditCardIssuer>

    override fun initialize(constraint: ValidCreditCardNumber) {
        this.issuer = constraint.issuer
    }

    override fun isValid(creditCardNumber: CreditCardNumber?, cxt: ConstraintValidatorContext): Boolean =
        creditCardNumber?.let {
            CreditCardValidator(issuer.map { it.issuer }.sum()).isValid(creditCardNumber)
        } ?: true

    enum class CreditCardIssuer(val issuer: Long) {
        NONE(CreditCardValidator.NONE),
        AMEX(CreditCardValidator.AMEX),
        VISA(CreditCardValidator.VISA),
        MASTERCARD(CreditCardValidator.MASTERCARD),
        DISCOVER(CreditCardValidator.DISCOVER),
        DINERS(CreditCardValidator.DINERS),
        VPAY(CreditCardValidator.VPAY),
    }

}

@Target(FUNCTION, PROPERTY_SETTER, PROPERTY_GETTER, FIELD, VALUE_PARAMETER)
@Retention(RUNTIME)
@Constraint(validatedBy = [IbanValidator::class])
annotation class ValidIban(
    val message: String = "Invalid IBAN",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class IbanValidator : ConstraintValidator<ValidIban, String> {

    override fun initialize(constraint: ValidIban) { }

    override fun isValid(iban: Iban?, cxt: ConstraintValidatorContext): Boolean =
        iban?.let { IBANValidator().isValid(iban) } ?: true

}