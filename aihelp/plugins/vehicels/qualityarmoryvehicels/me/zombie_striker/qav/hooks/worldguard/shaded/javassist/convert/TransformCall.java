/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Modifier;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.Transformer;

public class TransformCall
extends Transformer {
    protected String classname;
    protected String methodname;
    protected String methodDescriptor;
    protected String newClassname;
    protected String newMethodname;
    protected boolean newMethodIsPrivate;
    protected int newIndex;
    protected ConstPool constPool;

    public TransformCall(Transformer transformer, CtMethod ctMethod, CtMethod ctMethod2) {
        this(transformer, ctMethod.getName(), ctMethod2);
        this.classname = ctMethod.getDeclaringClass().getName();
    }

    public TransformCall(Transformer transformer, String string, CtMethod ctMethod) {
        super(transformer);
        this.methodname = string;
        this.methodDescriptor = ctMethod.getMethodInfo2().getDescriptor();
        this.classname = this.newClassname = ctMethod.getDeclaringClass().getName();
        this.newMethodname = ctMethod.getName();
        this.constPool = null;
        this.newMethodIsPrivate = Modifier.isPrivate(ctMethod.getModifiers());
    }

    @Override
    public void initialize(ConstPool constPool, CodeAttribute codeAttribute) {
        if (this.constPool != constPool) {
            this.newIndex = 0;
        }
    }

    @Override
    public int transform(CtClass ctClass, int n, CodeIterator codeIterator, ConstPool constPool) {
        int n2;
        String string;
        int n3 = codeIterator.byteAt(n);
        if ((n3 == 185 || n3 == 183 || n3 == 184 || n3 == 182) && (string = constPool.eqMember(this.methodname, this.methodDescriptor, n2 = codeIterator.u16bitAt(n + 1))) != null && this.matchClass(string, ctClass.getClassPool())) {
            int n4 = constPool.getMemberNameAndType(n2);
            n = this.match(n3, n, codeIterator, constPool.getNameAndTypeDescriptor(n4), constPool);
        }
        return n;
    }

    private boolean matchClass(String string, ClassPool classPool) {
        if (this.classname.equals(string)) {
            return true;
        }
        try {
            CtClass ctClass = classPool.get(string);
            CtClass ctClass2 = classPool.get(this.classname);
            if (ctClass.subtypeOf(ctClass2)) {
                try {
                    CtMethod ctMethod = ctClass.getMethod(this.methodname, this.methodDescriptor);
                    return ctMethod.getDeclaringClass().getName().equals(this.classname);
                } catch (NotFoundException notFoundException) {
                    return true;
                }
            }
        } catch (NotFoundException notFoundException) {
            return false;
        }
        return false;
    }

    protected int match(int n, int n2, CodeIterator codeIterator, int n3, ConstPool constPool) {
        if (this.newIndex == 0) {
            int n4 = constPool.addNameAndTypeInfo(constPool.addUtf8Info(this.newMethodname), n3);
            int n5 = constPool.addClassInfo(this.newClassname);
            if (n == 185) {
                this.newIndex = constPool.addInterfaceMethodrefInfo(n5, n4);
            } else {
                if (this.newMethodIsPrivate && n == 182) {
                    codeIterator.writeByte(183, n2);
                }
                this.newIndex = constPool.addMethodrefInfo(n5, n4);
            }
            this.constPool = constPool;
        }
        codeIterator.write16bit(this.newIndex, n2 + 1);
        return n2;
    }
}

