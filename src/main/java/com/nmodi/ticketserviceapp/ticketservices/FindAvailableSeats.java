package com.nmodi.ticketserviceapp.ticketservices;

import java.util.List;

import com.nmodi.ticketserviceapp.grid.Seat;
import com.nmodi.ticketserviceapp.grid.SeatGrid;

public interface FindAvailableSeats {

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
}
