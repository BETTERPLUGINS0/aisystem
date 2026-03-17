/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.rmi;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.rmi.ObjectImporter;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.rmi.RemoteException;

public class Sample {
    private ObjectImporter importer;
    private int objectId;

    public Object forward(Object[] objectArray, int n) {
        return this.importer.call(this.objectId, n, objectArray);
    }

    public static Object forwardStatic(Object[] objectArray, int n) {
        throw new RemoteException("cannot call a static method.");
    }
}

