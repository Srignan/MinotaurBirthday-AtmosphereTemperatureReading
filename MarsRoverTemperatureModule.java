import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MarsRoverTemperatureModule {

    private static final int NUM_SENSORS = 8;
    private static final int NUM_READINGS = 60; //hourly ratings

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(NUM_SENSORS);
        ConcurrentHashMap<Integer, ConcurrentLinkedDeque<Float>> temperatureReadings = new ConcurrentHashMap<>();

        for (int i = 0; i < NUM_SENSORS; i++) {
            TemperatureSensor sensor = new TemperatureSensor(i, temperatureReadings);
            executor.scheduleAtFixedRate(sensor, 0, 1, TimeUnit.MINUTES);
        }

        executor.scheduleAtFixedRate(() -> {
            compileHourlyReport(temperatureReadings);
            temperatureReadings.values().forEach(Deque::clear);
        }, 1, 1, TimeUnit.HOURS);
    }

    private static void compileHourlyReport(ConcurrentHashMap<Integer, ConcurrentLinkedDeque<Float>> temperatureReadings) {
        List<Float> allReadings = temperatureReadings.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<Float> top5Highest = allReadings.stream()
                .sorted(Comparator.reverseOrder())
                .limit(5)
                .collect(Collectors.toList());

        List<Float> top5Lowest = allReadings.stream()
                .sorted()
                .limit(5)
                .collect(Collectors.toList());

        float maxDifference = Float.MIN_VALUE;
        int startMinute = 0;
        for (int i = 0; i < NUM_READINGS - 10; i++) {
            float minTemp = Collections.min(allReadings.subList(i, i + 10));
            float maxTemp = Collections.max(allReadings.subList(i, i + 10));
            float difference = maxTemp - minTemp;
            if (difference > maxDifference) {
                maxDifference = difference;
                startMinute = i;
            }
        }

        System.out.println("Top 5 Highest Temperatures: " + top5Highest);
        System.out.println("Top 5 Lowest Temperatures: " + top5Lowest);
        System.out.println("Largest temperature difference between minute " + startMinute + " and " + (startMinute + 10));
    }
}
