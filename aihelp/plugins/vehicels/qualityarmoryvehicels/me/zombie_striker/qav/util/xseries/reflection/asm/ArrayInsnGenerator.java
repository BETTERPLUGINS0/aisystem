/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.Type
 *  org.objectweb.asm.commons.GeneratorAdapter
 */
package me.zombie_striker.qav.util.xseries.reflection.asm;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;

final class ArrayInsnGenerator {
    private final GeneratorAdapter mv;
    private final int length;
    private int index = 0;
    private final int storeInsn;

    public ArrayInsnGenerator(GeneratorAdapter generatorAdapter, Class<?> clazz, int n) {
        if (clazz.getComponentType() != null) {
            throw new IllegalArgumentException("The raw array element type must be given, not the array type itself: " + clazz);
        }
        this.mv = generatorAdapter;
        this.length = n;
        Type type = Type.getType(clazz);
        this.storeInsn = clazz == Object.class ? -1 : type.getOpcode(79);
        generatorAdapter.push(n);
        generatorAdapter.newArray(type);
    }

    private boolean isDynamicStoreInsn() {
        return this.storeInsn == -1;
    }

    public void add(Runnable runnable) {
        if (this.isDynamicStoreInsn()) {
            throw new IllegalStateException("Must provide the type of stored object since this is a dynamic type array");
        }
        this.add(runnable, this.storeInsn);
    }

    public void add(Type type, Runnable runnable) {
        this.add(runnable, type.getOpcode(79));
    }

    private void add(Runnable runnable, int n) {
        if (this.index >= this.length) {
            throw new IllegalStateException("Array is already full, at index " + this.index);
        }
        this.mv.visitInsn(89);
        this.mv.push(this.index++);
        runnable.run();
        this.mv.visitInsn(n);
    }
}

