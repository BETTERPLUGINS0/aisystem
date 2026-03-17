/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.metrics.json;

import java.util.Arrays;
import java.util.stream.Collectors;

public class JsonObjectBuilder {
    private StringBuilder builder = new StringBuilder();
    private boolean hasAtLeastOneField = false;

    public JsonObjectBuilder() {
        this.builder.append("{");
    }

    public JsonObjectBuilder appendNull(String string) {
        this.appendFieldUnescaped(string, "null");
        return this;
    }

    public JsonObjectBuilder appendField(String string, String string2) {
        if (string2 == null) {
            throw new IllegalArgumentException("JSON value must not be null");
        }
        this.appendFieldUnescaped(string, "\"" + JsonObjectBuilder.escape(string2) + "\"");
        return this;
    }

    public JsonObjectBuilder appendField(String string, int n) {
        this.appendFieldUnescaped(string, String.valueOf(n));
        return this;
    }

    public JsonObjectBuilder appendField(String string, JsonObject jsonObject) {
        if (jsonObject == null) {
            throw new IllegalArgumentException("JSON object must not be null");
        }
        this.appendFieldUnescaped(string, jsonObject.toString());
        return this;
    }

    public JsonObjectBuilder appendField(String string2, String[] stringArray) {
        if (stringArray == null) {
            throw new IllegalArgumentException("JSON values must not be null");
        }
        String string3 = Arrays.stream(stringArray).map(string -> "\"" + JsonObjectBuilder.escape(string) + "\"").collect(Collectors.joining(","));
        this.appendFieldUnescaped(string2, "[" + string3 + "]");
        return this;
    }

    public JsonObjectBuilder appendField(String string, int[] nArray) {
        if (nArray == null) {
            throw new IllegalArgumentException("JSON values must not be null");
        }
        String string2 = Arrays.stream(nArray).mapToObj(String::valueOf).collect(Collectors.joining(","));
        this.appendFieldUnescaped(string, "[" + string2 + "]");
        return this;
    }

    public JsonObjectBuilder appendField(String string, JsonObject[] jsonObjectArray) {
        if (jsonObjectArray == null) {
            throw new IllegalArgumentException("JSON values must not be null");
        }
        String string2 = Arrays.stream(jsonObjectArray).map(JsonObject::toString).collect(Collectors.joining(","));
        this.appendFieldUnescaped(string, "[" + string2 + "]");
        return this;
    }

    private void appendFieldUnescaped(String string, String string2) {
        if (this.builder == null) {
            throw new IllegalStateException("JSON has already been built");
        }
        if (string == null) {
            throw new IllegalArgumentException("JSON key must not be null");
        }
        if (this.hasAtLeastOneField) {
            this.builder.append(",");
        }
        this.builder.append("\"").append(JsonObjectBuilder.escape(string)).append("\":").append(string2);
        this.hasAtLeastOneField = true;
    }

    public JsonObject build() {
        if (this.builder == null) {
            throw new IllegalStateException("JSON has already been built");
        }
        JsonObject jsonObject = new JsonObject(this.builder.append("}").toString());
        this.builder = null;
        return jsonObject;
    }

    private static String escape(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (c == '\"') {
                stringBuilder.append("\\\"");
                continue;
            }
            if (c == '\\') {
                stringBuilder.append("\\\\");
                continue;
            }
            if (c <= '\u000f') {
                stringBuilder.append("\\u000").append(Integer.toHexString(c));
                continue;
            }
            if (c <= '\u001f') {
                stringBuilder.append("\\u00").append(Integer.toHexString(c));
                continue;
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public static class JsonObject {
        private final String value;

        private JsonObject(String string) {
            this.value = string;
        }

        public String toString() {
            return this.value;
        }
    }
}

