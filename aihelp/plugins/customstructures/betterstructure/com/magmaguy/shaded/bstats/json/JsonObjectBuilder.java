/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.shaded.bstats.json;

import java.util.Arrays;
import java.util.stream.Collectors;

public class JsonObjectBuilder {
    private StringBuilder builder = new StringBuilder();
    private boolean hasAtLeastOneField = false;

    public JsonObjectBuilder() {
        this.builder.append("{");
    }

    public JsonObjectBuilder appendNull(String key) {
        this.appendFieldUnescaped(key, "null");
        return this;
    }

    public JsonObjectBuilder appendField(String key, String value) {
        if (value == null) {
            throw new IllegalArgumentException("JSON value must not be null");
        }
        this.appendFieldUnescaped(key, "\"" + JsonObjectBuilder.escape(value) + "\"");
        return this;
    }

    public JsonObjectBuilder appendField(String key, int value) {
        this.appendFieldUnescaped(key, String.valueOf(value));
        return this;
    }

    public JsonObjectBuilder appendField(String key, JsonObject object) {
        if (object == null) {
            throw new IllegalArgumentException("JSON object must not be null");
        }
        this.appendFieldUnescaped(key, object.toString());
        return this;
    }

    public JsonObjectBuilder appendField(String key, String[] values2) {
        if (values2 == null) {
            throw new IllegalArgumentException("JSON values must not be null");
        }
        String escapedValues = Arrays.stream(values2).map(value -> "\"" + JsonObjectBuilder.escape(value) + "\"").collect(Collectors.joining(","));
        this.appendFieldUnescaped(key, "[" + escapedValues + "]");
        return this;
    }

    public JsonObjectBuilder appendField(String key, int[] values2) {
        if (values2 == null) {
            throw new IllegalArgumentException("JSON values must not be null");
        }
        String escapedValues = Arrays.stream(values2).mapToObj(String::valueOf).collect(Collectors.joining(","));
        this.appendFieldUnescaped(key, "[" + escapedValues + "]");
        return this;
    }

    public JsonObjectBuilder appendField(String key, JsonObject[] values2) {
        if (values2 == null) {
            throw new IllegalArgumentException("JSON values must not be null");
        }
        String escapedValues = Arrays.stream(values2).map(JsonObject::toString).collect(Collectors.joining(","));
        this.appendFieldUnescaped(key, "[" + escapedValues + "]");
        return this;
    }

    private void appendFieldUnescaped(String key, String escapedValue) {
        if (this.builder == null) {
            throw new IllegalStateException("JSON has already been built");
        }
        if (key == null) {
            throw new IllegalArgumentException("JSON key must not be null");
        }
        if (this.hasAtLeastOneField) {
            this.builder.append(",");
        }
        this.builder.append("\"").append(JsonObjectBuilder.escape(key)).append("\":").append(escapedValue);
        this.hasAtLeastOneField = true;
    }

    public JsonObject build() {
        if (this.builder == null) {
            throw new IllegalStateException("JSON has already been built");
        }
        JsonObject object = new JsonObject(this.builder.append("}").toString());
        this.builder = null;
        return object;
    }

    private static String escape(String value) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.length(); ++i) {
            char c2 = value.charAt(i);
            if (c2 == '\"') {
                builder.append("\\\"");
                continue;
            }
            if (c2 == '\\') {
                builder.append("\\\\");
                continue;
            }
            if (c2 <= '\u000f') {
                builder.append("\\u000").append(Integer.toHexString(c2));
                continue;
            }
            if (c2 <= '\u001f') {
                builder.append("\\u00").append(Integer.toHexString(c2));
                continue;
            }
            builder.append(c2);
        }
        return builder.toString();
    }

    public static class JsonObject {
        private final String value;

        private JsonObject(String value) {
            this.value = value;
        }

        public String toString() {
            return this.value;
        }
    }
}

