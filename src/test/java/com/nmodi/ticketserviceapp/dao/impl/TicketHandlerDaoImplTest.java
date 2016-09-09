package com.nmodi.ticketserviceapp.dao.impl;

import com.nmodi.ticketserviceapp.dao.TicketHandlerDao;
import com.nmodi.ticketserviceapp.exception.CustomerRequestNotValidException;
import com.nmodi.ticketserviceapp.exception.SeatingArrangementNotValidException;
import com.nmodi.ticketserviceapp.grid.Seat;
import com.nmodi.ticketserviceapp.grid.SeatGrid;
import com.nmodi.ticketserviceapp.grid.SeatStatus;
import com.nmodi.ticketserviceapp.service.TicketService;
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
 * This unit test is to verify whether
 * hold best available seats are working as expected
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext-test.xml")
public class TicketHandlerDaoImplTest {

    @Resource
    private TicketHandlerDao ticketHandlerDao;

    @Resource
    private SeatGrid sourceSeatGrid = null;

    @Before
    public void setUp() {
        sourceSeatGrid = new SeatGrid(7, 7);
        int[][] seats = new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0}};
        SeatGridTestUtil.setGridSeats(sourceSeatGrid, seats);
    }

    /**
     * This is to test to get available seats as list - success scenario
     * List<Seat> getAvailableSeatsAsList(SeatGrid sourceSeatGrid)
     */
    @Test
    public void testGetAvailableSeatsForSuccessScenario(){
        List<Seat> availableSeats = ticketHandlerDao.getAvailableSeatsAsList(sourceSeatGrid);
        // 4 seats, held for few seconds
        assertThat(availableSeats.size(), is(49));
        for (Seat availableSeat: availableSeats) {
            assertThat(availableSeat.getSeatStatus(), is(SeatStatus.OPEN));
        }
    }

    /**
     * This is to test to get available seats as list, when no seats are available
     * List<Seat> getAvailableSeatsAsList(SeatGrid sourceSeatGrid)
     */
    @Test
    public void testGetAvailableSeatWhenNoSeatsAvailable() throws InterruptedException {
        SeatGrid seatGrid = new SeatGrid(5, 5);
        int[][] seats = new int[][]{
                {2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2}};
        SeatGridTestUtil.setGridSeats(seatGrid, seats);

        List<Seat> availableSeats = ticketHandlerDao.getAvailableSeatsAsList(seatGrid);
        // 4 seats, held for few seconds
        assertThat(availableSeats.size(), is(0));
    }

    /**
     * This is to test to get available seats as list, when seat grid is null
     * List<Seat> getAvailableSeatsAsList(SeatGrid sourceSeatGrid)
     */
    @Test
    public void testGetAvailableSeatWithInvalidSeatGrid() throws InterruptedException {
        try {
            List<Seat> availableSeats = ticketHandlerDao.getAvailableSeatsAsList(null);
            fail("source seat grid can not be null.");
        } catch (SeatingArrangementNotValidException ex) {
            assertTrue(true);
        }
    }

    /**
     * This is to test to hold the seat for the customer, when no seats are available
     * List<Seat> holdBestAvailableSeats(SeatGrid sourceSeatGrid, int requestSeats);
     */
    @Test
    public void testToHoldSeatsWhenNoSeatsAvailableScenario() throws InterruptedException {
        SeatGrid actualSeatGrid = new SeatGrid(5, 5);
        int[][] seats = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2}};
        SeatGridTestUtil.setGridSeats(actualSeatGrid, seats);
        List<Seat> availableSeats = ticketHandlerDao.getBestAvailableSeats(actualSeatGrid, 2);
        assertThat(availableSeats.size(), is(2));
        for (Seat heldSeat: availableSeats) {
            assertThat(heldSeat.getSeatStatus(), is(SeatStatus.HOLD));
        }
        assertThat(ticketHandlerDao.getAvailableSeatsAsList(actualSeatGrid).size(), is(8));
    }
}
