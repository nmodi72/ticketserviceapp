package com.nmodi.ticketserviceapp.ticketservices;

import com.nmodi.ticketserviceapp.client.Customer;
import com.nmodi.ticketserviceapp.exception.SeatingArrangementNotValidException;
import com.nmodi.ticketserviceapp.grid.Seat;
import com.nmodi.ticketserviceapp.grid.SeatGrid;
import com.nmodi.ticketserviceapp.grid.SeatStatus;
import com.nmodi.ticketserviceapp.service.TicketService;
import com.nmodi.ticketserviceapp.util.GenerateCustomerTestUtil;
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
public class HoldBestAvailableSeatsTest {

    @Resource
    private TicketService ticketServiceImpl;

    @Resource
    private SeatGrid sourceSeatGrid = null;

    @Resource
    private SeatGrid expectedSeatGrid = null;

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

        expectedSeatGrid = new SeatGrid(7, 7);
        int[][] expectedResultSeats = new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0}};
        SeatGridTestUtil.setGridSeats(expectedSeatGrid, expectedResultSeats);
    }

    /**
     * This is to test to hold and reserve the seat for the customer
     * List<Seat> holdBestAvailableSeats(SeatGrid sourceSeatGrid, Customer customer)
     */
    @Test
    public void testHoldBestAvailableSeatsForSuccessScenario() {
        List<Seat> availableSeats = ticketServiceImpl.holdBestAvailableSeats(sourceSeatGrid, new Customer(1L, 4, true));

        List<Seat> expectedAvailableSeats = ticketServiceImpl.holdBestAvailableSeats(expectedSeatGrid, new Customer(1L, 4, true));

        for (Seat availableSeat: availableSeats) {
            assertThat(availableSeat.getSeatStatus(), is(SeatStatus.RESERVED));
        }
        assertThat(ticketServiceImpl.findTotalNumberOfAvailableSeats(sourceSeatGrid), is(45));
        assertThat(availableSeats, is(expectedAvailableSeats));
    }

    /**
     * This is to test to hold and reserve the seat for the customer, when limited seats are available
     *List<Seat> holdBestAvailableSeats(SeatGrid sourceSeatGrid, Customer customer);
     */
    @Test
    public void testHoldBestAvailableSeatsForLimitedSeatsAvailableScenario() {
        SeatGrid actualSeatGrid = new SeatGrid(5, 5);
        int[][] seats = new int[][]{
                {2, 2, 2, 2, 2},
                {2, 0, 2, 2, 2},
                {2, 2, 0, 2, 2},
                {2, 2, 2, 0, 2},
                {2, 2, 2, 2, 0}};
        SeatGridTestUtil.setGridSeats(actualSeatGrid, seats);

        List<Seat> availableSeats = ticketServiceImpl.holdBestAvailableSeats(actualSeatGrid, new Customer(1L, 4, true));

        assertThat(ticketServiceImpl.findTotalNumberOfAvailableSeats(actualSeatGrid), is(0));
    }

}
