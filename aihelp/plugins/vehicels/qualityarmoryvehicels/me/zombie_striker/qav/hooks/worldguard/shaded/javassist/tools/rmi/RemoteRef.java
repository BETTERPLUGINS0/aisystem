/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.rmi;

import java.io.Serializable;

public class RemoteRef
implements Serializable {
    private static final long serialVersionUID = 1L;
    public int oid;
    public String classname;

    public RemoteRef(int n) {
        this.oid = n;
        this.classname = null;
    }

    public RemoteRef(int n, String string) {
        this.oid = n;
        this.classname = string;
    }
}

