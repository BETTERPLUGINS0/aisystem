/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.hjson;

import org.hjson.JsonType;
import org.hjson.JsonValue;

class JsonDsf
extends JsonValue {
    private final Object value;

    JsonDsf(Object object) {
        this.value = object;
    }

    @Override
    public String toString() {
        return "null";
    }

    @Override
    public JsonType getType() {
        return JsonType.DSF;
    }

    @Override
    public Object asDsf() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }
}

