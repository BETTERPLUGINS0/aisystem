/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.libs.slf4j.event;

public class KeyValuePair {
    public final String key;
    public final Object value;

    public KeyValuePair(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String toString() {
        return String.valueOf(this.key) + "=\"" + String.valueOf(this.value) + "\"";
    }
}

