package com.nmodi.ticketserviceapp.operation;

import com.nmodi.ticketserviceapp.exception.SeatingArrangementNotValidException;
import com.nmodi.ticketserviceapp.grid.Seat;
import com.nmodi.ticketserviceapp.grid.SeatGrid;
import com.nmodi.ticketserviceapp.grid.SeatStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the find available util class to find the total available/open seats to hold/reserve within venue.
 */
public class DefaultFindAvailableSeatOperation implements FindAvailableSeatOperation {

    /**
     * This method is used to get all available seats at list.
     *
     * @param sourceSeatGrid The source seat grid.
     * @return The list of held seats
     */
    @Override
    public List<Seat> getAvailableSeatsAsList(SeatGrid sourceSeatGrid) {
        if (sourceSeatGrid == null) {
            throw new SeatingArrangementNotValidException("source seat grid can not be null.");
        }
        List<Seat> availableSeats = new ArrayList<Seat>();
        for (int x = 0; x < sourceSeatGrid.getNoOfRows(); x++) {
            for (int y = 0; y < sourceSeatGrid.getNoOfColumns(); y++) {
                if (sourceSeatGrid.getSeatStatus(x, y).equals(SeatStatus.OPEN)) {
                    availableSeats.add(sourceSeatGrid.getSeat(x, y));
                }
            }
        }
        return availableSeats;
    }
}
