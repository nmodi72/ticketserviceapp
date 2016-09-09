package com.nmodi.ticketserviceapp.dao.impl;

import com.nmodi.ticketserviceapp.dao.TicketHandlerDao;
import com.nmodi.ticketserviceapp.exception.CustomerRequestNotValidException;
import com.nmodi.ticketserviceapp.exception.SeatingArrangementNotValidException;
import com.nmodi.ticketserviceapp.grid.Seat;
import com.nmodi.ticketserviceapp.grid.SeatGrid;
import com.nmodi.ticketserviceapp.grid.SeatStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the find available util class to find the total available/open seats to hold/reserve within venue.
 */
public class TicketHandlerDaoImpl implements TicketHandlerDao {

    /**
     * This method is used to get all available seats at list.
     *
     * @param sourceSeatGrid The source seat grid.
     * @return The list of held seats
     */
    @Override
    public List<Seat> getAvailableSeatsAsList(SeatGrid sourceSeatGrid) {
        if (sourceSeatGrid == null) {
            throw new SeatingArrangementNotValidException("source seat grid can not be null.");
        }
        List<Seat> availableSeats = new ArrayList<Seat>();
        for (int x = 0; x < sourceSeatGrid.getNoOfRows(); x++) {
            for (int y = 0; y < sourceSeatGrid.getNoOfColumns(); y++) {
                if (sourceSeatGrid.getSeatStatus(x, y).equals(SeatStatus.OPEN)) {
                    availableSeats.add(sourceSeatGrid.getSeat(x, y));
                }
            }
        }
        return availableSeats;
    }

    /**
     * This method is used to find and hold the particular seat for given venue.
     *
     * @param sourceSeatGrid The source seat grid.
     * @param requiredSeatCount The number of seats coming in request.
     * @return The list of held seats
     */
    @Override
    public List<Seat> getBestAvailableSeats(SeatGrid sourceSeatGrid, int requiredSeatCount) {

        if (sourceSeatGrid == null) {
            throw new SeatingArrangementNotValidException("source seat grid can not be null.");
        }
        if (requiredSeatCount == 0) {
            throw new CustomerRequestNotValidException("The number of seats in request can not be 0");
        }
        List<Seat> availableSeats = new ArrayList<Seat>();
        int heldSeats = 0;
        while (heldSeats < requiredSeatCount) {
            int highestSeatSlot = 0;
            int bestRowToAccomodateGuest = Integer.MIN_VALUE;
            // assign the best seats if available, the far is better
            for (int row = (sourceSeatGrid.getNoOfRows() - 1); row >= 0; row--) {
                List<Seat> initiallyAssignedSeatList = assignSeats(sourceSeatGrid, row, requiredSeatCount);
                availableSeats.addAll(initiallyAssignedSeatList);
                heldSeats = heldSeats + initiallyAssignedSeatList.size();
                // when request is fulfilled
                if(heldSeats == requiredSeatCount) {
                    return availableSeats;
                }
                // based on the request, find best suitable row and place if the request is not easily fulfil
                // E.g. if 5 seats are requested and seats are available in 1, 2 and 3 blocks then it should fulfil 3 first.
                List<Integer> slotsAvailableByRow = getSeatCountsInGroupsByRow(sourceSeatGrid, row);
                for (Integer slotAvailable : slotsAvailableByRow) {
                    if (slotAvailable > highestSeatSlot) {
                        highestSeatSlot = slotAvailable;
                        bestRowToAccomodateGuest = row;
                    }
                }
            }
            // Still some s\eats are not issued
            if (heldSeats != requiredSeatCount) {
                if (bestRowToAccomodateGuest != Integer.MIN_VALUE) {
                    int remainingSeats = requiredSeatCount - heldSeats;
                    highestSeatSlot = remainingSeats < highestSeatSlot ? remainingSeats : highestSeatSlot;
                    List<Seat> bestAssignedSeatList = assignSeats(sourceSeatGrid, bestRowToAccomodateGuest, highestSeatSlot);
                    availableSeats.addAll(bestAssignedSeatList);
                    heldSeats = heldSeats + bestAssignedSeatList.size();
                }
            }
        }
        return availableSeats;
    }

    /**
     * This method is used to assign the seats.
     *
     * @param sourceSeatGrid The source seat grid.
     * @param requiredSeatCount The number of seats coming in request.
     * @return The list of held seats
     */
    private static List<Seat> assignSeats(SeatGrid sourceSeatGrid, int row, int requiredSeatCount) {
        // a list which includes number of seats available together in group
        List<Integer> seatGroup = getSeatCountsInGroupsByRow(sourceSeatGrid, row);
        if (seatGroup.contains(requiredSeatCount)) {
            return findAndHoldSeatsNextToEachOther(sourceSeatGrid, row, requiredSeatCount, 0);
        } else {
            // maximum suitable seat, based on the number of requested seats
            // For example, if a person wants to request only single seat and a row has 2, 3 adn 4 seat blocks available than
            // the person should be given a ticket from 2 seat blocks (based on difference between requiredSeatCount and blockSize)
            int maxSuitableSeat = Integer.MAX_VALUE;
            for (Integer seatBlockSize: seatGroup) {
                int differenceInSeatBlockAndRequestedSeat = seatBlockSize - requiredSeatCount;
                maxSuitableSeat = (differenceInSeatBlockAndRequestedSeat > 0) &&
                        (differenceInSeatBlockAndRequestedSeat < maxSuitableSeat)? seatBlockSize : maxSuitableSeat;
            }
            // check whether the maxSuitableSeat is updated and able to allot requested seats
            if ((maxSuitableSeat != Integer.MAX_VALUE) && (maxSuitableSeat > requiredSeatCount)) {
                return findAndHoldSeatsNextToEachOther(sourceSeatGrid, row, requiredSeatCount, maxSuitableSeat);
            }
        }
        return new ArrayList<Seat>();
    }

    /**
     * This method is used to book/hold the seats, which also takes a look for
     * availability of neighbor seats.
     *
     * @param sourceSeatGrid The source seat grid.
     * @param row The row number.
     * @param requiredSeatCount The number of required seats
     * @param highestAvailableSeat The highest available seat from which the seat should be given.
     * @return The list of integer, which gives the available seat blocks in specific row
     */
    private static List<Seat> findAndHoldSeatsNextToEachOther(SeatGrid sourceSeatGrid, int row, int requiredSeatCount, int highestAvailableSeat) {
        List<Seat> seats = new ArrayList<Seat>();
        for (int seatNumber = 0; seatNumber < sourceSeatGrid.getNoOfColumns(); seatNumber++) {
            if (sourceSeatGrid.getSeatStatus(row, seatNumber).equals(SeatStatus.OPEN)) {
                int seatGroupCount = 1;
                int nextSeat = (seatNumber + 1) < sourceSeatGrid.getNoOfColumns() ? seatNumber + 1 : -1;
                while (nextSeat > 0 && sourceSeatGrid.getSeatStatus(row, nextSeat).equals(SeatStatus.OPEN)) {
                    seatGroupCount++;
                    nextSeat = (nextSeat + 1) < sourceSeatGrid.getNoOfColumns() ? nextSeat + 1 : -1;
                }
                if (seatGroupCount == requiredSeatCount) {
                    while (requiredSeatCount != 0) {
                        // Hold specific seat
                        sourceSeatGrid.setSeatStatus(row, seatNumber, SeatStatus.HOLD);
                        seats.add(sourceSeatGrid.getSeat(row, seatNumber++));
                        requiredSeatCount--;
                    }
                    return seats;
                } else if ((seatGroupCount == highestAvailableSeat) &&
                        (seatGroupCount > requiredSeatCount)) {
                    while (requiredSeatCount != 0) {
                        // Hold specific seat
                        sourceSeatGrid.setSeatStatus(row, seatNumber, SeatStatus.HOLD);
                        seats.add(sourceSeatGrid.getSeat(row, seatNumber++));
                        requiredSeatCount--;
                    }
                    return seats;
                } else {
                    seatNumber = seatNumber + seatGroupCount;
                }
            }
        }
        return seats;
    }

    /**
     * This method is used to get the seat count by row.
     *
     * @param sourceSeatGrid The source seat grid.
     * @param row The row number.
     * @return The list of integer, which gives the available seat blocks in specific row
     */
    private static List<Integer> getSeatCountsInGroupsByRow(SeatGrid sourceSeatGrid, int row) {
        List<Integer> seatGroup = new ArrayList<Integer>();
        for (int seatNumber = 0; seatNumber < sourceSeatGrid.getNoOfColumns(); seatNumber++) {
            if (sourceSeatGrid.getSeatStatus(row, seatNumber).equals(SeatStatus.OPEN)) {
                int seatGroupCount = 1;
                int nextSeat = (seatNumber + 1) < sourceSeatGrid.getNoOfColumns() ? seatNumber + 1 : -1;
                while (nextSeat > 0 && sourceSeatGrid.getSeatStatus(row, nextSeat).equals(SeatStatus.OPEN)) {
                    seatGroupCount++;
                    nextSeat = (nextSeat + 1) < sourceSeatGrid.getNoOfColumns() ? nextSeat + 1 : -1;
                }
                seatGroup.add(seatGroupCount);
                seatNumber = seatNumber + seatGroupCount;
            }
        }
        return seatGroup;
    }
}
