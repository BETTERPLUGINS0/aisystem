/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.libs.sidebar;

import java.util.Objects;
import java.util.concurrent.Callable;

public class PlaceholderProvider {
    private final String placeholder;
    private final Callable<String> replacement;

    public PlaceholderProvider(String placeholder, Callable<String> replacement) {
        this.placeholder = placeholder;
        this.replacement = replacement;
    }

    public String getPlaceholder() {
        return this.placeholder;
    }

    public String getReplacement() {
        try {
            String rep = this.replacement.call();
            return null == rep ? "null" : rep;
        } catch (Exception e) {
            return "-";
        }
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof PlaceholderProvider)) {
            return false;
        }
        PlaceholderProvider that = (PlaceholderProvider)o;
        return that.placeholder.equalsIgnoreCase(this.placeholder);
    }

    public int hashCode() {
        return Objects.hash(this.placeholder, this.replacement);
    }
}

