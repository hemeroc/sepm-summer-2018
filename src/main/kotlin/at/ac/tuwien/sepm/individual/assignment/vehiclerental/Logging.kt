package at.ac.tuwien.sepm.individual.assignment.vehiclerental

import mu.KLogger
import mu.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import java.lang.System.currentTimeMillis

@Configuration
@Aspect
class LoggingAspect {

    @Around("within(org.springframework.data.repository.Repository+) && execution(* *(..))")
    fun aroundSpringRepositories(joinPoint: ProceedingJoinPoint): Any? {
        val logger = LoggerFactory.getLogger(joinPoint.target.javaClass.interfaces[0])
        val startTime = currentTimeMillis()
        logger.debug("calling ${joinPoint.signature.name}(${joinPoint.args.joinToString()})")
        val proceed = joinPoint.proceed()
        logger.debug("calling ${joinPoint.signature.toShortString()} took ${currentTimeMillis() - startTime}ms and returned $proceed")
        return proceed
    }

}

private val loggerMap: MutableMap<Class<Any>, KLogger> = HashMap()

val Any.logger: KLogger
    get() = loggerMap.getOrPut(this.javaClass, { KotlinLogging.logger(this.javaClass.canonicalName) })
