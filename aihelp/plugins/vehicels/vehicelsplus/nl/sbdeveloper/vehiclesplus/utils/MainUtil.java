/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.utils;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import lombok.Generated;

public final class MainUtil {
    private static final NumberFormat formatter = NumberFormat.getCurrencyInstance();

    public static String ___(double d) {
        return formatter.format(d).replaceAll("\u00a0", "");
    }

    public static String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static String formatDouble(Double d, Integer n) {
        return new DecimalFormat("#." + "0".repeat(n.intValue())).format(d);
    }

    public static String generateRandomString(int n) {
        StringBuilder stringBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < n; ++i) {
            int n2 = (int)(secureRandom.nextDouble() * 36.0);
            stringBuilder.append((char)(n2 < 26 ? n2 + 97 : n2 - 26 + 48));
        }
        return stringBuilder.toString();
    }

    @Generated
    private MainUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

