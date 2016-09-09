package com.nmodi.ticketserviceapp.grid;

import com.nmodi.ticketserviceapp.exception.SeatingArrangementNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides the grid implementation which is the foundation for the seats to reside.
 */
public class SeatGrid {

    /**
     * The logger
     */
    private final Logger LOGGER = LoggerFactory.getLogger(SeatGrid.class);

    /**
     * The default number of rows to set
     */
    private static final int DEFAULT_NO_OF_ROWS = 10;

    /**
     * The default number of columns to set
     */
    private static final int DEFAULT_NO_OF_COLUMNS = 10;

    /**
     * The seats array
     */
    private int[][] seats;

    /**
     * This is the no-arg constructor. which internally makes call to the 2-arg constructor with the default no of rows
     * and columns.
     */
    public SeatGrid() {
        this(DEFAULT_NO_OF_ROWS, DEFAULT_NO_OF_COLUMNS);
    }

    /**
     * This is the 2-arg constructor. which internally makes call to the 3-arg constructor with the default seat status
     * which is open.
     * 
     * @param noOfRows The no of rows in the theater.
     * @param noOfColumns The no of columns in the theater.
     */
    public SeatGrid(int noOfRows, int noOfColumns) {
        this(noOfRows, noOfColumns, SeatStatus.OPEN);
    }

    /**
     * This is the 3-arg constructor.
     * 
     * @param noOfRows The no of rows in the theater.
     * @param noOfColumns The no of columns in the theater.
     * @param seatStatus The seat status to set for each seat.
     */
    public SeatGrid(int noOfRows, int noOfColumns, SeatStatus seatStatus) {
        LOGGER.debug("Creating SeatGrid for {} rows and {} columns", noOfRows, noOfColumns);

        if (noOfRows <= 0 || noOfColumns <= 0) {
            LOGGER.error("The no of rows and no of columns must be greater than 0: No of rows: {} No of column: {}", noOfRows, noOfColumns);
            throw new SeatingArrangementNotValidException("The no of rows and no of columns must be greater than 0");
        }
        seats = new int[noOfRows][noOfColumns];
        for (int i = 0; i < noOfRows; i++) {
            for (int j = 0; j < noOfColumns; j++) {
                seats[i][j] = seatStatus.getSeatStatusValue();
            }
        }
    }

    /**
     * This method is used to set the status of a particular seat.
     * 
     * @param row The row position.
     * @param column The column position.
     * @param seatStatus The status of the seat to set.
     */
    public void setSeatStatus(int row, int column, SeatStatus seatStatus) {
        LOGGER.debug("Setting seat status for {} row and {} column to {}", row, column, seatStatus);
        if (row >= seats.length || column >= seats[0].length || row < 0 || column < 0) {
            LOGGER.error("The no of rows and no of columns must be updated to valid limits. {} X {}",
                    seats.length, seats[0].length);
            throw new SeatingArrangementNotValidException(
                    "The no of rows and no of columns must be updated to valid limits." + seats.length + "X"
                            + seats[0].length);
        }
        seats[row][column] = seatStatus.getSeatStatusValue();
    }

    /**
     * This method is used to get the status of a particular seat.
     * 
     * @param row The row position.
     * @param column The column position.
     * @return The SeatStatus enum value
     */
    public SeatStatus getSeatStatus(int row, int column) {
        if (row >= seats.length || column >= seats[0].length || row < 0 || column < 0) {
            LOGGER.error("The no of rows and no of columns must be requested in valid limits. {} X {}",
                    seats.length, seats[0].length);
            throw new SeatingArrangementNotValidException(
                    "The no of rows and no of columns must be requested in valid limits." + seats.length + "X"
                            + seats[0].length);
        }
        switch (seats[row][column]) {
        case 0:
            return SeatStatus.OPEN;
        case 1:
            return SeatStatus.HOLD;
        case 2:
            return SeatStatus.RESERVED;
        default:
            // Not implemented
            return null;
        }
    }

    /**
     * This method is used to get the particular seat detail.
     * 
     * @param row The row position.
     * @param column The column position.
     * @return The new seat object
     */
    public Seat getSeat(int row, int column) {
        if (row >= seats.length || column >= seats[0].length || row < 0 || column < 0) {
            LOGGER.error("The no of rows and no of columns must be requested in valid limits." +
                    seats.length + "X" + seats[0].length);
            throw new SeatingArrangementNotValidException(
                    "The no of rows and no of columns must be requested in valid limits." + seats.length + "X"
                            + seats[0].length);
        }
        SeatStatus seatStatus = null;
        switch (seats[row][column]) {
        case 0:
            seatStatus = SeatStatus.OPEN;
            break;
        case 1:
            seatStatus = SeatStatus.HOLD;
            break;
        case 2:
            seatStatus = SeatStatus.RESERVED;
            break;
        default:
            // Not implemented
        }
        return new Seat(row, column, seatStatus);
    }

    /**
     * This method returns the no of rows.
     * 
     * @return Returns the no of rows.
     */
    public int getNoOfRows() {
        if (seats == null) {
            LOGGER.error("Seating arrangement cant be null");
            throw new SeatingArrangementNotValidException("Seating arrangement cant be null");
        }
        return seats.length;
    }

    /**
     * This method returns the no of columns.
     * 
     * @return Returns the no of columns.
     */
    public int getNoOfColumns() {
        String methodName = "getNoOfColumns";
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(methodName);
        }
        if (seats.length <= 0) {
            LOGGER.error("The no of rows should be greater than 0");
            throw new SeatingArrangementNotValidException("The no of rows should be greater than 0");
        }
        return seats[0].length;
    }
}
