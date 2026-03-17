/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.DataInputStream;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;

public class DeprecatedAttribute
extends AttributeInfo {
    public static final String tag = "Deprecated";

    DeprecatedAttribute(ConstPool constPool, int n, DataInputStream dataInputStream) {
        super(constPool, n, dataInputStream);
    }

    public DeprecatedAttribute(ConstPool constPool) {
        super(constPool, tag, new byte[0]);
    }

    @Override
    public AttributeInfo copy(ConstPool constPool, Map<String, String> map) {
        return new DeprecatedAttribute(constPool);
    }
}

