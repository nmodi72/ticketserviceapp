package com.nmodi.ticketserviceapp.grid;

import lombok.Getter;
import lombok.Setter;

/**
 * This class defines details of an individual seat.
 */
@Getter
@Setter
public class Seat {

    /**
     * The row
     */
    private int row;

    /**
     * The column
     */
    private int column;

    /**
     * The seat status
     */
    private SeatStatus seatStatus;

    /**
     * Constructor for a Seat.
     * 
     * @param row The row position of the seat.
     * @param column The column position of the seat.
     * @param seatStatus The status of the seat.
     */
    public Seat(int row, int column, SeatStatus seatStatus) {
        this.row = row;
        this.column = column;
        this.seatStatus = seatStatus;
    }

    /* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((seatStatus == null) ? 0 : seatStatus.hashCode());
        result = prime * result + column;
        result = prime * result + row;
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Seat other = (Seat) obj;
        if (seatStatus.getSeatStatusValue() != other.seatStatus.getSeatStatusValue())
            return false;
        if (column != other.column)
            return false;
        if (row != other.row)
            return false;
        return true;
    }

}
