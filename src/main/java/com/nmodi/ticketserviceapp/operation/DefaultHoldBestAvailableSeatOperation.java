package com.nmodi.ticketserviceapp.operation;

import com.nmodi.ticketserviceapp.exception.CustomerRequestNotValidException;
import com.nmodi.ticketserviceapp.exception.SeatingArrangementNotValidException;
import com.nmodi.ticketserviceapp.grid.Seat;
import com.nmodi.ticketserviceapp.grid.SeatGrid;
import com.nmodi.ticketserviceapp.grid.SeatStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the util class to find best available seats based on the number of requested seats
 */
public final class DefaultHoldBestAvailableSeatOperation implements HoldBestAvailableSeatOperation{

    /**
     * The Logger
     */
    private final Logger LOGGER = LoggerFactory.getLogger(SeatGrid.class);

    /**
     * This method is used to find and hold the particular seat for given venue.
     *
     * @param sourceSeatGrid The source seat grid.
     * @param numberOfRequestedSeats The number of seats coming in request.
     * @return The list of held seats
     */
    @Override
    public List<Seat> getBestAvailableSeats(SeatGrid sourceSeatGrid, int numberOfRequestedSeats) {

        if (sourceSeatGrid == null) {
            throw new SeatingArrangementNotValidException("source seat grid can not be null.");
        }
        if (numberOfRequestedSeats == 0) {
            throw new CustomerRequestNotValidException("The number of seats in request can not be 0");
        }
        List<Seat> availableSeats = new ArrayList<Seat>();
        int heldSeats = 0;
        while (heldSeats < numberOfRequestedSeats ) {
            int highestSeatSlot = 0;
            int bestRowToAccomodateGuest = Integer.MIN_VALUE;
            // assign the best seats if available, the far is better
            for (int row = (sourceSeatGrid.getNoOfRows() - 1); row >= 0; row--) {
                List<Seat> initiallyAssignedSeatList = issueSeats(sourceSeatGrid, row, numberOfRequestedSeats);
                availableSeats.addAll(initiallyAssignedSeatList);
                heldSeats = heldSeats + initiallyAssignedSeatList.size();
                // when request is fulfilled
                if(heldSeats == numberOfRequestedSeats) {
                    return availableSeats;
                }
                // based on the request, find best suitable row and place if the request is not easily fulfil
                // E.g. if 5 seats are requested and seats are available in 1, 2 and 3 blocks then it should fulfil 3 first.
                List<Integer> slotsAvailableByRow = getSeatsByClusterByRow(sourceSeatGrid, row);
                for (Integer slotAvailable : slotsAvailableByRow) {
                    if (slotAvailable > highestSeatSlot) {
                        highestSeatSlot = slotAvailable;
                        bestRowToAccomodateGuest = row;
                    }
                }
            }
            // Still some s\eats are not issued
            if (heldSeats != numberOfRequestedSeats) {
                if (bestRowToAccomodateGuest != Integer.MIN_VALUE) {
                    int remainingSeats = numberOfRequestedSeats - heldSeats;
                    highestSeatSlot = remainingSeats < highestSeatSlot ? remainingSeats : highestSeatSlot;
                    List<Seat> bestAssignedSeatList = issueSeats(sourceSeatGrid, bestRowToAccomodateGuest, highestSeatSlot);
                    availableSeats.addAll(bestAssignedSeatList);
                    heldSeats = heldSeats + bestAssignedSeatList.size();
                }
            }
        }
        return availableSeats;
    }

    /**
     * This method is used to issue the seats to guest.
     *
     * @param sourceSeatGrid The source seat grid.
     * @param numberOfRequestedSeats The number of seats coming in request.
     * @return The list of held seats
     */
    private static List<Seat> issueSeats(SeatGrid sourceSeatGrid, int row, int numberOfRequestedSeats) {
        // a list which includes number of seats available together in cluster
        List<Integer> seatClusterSets = getSeatsByClusterByRow(sourceSeatGrid, row);
        if (seatClusterSets.contains(numberOfRequestedSeats)) {
            return getNeighborSeats(sourceSeatGrid, row, numberOfRequestedSeats, 0);
        } else {
            // maximum suitable seat, based on the number of requested seats
            // For example, if a person wants to request only single seat and a row has 2, 3 adn 4 seat blocks available than
            // the person should be given a ticket from 2 seat blocks (based on difference between numberOfRequestedSeats and blockSize)
            int maxSuitableSeat = Integer.MAX_VALUE;
            for (Integer seatBlockSize: seatClusterSets) {
                int differenceInSeatBlockAndRequestedSeat = seatBlockSize - numberOfRequestedSeats;
                maxSuitableSeat = (differenceInSeatBlockAndRequestedSeat > 0) &&
                        (differenceInSeatBlockAndRequestedSeat < maxSuitableSeat)? seatBlockSize : maxSuitableSeat;
            }
            // check whether the maxSuitableSeat is updated and able to allot requested seats
            if ((maxSuitableSeat != Integer.MAX_VALUE) && (maxSuitableSeat > numberOfRequestedSeats)) {
                return getNeighborSeats(sourceSeatGrid, row, numberOfRequestedSeats, maxSuitableSeat);
            }
        }
        return new ArrayList<Seat>();
    }

    /**
     * This method is used to get the seat blocks for given row.
     *
     * @param sourceSeatGrid The source seat grid.
     * @param row The row number.
     * @return The list of integer, which gives the available seat blocks in specific row
     */
    private static List<Integer> getSeatsByClusterByRow(SeatGrid sourceSeatGrid, int row) {
        List<Integer> seatCluster = new ArrayList<Integer>();
        for (int seatNumber = 0; seatNumber < sourceSeatGrid.getNoOfColumns(); seatNumber++) {
            if (sourceSeatGrid.getSeatStatus(row, seatNumber).equals(SeatStatus.OPEN)) {
                int seatClusterCount = 1;
                int nextSeat = (seatNumber + 1) < sourceSeatGrid.getNoOfColumns() ? seatNumber + 1 : -1;
                while (nextSeat > 0 && sourceSeatGrid.getSeatStatus(row, nextSeat).equals(SeatStatus.OPEN)) {
                    seatClusterCount++;
                    nextSeat = (nextSeat + 1) < sourceSeatGrid.getNoOfColumns() ? nextSeat + 1 : -1;
                }
                seatCluster.add(seatClusterCount);
                seatNumber = seatNumber + seatClusterCount;
            }
        }
        return seatCluster;
    }

    /**
     * This method is used to book/hold the seats, which also takes a look for
     * availability of neighbor seats for specific request.
     *
     * @param sourceSeatGrid The source seat grid.
     * @param row The row number.
     * @param numberOfRequiredSeats The number of required seats
     * @param highestAvailableSeat The highest available seat from which the seat should be given.
     * @return The list of integer, which gives the available seat blocks in specific row
     */
    private static List<Seat> getNeighborSeats(SeatGrid sourceSeatGrid, int row, int numberOfRequiredSeats, int highestAvailableSeat) {
        List<Seat> seats = new ArrayList<Seat>();
        for (int seatNumber = 0; seatNumber < sourceSeatGrid.getNoOfColumns(); seatNumber++) {
            if (sourceSeatGrid.getSeatStatus(row, seatNumber).equals(SeatStatus.OPEN)) {
                int seatClusterCount = 1;
                int nextSeat = (seatNumber + 1) < sourceSeatGrid.getNoOfColumns() ? seatNumber + 1 : -1;
                while (nextSeat > 0 && sourceSeatGrid.getSeatStatus(row, nextSeat).equals(SeatStatus.OPEN)) {
                    seatClusterCount++;
                    nextSeat = (nextSeat + 1) < sourceSeatGrid.getNoOfColumns() ? nextSeat + 1 : -1;
                }
                if (seatClusterCount == numberOfRequiredSeats) {
                    while (numberOfRequiredSeats != 0) {
                        // Hold specific seat
                        sourceSeatGrid.setSeatStatus(row, seatNumber, SeatStatus.HOLD);
                        seats.add(sourceSeatGrid.getSeat(row, seatNumber++));
                        numberOfRequiredSeats--;
                    }
                    return seats;
                } else if ((seatClusterCount == highestAvailableSeat) &&
                        (seatClusterCount > numberOfRequiredSeats)) {
                    while (numberOfRequiredSeats != 0) {
                        // Hold specific seat
                        sourceSeatGrid.setSeatStatus(row, seatNumber, SeatStatus.HOLD);
                        seats.add(sourceSeatGrid.getSeat(row, seatNumber++));
                        numberOfRequiredSeats--;
                    }
                    return seats;
                } else {
                    seatNumber = seatNumber + seatClusterCount;
                }
            }
        }
        return seats;
    }
}
