package at.ac.tuwien.sepm.individual.assignment.vehiclerental

import org.hibernate.validator.internal.util.logging.LoggerFactory
import org.javamoney.moneta.Money
import java.lang.invoke.MethodHandles
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.constraints.Size

class SizeValidatorForMoney : ConstraintValidator<Size, Money> {

    companion object {
        private val LOG = LoggerFactory.make(MethodHandles.lookup())!!
    }

    private var max: Int = 0
    private var min: Int = 0

    override fun isValid(value: Money?, context: ConstraintValidatorContext?): Boolean {
        return value?.number?.intValueExact()?:min >= min && value?.number?.intValueExact()?:max <= max
    }

    override fun initialize(parameters: Size?) {
        min = parameters!!.min
        max = parameters.max
        if (max < min)
            throw LOG.lengthCannotBeNegativeException
    }

}
