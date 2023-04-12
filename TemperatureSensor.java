import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TemperatureSensor implements Runnable {
    private final int sensorId;
    private final ConcurrentHashMap<Integer, ConcurrentLinkedDeque<Float>> temperatureReadings;
    private final Random random;

    public TemperatureSensor(int sensorId, ConcurrentHashMap<Integer, ConcurrentLinkedDeque<Float>> temperatureReadings) {
        this.sensorId = sensorId;
        this.temperatureReadings = temperatureReadings;
        this.temperatureReadings.put(sensorId, new ConcurrentLinkedDeque<>());
        this.random = new Random();
    }

    @Override
    public void run() {
        float temperature = randomTemperature();
        temperatureReadings.get(sensorId).add(temperature);
        System.out.printf("Sensor %d: Recorded temperature %.2fF%n", sensorId, temperature);
    }

    private float randomTemperature() {
        return random.nextFloat() * (70F - (-100F)) + (-100F);
    }
}
