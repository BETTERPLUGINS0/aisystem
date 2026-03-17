/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler;

import java.util.HashMap;

public final class KeywordTable
extends HashMap<String, Integer> {
    private static final long serialVersionUID = 1L;

    public int lookup(String string) {
        return this.containsKey(string) ? (Integer)this.get(string) : -1;
    }

    public void append(String string, int n) {
        this.put(string, n);
    }
}

