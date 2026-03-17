/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.hjson;

import java.math.BigDecimal;
import org.hjson.JsonType;
import org.hjson.JsonValue;

class JsonNumber
extends JsonValue {
    private final double value;

    JsonNumber(double d) {
        this.value = d;
    }

    @Override
    public String toString() {
        long l = (long)this.value;
        if ((double)l == this.value) {
            return Long.toString(l);
        }
        String string = BigDecimal.valueOf(this.value).toEngineeringString();
        if (string.endsWith(".0")) {
            return string.substring(0, string.length() - 2);
        }
        if (string.contains("E")) {
            string = Double.toString(this.value);
            string = string.replace("E-", "e-").replace("E", "e+");
        }
        return string;
    }

    @Override
    public JsonType getType() {
        return JsonType.NUMBER;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public int asInt() {
        return (int)this.value;
    }

    @Override
    public long asLong() {
        return (long)this.value;
    }

    @Override
    public float asFloat() {
        return (float)this.value;
    }

    @Override
    public double asDouble() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return Double.valueOf(this.value).hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        JsonNumber jsonNumber = (JsonNumber)object;
        return this.value == jsonNumber.value;
    }
}

