/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect;

import java.io.PrintStream;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect.CompiledClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect.Reflection;

public class Compiler {
    public static void main(String[] stringArray) {
        if (stringArray.length == 0) {
            Compiler.help(System.err);
            return;
        }
        CompiledClass[] compiledClassArray = new CompiledClass[stringArray.length];
        int n = Compiler.parse(stringArray, compiledClassArray);
        if (n < 1) {
            System.err.println("bad parameter.");
            return;
        }
        Compiler.processClasses(compiledClassArray, n);
    }

    private static void processClasses(CompiledClass[] compiledClassArray, int n) {
        int n2;
        Reflection reflection = new Reflection();
        ClassPool classPool = ClassPool.getDefault();
        reflection.start(classPool);
        for (n2 = 0; n2 < n; ++n2) {
            CtClass ctClass = classPool.get(compiledClassArray[n2].classname);
            if (compiledClassArray[n2].metaobject != null || compiledClassArray[n2].classobject != null) {
                String string = compiledClassArray[n2].metaobject == null ? "me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect.Metaobject" : compiledClassArray[n2].metaobject;
                String string2 = compiledClassArray[n2].classobject == null ? "me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect.ClassMetaobject" : compiledClassArray[n2].classobject;
                if (!reflection.makeReflective(ctClass, classPool.get(string), classPool.get(string2))) {
                    System.err.println("Warning: " + ctClass.getName() + " is reflective.  It was not changed.");
                }
                System.err.println(ctClass.getName() + ": " + string + ", " + string2);
                continue;
            }
            System.err.println(ctClass.getName() + ": not reflective");
        }
        for (n2 = 0; n2 < n; ++n2) {
            reflection.onLoad(classPool, compiledClassArray[n2].classname);
            classPool.get(compiledClassArray[n2].classname).writeFile();
        }
    }

    private static int parse(String[] stringArray, CompiledClass[] compiledClassArray) {
        int n = -1;
        for (int i = 0; i < stringArray.length; ++i) {
            String string = stringArray[i];
            if (string.equals("-m")) {
                if (n < 0 || i + 1 > stringArray.length) {
                    return -1;
                }
                compiledClassArray[n].metaobject = stringArray[++i];
                continue;
            }
            if (string.equals("-c")) {
                if (n < 0 || i + 1 > stringArray.length) {
                    return -1;
                }
                compiledClassArray[n].classobject = stringArray[++i];
                continue;
            }
            if (string.charAt(0) == '-') {
                return -1;
            }
            CompiledClass compiledClass = new CompiledClass();
            compiledClass.classname = string;
            compiledClass.metaobject = null;
            compiledClass.classobject = null;
            compiledClassArray[++n] = compiledClass;
        }
        return n + 1;
    }

    private static void help(PrintStream printStream) {
        printStream.println("Usage: java javassist.tools.reflect.Compiler");
        printStream.println("            (<class> [-m <metaobject>] [-c <class metaobject>])+");
    }
}

