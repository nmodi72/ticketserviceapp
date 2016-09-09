package com.nmodi.ticketserviceapp.service;

import com.nmodi.ticketserviceapp.exception.ReservationRequestNotValidException;
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
 * reserve the held seats are working as expected
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext-test.xml")
public class ReserveHeldSeatsTest {

    @Resource
    private TicketService ticketServiceImpl;

    @Resource
    private SeatGrid sourceSeatGrid = null;

    @Before
    public void setUp() {
        sourceSeatGrid = new SeatGrid(7, 7);
        int[][] seats = new int[][]{
                {2, 2, 2, 2, 2, 2, 2},
                {0, 2, 0, 0, 0, 2, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 2, 0, 0, 0},
                {0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 2}};
        SeatGridTestUtil.setGridSeats(sourceSeatGrid, seats);
    }

    /**
     * This is to test to hold and reserve the seat for the customer
     */
    @Test
    public void testReserveHeldSeatsSuccessScenario() throws InterruptedException {
        List<Seat> heldSeats = ticketServiceImpl.holdBestAvailableSeats(sourceSeatGrid, 4);
        // 4 seats, held for few seconds
        assertThat(heldSeats.size(), is(4));
        for (Seat heldSeat: heldSeats) {
            assertThat(heldSeat.getSeatStatus(), is(SeatStatus.HOLD));
        }
        assertThat(ticketServiceImpl.findTotalNumberOfAvailableSeats(sourceSeatGrid), is(32));
        // reserving seats
        List<Seat> availableSeats = ticketServiceImpl.reserveHeldSeats(sourceSeatGrid, heldSeats);
        assertThat(availableSeats.size(), is(4));
        for (Seat availableSeat: heldSeats) {
            assertThat(availableSeat.getSeatStatus(), is(SeatStatus.RESERVED));
        }
    }

    /**
     * This test is to hold and reserve all the available seats
     */
    @Test
    public void testHoldAndReserveAllSeatsScenario() throws InterruptedException {
        // Lets hold 6 seats.
        List<Seat> heldSeats = ticketServiceImpl.holdBestAvailableSeats(sourceSeatGrid, 6);

        // 6 seats, held for few seconds
        assertThat(heldSeats.size(), is(6));
        for (Seat heldSeat: heldSeats) {
            assertThat(heldSeat.getSeatStatus(), is(SeatStatus.HOLD));
        }
        assertThat(ticketServiceImpl.findTotalNumberOfAvailableSeats(sourceSeatGrid), is(30));

        // reserving held seats
        List<Seat> reserveSeats = ticketServiceImpl.reserveHeldSeats(sourceSeatGrid, heldSeats);
        assertThat(reserveSeats.size(), is(6));
        for (Seat reserveSeat: heldSeats) {
            assertThat(reserveSeat.getSeatStatus(), is(SeatStatus.RESERVED));
        }
        assertThat(ticketServiceImpl.findTotalNumberOfAvailableSeats(sourceSeatGrid), is(30));

        // Again we'll book the rest available seats
        List<Seat> heldSeatsAtAnotherCall = ticketServiceImpl.holdBestAvailableSeats(sourceSeatGrid, 30);

        // 6 seats, held for few seconds
        assertThat(heldSeatsAtAnotherCall.size(), is(30));
        for (Seat heldSeat: heldSeatsAtAnotherCall) {
            assertThat(heldSeat.getSeatStatus(), is(SeatStatus.HOLD));
        }
        assertThat(ticketServiceImpl.findTotalNumberOfAvailableSeats(sourceSeatGrid), is(0));

        // reserving held seats
        List<Seat> reserveSeatsAtAnotherCall = ticketServiceImpl.reserveHeldSeats(sourceSeatGrid, heldSeatsAtAnotherCall);
        assertThat(reserveSeatsAtAnotherCall.size(), is(30));
        for (Seat reserveSeat: reserveSeatsAtAnotherCall) {
            assertThat(reserveSeat.getSeatStatus(), is(SeatStatus.RESERVED));
        }
        assertThat(ticketServiceImpl.findTotalNumberOfAvailableSeats(sourceSeatGrid), is(0));
    }

    /**
     * This is to test try to reserve the seat after the system makes it available again
     */
    @Test
    public void testToReserveSeatsAfterTimeOutScenario() throws InterruptedException {
        SeatGrid actualSeatGrid = new SeatGrid(5, 5);
        int[][] seats = new int[][]{
                {2, 0, 0, 0, 2},
                {2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2}};
        SeatGridTestUtil.setGridSeats(actualSeatGrid, seats);
        List<Seat> heldSeatList = ticketServiceImpl.holdBestAvailableSeats(actualSeatGrid, 3);
        assertThat(ticketServiceImpl.findTotalNumberOfAvailableSeats(actualSeatGrid), is(0));
        for (Seat heldSeat: heldSeatList) {
            assertThat(heldSeat.getSeatStatus(), is(SeatStatus.HOLD));
        }
        // Lets wait for 5 sec. to make system the seat open/available again
        Thread.sleep(5000);
        for (Seat heldSeat: heldSeatList) {
            assertThat(heldSeat.getSeatStatus(), is(SeatStatus.OPEN));
        }
        // Lets reserve the seats directly
        try {
            List<Seat> availableSeats = ticketServiceImpl.reserveHeldSeats(actualSeatGrid, heldSeatList);
            fail("Request Invalid: The requested seat is not held.");
        } catch (ReservationRequestNotValidException ex) {
            assertTrue(true);
        }
    }
}
