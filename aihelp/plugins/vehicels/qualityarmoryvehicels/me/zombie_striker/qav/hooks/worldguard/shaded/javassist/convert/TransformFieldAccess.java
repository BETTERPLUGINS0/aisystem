/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtField;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Modifier;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.TransformReadField;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.Transformer;

public final class TransformFieldAccess
extends Transformer {
    private String newClassname;
    private String newFieldname;
    private String fieldname;
    private CtClass fieldClass;
    private boolean isPrivate;
    private int newIndex;
    private ConstPool constPool;

    public TransformFieldAccess(Transformer transformer, CtField ctField, String string, String string2) {
        super(transformer);
        this.fieldClass = ctField.getDeclaringClass();
        this.fieldname = ctField.getName();
        this.isPrivate = Modifier.isPrivate(ctField.getModifiers());
        this.newClassname = string;
        this.newFieldname = string2;
        this.constPool = null;
    }

    @Override
    public void initialize(ConstPool constPool, CodeAttribute codeAttribute) {
        if (this.constPool != constPool) {
            this.newIndex = 0;
        }
    }

    @Override
    public int transform(CtClass ctClass, int n, CodeIterator codeIterator, ConstPool constPool) {
        int n2 = codeIterator.byteAt(n);
        if (n2 == 180 || n2 == 178 || n2 == 181 || n2 == 179) {
            int n3 = codeIterator.u16bitAt(n + 1);
            String string = TransformReadField.isField(ctClass.getClassPool(), constPool, this.fieldClass, this.fieldname, this.isPrivate, n3);
            if (string != null) {
                if (this.newIndex == 0) {
                    int n4 = constPool.addNameAndTypeInfo(this.newFieldname, string);
                    this.newIndex = constPool.addFieldrefInfo(constPool.addClassInfo(this.newClassname), n4);
                    this.constPool = constPool;
                }
                codeIterator.write16bit(this.newIndex, n + 1);
            }
        }
        return n;
    }
}

