/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.hjson;

import org.hjson.JsonValue;

public interface IHjsonDsfProvider {
    public String getName();

    public String getDescription();

    public JsonValue parse(String var1);

    public String stringify(JsonValue var1);
}

