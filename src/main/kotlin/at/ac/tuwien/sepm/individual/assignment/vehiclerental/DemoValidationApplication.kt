package at.ac.tuwien.sepm.individual.assignment.vehiclerental

import org.apache.commons.validator.routines.CreditCardValidator
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import javax.persistence.Embeddable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.*
import javax.validation.constraints.Size
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@SpringBootApplication
class DemoValidationApplication

@Repository
interface FooRepository : CrudRepository<Foo, Long>

typealias CC = String

@Entity
data class Foo(
        @Id
        @GeneratedValue
        val id: Long = 0,
        @get:Valid
        val bar: Bar
)

@Embeddable
data class Bar(
        @get:VCCN
        @get:Size(min = 10, max = 15)
        val name: CC
)

@Component
class CLR(val fooRepository: FooRepository) : CommandLineRunner {
    override fun run(vararg args: String?) {
        println(fooRepository.save(Foo(bar = Bar("Hello"))))
        println(fooRepository.save(Foo(bar = Bar("Hello World"))))
    }
}

fun main(args: Array<String>) {
    runApplication<DemoValidationApplication>(*args)
}


@Target(FUNCTION, PROPERTY_SETTER, PROPERTY_GETTER, FIELD, VALUE_PARAMETER)
@Retention(RUNTIME)
@Constraint(validatedBy = [VCCNV::class])
annotation class VCCN(
        val issuer: Array<VCCNV.CreditCardIssuer> = [],
        val message: String = "Invalid credit card number",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)

class VCCNV : ConstraintValidator<VCCN, String> {

    lateinit var issuer: Array<VCCNV.CreditCardIssuer>

    override fun initialize(constraint: VCCN) {
        this.issuer = constraint.issuer
    }

    override fun isValid(creditCardNumber: CC?, cxt: ConstraintValidatorContext): Boolean =
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
