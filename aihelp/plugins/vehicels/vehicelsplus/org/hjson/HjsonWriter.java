/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.hjson;

import java.io.Writer;
import java.util.regex.Pattern;
import org.hjson.HjsonDsf;
import org.hjson.HjsonOptions;
import org.hjson.HjsonParser;
import org.hjson.IHjsonDsfProvider;
import org.hjson.JsonArray;
import org.hjson.JsonObject;
import org.hjson.JsonValue;
import org.hjson.JsonWriter;

class HjsonWriter {
    private IHjsonDsfProvider[] dsfProviders;
    static String commonRange = "\\x7f-\\x9f\\x{00ad}\\x{0600}-\\x{0604}\\x{070f}\\x{17b4}\\x{17b5}\\x{200c}-\\x{200f}\\x{2028}-\\x{202f}\\x{2060}-\\x{206f}\\x{feff}\\x{fff0}-\\x{ffff}";
    static Pattern needsEscape = Pattern.compile("[\\\\\\\"\\x00-\\x1f" + commonRange + "]");
    static Pattern needsQuotes = Pattern.compile("^\\s|^\"|^'|^#|^/\\*|^//|^\\{|^\\}|^\\[|^\\]|^:|^,|\\s$|[\\x00-\\x1f\\x7f-\\x9f\\x{00ad}\\x{0600}-\\x{0604}\\x{070f}\\x{17b4}\\x{17b5}\\x{200c}-\\x{200f}\\x{2028}-\\x{202f}\\x{2060}-\\x{206f}\\x{feff}\\x{fff0}-\\x{ffff}]");
    static Pattern needsEscapeML = Pattern.compile("'''|^[\\s]+$|[\\x00-\\x08\\x0b-\\x1f" + commonRange + "]");
    static Pattern needsEscapeName = Pattern.compile("[,\\{\\[\\}\\]\\s:#\"']|//|/\\*");

    public HjsonWriter(HjsonOptions hjsonOptions) {
        this.dsfProviders = hjsonOptions != null ? hjsonOptions.getDsfProviders() : new IHjsonDsfProvider[0];
    }

    void nl(Writer writer, int n) {
        writer.write(JsonValue.eol);
        for (int i = 0; i < n; ++i) {
            writer.write("  ");
        }
    }

    public void save(JsonValue jsonValue, Writer writer, int n, String string, boolean bl) {
        if (jsonValue == null) {
            writer.write(string);
            writer.write("null");
            return;
        }
        String string2 = HjsonDsf.stringify(this.dsfProviders, jsonValue);
        if (string2 != null) {
            writer.write(string);
            writer.write(string2);
            return;
        }
        switch (jsonValue.getType()) {
            case OBJECT: {
                JsonObject jsonObject = jsonValue.asObject();
                if (!bl) {
                    if (jsonObject.size() > 0) {
                        this.nl(writer, n);
                    } else {
                        writer.write(string);
                    }
                }
                writer.write(123);
                for (JsonObject.Member member : jsonObject) {
                    this.nl(writer, n + 1);
                    writer.write(HjsonWriter.escapeName(member.getName()));
                    writer.write(":");
                    this.save(member.getValue(), writer, n + 1, " ", false);
                }
                if (jsonObject.size() > 0) {
                    this.nl(writer, n);
                }
                writer.write(125);
                break;
            }
            case ARRAY: {
                JsonArray jsonArray = jsonValue.asArray();
                int n2 = jsonArray.size();
                if (!bl) {
                    if (n2 > 0) {
                        this.nl(writer, n);
                    } else {
                        writer.write(string);
                    }
                }
                writer.write(91);
                for (int i = 0; i < n2; ++i) {
                    this.nl(writer, n + 1);
                    this.save(jsonArray.get(i), writer, n + 1, "", true);
                }
                if (n2 > 0) {
                    this.nl(writer, n);
                }
                writer.write(93);
                break;
            }
            case BOOLEAN: {
                writer.write(string);
                writer.write(jsonValue.isTrue() ? "true" : "false");
                break;
            }
            case STRING: {
                this.writeString(jsonValue.asString(), writer, n, string);
                break;
            }
            default: {
                writer.write(string);
                writer.write(jsonValue.toString());
            }
        }
    }

    static String escapeName(String string) {
        if (string.length() == 0 || needsEscapeName.matcher(string).find() || needsEscape.matcher(string).find()) {
            return "\"" + JsonWriter.escapeString(string) + "\"";
        }
        return string;
    }

    void writeString(String string, Writer writer, int n, String string2) {
        if (string.length() == 0) {
            writer.write(string2 + "\"\"");
            return;
        }
        char c = string.charAt(0);
        char c2 = string.charAt(string.length() - 1);
        char c3 = string.length() > 1 ? string.charAt(1) : (char)'\u0000';
        char c4 = string.length() > 2 ? string.charAt(2) : (char)'\u0000';
        boolean bl = needsQuotes.matcher(string).find();
        if (bl || HjsonParser.isWhiteSpace(c) || HjsonParser.isWhiteSpace(c2) || c == '\"' || c == '\'' || c == '#' || c == '/' && (c3 == '*' || c3 == '/') || JsonValue.isPunctuatorChar(c) || HjsonParser.tryParseNumber(string, true) != null || HjsonWriter.startsWithKeyword(string)) {
            if (!needsEscape.matcher(string).find()) {
                writer.write(string2 + "\"" + string + "\"");
            } else if (!needsEscapeML.matcher(string).find()) {
                this.writeMLString(string, writer, n, string2);
            } else {
                writer.write(string2 + "\"" + JsonWriter.escapeString(string) + "\"");
            }
        } else {
            writer.write(string2 + string);
        }
    }

    void writeMLString(String string, Writer writer, int n, String string2) {
        String[] stringArray = string.replace("\r", "").split("\n", -1);
        if (stringArray.length == 1) {
            writer.write(string2 + "'''");
            writer.write(stringArray[0]);
            writer.write("'''");
        } else {
            this.nl(writer, ++n);
            writer.write("'''");
            for (String string3 : stringArray) {
                this.nl(writer, string3.length() > 0 ? n : 0);
                writer.write(string3);
            }
            this.nl(writer, n);
            writer.write("'''");
        }
    }

    static boolean startsWithKeyword(String string) {
        int n;
        if (string.startsWith("true") || string.startsWith("null")) {
            n = 4;
        } else if (string.startsWith("false")) {
            n = 5;
        } else {
            return false;
        }
        while (n < string.length() && HjsonParser.isWhiteSpace(string.charAt(n))) {
            ++n;
        }
        if (n == string.length()) {
            return true;
        }
        char c = string.charAt(n);
        return c == ',' || c == '}' || c == ']' || c == '#' || c == '/' && string.length() > n + 1 && (string.charAt(n + 1) == '/' || string.charAt(n + 1) == '*');
    }
}

