package com.nmodi.ticketserviceapp.util;

import com.nmodi.ticketserviceapp.client.Customer;
import com.nmodi.ticketserviceapp.grid.SeatGrid;
import com.nmodi.ticketserviceapp.grid.SeatStatus;

/**
 * This is just a utility class to help tests to generate customer.
 */
public class GenerateCustomerTestUtil {
    /**
     * This is id counter
     */
    private static long idCounter = 0;

    /**
     * This is a utility method to create new id for new customer.
     */
    public static synchronized long createID()
    {
        return idCounter++;
    }
    /**
     * This is a utility method to create customer.
     */
    public static Customer createCustomer() {
        return new Customer(createID(), 4, true);
    }
}
