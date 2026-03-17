/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ExceptionTable;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Opcode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis.Executor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis.Frame;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis.IntQueue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis.Subroutine;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis.SubroutineScanner;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis.Type;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis.Util;

public class Analyzer
implements Opcode {
    private final SubroutineScanner scanner = new SubroutineScanner();
    private CtClass clazz;
    private ExceptionInfo[] exceptions;
    private Frame[] frames;
    private Subroutine[] subroutines;

    public Frame[] analyze(CtClass ctClass, MethodInfo methodInfo) {
        this.clazz = ctClass;
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        if (codeAttribute == null) {
            return null;
        }
        int n = codeAttribute.getMaxLocals();
        int n2 = codeAttribute.getMaxStack();
        int n3 = codeAttribute.getCodeLength();
        CodeIterator codeIterator = codeAttribute.iterator();
        IntQueue intQueue = new IntQueue();
        this.exceptions = this.buildExceptionInfo(methodInfo);
        this.subroutines = this.scanner.scan(methodInfo);
        Executor executor = new Executor(ctClass.getClassPool(), methodInfo.getConstPool());
        this.frames = new Frame[n3];
        this.frames[codeIterator.lookAhead()] = this.firstFrame(methodInfo, n, n2);
        intQueue.add(codeIterator.next());
        while (!intQueue.isEmpty()) {
            this.analyzeNextEntry(methodInfo, codeIterator, intQueue, executor);
        }
        return this.frames;
    }

    public Frame[] analyze(CtMethod ctMethod) {
        return this.analyze(ctMethod.getDeclaringClass(), ctMethod.getMethodInfo2());
    }

    private void analyzeNextEntry(MethodInfo methodInfo, CodeIterator codeIterator, IntQueue intQueue, Executor executor) {
        int n = intQueue.take();
        codeIterator.move(n);
        codeIterator.next();
        Frame frame = this.frames[n].copy();
        Subroutine subroutine = this.subroutines[n];
        try {
            executor.execute(methodInfo, n, codeIterator, frame, subroutine);
        } catch (RuntimeException runtimeException) {
            throw new BadBytecode(runtimeException.getMessage() + "[pos = " + n + "]", (Throwable)runtimeException);
        }
        int n2 = codeIterator.byteAt(n);
        if (n2 == 170) {
            this.mergeTableSwitch(intQueue, n, codeIterator, frame);
        } else if (n2 == 171) {
            this.mergeLookupSwitch(intQueue, n, codeIterator, frame);
        } else if (n2 == 169) {
            this.mergeRet(intQueue, codeIterator, n, frame, subroutine);
        } else if (Util.isJumpInstruction(n2)) {
            int n3 = Util.getJumpTarget(n, codeIterator);
            if (Util.isJsr(n2)) {
                this.mergeJsr(intQueue, this.frames[n], this.subroutines[n3], n, this.lookAhead(codeIterator, n));
            } else if (!Util.isGoto(n2)) {
                this.merge(intQueue, frame, this.lookAhead(codeIterator, n));
            }
            this.merge(intQueue, frame, n3);
        } else if (n2 != 191 && !Util.isReturn(n2)) {
            this.merge(intQueue, frame, this.lookAhead(codeIterator, n));
        }
        this.mergeExceptionHandlers(intQueue, methodInfo, n, frame);
    }

    private ExceptionInfo[] buildExceptionInfo(MethodInfo methodInfo) {
        ConstPool constPool = methodInfo.getConstPool();
        ClassPool classPool = this.clazz.getClassPool();
        ExceptionTable exceptionTable = methodInfo.getCodeAttribute().getExceptionTable();
        ExceptionInfo[] exceptionInfoArray = new ExceptionInfo[exceptionTable.size()];
        for (int i = 0; i < exceptionTable.size(); ++i) {
            Type type;
            int n = exceptionTable.catchType(i);
            try {
                type = n == 0 ? Type.THROWABLE : Type.get(classPool.get(constPool.getClassInfo(n)));
            } catch (NotFoundException notFoundException) {
                throw new IllegalStateException(notFoundException.getMessage());
            }
            exceptionInfoArray[i] = new ExceptionInfo(exceptionTable.startPc(i), exceptionTable.endPc(i), exceptionTable.handlerPc(i), type);
        }
        return exceptionInfoArray;
    }

    private Frame firstFrame(MethodInfo methodInfo, int n, int n2) {
        CtClass[] ctClassArray;
        int n3 = 0;
        Frame frame = new Frame(n, n2);
        if ((methodInfo.getAccessFlags() & 8) == 0) {
            frame.setLocal(n3++, Type.get(this.clazz));
        }
        try {
            ctClassArray = Descriptor.getParameterTypes(methodInfo.getDescriptor(), this.clazz.getClassPool());
        } catch (NotFoundException notFoundException) {
            throw new RuntimeException(notFoundException);
        }
        for (int i = 0; i < ctClassArray.length; ++i) {
            Type type = this.zeroExtend(Type.get(ctClassArray[i]));
            frame.setLocal(n3++, type);
            if (type.getSize() != 2) continue;
            frame.setLocal(n3++, Type.TOP);
        }
        return frame;
    }

    private int getNext(CodeIterator codeIterator, int n, int n2) {
        codeIterator.move(n);
        codeIterator.next();
        int n3 = codeIterator.lookAhead();
        codeIterator.move(n2);
        codeIterator.next();
        return n3;
    }

    private int lookAhead(CodeIterator codeIterator, int n) {
        if (!codeIterator.hasNext()) {
            throw new BadBytecode("Execution falls off end! [pos = " + n + "]");
        }
        return codeIterator.lookAhead();
    }

    private void merge(IntQueue intQueue, Frame frame, int n) {
        boolean bl;
        Frame frame2 = this.frames[n];
        if (frame2 == null) {
            this.frames[n] = frame.copy();
            bl = true;
        } else {
            bl = frame2.merge(frame);
        }
        if (bl) {
            intQueue.add(n);
        }
    }

    private void mergeExceptionHandlers(IntQueue intQueue, MethodInfo methodInfo, int n, Frame frame) {
        for (int i = 0; i < this.exceptions.length; ++i) {
            ExceptionInfo exceptionInfo = this.exceptions[i];
            if (n < exceptionInfo.start || n >= exceptionInfo.end) continue;
            Frame frame2 = frame.copy();
            frame2.clearStack();
            frame2.push(exceptionInfo.type);
            this.merge(intQueue, frame2, exceptionInfo.handler);
        }
    }

    private void mergeJsr(IntQueue intQueue, Frame frame, Subroutine subroutine, int n, int n2) {
        if (subroutine == null) {
            throw new BadBytecode("No subroutine at jsr target! [pos = " + n + "]");
        }
        Frame frame2 = this.frames[n2];
        boolean bl = false;
        if (frame2 == null) {
            frame2 = this.frames[n2] = frame.copy();
            bl = true;
        } else {
            for (int i = 0; i < frame.localsLength(); ++i) {
                if (subroutine.isAccessed(i)) continue;
                Type type = frame2.getLocal(i);
                Type type2 = frame.getLocal(i);
                if (type == null) {
                    frame2.setLocal(i, type2);
                    bl = true;
                    continue;
                }
                type2 = type.merge(type2);
                frame2.setLocal(i, type2);
                if (type2.equals(type) && !type2.popChanged()) continue;
                bl = true;
            }
        }
        if (!frame2.isJsrMerged()) {
            frame2.setJsrMerged(true);
            bl = true;
        }
        if (bl && frame2.isRetMerged()) {
            intQueue.add(n2);
        }
    }

    private void mergeLookupSwitch(IntQueue intQueue, int n, CodeIterator codeIterator, Frame frame) {
        int n2 = (n & 0xFFFFFFFC) + 4;
        this.merge(intQueue, frame, n + codeIterator.s32bitAt(n2));
        int n3 = codeIterator.s32bitAt(n2 += 4);
        int n4 = n3 * 8 + (n2 += 4);
        n2 += 4;
        while (n2 < n4) {
            int n5 = codeIterator.s32bitAt(n2) + n;
            this.merge(intQueue, frame, n5);
            n2 += 8;
        }
    }

    private void mergeRet(IntQueue intQueue, CodeIterator codeIterator, int n, Frame frame, Subroutine subroutine) {
        if (subroutine == null) {
            throw new BadBytecode("Ret on no subroutine! [pos = " + n + "]");
        }
        for (int n2 : subroutine.callers()) {
            int n3 = this.getNext(codeIterator, n2, n);
            boolean bl = false;
            Frame frame2 = this.frames[n3];
            if (frame2 == null) {
                frame2 = this.frames[n3] = frame.copyStack();
                bl = true;
            } else {
                bl = frame2.mergeStack(frame);
            }
            for (int n4 : subroutine.accessed()) {
                Type type;
                Type type2 = frame2.getLocal(n4);
                if (type2 == (type = frame.getLocal(n4))) continue;
                frame2.setLocal(n4, type);
                bl = true;
            }
            if (!frame2.isRetMerged()) {
                frame2.setRetMerged(true);
                bl = true;
            }
            if (!bl || !frame2.isJsrMerged()) continue;
            intQueue.add(n3);
        }
    }

    private void mergeTableSwitch(IntQueue intQueue, int n, CodeIterator codeIterator, Frame frame) {
        int n2 = (n & 0xFFFFFFFC) + 4;
        this.merge(intQueue, frame, n + codeIterator.s32bitAt(n2));
        int n3 = codeIterator.s32bitAt(n2 += 4);
        int n4 = codeIterator.s32bitAt(n2 += 4);
        int n5 = (n4 - n3 + 1) * 4 + (n2 += 4);
        while (n2 < n5) {
            int n6 = codeIterator.s32bitAt(n2) + n;
            this.merge(intQueue, frame, n6);
            n2 += 4;
        }
    }

    private Type zeroExtend(Type type) {
        if (type == Type.SHORT || type == Type.BYTE || type == Type.CHAR || type == Type.BOOLEAN) {
            return Type.INTEGER;
        }
        return type;
    }

    private static class ExceptionInfo {
        private int end;
        private int handler;
        private int start;
        private Type type;

        private ExceptionInfo(int n, int n2, int n3, Type type) {
            this.start = n;
            this.end = n2;
            this.handler = n3;
            this.type = type;
        }
    }
}

