package helpers;

import java.util.Random;

/**
 * Simulated Annealing with geometric (exponential) cooling.
 *
 * Key differences from the old version:
 *  - Geometric cooling (T *= alpha each step) instead of linear subtraction.
 *    This spends proportionally more time at low temperatures where good
 *    solutions are refined — the standard approach in SA literature.
 *  - Standard Boltzmann acceptance: exp(delta/T) > random().
 *    The old version had a hardcoded threshold of 0.0085 that wrongly
 *    blocked valid uphill moves at low temperature.
 *  - isHot() now checks iteration count, not temperature crossing zero.
 */
public class SimulatedAnnealing {

    private final Random random = new Random();

    private double currentTemperature;
    private final double alpha;         // cooling factor per step, e.g. 0.99999995
    private final long totalSteps;
    private long stepsDone = 0;

    /**
     * @param startTemperature  initial temperature (e.g. 10.0)
     * @param endTemperature    final temperature (e.g. 0.001)
     * @param totalSteps        number of SA iterations (e.g. 500_000)
     *
     * alpha is derived automatically so temperature decays from start to end
     * over exactly totalSteps steps: alpha = (end/start)^(1/steps)
     */
    public SimulatedAnnealing(double startTemperature, double endTemperature, long totalSteps) {
        this.currentTemperature = startTemperature;
        this.totalSteps = totalSteps;
        // geometric decay: T_final = T_start * alpha^steps  =>  alpha = (T_final/T_start)^(1/steps)
        this.alpha = Math.pow(endTemperature / startTemperature, 1.0 / totalSteps);
    }

    /**
     * Standard SA acceptance.
     * Better moves always accepted.
     * Worse moves accepted with probability exp(delta / T).
     * Temperature is decreased by one step each call.
     */
    public boolean accept(double newScore, double currentScore) {
        stepsDone++;
        currentTemperature *= alpha;

        if (newScore >= currentScore) return true;

        double prob = Math.exp((newScore - currentScore) / currentTemperature);
        return random.nextDouble() < prob;
    }

    public boolean isHot() {
        return stepsDone < totalSteps;
    }

    public double getTemperatureRatio() {
        return (double)(totalSteps - stepsDone) / totalSteps;
    }

    public double getCurrentTemperature() {
        return currentTemperature;
    }
}
