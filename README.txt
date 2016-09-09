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

Rules / assumptions
--------------------------------------------------------------------------------------------------------
* System will find the best available seats on these factors.
    a. it will try to accommodate the guest on back rows.
    b. system will try to give all the tickets together
    c. if the tickets are not available all together, it will try to give maximum seats together.

* The system will wait for 3 seconds after holding seats. if guest don't reserve it in this time then
system will make it available/open again

Project Structure:
--------------------------------------------------------------------------------------------------------
The project is implemented as a maven project using Spring 4.3.2 and junit 4.12 for test cases.

Build Notes
--------------------------------------------------------------------------------------------------------
To checkout in your local go to your terminal and type,

'git clone https://github.com/nmodi72/ticketserviceapp.git'
'cd ticketserviceapp'

Now, to build locally type
'mvn clean install'

Since I have used lombok library, please configure lombok in your IDE.

Design & Implementation:
--------------------------------------------------------------------------------------------------------
The classes/interfaces are included in the java folder - src/main/java folder.

    1. An enum that holds the different seat status. [com.nmodi.ticketserviceapp.grid.SeatStatus]
        eg: OPEN with value 0, HOLD with value 1 or RESERVED with value 2

    2. A class which provides the basic info of a Seat. [com.nmodi.ticketserviceapp.grid.Seat]
        eg: Seat positions like row and column, Current seat status.

    3. A class which retrieves/ updates the seat status. [com.nmodi.ticketserviceapp.grid.SeatGrid]

    4. TicketService interface is the service  which will be exposed. which is backed by
        TicketServiceImpl for implementation [com.nmodi.ticketserviceapp.service.TicketService & TicketServiceImpl]

    5. Basic Dao operations are in TicketHandlerDao and backed by TicketHandlerDaoImpl
        [com.nmodi.ticketserviceapp.dao.TicketHandlerDao & TicketHandlerDaoImpl]

    6. Expected exceptions are kept under [com.nmodi.ticketserviceapp.exception]


The base spring configuration (applicationContext.xml) is present in src/main/resources folder.

The logger properties (log4j.properties) is also present in src/main/resources folder.

Junit test cases
--------------------------------------------------------------------------------------------------------
The junit tests have been included in the test folder - src/test/java folder.

    1. Test for the methods in SeatGrid class [com.nmodi.ticketserviceapp.grid.SeatGridTest]

    2. Test for validating the Find and Hold Seats for TicketHandlerDaoImpl class
       [com.nmodi.ticketserviceapp.TicketHandlerDaoImplTest]

    3. Tests for validating service operations are

        a. For finding available seats and count are under FindAvailableSeatsTest
        [com.nmodi.ticketserviceapp.service.FindAvailableSeatsTest]

        b. Validation of the feature of holding seats for few seconds and available again(if not reserved)
          is under HoldBestAvailableSeatsTest [com.nmodi.ticketserviceapp.service.HoldBestAvailableSeatsTest]

        c. Validation of the feature of holding seats and reserving them in certain timeframe is included in
          ReserveHeldSeatsTest [com.nmodi.ticketserviceapp.service.ReserveHeldSeatsTest]

Improvement area
--------------------------------------------------------------------------------------------------------
* Will try to improve algorithm,
    a. After assigning maximum possible seats, I will try to search from that row instead of starting from last row.
    b. I will also include the customer's priority whether they want seats in front row or last row.
    c. I will try to reduce time complexity and make cleaner the algorithm code.