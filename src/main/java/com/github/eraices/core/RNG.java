package com.github.eraices.core;

public class RNG {
    // Returns a random int on the interval [min, max]
    public static int randomInt(int min, int max) {
        int range = max - min + 1;
        return (int)(Math.random() * range) + min;
    }

    // Returns a random double on the interval [min, max)
    public static double randomDouble(double min, double max) {
        double range = max - min;
        return (Math.random() * range) + min;
    }

    // Returns true "chance" percent of the time
    public static boolean percentChance(double chance) {
        return (Math.random() * 100.0) < chance;
    }
}
