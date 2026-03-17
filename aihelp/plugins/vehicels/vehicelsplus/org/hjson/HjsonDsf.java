/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.hjson;

import org.hjson.DsfHex;
import org.hjson.DsfMath;
import org.hjson.IHjsonDsfProvider;
import org.hjson.JsonString;
import org.hjson.JsonValue;

public class HjsonDsf {
    private HjsonDsf() {
    }

    public static IHjsonDsfProvider math() {
        return new DsfMath();
    }

    public static IHjsonDsfProvider hex(boolean bl) {
        return new DsfHex(bl);
    }

    static boolean isInvalidDsfChar(char c) {
        return c == '{' || c == '}' || c == '[' || c == ']' || c == ',';
    }

    static JsonValue parse(IHjsonDsfProvider[] iHjsonDsfProviderArray, String string) {
        for (IHjsonDsfProvider iHjsonDsfProvider : iHjsonDsfProviderArray) {
            try {
                JsonValue jsonValue = iHjsonDsfProvider.parse(string);
                if (jsonValue == null) continue;
                return jsonValue;
            } catch (Exception exception) {
                throw new RuntimeException("DSF-" + iHjsonDsfProvider.getName() + " failed; " + exception.getMessage());
            }
        }
        return new JsonString(string);
    }

    static String stringify(IHjsonDsfProvider[] iHjsonDsfProviderArray, JsonValue jsonValue) {
        for (IHjsonDsfProvider iHjsonDsfProvider : iHjsonDsfProviderArray) {
            try {
                char[] cArray;
                String string = iHjsonDsfProvider.stringify(jsonValue);
                if (string == null) continue;
                boolean bl = false;
                for (char c : cArray = string.toCharArray()) {
                    if (!HjsonDsf.isInvalidDsfChar(c)) continue;
                    bl = true;
                    break;
                }
                if (bl || string.length() == 0 || cArray[0] == '\"') {
                    throw new Exception("value may not be empty, start with a quote or contain a punctuator character except colon: " + string);
                }
                return string;
            } catch (Exception exception) {
                throw new RuntimeException("DSF-" + iHjsonDsfProvider.getName() + " failed; " + exception.getMessage());
            }
        }
        return null;
    }
}

