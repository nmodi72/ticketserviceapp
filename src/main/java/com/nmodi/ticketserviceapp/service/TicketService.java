package com.nmodi.ticketserviceapp.service;

import com.nmodi.ticketserviceapp.client.Customer;
import com.nmodi.ticketserviceapp.grid.Seat;
import com.nmodi.ticketserviceapp.grid.SeatGrid;

import java.util.List;

public interface TicketService {

    /**
     * This method is used to get all available seats at list.
     *
     * @param sourceSeatGrid The source seat grid.
     * @return The list of available seats
     */
    List<Seat> findTotalAvailableSeats(SeatGrid sourceSeatGrid);

    /**
     * This method is used to get all available seats at list.
     *
     * @param sourceSeatGrid The source seat grid.
     * @return The total available seats number
     */
    int findTotalNumberOfAvailableSeats(SeatGrid sourceSeatGrid);

    /**
     * This method is used to hold best available seats based on number of seats in request.
     *
     * @param sourceSeatGrid The source seat grid.
     * @param customer The customer .
     * @return The list of held seats
     */
    List<Seat> holdBestAvailableSeats(SeatGrid sourceSeatGrid, Customer customer);

    /**
     * This method is used to reserve the held seats.
     *
     * @param sourceSeatGrid The source seat grid/the venue
     * @param heldSeatList The held seats as list
     * @return The list of held seats, empty in case none are reserved
     */
    List<Seat> reserveHeldSeats(SeatGrid sourceSeatGrid, List<Seat> heldSeatList);

}
