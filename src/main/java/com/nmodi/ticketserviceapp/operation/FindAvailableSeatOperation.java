package com.nmodi.ticketserviceapp.operation;

import com.nmodi.ticketserviceapp.grid.Seat;
import com.nmodi.ticketserviceapp.grid.SeatGrid;

import java.util.List;

public interface FindAvailableSeatOperation {

    /**
     * This method is used to get all available seats at list.
     *
     * @param sourceSeatGrid The source seat grid.
     * @return The list of available seat
     */
    List<Seat> getAvailableSeatsAsList(SeatGrid sourceSeatGrid);
}
