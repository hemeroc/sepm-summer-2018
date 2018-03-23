package at.ac.tuwien.sepm.individual.assignment.vehiclerental.util

import mu.KLogger
import mu.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InjectionPoint
import org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
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

@Configuration
class LoggerConfig {

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    fun logger(injectionPoint: InjectionPoint): KLogger =
        KotlinLogging.logger(injectionPoint.methodParameter!!.containingClass.canonicalName)

}
