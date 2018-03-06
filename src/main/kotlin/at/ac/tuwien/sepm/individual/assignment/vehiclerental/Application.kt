package at.ac.tuwien.sepm.individual.assignment.vehiclerental

import at.ac.tuwien.sepm.individual.assignment.vehiclerental.DrivingLicenseType.B
import at.ac.tuwien.sepm.individual.assignment.vehiclerental.PowerSource.HUMAN
import at.ac.tuwien.sepm.individual.assignment.vehiclerental.PowerSource.MOTORIZED
import org.javamoney.moneta.Money
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@SpringBootApplication
@EnableJpaAuditing
class DemoApplication

@Component
@Transactional
class Runner(val vehicleRepository: VehicleRepository) : CommandLineRunner {
    override fun run(vararg args: String?) {
        logger.debug { "Application started" }
        vehicleRepository.deleteAll()
        val vehicle = Vehicle(name = "Bike 1", powerSource = HUMAN, pricePerHour = Money.of(25, EUR))
        println(vehicle)
        Thread.sleep(1000)
        vehicleRepository.save(vehicle)
        vehicleRepository.save(Vehicle(
            name = "Car 1",
            powerSource = MOTORIZED,
            pricePerHour = Money.of(25, EUR),
            licensePlateNumber = "W-123ABC",
            drivingLicenseTypes = mutableSetOf(B)
        ))
        vehicleRepository.findAll()
    }
}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
