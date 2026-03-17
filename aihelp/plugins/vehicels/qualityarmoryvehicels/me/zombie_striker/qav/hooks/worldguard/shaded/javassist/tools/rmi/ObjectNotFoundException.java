/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.rmi;

public class ObjectNotFoundException
extends Exception {
    private static final long serialVersionUID = 1L;

    public ObjectNotFoundException(String string) {
        super(string + " is not exported");
    }

    public ObjectNotFoundException(String string, Exception exception) {
        super(string + " because of " + exception.toString());
    }
}

