package com.nmodi.ticketserviceapp.ticketservices;

import com.nmodi.ticketserviceapp.exception.CustomerRequestNotValidException;
import com.nmodi.ticketserviceapp.exception.ReservationRequestNotValidException;
import com.nmodi.ticketserviceapp.grid.Seat;
import com.nmodi.ticketserviceapp.grid.SeatGrid;
import com.nmodi.ticketserviceapp.grid.SeatStatus;

import java.util.ArrayList;
import java.util.List;

public class DefaultReserveHeldSeats implements ReserveHeldSeats {

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
}
