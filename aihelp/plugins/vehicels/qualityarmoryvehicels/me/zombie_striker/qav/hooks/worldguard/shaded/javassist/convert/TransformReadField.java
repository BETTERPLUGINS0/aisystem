/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtField;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Modifier;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.Transformer;

public class TransformReadField
extends Transformer {
    protected String fieldname;
    protected CtClass fieldClass;
    protected boolean isPrivate;
    protected String methodClassname;
    protected String methodName;

    public TransformReadField(Transformer transformer, CtField ctField, String string, String string2) {
        super(transformer);
        this.fieldClass = ctField.getDeclaringClass();
        this.fieldname = ctField.getName();
        this.methodClassname = string;
        this.methodName = string2;
        this.isPrivate = Modifier.isPrivate(ctField.getModifiers());
    }

    static String isField(ClassPool classPool, ConstPool constPool, CtClass ctClass, String string, boolean bl, int n) {
        if (!constPool.getFieldrefName(n).equals(string)) {
            return null;
        }
        try {
            CtClass ctClass2 = classPool.get(constPool.getFieldrefClassName(n));
            if (ctClass2 == ctClass || !bl && TransformReadField.isFieldInSuper(ctClass2, ctClass, string)) {
                return constPool.getFieldrefType(n);
            }
        } catch (NotFoundException notFoundException) {
            // empty catch block
        }
        return null;
    }

    static boolean isFieldInSuper(CtClass ctClass, CtClass ctClass2, String string) {
        if (!ctClass.subclassOf(ctClass2)) {
            return false;
        }
        try {
            CtField ctField = ctClass.getField(string);
            return ctField.getDeclaringClass() == ctClass2;
        } catch (NotFoundException notFoundException) {
            return false;
        }
    }

    @Override
    public int transform(CtClass ctClass, int n, CodeIterator codeIterator, ConstPool constPool) {
        int n2 = codeIterator.byteAt(n);
        if (n2 == 180 || n2 == 178) {
            int n3 = codeIterator.u16bitAt(n + 1);
            String string = TransformReadField.isField(ctClass.getClassPool(), constPool, this.fieldClass, this.fieldname, this.isPrivate, n3);
            if (string != null) {
                if (n2 == 178) {
                    codeIterator.move(n);
                    n = codeIterator.insertGap(1);
                    codeIterator.writeByte(1, n);
                    n = codeIterator.next();
                }
                String string2 = "(Ljava/lang/Object;)" + string;
                int n4 = constPool.addClassInfo(this.methodClassname);
                int n5 = constPool.addMethodrefInfo(n4, this.methodName, string2);
                codeIterator.writeByte(184, n);
                codeIterator.write16bit(n5, n + 1);
                return n;
            }
        }
        return n;
    }
}

