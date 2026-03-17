/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.DataInputStream;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;

public class BootstrapMethodsAttribute
extends AttributeInfo {
    public static final String tag = "BootstrapMethods";

    BootstrapMethodsAttribute(ConstPool constPool, int n, DataInputStream dataInputStream) {
        super(constPool, n, dataInputStream);
    }

    public BootstrapMethodsAttribute(ConstPool constPool, BootstrapMethod[] bootstrapMethodArray) {
        super(constPool, tag);
        int n = 2;
        for (int i = 0; i < bootstrapMethodArray.length; ++i) {
            n += 4 + bootstrapMethodArray[i].arguments.length * 2;
        }
        byte[] byArray = new byte[n];
        ByteArray.write16bit(bootstrapMethodArray.length, byArray, 0);
        int n2 = 2;
        for (int i = 0; i < bootstrapMethodArray.length; ++i) {
            ByteArray.write16bit(bootstrapMethodArray[i].methodRef, byArray, n2);
            ByteArray.write16bit(bootstrapMethodArray[i].arguments.length, byArray, n2 + 2);
            int[] nArray = bootstrapMethodArray[i].arguments;
            n2 += 4;
            for (int j = 0; j < nArray.length; ++j) {
                ByteArray.write16bit(nArray[j], byArray, n2);
                n2 += 2;
            }
        }
        this.set(byArray);
    }

    public BootstrapMethod[] getMethods() {
        byte[] byArray = this.get();
        int n = ByteArray.readU16bit(byArray, 0);
        BootstrapMethod[] bootstrapMethodArray = new BootstrapMethod[n];
        int n2 = 2;
        for (int i = 0; i < n; ++i) {
            int n3 = ByteArray.readU16bit(byArray, n2);
            int n4 = ByteArray.readU16bit(byArray, n2 + 2);
            int[] nArray = new int[n4];
            n2 += 4;
            for (int j = 0; j < n4; ++j) {
                nArray[j] = ByteArray.readU16bit(byArray, n2);
                n2 += 2;
            }
            bootstrapMethodArray[i] = new BootstrapMethod(n3, nArray);
        }
        return bootstrapMethodArray;
    }

    @Override
    public AttributeInfo copy(ConstPool constPool, Map<String, String> map) {
        BootstrapMethod[] bootstrapMethodArray = this.getMethods();
        ConstPool constPool2 = this.getConstPool();
        for (int i = 0; i < bootstrapMethodArray.length; ++i) {
            BootstrapMethod bootstrapMethod = bootstrapMethodArray[i];
            bootstrapMethod.methodRef = constPool2.copy(bootstrapMethod.methodRef, constPool, map);
            for (int j = 0; j < bootstrapMethod.arguments.length; ++j) {
                bootstrapMethod.arguments[j] = constPool2.copy(bootstrapMethod.arguments[j], constPool, map);
            }
        }
        return new BootstrapMethodsAttribute(constPool, bootstrapMethodArray);
    }

    public static class BootstrapMethod {
        public int methodRef;
        public int[] arguments;

        public BootstrapMethod(int n, int[] nArray) {
            this.methodRef = n;
            this.arguments = nArray;
        }
    }
}

