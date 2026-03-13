/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.seasons.utils;

public class TransitionColors {
    private int red;
    private int green;
    private int blue;

    public TransitionColors(int n, int n2, int n3) {
        this.red = n;
        this.green = n2;
        this.blue = n3;
    }

    public static TransitionColors fromHex(String string) {
        int n = Integer.valueOf(string.substring(0, 2), 16);
        int n2 = Integer.valueOf(string.substring(2, 4), 16);
        int n3 = Integer.valueOf(string.substring(4, 6), 16);
        return new TransitionColors(n, n2, n3);
    }

    public String toHex() {
        return String.format("%02x%02x%02x", this.red, this.green, this.blue);
    }

    public static String[] getBlendedPhases(String string, String string2) {
        TransitionColors transitionColors = TransitionColors.fromHex(string);
        TransitionColors transitionColors2 = TransitionColors.fromHex(string2);
        int n = transitionColors2.red - transitionColors.red;
        int n2 = transitionColors2.green - transitionColors.green;
        int n3 = transitionColors2.blue - transitionColors.blue;
        int n4 = n / 4;
        int n5 = n2 / 4;
        int n6 = n3 / 4;
        TransitionColors transitionColors3 = new TransitionColors(transitionColors.red + n4, transitionColors.green + n5, transitionColors.blue + n6);
        TransitionColors transitionColors4 = new TransitionColors(transitionColors.red + 2 * n4, transitionColors.green + 2 * n5, transitionColors.blue + 2 * n6);
        TransitionColors transitionColors5 = new TransitionColors(transitionColors.red + 3 * n4, transitionColors.green + 3 * n5, transitionColors.blue + 3 * n6);
        return new String[]{transitionColors3.toHex(), transitionColors4.toHex(), transitionColors5.toHex()};
    }
}

