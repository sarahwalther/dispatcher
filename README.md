# Delivery Simulator

### Description
This application ia a simulation that compares the efficiency of two different approaches of dispatching couriers to deliver meals. 

### Running the simulation
There are 2 ways of running the simulator 
- MATCHED
    - this will run the simulation assuming the 'matched' strategy, where each courier is responsible for a specific order
    ```bash
    $ ./gradlew bootRun -Pargs=MATCHED
    ```
- FIRST_IN_FIRST_OUT
    - this will run the simulation assuming the 'first in first out' strategy, where an arriving courier is responsible for whichever order is ready first
    ```bash
    $ ./gradlew bootRun -Pargs=FIRST_IN_FIRST_OUT
    ```

### Running the tests
```bash
$ ./gradlew test
```
