package com.nmodi.ticketserviceapp.ticketservices;

import com.nmodi.ticketserviceapp.client.Customer;
import com.nmodi.ticketserviceapp.grid.Seat;
import com.nmodi.ticketserviceapp.grid.SeatGrid;

import java.util.List;

public interface HoldBestAvailableSeats {

    /**
     * This method is used to hold best available seats based on number of seats in request.
     *
     * @param sourceSeatGrid The source seat grid.
     * @param customer The customer .
     * @return The list of held seats
     */
    List<Seat> holdBestAvailableSeats(SeatGrid sourceSeatGrid, Customer customer);

}
