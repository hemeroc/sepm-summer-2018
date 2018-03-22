package at.ac.tuwien.sepm.individual.assignment.vehiclerental

import mu.KLogger
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit.DAYS
import javax.transaction.Transactional

@SpringBootApplication
@EnableJpaAuditing
class DemoApplication

@Component
@Transactional
class Runner(
    val logger: KLogger,
    val vehicleRepository: VehicleRepository,
    val bookingRepository: BookingRepository,
    val vehicleBookingRepository: VehicleBookingRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        vehicleBookingRepository.deleteAll()
        bookingRepository.deleteAll()
        vehicleRepository.deleteAll()

//        println()
//        logger.debug { "VEHICLE TEST" }
//        println()
//        val vehicle = vehicleRepository.save(Vehicle(
//            name = "Car 1",
//            powerSource = MOTORIZED,
//            pricePerHour = Money.of(0.00000000000000000000001, EUR),
//            licensePlateNumber = "W-123ABC",
//            drivingLicenseTypes = mutableSetOf(B)
//        ))

        println()
        logger.debug { "BOOKING TEST" }
        println()
        bookingRepository.save(Booking(
            startTime = LocalDateTime.now(),
            endTime = LocalDateTime.now().plus(1, DAYS),
//            payment = Payment(
//                iban = "INVALID IBAN",
//                creditCardNumber = "INVALD CREDIT CARD NUMBER"
//            ),
            iban = "INVALID IBAN"
        ))

//        println()
//        logger.debug { "VEHICLE BOOKING TEST" }
//        println()
//        vehicleBookingRepository.save(VehicleBooking(
//            vehicleBookingKey = VehicleBookingKey(vehicle = vehicle, booking = booking),
//            drivingLicenseInformation = DrivingLicenseInformation(
//                drivingLicenseNumber = "1234567890",
//                drivingLicenseIssuingDate = LocalDate.now().minus(4, YEARS)
//            )
//        ))

        println()
        logger.debug { "FIND ALL" }
        println()

//        vehicleRepository.findAll()
        bookingRepository.findAll()
//        vehicleBookingRepository.findAll()
    }
}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
