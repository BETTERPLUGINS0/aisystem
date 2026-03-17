/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

public class NotFoundException
extends Exception {
    private static final long serialVersionUID = 1L;

    public NotFoundException(String string) {
        super(string);
    }

    public NotFoundException(String string, Exception exception) {
        super(string + " because of " + exception.toString());
    }
}

