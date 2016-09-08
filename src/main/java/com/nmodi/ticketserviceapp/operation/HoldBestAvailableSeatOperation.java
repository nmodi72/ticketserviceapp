package com.nmodi.ticketserviceapp.operation;

import com.nmodi.ticketserviceapp.grid.Seat;
import com.nmodi.ticketserviceapp.grid.SeatGrid;

import java.util.List;

public interface HoldBestAvailableSeatOperation {

    /**
     * This method is used to get all available seats
     * and hold the seats as depending on the request.
     *
     * @param sourceSeatGrid The source seat grid.
     * @param numberOfRequestedSeats The number of seats in request
     * @return The list of held seat
     */
    List<Seat> getBestAvailableSeats(SeatGrid sourceSeatGrid, int numberOfRequestedSeats);
}
