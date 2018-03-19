package at.ac.tuwien.sepm.individual.assignment.vehiclerental

import org.hibernate.annotations.Columns
import org.hibernate.annotations.Type
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.money.Monetary.getCurrency
import javax.money.MonetaryAmount
import javax.persistence.*
import javax.persistence.FetchType.EAGER
import javax.persistence.GenerationType.AUTO
import javax.validation.constraints.Max
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

typealias DatabaseId = Long
typealias NumberOfSeats = Int
typealias LicensePlateNumber = String
typealias DrivingLicenseNumber = String
typealias WattHours = Double
typealias DrivingLicenseTypes = MutableSet<DrivingLicenseType>
typealias VehicleBookings = List<VehicleBooking>

private const val MONEY = "org.jadira.usertype.moneyandcurrency.moneta.PersistentMoneyAmountAndCurrency"
val EUR = getCurrency("EUR")!!

@Entity
@EntityListeners(AuditingEntityListener::class)
data class Vehicle(
    @Id @GeneratedValue(strategy = AUTO)
    val id: DatabaseId = 0,
    val vehicleId: UUID = UUID.randomUUID(),
    @get:Size(min = 3, max = 25)
    var name: String,
    @get:Size(min = 10, max = 2000)
    var description: String? = null,
    @get:Max(100)
    @get:Positive
    var numberOfSeats: NumberOfSeats? = null,
    @get:Size(min = 3, max = 10)
    var licensePlateNumber: LicensePlateNumber? = null,
    @get:Max(1_000_000)
    @get:Positive
    var power: WattHours? = null,
    @Enumerated(EnumType.STRING)
    var powerSource: PowerSource,
    @ElementCollection(fetch = EAGER, targetClass = DrivingLicenseType::class)
    @CollectionTable(
        name = "vehicle_required_driving_license_type",
        joinColumns = [(JoinColumn(name = "vehicle_id"))]
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "driving_license_type")
    val drivingLicenseTypes: DrivingLicenseTypes = mutableSetOf(),

    //@get:Max(1_000)
    //@get:Min(0)
    @get:Positive
    @Columns(columns = [(Column(name = "price_per_hour_currency")), (Column(name = "price_per_hour_amount"))])
    @Type(type = MONEY)
    var pricePerHour: MonetaryAmount,
    @OneToMany(mappedBy = "vehicleBookingKey.vehicle")
    val vehicleBookings: VehicleBookings = mutableListOf(),
    val deleted: Boolean = false,
    @CreatedDate
    val created: LocalDateTime = LocalDateTime.of(0, 1, 1, 0, 0),
    @LastModifiedDate
    val lastModified: LocalDateTime = LocalDateTime.of(0, 1, 1, 0, 0)
)

@Entity
@EntityListeners(AuditingEntityListener::class)
data class VehicleBooking(
    @EmbeddedId
    val vehicleBookingKey: VehicleBookingKey,
    val drivingLicenseInformation: DrivingLicenseInformation? = null
)

@Embeddable
data class VehicleBookingKey(
    @ManyToOne
    val vehicle: Vehicle,
    @ManyToOne
    val booking: Booking
) : Serializable

@Embeddable
data class DrivingLicenseInformation(
    val drivingLicenseNumber: DrivingLicenseNumber,
    val drivingLicenseIssuingDate: LocalDate
)

@Entity
@EntityListeners(AuditingEntityListener::class)
data class Booking(
    @Id @GeneratedValue(strategy = AUTO)
    val id: DatabaseId = 0,
    var startTime: LocalDateTime,
    var endTime: LocalDateTime,
    @OneToMany(mappedBy = "vehicleBookingKey.booking")
    val vehicleBookings: VehicleBookings = mutableListOf()
)


enum class PowerSource {
    MOTORIZED,
    HUMAN,
}

enum class DrivingLicenseType {
    A,
    B,
    C,
}
