package com.nmodi.ticketserviceapp.ticketservices;

import com.nmodi.ticketserviceapp.client.Customer;
import com.nmodi.ticketserviceapp.exception.CustomerRequestNotValidException;
import com.nmodi.ticketserviceapp.exception.SeatingArrangementNotValidException;
import com.nmodi.ticketserviceapp.grid.Seat;
import com.nmodi.ticketserviceapp.grid.SeatGrid;
import com.nmodi.ticketserviceapp.grid.SeatStatus;
import com.nmodi.ticketserviceapp.operation.DefaultFindAvailableSeatOperation;
import com.nmodi.ticketserviceapp.operation.DefaultHoldBestAvailableSeatOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;

public class DefaultHoldBestAvailableSeats implements HoldBestAvailableSeats {

    /**
     * The logger
     */
    private final Logger LOGGER = LoggerFactory.getLogger(DefaultHoldBestAvailableSeats.class);

    @Resource
    private ReserveHeldSeats reserveHeldSeats;

    @Resource
    private DefaultFindAvailableSeatOperation findAvailableSeatOperation;

    @Resource
    private DefaultHoldBestAvailableSeatOperation holdBestAvailableSeatOperation;

    /**
     * This method is used to hold best available seats based on number of seats in request.
     *
     * @param sourceSeatGrid The source seat grid.
     * @param customer The customer with requirement.
     * @return The list of held seats, empty list in case none are hold
     */
    @Override
    public List<Seat> holdBestAvailableSeats(SeatGrid sourceSeatGrid, Customer customer) {
        if (sourceSeatGrid.getNoOfRows() <= 0 || sourceSeatGrid.getNoOfColumns() <= 0) {
            throw new SeatingArrangementNotValidException(
                    "Seating arrangement for the venue is not proper, the rows and columns must be greater than 0");
        }
        if (customer == null || customer.getCustomerId() == 0) {
            throw new CustomerRequestNotValidException("The customer can't be null");
        }
        if (customer.getRequestedNumberOfSeats() <= 0 ||
                customer.getRequestedNumberOfSeats() > findAvailableSeatOperation.getAvailableSeatsAsList(sourceSeatGrid).size()) {
            throw new CustomerRequestNotValidException("Request order to hold seats must be valid, total available seats are: "
                    + findAvailableSeatOperation.getAvailableSeatsAsList(sourceSeatGrid).size());
        }
        // best available hold seat
        List<Seat> heldSeatList = holdBestAvailableSeatOperation.getBestAvailableSeats(sourceSeatGrid, customer.getRequestedNumberOfSeats());

        // if the response coming empty list, return ]
        if(heldSeatList == null || heldSeatList.size() == 0) {
            LOGGER.error("Response from hold seats operation is null or empty.");
            return heldSeatList;
        }

        // processing fake reuqest from customer to reserve the held seats
        if (customer.isCustomerInputAfterHoldingSeats()) {
            heldSeatList = reserveHeldSeats.reserveHeldSeats(sourceSeatGrid, heldSeatList);
        }

        // Further process the seat to check, whether the status was changed to Reserved
        // or make it available again after few second of time interval.
        return callTimer(sourceSeatGrid, heldSeatList);
    }

    /**
     * This method is used to observe that user committed for to reserve the held ticket.
     * Otherwise, it will be made open/available again.
     *
     * @param venueSeats The source seat grid.
     * @param heldSeatList The list of held seats.
     * @return The list of held seats
     */
    private List<Seat> callTimer(final SeatGrid venueSeats, final List<Seat> heldSeatList) {

        //create runnable thread to check whether the seat is still on hold, if so then convert
        Runnable runnableThread = new Runnable() {
            int counter = 0;
            public void run() {
                while (true) {
                    try {
                        System.out.println("Timer is at: " + counter);
                        if (counter > 5) {
                            for (Seat seat: heldSeatList) {
                                if (seat.getSeatStatus().equals(SeatStatus.HOLD)) {
                                    venueSeats.setSeatStatus(seat.getRow(), seat.getColumn(), SeatStatus.OPEN);
                                    heldSeatList.remove(heldSeatList.indexOf(seat));
//                                    heldSeatList.set(heldSeatList.indexOf(seat), venueSeats.getSeat(seat.getRow(), seat.getColumn()));
                                }
                            }
                            break;
                        }
                        Thread.sleep(1000);
                        counter++;
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        runnableThread.run();
        return heldSeatList;
    }
}
