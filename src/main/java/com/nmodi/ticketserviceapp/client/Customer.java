package com.nmodi.ticketserviceapp.client;

import lombok.Getter;
import lombok.Setter;

/**
 * This is the client customer class, which has all the necessary details that are expected from a client.
 */
@Getter
@Setter
public class Customer {
    /**
     * The customer id
     */
    long customerId;

    /**
     * Number of seats requested by customer
     */
    int requestedNumberOfSeats;

    /**
     * This is fake input on behalf of customer, about what customer wants to do after holding seats.
     *
     * true, if customer wants to reserve it
     * false otherwise
     */
    boolean customerInputAfterHoldingSeats;

    /**
     * Constructor for a Seat.
     *
     * @param customerId The customer's unique id
     * @param requestedNumberOfSeats The number of requested seats by customer
     * @param customerInputAfterHoldingSeats The fake, expected customer's input after holding seats
     */
    public Customer(long customerId, int requestedNumberOfSeats, boolean customerInputAfterHoldingSeats) {
        this.customerId = customerId;
        this.requestedNumberOfSeats = requestedNumberOfSeats;
        this.customerInputAfterHoldingSeats = customerInputAfterHoldingSeats;
    }
}
