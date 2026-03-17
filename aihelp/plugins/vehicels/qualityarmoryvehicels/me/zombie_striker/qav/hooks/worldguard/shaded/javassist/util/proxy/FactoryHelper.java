/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.security.ProtectionDomain;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.DefineClassHelper;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.ProxyFactory;

public class FactoryHelper {
    public static final Class<?>[] primitiveTypes = new Class[]{Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Void.TYPE};
    public static final String[] wrapperTypes = new String[]{"java.lang.Boolean", "java.lang.Byte", "java.lang.Character", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double", "java.lang.Void"};
    public static final String[] wrapperDesc = new String[]{"(Z)V", "(B)V", "(C)V", "(S)V", "(I)V", "(J)V", "(F)V", "(D)V"};
    public static final String[] unwarpMethods = new String[]{"booleanValue", "byteValue", "charValue", "shortValue", "intValue", "longValue", "floatValue", "doubleValue"};
    public static final String[] unwrapDesc = new String[]{"()Z", "()B", "()C", "()S", "()I", "()J", "()F", "()D"};
    public static final int[] dataSize = new int[]{1, 1, 1, 1, 1, 2, 1, 2};

    public static final int typeIndex(Class<?> clazz) {
        for (int i = 0; i < primitiveTypes.length; ++i) {
            if (primitiveTypes[i] != clazz) continue;
            return i;
        }
        throw new RuntimeException("bad type:" + clazz.getName());
    }

    public static Class<?> toClass(ClassFile classFile, ClassLoader classLoader) {
        return FactoryHelper.toClass(classFile, null, classLoader, null);
    }

    public static Class<?> toClass(ClassFile classFile, ClassLoader classLoader, ProtectionDomain protectionDomain) {
        return FactoryHelper.toClass(classFile, null, classLoader, protectionDomain);
    }

    public static Class<?> toClass(ClassFile classFile, Class<?> clazz, ClassLoader classLoader, ProtectionDomain protectionDomain) {
        try {
            byte[] byArray = FactoryHelper.toBytecode(classFile);
            if (ProxyFactory.onlyPublicMethods) {
                return DefineClassHelper.toPublicClass(classFile.getName(), byArray);
            }
            return DefineClassHelper.toClass(classFile.getName(), clazz, classLoader, protectionDomain, byArray);
        } catch (IOException iOException) {
            throw new CannotCompileException(iOException);
        }
    }

    public static Class<?> toClass(ClassFile classFile, MethodHandles.Lookup lookup) {
        try {
            byte[] byArray = FactoryHelper.toBytecode(classFile);
            return DefineClassHelper.toClass(lookup, byArray);
        } catch (IOException iOException) {
            throw new CannotCompileException(iOException);
        }
    }

    private static byte[] toBytecode(ClassFile classFile) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);){
            classFile.write(dataOutputStream);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static void writeFile(ClassFile classFile, String string) {
        try {
            FactoryHelper.writeFile0(classFile, string);
        } catch (IOException iOException) {
            throw new CannotCompileException(iOException);
        }
    }

    private static void writeFile0(ClassFile classFile, String string) {
        Object object;
        String string2 = classFile.getName();
        String string3 = string + File.separatorChar + string2.replace('.', File.separatorChar) + ".class";
        int n = string3.lastIndexOf(File.separatorChar);
        if (n > 0 && !((String)(object = string3.substring(0, n))).equals(".")) {
            new File((String)object).mkdirs();
        }
        object = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(string3)));
        try {
            classFile.write((DataOutputStream)object);
        } catch (IOException iOException) {
            throw iOException;
        } finally {
            ((FilterOutputStream)object).close();
        }
    }
}

