/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.DataInputStream;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MemberrefInfo;

class InterfaceMethodrefInfo
extends MemberrefInfo {
    static final int tag = 11;

    public InterfaceMethodrefInfo(int n, int n2, int n3) {
        super(n, n2, n3);
    }

    public InterfaceMethodrefInfo(DataInputStream dataInputStream, int n) {
        super(dataInputStream, n);
    }

    @Override
    public int getTag() {
        return 11;
    }

    @Override
    public String getTagName() {
        return "Interface";
    }

    @Override
    protected int copy2(ConstPool constPool, int n, int n2) {
        return constPool.addInterfaceMethodrefInfo(n, n2);
    }
}

