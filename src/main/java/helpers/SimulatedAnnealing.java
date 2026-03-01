package helpers;

import java.util.Random;

public class SimulatedAnnealing {
    private final Random random = new Random();
    private final double startTemperature;
    private double currentTemperature;
    private final double steps;
    private final double stepSize;

    public SimulatedAnnealing(double temperature, double steps) {
        this.currentTemperature = temperature;
        this.startTemperature = temperature;
        this.steps = steps;
        this.stepSize = startTemperature / steps;
    }

    /**
     * Simulated Annealing Acceptance Function
     * @param newKeyScore the score of the new key
     * @param currentKeyScore the score of the current key
     * @return whether the new key is accepted
     */
    public boolean acceptWithTemperature(double newKeyScore, double currentKeyScore) {
        currentTemperature -= stepSize;

        // Always accept better keys
        if (newKeyScore >= currentKeyScore) {
            return true;
        }

        double degradation = -Math.abs(currentKeyScore - newKeyScore);
        double acceptanceProbability = Math.exp(degradation / currentTemperature);

        return acceptanceProbability > 0.0085 && random.nextDouble() < acceptanceProbability;
    }

    /**
     * Returns true as long as we did not reach the end temperature
     * @return whether the process is still "hot"
     */
    public boolean isHot() {
        return currentTemperature > 0;
    }

    /**
     * Returns the current progress of the simulated annealing process
     * @return progress as a percentage
     */
    public double getProgress() {
        return (startTemperature - currentTemperature) / startTemperature;
    }


    public double getTemperatureRatio() {
        return currentTemperature / startTemperature;
    }
}
