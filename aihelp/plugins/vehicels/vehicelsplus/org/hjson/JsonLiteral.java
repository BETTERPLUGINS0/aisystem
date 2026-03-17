/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.hjson;

import org.hjson.JsonType;
import org.hjson.JsonValue;

class JsonLiteral
extends JsonValue {
    static final JsonValue NULL = new JsonLiteral(Iv.N);
    static final JsonValue TRUE = new JsonLiteral(Iv.T);
    static final JsonValue FALSE = new JsonLiteral(Iv.F);
    private final Iv value;

    private JsonLiteral(Iv iv) {
        this.value = iv;
    }

    @Override
    public String toString() {
        switch (this.value) {
            case T: {
                return "true";
            }
            case F: {
                return "false";
            }
            case N: {
                return "null";
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public JsonType getType() {
        return this.value == Iv.N ? JsonType.NULL : JsonType.BOOLEAN;
    }

    @Override
    public boolean isNull() {
        return this.value == Iv.N;
    }

    @Override
    public boolean isTrue() {
        return this.value == Iv.T;
    }

    @Override
    public boolean isFalse() {
        return this.value == Iv.F;
    }

    @Override
    public boolean isBoolean() {
        return this.value != Iv.N;
    }

    @Override
    public boolean asBoolean() {
        return this.value == Iv.N ? super.asBoolean() : this.value == Iv.T;
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
        JsonLiteral jsonLiteral = (JsonLiteral)object;
        return this.value == jsonLiteral.value;
    }

    static enum Iv {
        T,
        F,
        N;

    }
}

