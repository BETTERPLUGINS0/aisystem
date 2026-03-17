/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtField;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.TransformReadField;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.Transformer;

public final class TransformWriteField
extends TransformReadField {
    public TransformWriteField(Transformer transformer, CtField ctField, String string, String string2) {
        super(transformer, ctField, string, string2);
    }

    @Override
    public int transform(CtClass ctClass, int n, CodeIterator codeIterator, ConstPool constPool) {
        int n2 = codeIterator.byteAt(n);
        if (n2 == 181 || n2 == 179) {
            int n3 = codeIterator.u16bitAt(n + 1);
            String string = TransformWriteField.isField(ctClass.getClassPool(), constPool, this.fieldClass, this.fieldname, this.isPrivate, n3);
            if (string != null) {
                if (n2 == 179) {
                    CodeAttribute codeAttribute = codeIterator.get();
                    codeIterator.move(n);
                    char c = string.charAt(0);
                    if (c == 'J' || c == 'D') {
                        n = codeIterator.insertGap(3);
                        codeIterator.writeByte(1, n);
                        codeIterator.writeByte(91, n + 1);
                        codeIterator.writeByte(87, n + 2);
                        codeAttribute.setMaxStack(codeAttribute.getMaxStack() + 2);
                    } else {
                        n = codeIterator.insertGap(2);
                        codeIterator.writeByte(1, n);
                        codeIterator.writeByte(95, n + 1);
                        codeAttribute.setMaxStack(codeAttribute.getMaxStack() + 1);
                    }
                    n = codeIterator.next();
                }
                int n4 = constPool.addClassInfo(this.methodClassname);
                String string2 = "(Ljava/lang/Object;" + string + ")V";
                int n5 = constPool.addMethodrefInfo(n4, this.methodName, string2);
                codeIterator.writeByte(184, n);
                codeIterator.write16bit(n5, n + 1);
            }
        }
        return n;
    }
}

