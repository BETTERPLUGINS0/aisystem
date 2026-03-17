/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.wrapper;

import java.util.function.UnaryOperator;

public enum Casing {
    camelCase(string -> {
        if (string.length() < 2) {
            return string.toLowerCase();
        }
        return Character.toLowerCase(string.charAt(0)) + string.substring(1);
    }),
    snake_case(string -> {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Character.toLowerCase(string.charAt(0)));
        for (int i = 1; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (Character.isUpperCase(c)) {
                stringBuilder.append('_').append(Character.toLowerCase(c));
                continue;
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }),
    PascalCase(string -> {
        if (string.length() < 2) {
            return string.toUpperCase();
        }
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }),
    lowercase(String::toLowerCase),
    UPPERCASE(String::toUpperCase);

    private UnaryOperator<String> convert;

    private Casing(UnaryOperator<String> unaryOperator) {
        this.convert = unaryOperator;
    }

    public String convertString(String string) {
        return (String)this.convert.apply(string);
    }
}

