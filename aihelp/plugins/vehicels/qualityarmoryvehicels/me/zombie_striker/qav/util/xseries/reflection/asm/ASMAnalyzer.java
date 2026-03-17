/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.ClassReader
 *  org.objectweb.asm.ClassVisitor
 *  org.objectweb.asm.Type
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.MethodNode
 *  org.objectweb.asm.tree.analysis.Analyzer
 *  org.objectweb.asm.tree.analysis.AnalyzerException
 *  org.objectweb.asm.tree.analysis.Interpreter
 *  org.objectweb.asm.tree.analysis.SimpleVerifier
 *  org.objectweb.asm.util.CheckClassAdapter
 */
package me.zombie_striker.qav.util.xseries.reflection.asm;

import java.io.PrintWriter;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import me.zombie_striker.qav.util.xseries.reflection.asm.ASMVersion;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Interpreter;
import org.objectweb.asm.tree.analysis.SimpleVerifier;
import org.objectweb.asm.util.CheckClassAdapter;

final class ASMAnalyzer {
    private static final MethodHandle CheckClassAdapter_printAnalyzerResult;

    private ASMAnalyzer() {
    }

    protected static Throwable findCause(Throwable throwable, Predicate<Throwable> predicate) {
        Set set = Collections.newSetFromMap(new IdentityHashMap(5));
        Throwable throwable2 = throwable;
        do {
            if (!set.add(throwable2)) {
                return null;
            }
            if (!predicate.test(throwable2)) continue;
            return throwable2;
        } while ((throwable2 = throwable2.getCause()) != null);
        return null;
    }

    protected static void verify(ClassReader classReader, ClassLoader classLoader, boolean bl, PrintWriter printWriter) {
        ClassNode classNode = new ClassNode();
        classReader.accept((ClassVisitor)new CheckClassAdapter(ASMVersion.LATEST_ASM_OPCODE_VERSION, (ClassVisitor)classNode, false){}, 2);
        Type type = classNode.superName == null ? null : Type.getObjectType((String)classNode.superName);
        List list = classNode.methods;
        ArrayList<Type> arrayList = new ArrayList<Type>();
        for (String string : classNode.interfaces) {
            arrayList.add(Type.getObjectType((String)string));
        }
        for (String string : list) {
            boolean bl2;
            SimpleVerifier simpleVerifier = new SimpleVerifier(Type.getObjectType((String)classNode.name), type, arrayList, (classNode.access & 0x200) != 0);
            Analyzer analyzer = new Analyzer((Interpreter)simpleVerifier);
            if (classLoader != null) {
                simpleVerifier.setClassLoader(classLoader);
            }
            try {
                analyzer.analyze(classNode.name, (MethodNode)string);
                bl2 = false;
            } catch (AnalyzerException analyzerException) {
                ClassNotFoundException classNotFoundException = (ClassNotFoundException)ASMAnalyzer.findCause(analyzerException, throwable -> throwable instanceof ClassNotFoundException);
                if (classNotFoundException == null || classNotFoundException.getMessage() == null || !classNotFoundException.getMessage().contains("XSeriesGen")) {
                    bl2 = true;
                    analyzerException.printStackTrace(printWriter);
                }
                bl2 = false;
            }
            if (!bl && !bl2) continue;
            try {
                CheckClassAdapter_printAnalyzerResult.invokeExact((MethodNode)string, analyzer, printWriter);
            } catch (Throwable throwable2) {
                throw new IllegalStateException("Cannot write bytecode instructions: ", throwable2);
            }
        }
        printWriter.flush();
    }

    static {
        MethodHandle methodHandle;
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        try {
            Method method = CheckClassAdapter.class.getDeclaredMethod("printAnalyzerResult", MethodNode.class, Analyzer.class, PrintWriter.class);
            method.setAccessible(true);
            methodHandle = lookup.unreflect(method);
        } catch (IllegalAccessException | NoSuchMethodException reflectiveOperationException) {
            methodHandle = null;
        }
        CheckClassAdapter_printAnalyzerResult = methodHandle;
    }
}

