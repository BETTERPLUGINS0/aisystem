/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.google.gson;

import com.google.gson.JsonElement;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;

public final class JsonPrimitive
extends JsonElement {
    private static final Class<?>[] PRIMITIVE_TYPES = new Class[]{Integer.TYPE, Long.TYPE, Short.TYPE, Float.TYPE, Double.TYPE, Byte.TYPE, Boolean.TYPE, Character.TYPE, Integer.class, Long.class, Short.class, Float.class, Double.class, Byte.class, Boolean.class, Character.class};
    private Object value;

    public JsonPrimitive(Boolean bl) {
        this.setValue(bl);
    }

    public JsonPrimitive(Number number) {
        this.setValue(number);
    }

    public JsonPrimitive(String string) {
        this.setValue(string);
    }

    public JsonPrimitive(Character c) {
        this.setValue(c);
    }

    JsonPrimitive(Object object) {
        this.setValue(object);
    }

    @Override
    JsonPrimitive deepCopy() {
        return this;
    }

    void setValue(Object object) {
        if (object instanceof Character) {
            char c = ((Character)object).charValue();
            this.value = String.valueOf(c);
        } else {
            $Gson$Preconditions.checkArgument(object instanceof Number || JsonPrimitive.isPrimitiveOrString(object));
            this.value = object;
        }
    }

    public boolean isBoolean() {
        return this.value instanceof Boolean;
    }

    @Override
    Boolean getAsBooleanWrapper() {
        return (Boolean)this.value;
    }

    @Override
    public boolean getAsBoolean() {
        if (this.isBoolean()) {
            return this.getAsBooleanWrapper();
        }
        return Boolean.parseBoolean(this.getAsString());
    }

    public boolean isNumber() {
        return this.value instanceof Number;
    }

    @Override
    public Number getAsNumber() {
        return this.value instanceof String ? new LazilyParsedNumber((String)this.value) : (Number)this.value;
    }

    public boolean isString() {
        return this.value instanceof String;
    }

    @Override
    public String getAsString() {
        if (this.isNumber()) {
            return this.getAsNumber().toString();
        }
        if (this.isBoolean()) {
            return this.getAsBooleanWrapper().toString();
        }
        return (String)this.value;
    }

    @Override
    public double getAsDouble() {
        return this.isNumber() ? this.getAsNumber().doubleValue() : Double.parseDouble(this.getAsString());
    }

    @Override
    public BigDecimal getAsBigDecimal() {
        return this.value instanceof BigDecimal ? (BigDecimal)this.value : new BigDecimal(this.value.toString());
    }

    @Override
    public BigInteger getAsBigInteger() {
        return this.value instanceof BigInteger ? (BigInteger)this.value : new BigInteger(this.value.toString());
    }

    @Override
    public float getAsFloat() {
        return this.isNumber() ? this.getAsNumber().floatValue() : Float.parseFloat(this.getAsString());
    }

    @Override
    public long getAsLong() {
        return this.isNumber() ? this.getAsNumber().longValue() : Long.parseLong(this.getAsString());
    }

    @Override
    public short getAsShort() {
        return this.isNumber() ? this.getAsNumber().shortValue() : Short.parseShort(this.getAsString());
    }

    @Override
    public int getAsInt() {
        return this.isNumber() ? this.getAsNumber().intValue() : Integer.parseInt(this.getAsString());
    }

    @Override
    public byte getAsByte() {
        return this.isNumber() ? this.getAsNumber().byteValue() : Byte.parseByte(this.getAsString());
    }

    @Override
    public char getAsCharacter() {
        return this.getAsString().charAt(0);
    }

    private static boolean isPrimitiveOrString(Object object) {
        if (object instanceof String) {
            return true;
        }
        Class<?> clazz = object.getClass();
        for (Class<?> clazz2 : PRIMITIVE_TYPES) {
            if (!clazz2.isAssignableFrom(clazz)) continue;
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.value == null) {
            return 31;
        }
        if (JsonPrimitive.isIntegral(this)) {
            long l = this.getAsNumber().longValue();
            return (int)(l ^ l >>> 32);
        }
        if (this.value instanceof Number) {
            long l = Double.doubleToLongBits(this.getAsNumber().doubleValue());
            return (int)(l ^ l >>> 32);
        }
        return this.value.hashCode();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        JsonPrimitive jsonPrimitive = (JsonPrimitive)object;
        if (this.value == null) {
            return jsonPrimitive.value == null;
        }
        if (JsonPrimitive.isIntegral(this) && JsonPrimitive.isIntegral(jsonPrimitive)) {
            return this.getAsNumber().longValue() == jsonPrimitive.getAsNumber().longValue();
        }
        if (this.value instanceof Number && jsonPrimitive.value instanceof Number) {
            double d;
            double d2 = this.getAsNumber().doubleValue();
            return d2 == (d = jsonPrimitive.getAsNumber().doubleValue()) || Double.isNaN(d2) && Double.isNaN(d);
        }
        return this.value.equals(jsonPrimitive.value);
    }

    private static boolean isIntegral(JsonPrimitive jsonPrimitive) {
        if (jsonPrimitive.value instanceof Number) {
            Number number = (Number)jsonPrimitive.value;
            return number instanceof BigInteger || number instanceof Long || number instanceof Integer || number instanceof Short || number instanceof Byte;
        }
        return false;
    }
}

