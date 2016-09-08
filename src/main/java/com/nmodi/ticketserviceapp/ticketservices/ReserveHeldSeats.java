package com.nmodi.ticketserviceapp.ticketservices;

import com.nmodi.ticketserviceapp.client.Customer;
import com.nmodi.ticketserviceapp.grid.Seat;
import com.nmodi.ticketserviceapp.grid.SeatGrid;

import java.util.List;

public interface ReserveHeldSeats {

    /**
     * This method is used to reserve the held seats.
     *
     * @param sourceSeatGrid The source seat grid/the venue
     * @param heldSeatList The held seats as list
     * @return The list of held seats, empty in case none are reserved
     */
    List<Seat> reserveHeldSeats(SeatGrid sourceSeatGrid, List<Seat> heldSeatList);

}
