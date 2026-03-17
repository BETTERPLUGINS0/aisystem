/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sun.tools.attach.VirtualMachine
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util;

import com.sun.tools.attach.VirtualMachine;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.management.ManagementFactory;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipOutputStream;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;

public class HotSwapAgent {
    private static Instrumentation instrumentation = null;

    public Instrumentation instrumentation() {
        return instrumentation;
    }

    public static void premain(String string, Instrumentation instrumentation) {
        HotSwapAgent.agentmain(string, instrumentation);
    }

    public static void agentmain(String string, Instrumentation instrumentation) {
        if (!instrumentation.isRedefineClassesSupported()) {
            throw new RuntimeException("this JVM does not support redefinition of classes");
        }
        HotSwapAgent.instrumentation = instrumentation;
    }

    public static void redefine(Class<?> clazz, CtClass ctClass) {
        Class[] classArray = new Class[]{clazz};
        CtClass[] ctClassArray = new CtClass[]{ctClass};
        HotSwapAgent.redefine(classArray, ctClassArray);
    }

    public static void redefine(Class<?>[] classArray, CtClass[] ctClassArray) {
        HotSwapAgent.startAgent();
        ClassDefinition[] classDefinitionArray = new ClassDefinition[classArray.length];
        for (int i = 0; i < classArray.length; ++i) {
            classDefinitionArray[i] = new ClassDefinition(classArray[i], ctClassArray[i].toBytecode());
        }
        try {
            instrumentation.redefineClasses(classDefinitionArray);
        } catch (ClassNotFoundException classNotFoundException) {
            throw new NotFoundException(classNotFoundException.getMessage(), classNotFoundException);
        } catch (UnmodifiableClassException unmodifiableClassException) {
            throw new CannotCompileException(unmodifiableClassException.getMessage(), unmodifiableClassException);
        }
    }

    private static void startAgent() {
        if (instrumentation != null) {
            return;
        }
        try {
            File file = HotSwapAgent.createJarFile();
            String string = ManagementFactory.getRuntimeMXBean().getName();
            String string2 = string.substring(0, string.indexOf(64));
            VirtualMachine virtualMachine = VirtualMachine.attach((String)string2);
            virtualMachine.loadAgent(file.getAbsolutePath(), null);
            virtualMachine.detach();
        } catch (Exception exception) {
            throw new NotFoundException("hotswap agent", exception);
        }
        for (int i = 0; i < 10; ++i) {
            if (instrumentation != null) {
                return;
            }
            try {
                Thread.sleep(1000L);
                continue;
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        throw new NotFoundException("hotswap agent (timeout)");
    }

    public static File createAgentJarFile(String string) {
        return HotSwapAgent.createJarFile(new File(string));
    }

    private static File createJarFile() {
        File file = File.createTempFile("agent", ".jar");
        file.deleteOnExit();
        return HotSwapAgent.createJarFile(file);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static File createJarFile(File file) {
        Manifest manifest = new Manifest();
        Attributes attributes = manifest.getMainAttributes();
        attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        attributes.put(new Attributes.Name("Premain-Class"), HotSwapAgent.class.getName());
        attributes.put(new Attributes.Name("Agent-Class"), HotSwapAgent.class.getName());
        attributes.put(new Attributes.Name("Can-Retransform-Classes"), "true");
        attributes.put(new Attributes.Name("Can-Redefine-Classes"), "true");
        try (ZipOutputStream zipOutputStream = null;){
            zipOutputStream = new JarOutputStream((OutputStream)new FileOutputStream(file), manifest);
            String string = HotSwapAgent.class.getName();
            JarEntry jarEntry = new JarEntry(string.replace('.', '/') + ".class");
            ((JarOutputStream)zipOutputStream).putNextEntry(jarEntry);
            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass = classPool.get(string);
            zipOutputStream.write(ctClass.toBytecode());
            zipOutputStream.closeEntry();
        }
        return file;
    }
}

