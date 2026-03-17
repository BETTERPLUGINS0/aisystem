/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.hjson;

import java.util.regex.Pattern;
import org.hjson.IHjsonDsfProvider;
import org.hjson.JsonNumber;
import org.hjson.JsonValue;

class DsfHex
implements IHjsonDsfProvider {
    boolean stringify;
    static Pattern isHex = Pattern.compile("^0x[0-9A-Fa-f]+$");

    public DsfHex(boolean bl) {
        this.stringify = bl;
    }

    @Override
    public String getName() {
        return "hex";
    }

    @Override
    public String getDescription() {
        return "parse hexadecimal numbers prefixed with 0x";
    }

    @Override
    public JsonValue parse(String string) {
        if (isHex.matcher(string).find()) {
            return new JsonNumber(Long.parseLong(string.substring(2), 16));
        }
        return null;
    }

    @Override
    public String stringify(JsonValue jsonValue) {
        if (this.stringify && jsonValue.isNumber() && (double)jsonValue.asLong() == jsonValue.asDouble()) {
            return "0x" + Long.toHexString(jsonValue.asLong());
        }
        return null;
    }
}

