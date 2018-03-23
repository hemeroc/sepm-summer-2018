package at.ac.tuwien.sepm.individual.assignment.vehiclerental.repository

import at.ac.tuwien.sepm.individual.assignment.vehiclerental.model.Booking
import at.ac.tuwien.sepm.individual.assignment.vehiclerental.model.Vehicle
import at.ac.tuwien.sepm.individual.assignment.vehiclerental.model.VehicleBooking
import at.ac.tuwien.sepm.individual.assignment.vehiclerental.model.VehicleBookingKey
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface BookingRepository : PagingAndSortingRepository<Booking, Long>

@Repository
interface VehicleRepository : PagingAndSortingRepository<Vehicle, Long>

@Repository
interface VehicleBookingRepository : PagingAndSortingRepository<VehicleBooking, VehicleBookingKey>
