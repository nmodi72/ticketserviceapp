package com.nmodi.ticketserviceapp.grid;

/**
 * This is an enum which will define the seat status. 
 * The possible seat statuses are: 
 * OPEN - This will have a value of 0. 
 * HOLD - This will have a value of 1. 
 * RESERVED - This will have a value of 2.
 */
public enum SeatStatus {

    OPEN(0), HOLD(1), RESERVED(2);

    private int seatStatusValue;

    /**
     * Constructor to set the seat status value.
     * @param seatStatusValue The seat status value to set.
     */
    private SeatStatus(int seatStatusValue) {
        this.seatStatusValue = seatStatusValue;
    }

    /**
     * Getter method for seatStatusValue.
     * @return the seatStatusValue
     */
    public int getSeatStatusValue() {
        return seatStatusValue;
    }

    /**
     * Setter method for seatStatusValue.
     * @param seatStatusValue the seatStatusValue to set
     */
    public void setSeatStatusValue(int seatStatusValue) {
        this.seatStatusValue = seatStatusValue;
    }
}
