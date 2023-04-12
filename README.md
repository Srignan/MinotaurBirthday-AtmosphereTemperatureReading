# MinotaurBirthday-AtmosphereTemperatureReading
## Minotaur Birthday Problem
Q1.java is the only file for the first problem. There are multiple classes in the file.  

Efficiency: The efficiency of the program can be analyzed in terms of time and space complexity. The time complexity of the addPresent and removePresent methods in the PresentChain class is O(n) in the worst case, where n is the number of presents in the chain. The contains method also has a time complexity of O(n). As the program is concurrent, the overall time complexity is reduced since multiple threads (servants) work simultaneously to add and remove presents. The space complexity is O(n) for storing the presents in the chain.  

Correctness: The program is designed to ensure that the servants perform their tasks in the correct order: adding a present to the chain and then immediately writing a "Thank you" note and removing the present from the chain. The PresentChain class methods ensure the chain's integrity, preventing any inconsistencies or corruption. Additionally, the ServantThread class keeps track of the number of "Thank you" notes written by each servant, ensuring that we have a correct count of the total number of notes written.  

Progress guarantee: The program uses ReentrantLock to ensure that only one servant can access the chain at a time, avoiding race conditions and ensuring that progress is made. When a servant thread acquires the lock, it guarantees that no other threads can make progress until the lock is released. Since the time complexity of the critical section (addPresent, removePresent, and contains methods) is O(n) in the worst case, progress is ensured even in the presence of contention. However, using a more fine-grained locking strategy could further improve the progress guarantee, as it would allow multiple threads to work on different parts of the chain simultaneously.  

## Atmosphere Temperature Reading Problem
The MarsRoverTemperatureModule.java and the TemperatureSensor.java are the two files for this assignment.

Efficiency: The implemented solution is efficient due to the usage of concurrent data structures and executor services. The ConcurrentHashMap and ConcurrentLinkedDeque allow multiple threads to access and modify the data without causing excessive contention or performance issues. The ScheduledExecutorService manages the sensor threads and ensures they execute periodically, reducing the overhead of thread creation and management.  

Correctness: The correctness of the program is preserved by using concurrent data structures that provide thread-safe operations. The ConcurrentHashMap ensures that temperature readings are stored and retrieved correctly, even when accessed by multiple threads simultaneously. Similarly, the ConcurrentLinkedDeque ensures that the temperature readings for each sensor are safely added and accessed without risking data corruption or loss. The use of Java's Stream API for generating the hourly report guarantees that the top 5 highest and lowest temperatures and the 10-minute interval with the largest temperature difference are correctly calculated.  

Progress Guarantee: The progress guarantee is ensured by using non-blocking data structures and scheduling the sensor threads to run at a fixed rate. The ScheduledExecutorService schedules the temperature sensor threads to execute at a fixed rate of 1 minute, ensuring that each sensor takes a reading at the expected time. Furthermore, since the ConcurrentHashMap and ConcurrentLinkedDeque are non-blocking, the threads will not be delayed due to lock contention, ensuring that the temperature readings are taken at regular intervals as expected. The hourly report compilation is also scheduled at a fixed rate, ensuring that it is generated on time.  

It is important to note that while testing I used smaller intervals and a smaller time period, the program runs every minute and outputs the final results every hour as asked by the prompt.
