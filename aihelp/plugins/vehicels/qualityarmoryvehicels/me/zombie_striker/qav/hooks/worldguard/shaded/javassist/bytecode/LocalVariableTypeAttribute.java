/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.DataInputStream;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.LocalVariableAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.SignatureAttribute;

public class LocalVariableTypeAttribute
extends LocalVariableAttribute {
    public static final String tag = "LocalVariableTypeTable";

    public LocalVariableTypeAttribute(ConstPool constPool) {
        super(constPool, tag, new byte[2]);
        ByteArray.write16bit(0, this.info, 0);
    }

    LocalVariableTypeAttribute(ConstPool constPool, int n, DataInputStream dataInputStream) {
        super(constPool, n, dataInputStream);
    }

    private LocalVariableTypeAttribute(ConstPool constPool, byte[] byArray) {
        super(constPool, tag, byArray);
    }

    @Override
    String renameEntry(String string, String string2, String string3) {
        return SignatureAttribute.renameClass(string, string2, string3);
    }

    @Override
    String renameEntry(String string, Map<String, String> map) {
        return SignatureAttribute.renameClass(string, map);
    }

    @Override
    LocalVariableAttribute makeThisAttr(ConstPool constPool, byte[] byArray) {
        return new LocalVariableTypeAttribute(constPool, byArray);
    }
}

