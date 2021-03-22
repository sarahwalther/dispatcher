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

### Thoughts
Some of the tests are pretty slow as written. Ideally I'd spend a little more time figuring out how to manipulate the timer inside of the action block for the TimerTask in the FirstInFirstOutDelivery.

As written, in order to compare the efficiency between the 2 strategies the function has to get called with 2 different arguments.

I made use of Kotlin's native capabilities to deal with threading as much as possible.

The system is designed in a way that would enable users to add new strategies easily going forward. The existing ones should not need altering for that to be possible.