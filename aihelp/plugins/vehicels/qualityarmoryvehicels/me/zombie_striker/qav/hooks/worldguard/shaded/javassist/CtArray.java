/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtConstructor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;

final class CtArray
extends CtClass {
    protected ClassPool pool;
    private CtClass[] interfaces = null;

    CtArray(String string, ClassPool classPool) {
        super(string);
        this.pool = classPool;
    }

    @Override
    public ClassPool getClassPool() {
        return this.pool;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public int getModifiers() {
        int n = 16;
        try {
            n |= this.getComponentType().getModifiers() & 7;
        } catch (NotFoundException notFoundException) {
            // empty catch block
        }
        return n;
    }

    @Override
    public CtClass[] getInterfaces() {
        if (this.interfaces == null) {
            Class<?>[] classArray = Object[].class.getInterfaces();
            this.interfaces = new CtClass[classArray.length];
            for (int i = 0; i < classArray.length; ++i) {
                this.interfaces[i] = this.pool.get(classArray[i].getName());
            }
        }
        return this.interfaces;
    }

    @Override
    public boolean subtypeOf(CtClass ctClass) {
        if (super.subtypeOf(ctClass)) {
            return true;
        }
        String string = ctClass.getName();
        if (string.equals("java.lang.Object")) {
            return true;
        }
        CtClass[] ctClassArray = this.getInterfaces();
        for (int i = 0; i < ctClassArray.length; ++i) {
            if (!ctClassArray[i].subtypeOf(ctClass)) continue;
            return true;
        }
        return ctClass.isArray() && this.getComponentType().subtypeOf(ctClass.getComponentType());
    }

    @Override
    public CtClass getComponentType() {
        String string = this.getName();
        return this.pool.get(string.substring(0, string.length() - 2));
    }

    @Override
    public CtClass getSuperclass() {
        return this.pool.get("java.lang.Object");
    }

    @Override
    public CtMethod[] getMethods() {
        try {
            return this.getSuperclass().getMethods();
        } catch (NotFoundException notFoundException) {
            return super.getMethods();
        }
    }

    @Override
    public CtMethod getMethod(String string, String string2) {
        return this.getSuperclass().getMethod(string, string2);
    }

    @Override
    public CtConstructor[] getConstructors() {
        try {
            return this.getSuperclass().getConstructors();
        } catch (NotFoundException notFoundException) {
            return super.getConstructors();
        }
    }
}

