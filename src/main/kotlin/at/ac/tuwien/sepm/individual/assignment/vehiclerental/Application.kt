package at.ac.tuwien.sepm.individual.assignment.vehiclerental

import at.ac.tuwien.sepm.individual.assignment.vehiclerental.model.Booking
import at.ac.tuwien.sepm.individual.assignment.vehiclerental.model.Payment
import at.ac.tuwien.sepm.individual.assignment.vehiclerental.repository.BookingRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit.DAYS

@SpringBootApplication
@EnableJpaAuditing
class DemoApplication

@Component
class Runner(
    val bookingRepository: BookingRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        bookingRepository.deleteAll()
        bookingRepository.save(Booking(
            startTime = LocalDateTime.now(),
            endTime = LocalDateTime.now().plus(1, DAYS),
            payment = Payment(
                iban = "INVALID IBAN",
                creditCardNumber = "INVALD CREDIT CARD NUMBER"
            )
        ))
        println(bookingRepository.findAll())
    }
}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
