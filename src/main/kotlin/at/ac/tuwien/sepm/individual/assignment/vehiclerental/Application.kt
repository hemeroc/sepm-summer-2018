package at.ac.tuwien.sepm.individual.assignment.vehiclerental

import at.ac.tuwien.sepm.individual.assignment.vehiclerental.DrivingLicenseType.B
import at.ac.tuwien.sepm.individual.assignment.vehiclerental.PowerSource.MOTORIZED
import mu.KLogger
import org.javamoney.moneta.Money
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit.YEARS
import java.time.temporal.ChronoUnit.DAYS
import javax.transaction.Transactional

@SpringBootApplication
@EnableJpaAuditing
class DemoApplication

@Component
@Transactional
class Runner(
    val logger: KLogger,
    val vehicleService: VehicleService,
    val vehicleRepository: VehicleRepository,
    val bookingRepository: BookingRepository,
    val vehicleBookingRepository: VehicleBookingRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        vehicleBookingRepository.deleteAll()
        bookingRepository.deleteAll()
        vehicleRepository.deleteAll()
        logger.debug { }
        logger.debug { "VEHICLE TEST" }
        logger.debug { }
        val vehicle = vehicleService.save(Vehicle(
            name = "Car 1",
            powerSource = MOTORIZED,
            pricePerHour = Money.of(0.00000000000000000000001, EUR),
            licensePlateNumber = "W-123ABC",
            drivingLicenseTypes = mutableSetOf(B)
        ))
        logger.debug { }
        logger.debug { "BOOKING TEST" }
        logger.debug { }
        val booking = bookingRepository.save(Booking(
            startTime = LocalDateTime.now(),
            endTime = LocalDateTime.now().plus(1, DAYS),
            payment = Payment(
                iban = null,
                creditCardNumber = null
            )
        ))
        logger.debug { }
        logger.debug { "VEHICLE BOOKING TEST" }
        logger.debug { }
        vehicleBookingRepository.save(VehicleBooking(
            vehicleBookingKey = VehicleBookingKey(vehicle = vehicle, booking = booking),
            drivingLicenseInformation = DrivingLicenseInformation(
                drivingLicenseNumber = "1234567890",
                drivingLicenseIssuingDate = LocalDate.now().minus(4, YEARS)
            )
        ))
        vehicleRepository.findAll()
        bookingRepository.findAll()
        vehicleBookingRepository.findAll()
    }
}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
