/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.magmacore.util;

public class Round {
    private Round() {
        throw new IllegalStateException("Utility class");
    }

    public static double decimalPlaces(double value, int places) {
        double number = Math.pow(10.0, places);
        return (double)Math.round(value * number) / number;
    }

    public static float decimalPlaces(float value, int places) {
        double number = Math.pow(10.0, places);
        return (float)Math.round((double)value * number) / (float)number;
    }

    public static double fourDecimalPlaces(double value) {
        return Round.decimalPlaces(value, 4);
    }

    public static float fourDecimalPlaces(float value) {
        return Round.decimalPlaces(value, 4);
    }

    public static double twoDecimalPlaces(double value) {
        return Round.decimalPlaces(value, 2);
    }

    public static double oneDecimalPlace(double value) {
        return Round.decimalPlaces(value, 1);
    }
}

