package com.nmodi.ticketserviceapp.service;

import com.nmodi.ticketserviceapp.exception.CustomerRequestNotValidException;
import com.nmodi.ticketserviceapp.grid.Seat;
import com.nmodi.ticketserviceapp.grid.SeatGrid;
import com.nmodi.ticketserviceapp.grid.SeatStatus;
import com.nmodi.ticketserviceapp.util.SeatGridTestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This test is to verify whether
 * hold best available seats are working as expected
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext-test.xml")
public class HoldBestAvailableSeatsTest {

    @Resource
    private TicketService ticketServiceImpl;

    @Resource
    private SeatGrid sourceSeatGrid = null;

    @Before
    public void setUp() {
        sourceSeatGrid = new SeatGrid(7, 7);
        int[][] seats = new int[][]{
                {0, 0, 2, 2, 0, 0, 0},
                {0, 2, 0, 2, 0, 2, 0},
                {0, 0, 0, 0, 2, 2, 0},
                {0, 0, 0, 2, 0, 0, 0},
                {0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 2}};
        SeatGridTestUtil.setGridSeats(sourceSeatGrid, seats);
    }

    /**
     * This is to test to hold and reserve the seat for the customer
     * List<Seat> holdBestAvailableSeats(SeatGrid sourceSeatGrid, int requestSeats)
     */
    @Test
    public void testHoldBestAvailableSeatsForSuccessScenario() throws InterruptedException {
        List<Seat> availableSeats = ticketServiceImpl.holdBestAvailableSeats(sourceSeatGrid, 4);
        // 4 seats, held for few seconds
        assertThat(availableSeats.size(), is(4));
        for (Seat heldSeat: availableSeats) {
            assertThat(heldSeat.getSeatStatus(), is(SeatStatus.HOLD));
        }
        assertThat(ticketServiceImpl.findTotalNumberOfAvailableSeats(sourceSeatGrid), is(34));
        // Lets sleep thread for few second and again expect the result
        Thread.sleep(5000);
        // seats available again because those were not reserved
        assertThat(ticketServiceImpl.findTotalNumberOfAvailableSeats(sourceSeatGrid), is(38));
        for (Seat heldSeat: availableSeats) {
            assertThat(heldSeat.getSeatStatus(), is(SeatStatus.OPEN));
        }
    }

    /**
     * This is to test to hold the seat for the customer, when limited seats are available
     *List<Seat> holdBestAvailableSeats(SeatGrid sourceSeatGrid, int requestSeats);
     */
    @Test
    public void testHoldBestAvailableSeatsForLimitedSeatsAvailableScenario() throws InterruptedException {
        SeatGrid actualSeatGrid = new SeatGrid(5, 5);
        int[][] seats = new int[][]{
                {2, 2, 2, 2, 2},
                {2, 0, 2, 0, 2},
                {2, 2, 0, 2, 2},
                {2, 2, 2, 0, 2},
                {0, 2, 2, 2, 0}};
        SeatGridTestUtil.setGridSeats(actualSeatGrid, seats);

        List<Seat> availableSeats = ticketServiceImpl.holdBestAvailableSeats(actualSeatGrid, 6);
        // all seats are unavailable and six were held
        assertThat(ticketServiceImpl.findTotalNumberOfAvailableSeats(actualSeatGrid), is(0));
        // Lets sleep thread
        Thread.sleep(5000);
        // seats available again because those were not reserved
        assertThat(ticketServiceImpl.findTotalNumberOfAvailableSeats(actualSeatGrid), is(6));
    }

    /**
     * This is to test to hold the seat for the customer, when no seats are available
     * List<Seat> holdBestAvailableSeats(SeatGrid sourceSeatGrid, int requestSeats);
     */
    @Test
    public void testToHoldSeatsWhenNoSeatsAvailableScenario() throws InterruptedException {
        SeatGrid actualSeatGrid = new SeatGrid(5, 5);
        int[][] seats = new int[][]{
                {2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2}};
        SeatGridTestUtil.setGridSeats(actualSeatGrid, seats);
        try {
            List<Seat> availableSeats = ticketServiceImpl.holdBestAvailableSeats(actualSeatGrid, 2);
            fail("Request order to hold seats must be valid, total available seats are: 0");
        } catch (CustomerRequestNotValidException ex) {
            assertTrue(true);
        }
    }

    /**
     * This is to test to hold the seat, when other customer already held some available seats
     * List<Seat> holdBestAvailableSeats(SeatGrid sourceSeatGrid, int requestSeats);
     */
    @Test
    public void testHoldSeatsWhenAllSeatsAreHeldScenario() throws InterruptedException {
        SeatGrid actualSeatGrid = new SeatGrid(5, 5);
        int[][] seats = new int[][]{
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2}};
        SeatGridTestUtil.setGridSeats(actualSeatGrid, seats);
        try {
            List<Seat> availableSeats = ticketServiceImpl.holdBestAvailableSeats(actualSeatGrid, 4);
            fail("Request order to hold seats must be valid, total available seats are: 0");
        } catch (CustomerRequestNotValidException ex) {
            assertTrue(true);
        }
    }

    /**
     * This is to test to hold the seat, when requested seat number is not valid
     * List<Seat> holdBestAvailableSeats(SeatGrid sourceSeatGrid, int requestSeats);
     */
    @Test
    public void testHoldSeatsForInvalidRequest(){
        SeatGrid actualSeatGrid = new SeatGrid(5, 5);
        int[][] seats = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2}};
        SeatGridTestUtil.setGridSeats(actualSeatGrid, seats);
        try {
            List<Seat> availableSeats = ticketServiceImpl.holdBestAvailableSeats(actualSeatGrid, -3);
            fail("Request order to hold seats must be valid, total available seats are: 15");
        } catch (CustomerRequestNotValidException ex) {
            assertTrue(true);
        }
    }
}
