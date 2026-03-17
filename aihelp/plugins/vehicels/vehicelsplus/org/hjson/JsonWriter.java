/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.hjson;

import java.io.Writer;
import java.util.regex.Matcher;
import org.hjson.HjsonWriter;
import org.hjson.JsonArray;
import org.hjson.JsonObject;
import org.hjson.JsonValue;

class JsonWriter {
    boolean format;

    public JsonWriter(boolean bl) {
        this.format = bl;
    }

    void nl(Writer writer, int n) {
        if (this.format) {
            writer.write(JsonValue.eol);
            for (int i = 0; i < n; ++i) {
                writer.write("  ");
            }
        }
    }

    public void save(JsonValue jsonValue, Writer writer, int n) {
        boolean bl = false;
        switch (jsonValue.getType()) {
            case OBJECT: {
                JsonObject jsonObject = jsonValue.asObject();
                writer.write(123);
                for (JsonObject.Member member : jsonObject) {
                    if (bl) {
                        writer.write(",");
                    }
                    this.nl(writer, n + 1);
                    writer.write(34);
                    writer.write(JsonWriter.escapeString(member.getName()));
                    writer.write("\":");
                    JsonValue jsonValue2 = member.getValue();
                    if (this.format) {
                        writer.write(" ");
                    }
                    if (jsonValue2 == null) {
                        writer.write("null");
                    } else {
                        this.save(jsonValue2, writer, n + 1);
                    }
                    bl = true;
                }
                if (bl) {
                    this.nl(writer, n);
                }
                writer.write(125);
                break;
            }
            case ARRAY: {
                JsonArray jsonArray = jsonValue.asArray();
                int n2 = jsonArray.size();
                writer.write(91);
                for (int i = 0; i < n2; ++i) {
                    if (i > 0) {
                        writer.write(",");
                    }
                    JsonValue jsonValue3 = jsonArray.get(i);
                    this.nl(writer, n + 1);
                    this.save(jsonArray.get(i), writer, n + 1);
                }
                if (n2 > 0) {
                    this.nl(writer, n);
                }
                writer.write(93);
                break;
            }
            case BOOLEAN: {
                writer.write(jsonValue.isTrue() ? "true" : "false");
                break;
            }
            case STRING: {
                writer.write(34);
                writer.write(JsonWriter.escapeString(jsonValue.asString()));
                writer.write(34);
                break;
            }
            default: {
                writer.write(jsonValue.toString());
            }
        }
    }

    static String escapeString(String string) {
        if (string == null) {
            return null;
        }
        int n = 0;
        StringBuilder stringBuilder = new StringBuilder();
        Matcher matcher = HjsonWriter.needsEscape.matcher(string);
        while (matcher.find()) {
            stringBuilder.append(string, n, matcher.start()).append(JsonWriter.getEscapedChar(matcher.group().charAt(0)));
            n = matcher.end();
        }
        if (n < 1) {
            return string;
        }
        stringBuilder.append(string, n, string.length());
        return stringBuilder.toString();
    }

    private static String getEscapedChar(char c) {
        switch (c) {
            case '\"': {
                return "\\\"";
            }
            case '\t': {
                return "\\t";
            }
            case '\n': {
                return "\\n";
            }
            case '\r': {
                return "\\r";
            }
            case '\f': {
                return "\\f";
            }
            case '\b': {
                return "\\b";
            }
            case '\\': {
                return "\\\\";
            }
        }
        return "\\u" + String.format("%04x", c);
    }
}

