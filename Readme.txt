This app consists in a Rest API that consume the "cex.io" api to retrieve the price of bitcoin.
I used a Spring Scheduled Annotation in order to set a crone that execute each ten seconds to invoke the cex.io api
and save this data.
For made that use a WebClient to invoke the cex.io api, user the library Jackson to deserialize the json response to my
BTCPrice entity and  serialize my entity to a json. To persist the data i parse the data to entity BTCPrice, set the
time when was retrieved and serialize this object and save in a file, adding on that a json response per line.

## Main Class ##

    - BTCController : Expose the tree endpoints to resolve the problem.
    - BTCService : Define the methods to retrieve the data from the cex.io api, and the method to calculate the average
     of prices between two dates, retrieve the max price and get all historic data.
    - PersistenceService: This service has the behavior of persistence tier. Then save the data and read it in the file.
    And define the method to serialize and deserialize the json and entities.
    - SchedulerService: Its function is invoke the other service each ten seconds in order to retrieve the data from

The app used too the Swagger library to expose the endpoints defined -> http://localhost:8080/swagger-ui.html#!/

The endpoints defined are:

 - GET - /btc/price/history: Return a list of value retrieved from the cex.io api saved.
 - GET - /btc/price: This service require a local date time like parameter with this format "2021-07-29T18:04:03.777"
 and return the price on historic data saved that match with this param.
 - GET - /btc/price/avg: This service require two local date time params whit this format "2021-07-29T18:04:03.777", in
 order to indicate a interval of time to search prices and get his average. With that params this service return the
 average of prices between this two date times and return too the percentage difference between the max value on the
 historic data and the average.

## Unit Test ##
Add some unit test cases using Mockito and Spring Test.
Define a file "BTCPriceHistoryTestRead.txt" for a test case.
Keep to do add more test cases to increase the coverage.
