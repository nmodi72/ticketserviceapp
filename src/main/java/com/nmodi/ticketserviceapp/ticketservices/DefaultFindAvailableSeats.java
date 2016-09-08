package com.nmodi.ticketserviceapp.ticketservices;

import java.util.List;

import com.nmodi.ticketserviceapp.exception.SeatingArrangementNotValidException;
import com.nmodi.ticketserviceapp.grid.Seat;
import com.nmodi.ticketserviceapp.grid.SeatGrid;
import com.nmodi.ticketserviceapp.operation.DefaultFindAvailableSeatOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

public class DefaultFindAvailableSeats implements FindAvailableSeats {

    /**
     * The Logger
     */
    private final Logger LOGGER = LoggerFactory.getLogger(SeatGrid.class);

    @Resource
    private DefaultFindAvailableSeatOperation findAvailableSeatOperation;

    /**
     * This method is used to get all available seats at list.
     *
     * @param sourceSeatGrid The source seat grid.
     * @return The list of held seats
     */
    @Override
    public List<Seat> findTotalAvailableSeats(SeatGrid sourceSeatGrid) {
        if(sourceSeatGrid == null) {
            LOGGER.error("Seating arrangement for the venue is not proper, the venue cant be null");
            throw new SeatingArrangementNotValidException(
                    "Seating arrangement for the venue is not proper, the venue cant be null");
        }
        if (sourceSeatGrid.getNoOfRows() <= 0 || sourceSeatGrid.getNoOfColumns() <= 0) {
            LOGGER.error("Seating arrangement for the venue is not proper, the rows and columns must be greater than 0.");
            throw new SeatingArrangementNotValidException(
                    "Seating arrangement for the venue is not proper, the rows and columns must be greater than 0");
        }
        return findAvailableSeatOperation.getAvailableSeatsAsList(sourceSeatGrid);
    }

    /**
     *
     * This method is used to get all available seats at list.
     *
     * @param sourceSeatGrid The source seat grid.
     * @return The total available seats number
     */
    @Override
    public int findTotalNumberOfAvailableSeats(SeatGrid sourceSeatGrid) {
        return findTotalAvailableSeats(sourceSeatGrid).size();
    }
}
