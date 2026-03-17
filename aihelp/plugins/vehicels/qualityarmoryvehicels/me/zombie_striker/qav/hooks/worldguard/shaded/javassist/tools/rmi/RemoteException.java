/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.rmi;

public class RemoteException
extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RemoteException(String string) {
        super(string);
    }

    public RemoteException(Exception exception) {
        super("by " + exception.toString());
    }
}

