package com.nmodi.ticketserviceapp.service;

import com.nmodi.ticketserviceapp.exception.SeatingArrangementNotValidException;
import com.nmodi.ticketserviceapp.grid.Seat;
import com.nmodi.ticketserviceapp.grid.SeatGrid;
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
 * This test is to verify whether find available seats are working as required.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext-test.xml")
public class FindAvailableSeatsTest {

    @Resource
    private TicketService ticketServiceImpl;

    @Resource
    private SeatGrid sourceSeatGrid = null;

    @Resource
    private SeatGrid expectedSeatGrid = null;

    @Before
    public void setUp() {
        sourceSeatGrid = new SeatGrid(12, 14);
        int[][] seats = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        SeatGridTestUtil.setGridSeats(sourceSeatGrid, seats);

        expectedSeatGrid = new SeatGrid(12, 14);
        int[][] expectedResultSeats = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        SeatGridTestUtil.setGridSeats(expectedSeatGrid, expectedResultSeats);
    }

    /**
     * This is to test the find total available seats for success scenario
     * List<Seat> findTotalAvailableSeats(SeatGrid sourceSeatGrid)
     */
    @Test
    public void testFindTotalAvailableSeatsForSuccessScenario() {
        List<Seat> availableSeats = ticketServiceImpl.findTotalAvailableSeats(sourceSeatGrid);

        List<Seat> expectedAvailableSeats = ticketServiceImpl.findTotalAvailableSeats(expectedSeatGrid);

        assertThat(availableSeats, is(expectedAvailableSeats));
    }

    /**
     * This is to test the find total available seats for failure scenario, when SourceGrid is null
     * List<Seat> findTotalAvailableSeats(SeatGrid sourceSeatGrid)
     */
    @Test
    public void testFindTotalAvailableSeatsWhenSourceGridIsNull() {
        try {
            List<Seat> availableSeats = ticketServiceImpl.findTotalAvailableSeats(null);
            fail("Seating arrangement for the venue is not proper, the venue cant be null");
        } catch (SeatingArrangementNotValidException ex) {
            assertTrue(true);
        }
    }

    /**
     * This is to test the find total available seats for failure scenario,
     * when no available seat in SeatGrid
     * List<Seat> findTotalAvailableSeats(SeatGrid sourceSeatGrid)
     */
    @Test
    public void testFindTotalAvailableSeatsWhenNoSeatsAvailable() {
        SeatGrid actualSeatGrid = new SeatGrid(5, 5);
        int[][] seats = new int[][]{
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1}};
        SeatGridTestUtil.setGridSeats(actualSeatGrid, seats);
        List<Seat> availableSeats = ticketServiceImpl.findTotalAvailableSeats(actualSeatGrid);
        assertThat(availableSeats.size(), is(0));
    }

    /**
     * This is to test the find total number of available seats for success scenario
     * int findTotalNumberOfAvailableSeats(SeatGrid sourceSeatGrid)
     */
    @Test
    public void testFindTotalNumberOfAvailableSeatsForSuccessScenario() {
        int availableSeatsNumber = ticketServiceImpl.findTotalNumberOfAvailableSeats(sourceSeatGrid);

        int expectedAvailableSeatsNumber = ticketServiceImpl.findTotalNumberOfAvailableSeats(expectedSeatGrid);

        assertThat(availableSeatsNumber, is(expectedAvailableSeatsNumber));
    }

    /**
     * This is to test the find total number of available seats for failure scenario,
     * when no available seat in SeatGrid
     * int findTotalNumberOfAvailableSeats(SeatGrid sourceSeatGrid)
     */
    @Test
    public void testFindTotalNumberOfAvailableSeatsForSomeSuccessScenario() {
        SeatGrid actualSeatGrid = new SeatGrid(5, 5);
        int[][] seats = new int[][]{
                {2, 2, 2, 2, 2},
                {1, 1, 1, 1, 1},
                {1, 1, 0, 1, 1},
                {1, 1, 2, 2, 1},
                {1, 1, 1, 1, 1}};
        SeatGridTestUtil.setGridSeats(actualSeatGrid, seats);
        int availableSeatsNumber = ticketServiceImpl.findTotalNumberOfAvailableSeats(actualSeatGrid);
        assertThat(availableSeatsNumber, is(1));
    }

    /**
     * This is to test the find total number of available seats for failure scenario,
     * when SourceGrid is null
     * int findTotalNumberOfAvailableSeats(SeatGrid sourceSeatGrid)
     */
    @Test
    public void testFindTotalNumberOfAvailableSeatsWhenSourceGridIsNull() {
        try {
            int availableSeatsNumber = ticketServiceImpl.findTotalNumberOfAvailableSeats(null);
            fail("Seating arrangement for the venue is not proper, the venue cant be null");
        } catch (SeatingArrangementNotValidException ex) {
            assertTrue(true);
        }
    }
}
