package at.ac.tuwien.sepm.individual.assignment.vehiclerental

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import javax.validation.Valid

@Repository
interface BookingRepository : PagingAndSortingRepository<Booking, Long>

@Repository
interface VehicleRepository : PagingAndSortingRepository<Vehicle, Long>

@Repository
interface VehicleBookingRepository : PagingAndSortingRepository<VehicleBooking, Long>
