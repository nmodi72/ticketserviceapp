package com.nmodi.ticketserviceapp.service;

import com.nmodi.ticketserviceapp.dao.TicketHandlerDao;
import com.nmodi.ticketserviceapp.exception.CustomerRequestNotValidException;
import com.nmodi.ticketserviceapp.exception.ReservationRequestNotValidException;
import com.nmodi.ticketserviceapp.exception.SeatingArrangementNotValidException;
import com.nmodi.ticketserviceapp.grid.Seat;
import com.nmodi.ticketserviceapp.grid.SeatGrid;
import com.nmodi.ticketserviceapp.grid.SeatStatus;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TicketServiceImpl implements TicketService {

    /**
     * The Logger
     */
    private final Logger LOGGER = LoggerFactory.getLogger(TicketServiceImpl.class);

    /**
     * The ticket handler dao
     */
    @Resource
    private TicketHandlerDao ticketHandlerDao;

    /**
     * The executor servcie
     */
    private static final ExecutorService threadpool = Executors.newFixedThreadPool(Integer.MAX_VALUE);

    /**
     * The held seat grid
     */
    @Getter
    @Setter
    private SeatGrid seatGridHeld;

    /**
     * The held seat list
     */
    @Getter
    @Setter
    private List<Seat> seatListHeld;

    @Getter
    @Setter
    private boolean checkHeldSeats = false;

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
        return ticketHandlerDao.getAvailableSeatsAsList(sourceSeatGrid);
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

    /**
     * This method is used to hold best available seats based on number of seats in request.
     *
     * @param sourceSeatGrid The source seat grid.
     * @param requestedSeats The requested number of seats
     * @return The list of held seats, empty list in case none are hold
     */
    @Override
    public List<Seat> holdBestAvailableSeats(SeatGrid sourceSeatGrid, int requestedSeats) {
        if (sourceSeatGrid.getNoOfRows() <= 0 || sourceSeatGrid.getNoOfColumns() <= 0) {
            throw new SeatingArrangementNotValidException(
                    "Seating arrangement for the venue is not proper, the rows and columns must be greater than 0");
        }
        if (requestedSeats <= 0 ||
                requestedSeats > ticketHandlerDao.getAvailableSeatsAsList(sourceSeatGrid).size()) {
            throw new CustomerRequestNotValidException("Request order to hold seats must be valid, total available seats are: "
                    + ticketHandlerDao.getAvailableSeatsAsList(sourceSeatGrid).size());
        }
        // best available hold seat
        List<Seat> heldSeatList = ticketHandlerDao.getBestAvailableSeats(sourceSeatGrid, requestedSeats);

        setSeatGridHeld(sourceSeatGrid);
        setSeatListHeld(heldSeatList);
        setCheckHeldSeats(true);
        threadpool.submit(thread);

        return heldSeatList;
    }

    /**
     * This method is used to reserve the held seats.
     *
     * @param sourceSeatGrid The source seat grid/the venue
     * @param heldSeatList The held seats as list
     * @return The list of held seats, empty in case none are reserved
     */
    @Override
    public List<Seat> reserveHeldSeats(SeatGrid sourceSeatGrid, List<Seat> heldSeatList) {

        if (heldSeatList == null || heldSeatList.size() == 0) {
            throw new ReservationRequestNotValidException("held seats list can't be null or empty");
        }

        for (Seat seat: heldSeatList) {
            if (!seat.getSeatStatus().equals(SeatStatus.HOLD)) {
                throw new ReservationRequestNotValidException("Request Invalid: The requested seat is not held.");
            }
            sourceSeatGrid.setSeatStatus(seat.getRow(), seat.getColumn(), SeatStatus.RESERVED);
            heldSeatList.set(heldSeatList.indexOf(seat), sourceSeatGrid.getSeat(seat.getRow(), seat.getColumn()));
        }
        return heldSeatList;
    }

    /**
     * The thread instance to verify whether the seat is reserved after 3 sec of interval.
     *
     */
    Thread thread = new Thread( new Runnable() {
        int counter = 0;
        public void run() {
            while (true) {
                try {
                    if (counter > 3) {
                        for (Seat seat : seatListHeld) {
                            if (seat.getSeatStatus().equals(SeatStatus.HOLD)) {
                                seatGridHeld.setSeatStatus(seat.getRow(), seat.getColumn(), SeatStatus.OPEN);
                                seatListHeld.set(seatListHeld.indexOf(seat), seatGridHeld.getSeat(seat.getRow(), seat.getColumn()));
                            }
                        }
                        checkHeldSeats = false;
                    }
                    Thread.sleep(1000);
                    counter++;
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                counter = checkHeldSeats == false ? 0 : counter;
            }
        }
    });

    @PreDestroy
    public void preDestroy() {
        threadpool.shutdown();
    }
}
