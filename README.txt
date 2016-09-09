Ticket Service

About the project:
--------------------------------------------------------------------------------------------------------
This is a sample ticket service that facilitates the discovery, temporary hold and final reservation of
seats within a high demand performance venue.

Functionality for the ticket service
--------------------------------------------------------------------------------------------------------
* Find the number of seats available in the venue.
* Find and hold the best available seats on behalf of the customer.
* Reserve and commit a specific group of held seats for a customer.

Project Structure:
--------------------------------------------------------------------------------------------------------
The project is implemented as a maven project using Spring 4.3.2 and junit 4.12 for test cases.

Design & Implementation:
--------------------------------------------------------------------------------------------------------
The classes/interfaces are included in the java folder - src/main/java folder.

    1. An enum that holds the different seat status. [com.nmodi.ticketserviceapp.grid.SeatStatus]
        eg: OPEN with value 0, HOLD with value 1 or RESERVED with value 2

    2. A class which provides the basic info of a Seat. [com.nmodi.ticketserviceapp.grid.Seat]
        eg: Seat positions like row and column, Current seat status.

    3. A class which retrieves/ updates the seat status. [com.nmodi.ticketserviceapp.grid.SeatGrid]

    4. 



The base spring configuration (applicationContext.xml) is present in src/main/resources folder.

The logger properties (log4j.properties) is also present in src/main/resources folder.

Junit test cases
--------------------------------------------------------------------------------------------------------