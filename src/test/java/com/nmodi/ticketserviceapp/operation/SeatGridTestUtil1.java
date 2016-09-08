package com.nmodi.ticketserviceapp.operation;

import com.nmodi.ticketserviceapp.grid.SeatGrid;
import com.nmodi.ticketserviceapp.grid.SeatStatus;

/**
 * This is just a utility class to help convert the array of seats to seat grid. This is used
 * by the test cases for easily setting up seat grids.
 */
public class SeatGridTestUtil1 {

    /**
     * This is a utility method to set the seats to source grid.
     * @param sourceSeatGrid The source seat grid.
     * @param seats The seats which needs to be set to the source grid.
     */
    public static void setGridSeats(SeatGrid sourceSeatGrid, int[][] seats) {

        for(int i = 0; i < seats.length; i++) {

            for(int j = 0; j < seats[i].length; j++) {
                SeatStatus seatStatus = null;
                switch (seats[i][j]) {
                    case 0:
                        seatStatus = SeatStatus.OPEN;
                        break;
                    case 1:
                        seatStatus = SeatStatus.HOLD;
                        break;
                    case 2:
                        seatStatus = SeatStatus.RESERVED;
                    default:
                        //Not implemented
                }
                sourceSeatGrid.setSeatStatus(i, j, seatStatus);
            }
        }
    }
}
