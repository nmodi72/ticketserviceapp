package com.nmodi.ticketserviceapp.grid;


import com.nmodi.ticketserviceapp.exception.SeatingArrangementNotValidException;
import com.nmodi.ticketserviceapp.util.SeatGridTestUtil;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This is a junit test to test the methods in SeatGrid class
 * {@link SeatGrid}
 */
public class SeatGridTest {

    /**
     * The SeatGrid object
     */
    private SeatGrid seatGrid = null;

    /**
     * Test set up settings
     */
    @Before
    public final void setUpTestSettings() {
        seatGrid = new SeatGrid(8, 8);
        int[][] seats = new int[][]{
                {0, 1, 2, 0, 2, 2, 2, 0},
                {0, 1, 2, 0, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 2, 0, 1, 1, 1, 0},
                {0, 1, 2, 0, 2, 2, 2, 0},
                {0, 2, 2, 0, 1, 1, 1, 0},
                {0, 2, 2, 0, 1, 1, 1, 0},
                {0, 1, 2, 0, 2, 2, 2, 0}};
        SeatGridTestUtil.setGridSeats(seatGrid, seats);
    }

    /**
     * This is to test the following method
     * public Seat getSeat(int row, int column) - Success Scenario
     */
    @Test
    public void getSeatOfGivenRowAndColumnPositionTest() {

        Seat actualSeat = seatGrid.getSeat(7, 0);

        Seat resultSeat = new Seat(7, 0, SeatStatus.OPEN);

        assertThat(actualSeat.getSeatStatus(), is(resultSeat.getSeatStatus()));
        assertThat(actualSeat, is(resultSeat));
    }

    /**
     * This is to test the following method
     * public Seat getSeat(int row, int column) - Failure Scenario
     */
    @Test
    public void getSeatOutOfBoudryTest() {
        try {
            seatGrid.getSeat(9, 9);
            fail("Test case fails if it reaches here.");
        } catch(Exception e) {
            assertTrue(true);
        }
    }

    /**
     * This is to test creating SeatGrid for 0 row and 0 column - Failure Scenario
     */
    @Test
    public void testCreateSeatGridWithZeroRowAndZeroColum() {
        try {
            SeatGrid seatGrid = new SeatGrid(0, 0);
            fail("The no of rows and no of columns must be greater than 0");
        } catch(SeatingArrangementNotValidException e) {
            assertTrue(true);
        }
    }

    /**
     * This is to test the following method
     * public SeatStatus getSeatStatus(int row, int column) - Success Scenario
     */
    @Test
    public void getSeatStatusForGivenRowAndColumn() {
        SeatStatus actualSeatStatus = seatGrid.getSeatStatus(2, 2);

        Seat resultSeat = new Seat(2, 2, SeatStatus.OPEN);

        assertThat(actualSeatStatus, is(resultSeat.getSeatStatus()));
    }

    /**
     * This is to test the following method
     * public SeatStatus getSeatStatus(int row, int column) - Failure Scenario
     */
    @Test
    public void getSeatStatusForOutOfBoundryFailure() {
        try {
            seatGrid.getSeatStatus(10, 10);
            fail("Test case fails if it reaches here.");
        } catch(Exception e) {
            assertTrue(true);
        }
    }

    /**
     * This is to test the following method
     * public void setSeatStatus(int row, int column, SeatStatus seatStatus) - Success Scenario
     */
    @Test
    public void testUpdateSeatStatus() {
        seatGrid.setSeatStatus(3, 3, SeatStatus.HOLD);

        SeatStatus actualSeatStatus = seatGrid.getSeatStatus(3, 3);

        assertThat(actualSeatStatus, is(SeatStatus.HOLD));
    }

    /**
     * This is to test the following method
     * public void setSeatStatus(int row, int column, SeatStatus seatStatus) - Failure Scenario
     */
    @Test
    public void testUpdateSeatStatusOutOfBoundryFailure() {
        try {
            seatGrid.setSeatStatus(10, 10, SeatStatus.RESERVED);
            fail("Test case fails if it reaches here.");
        } catch(Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSeat() {
        Seat[][] seats = new Seat[5][5];
    }
}
