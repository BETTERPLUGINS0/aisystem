/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtField;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Modifier;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.TransformAccessArrayField;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.TransformAfter;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.TransformBefore;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.TransformCall;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.TransformCallToStatic;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.TransformFieldAccess;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.TransformNew;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.TransformNewClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.TransformReadField;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.TransformWriteField;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.Transformer;

public class CodeConverter {
    protected Transformer transformers = null;

    public void replaceNew(CtClass ctClass, CtClass ctClass2, String string) {
        this.transformers = new TransformNew(this.transformers, ctClass.getName(), ctClass2.getName(), string);
    }

    public void replaceNew(CtClass ctClass, CtClass ctClass2) {
        this.transformers = new TransformNewClass(this.transformers, ctClass.getName(), ctClass2.getName());
    }

    public void redirectFieldAccess(CtField ctField, CtClass ctClass, String string) {
        this.transformers = new TransformFieldAccess(this.transformers, ctField, ctClass.getName(), string);
    }

    public void replaceFieldRead(CtField ctField, CtClass ctClass, String string) {
        this.transformers = new TransformReadField(this.transformers, ctField, ctClass.getName(), string);
    }

    public void replaceFieldWrite(CtField ctField, CtClass ctClass, String string) {
        this.transformers = new TransformWriteField(this.transformers, ctField, ctClass.getName(), string);
    }

    public void replaceArrayAccess(CtClass ctClass, ArrayAccessReplacementMethodNames arrayAccessReplacementMethodNames) {
        this.transformers = new TransformAccessArrayField(this.transformers, ctClass.getName(), arrayAccessReplacementMethodNames);
    }

    public void redirectMethodCall(CtMethod ctMethod, CtMethod ctMethod2) {
        String string;
        String string2 = ctMethod.getMethodInfo2().getDescriptor();
        if (!string2.equals(string = ctMethod2.getMethodInfo2().getDescriptor())) {
            throw new CannotCompileException("signature mismatch: " + ctMethod2.getLongName());
        }
        int n = ctMethod.getModifiers();
        int n2 = ctMethod2.getModifiers();
        if (Modifier.isStatic(n) != Modifier.isStatic(n2) || Modifier.isPrivate(n) && !Modifier.isPrivate(n2) || ctMethod.getDeclaringClass().isInterface() != ctMethod2.getDeclaringClass().isInterface()) {
            throw new CannotCompileException("invoke-type mismatch " + ctMethod2.getLongName());
        }
        this.transformers = new TransformCall(this.transformers, ctMethod, ctMethod2);
    }

    public void redirectMethodCall(String string, CtMethod ctMethod) {
        this.transformers = new TransformCall(this.transformers, string, ctMethod);
    }

    public void redirectMethodCallToStatic(CtMethod ctMethod, CtMethod ctMethod2) {
        this.transformers = new TransformCallToStatic(this.transformers, ctMethod, ctMethod2);
    }

    public void insertBeforeMethod(CtMethod ctMethod, CtMethod ctMethod2) {
        try {
            this.transformers = new TransformBefore(this.transformers, ctMethod, ctMethod2);
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        }
    }

    public void insertAfterMethod(CtMethod ctMethod, CtMethod ctMethod2) {
        try {
            this.transformers = new TransformAfter(this.transformers, ctMethod, ctMethod2);
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        }
    }

    protected void doit(CtClass ctClass, MethodInfo methodInfo, ConstPool constPool) {
        int n;
        Transformer transformer;
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        if (codeAttribute == null || this.transformers == null) {
            return;
        }
        for (transformer = this.transformers; transformer != null; transformer = transformer.getNext()) {
            transformer.initialize(constPool, ctClass, methodInfo);
        }
        CodeIterator codeIterator = codeAttribute.iterator();
        while (codeIterator.hasNext()) {
            try {
                n = codeIterator.next();
                for (transformer = this.transformers; transformer != null; transformer = transformer.getNext()) {
                    n = transformer.transform(ctClass, n, codeIterator, constPool);
                }
            } catch (BadBytecode badBytecode) {
                throw new CannotCompileException(badBytecode);
            }
        }
        n = 0;
        int n2 = 0;
        for (transformer = this.transformers; transformer != null; transformer = transformer.getNext()) {
            int n3 = transformer.extraLocals();
            if (n3 > n) {
                n = n3;
            }
            if ((n3 = transformer.extraStack()) <= n2) continue;
            n2 = n3;
        }
        for (transformer = this.transformers; transformer != null; transformer = transformer.getNext()) {
            transformer.clean();
        }
        if (n > 0) {
            codeAttribute.setMaxLocals(codeAttribute.getMaxLocals() + n);
        }
        if (n2 > 0) {
            codeAttribute.setMaxStack(codeAttribute.getMaxStack() + n2);
        }
        try {
            methodInfo.rebuildStackMapIf6(ctClass.getClassPool(), ctClass.getClassFile2());
        } catch (BadBytecode badBytecode) {
            throw new CannotCompileException(badBytecode.getMessage(), badBytecode);
        }
    }

    public static interface ArrayAccessReplacementMethodNames {
        public String byteOrBooleanRead();

        public String byteOrBooleanWrite();

        public String charRead();

        public String charWrite();

        public String doubleRead();

        public String doubleWrite();

        public String floatRead();

        public String floatWrite();

        public String intRead();

        public String intWrite();

        public String longRead();

        public String longWrite();

        public String objectRead();

        public String objectWrite();

        public String shortRead();

        public String shortWrite();
    }

    public static class DefaultArrayAccessReplacementMethodNames
    implements ArrayAccessReplacementMethodNames {
        @Override
        public String byteOrBooleanRead() {
            return "arrayReadByteOrBoolean";
        }

        @Override
        public String byteOrBooleanWrite() {
            return "arrayWriteByteOrBoolean";
        }

        @Override
        public String charRead() {
            return "arrayReadChar";
        }

        @Override
        public String charWrite() {
            return "arrayWriteChar";
        }

        @Override
        public String doubleRead() {
            return "arrayReadDouble";
        }

        @Override
        public String doubleWrite() {
            return "arrayWriteDouble";
        }

        @Override
        public String floatRead() {
            return "arrayReadFloat";
        }

        @Override
        public String floatWrite() {
            return "arrayWriteFloat";
        }

        @Override
        public String intRead() {
            return "arrayReadInt";
        }

        @Override
        public String intWrite() {
            return "arrayWriteInt";
        }

        @Override
        public String longRead() {
            return "arrayReadLong";
        }

        @Override
        public String longWrite() {
            return "arrayWriteLong";
        }

        @Override
        public String objectRead() {
            return "arrayReadObject";
        }

        @Override
        public String objectWrite() {
            return "arrayWriteObject";
        }

        @Override
        public String shortRead() {
            return "arrayReadShort";
        }

        @Override
        public String shortWrite() {
            return "arrayWriteShort";
        }
    }
}

