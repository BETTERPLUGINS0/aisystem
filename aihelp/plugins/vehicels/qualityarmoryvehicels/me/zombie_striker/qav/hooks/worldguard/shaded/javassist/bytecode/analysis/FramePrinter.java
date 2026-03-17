/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis;

import java.io.PrintStream;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Modifier;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.InstructionPrinter;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis.Analyzer;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis.Frame;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis.Type;

public final class FramePrinter {
    private final PrintStream stream;

    public FramePrinter(PrintStream printStream) {
        this.stream = printStream;
    }

    public static void print(CtClass ctClass, PrintStream printStream) {
        new FramePrinter(printStream).print(ctClass);
    }

    public void print(CtClass ctClass) {
        CtMethod[] ctMethodArray = ctClass.getDeclaredMethods();
        for (int i = 0; i < ctMethodArray.length; ++i) {
            this.print(ctMethodArray[i]);
        }
    }

    private String getMethodString(CtMethod ctMethod) {
        try {
            return Modifier.toString(ctMethod.getModifiers()) + " " + ctMethod.getReturnType().getName() + " " + ctMethod.getName() + Descriptor.toString(ctMethod.getSignature()) + ";";
        } catch (NotFoundException notFoundException) {
            throw new RuntimeException(notFoundException);
        }
    }

    public void print(CtMethod ctMethod) {
        Frame[] frameArray;
        this.stream.println("\n" + this.getMethodString(ctMethod));
        MethodInfo methodInfo = ctMethod.getMethodInfo2();
        ConstPool constPool = methodInfo.getConstPool();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        if (codeAttribute == null) {
            return;
        }
        try {
            frameArray = new Analyzer().analyze(ctMethod.getDeclaringClass(), methodInfo);
        } catch (BadBytecode badBytecode) {
            throw new RuntimeException(badBytecode);
        }
        int n = String.valueOf(codeAttribute.getCodeLength()).length();
        CodeIterator codeIterator = codeAttribute.iterator();
        while (codeIterator.hasNext()) {
            int n2;
            try {
                n2 = codeIterator.next();
            } catch (BadBytecode badBytecode) {
                throw new RuntimeException(badBytecode);
            }
            this.stream.println(n2 + ": " + InstructionPrinter.instructionString(codeIterator, n2, constPool));
            this.addSpacing(n + 3);
            Frame frame = frameArray[n2];
            if (frame == null) {
                this.stream.println("--DEAD CODE--");
                continue;
            }
            this.printStack(frame);
            this.addSpacing(n + 3);
            this.printLocals(frame);
        }
    }

    private void printStack(Frame frame) {
        this.stream.print("stack [");
        int n = frame.getTopIndex();
        for (int i = 0; i <= n; ++i) {
            if (i > 0) {
                this.stream.print(", ");
            }
            Type type = frame.getStack(i);
            this.stream.print(type);
        }
        this.stream.println("]");
    }

    private void printLocals(Frame frame) {
        this.stream.print("locals [");
        int n = frame.localsLength();
        for (int i = 0; i < n; ++i) {
            Type type;
            if (i > 0) {
                this.stream.print(", ");
            }
            this.stream.print((type = frame.getLocal(i)) == null ? "empty" : type.toString());
        }
        this.stream.println("]");
    }

    private void addSpacing(int n) {
        while (n-- > 0) {
            this.stream.print(' ');
        }
    }
}

