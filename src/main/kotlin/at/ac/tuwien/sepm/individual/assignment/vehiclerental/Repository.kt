package at.ac.tuwien.sepm.individual.assignment.vehiclerental

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import javax.validation.Valid

@Repository
interface BookingRepository : PagingAndSortingRepository<Booking, Long>

@Repository
interface VehicleRepository : PagingAndSortingRepository<Vehicle, Long> {
    override fun <S : @Valid Vehicle?> save(@Valid p0: @Valid S): @Valid S
}

@Repository
interface VehicleBookingRepository : PagingAndSortingRepository<VehicleBooking, Long>
