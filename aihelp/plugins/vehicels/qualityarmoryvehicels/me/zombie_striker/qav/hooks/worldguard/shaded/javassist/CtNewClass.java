/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import java.io.DataOutputStream;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClassType;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtConstructor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtNewConstructor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Modifier;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;

class CtNewClass
extends CtClassType {
    protected boolean hasConstructor;

    CtNewClass(String string, ClassPool classPool, boolean bl, CtClass ctClass) {
        super(string, classPool);
        this.wasChanged = true;
        String string2 = bl || ctClass == null ? null : ctClass.getName();
        this.classfile = new ClassFile(bl, string, string2);
        if (bl && ctClass != null) {
            this.classfile.setInterfaces(new String[]{ctClass.getName()});
        }
        this.setModifiers(Modifier.setPublic(this.getModifiers()));
        this.hasConstructor = bl;
    }

    @Override
    protected void extendToString(StringBuilder stringBuilder) {
        if (this.hasConstructor) {
            stringBuilder.append("hasConstructor ");
        }
        super.extendToString(stringBuilder);
    }

    @Override
    public void addConstructor(CtConstructor ctConstructor) {
        this.hasConstructor = true;
        super.addConstructor(ctConstructor);
    }

    @Override
    public void toBytecode(DataOutputStream dataOutputStream) {
        if (!this.hasConstructor) {
            try {
                this.inheritAllConstructors();
                this.hasConstructor = true;
            } catch (NotFoundException notFoundException) {
                throw new CannotCompileException(notFoundException);
            }
        }
        super.toBytecode(dataOutputStream);
    }

    public void inheritAllConstructors() {
        CtClass ctClass = this.getSuperclass();
        CtConstructor[] ctConstructorArray = ctClass.getDeclaredConstructors();
        int n = 0;
        for (int i = 0; i < ctConstructorArray.length; ++i) {
            CtConstructor ctConstructor = ctConstructorArray[i];
            int n2 = ctConstructor.getModifiers();
            if (!this.isInheritable(n2, ctClass)) continue;
            CtConstructor ctConstructor2 = CtNewConstructor.make(ctConstructor.getParameterTypes(), ctConstructor.getExceptionTypes(), this);
            ctConstructor2.setModifiers(n2 & 7);
            this.addConstructor(ctConstructor2);
            ++n;
        }
        if (n < 1) {
            throw new CannotCompileException("no inheritable constructor in " + ctClass.getName());
        }
    }

    private boolean isInheritable(int n, CtClass ctClass) {
        if (Modifier.isPrivate(n)) {
            return false;
        }
        if (Modifier.isPackage(n)) {
            String string = this.getPackageName();
            String string2 = ctClass.getPackageName();
            if (string == null) {
                return string2 == null;
            }
            return string.equals(string2);
        }
        return true;
    }
}

