/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.hjson;

import org.hjson.IHjsonDsfProvider;
import org.hjson.JsonNumber;
import org.hjson.JsonValue;

class DsfMath
implements IHjsonDsfProvider {
    DsfMath() {
    }

    @Override
    public String getName() {
        return "math";
    }

    @Override
    public String getDescription() {
        return "support for Inf/inf, -Inf/-inf, Nan/naN and -0";
    }

    @Override
    public JsonValue parse(String string) {
        switch (string) {
            case "+inf": 
            case "inf": 
            case "+Inf": 
            case "Inf": {
                return new JsonNumber(Double.POSITIVE_INFINITY);
            }
            case "-inf": 
            case "-Inf": {
                return new JsonNumber(Double.NEGATIVE_INFINITY);
            }
            case "nan": 
            case "NaN": {
                return new JsonNumber(Double.NaN);
            }
        }
        return null;
    }

    @Override
    public String stringify(JsonValue jsonValue) {
        if (!jsonValue.isNumber()) {
            return null;
        }
        double d = jsonValue.asDouble();
        if (d == Double.POSITIVE_INFINITY) {
            return "Inf";
        }
        if (d == Double.NEGATIVE_INFINITY) {
            return "-Inf";
        }
        if (Double.isNaN(d)) {
            return "NaN";
        }
        if (d == 0.0 && 1.0 / d == Double.NEGATIVE_INFINITY) {
            return "-0";
        }
        return null;
    }
}

