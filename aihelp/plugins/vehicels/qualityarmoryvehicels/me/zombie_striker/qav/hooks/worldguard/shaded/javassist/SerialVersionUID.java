/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtConstructor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtField;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;

public class SerialVersionUID {
    public static void setSerialVersionUID(CtClass ctClass) {
        try {
            ctClass.getDeclaredField("serialVersionUID");
            return;
        } catch (NotFoundException notFoundException) {
            if (!SerialVersionUID.isSerializable(ctClass)) {
                return;
            }
            CtField ctField = new CtField(CtClass.longType, "serialVersionUID", ctClass);
            ctField.setModifiers(26);
            ctClass.addField(ctField, SerialVersionUID.calculateDefault(ctClass) + "L");
            return;
        }
    }

    private static boolean isSerializable(CtClass ctClass) {
        ClassPool classPool = ctClass.getClassPool();
        return ctClass.subtypeOf(classPool.get("java.io.Serializable"));
    }

    public static long calculateDefault(CtClass ctClass) {
        try {
            int n;
            int n2;
            int n3;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            ClassFile classFile = ctClass.getClassFile();
            String string = SerialVersionUID.javaName(ctClass);
            dataOutputStream.writeUTF(string);
            CtMethod[] ctMethodArray = ctClass.getDeclaredMethods();
            int n4 = ctClass.getModifiers();
            if ((n4 & 0x200) != 0) {
                n4 = ctMethodArray.length > 0 ? (n4 |= 0x400) : (n4 &= 0xFFFFFBFF);
            }
            dataOutputStream.writeInt(n4);
            Object[] objectArray = classFile.getInterfaces();
            for (n3 = 0; n3 < objectArray.length; ++n3) {
                objectArray[n3] = SerialVersionUID.javaName((String)objectArray[n3]);
            }
            Arrays.sort(objectArray);
            for (n3 = 0; n3 < objectArray.length; ++n3) {
                dataOutputStream.writeUTF((String)objectArray[n3]);
            }
            CtField[] ctFieldArray = ctClass.getDeclaredFields();
            Arrays.sort(ctFieldArray, new Comparator<CtField>(){

                @Override
                public int compare(CtField ctField, CtField ctField2) {
                    return ctField.getName().compareTo(ctField2.getName());
                }
            });
            for (int i = 0; i < ctFieldArray.length; ++i) {
                CtField ctField = ctFieldArray[i];
                int n5 = ctField.getModifiers();
                if ((n5 & 2) != 0 && (n5 & 0x88) != 0) continue;
                dataOutputStream.writeUTF(ctField.getName());
                dataOutputStream.writeInt(n5);
                dataOutputStream.writeUTF(ctField.getFieldInfo2().getDescriptor());
            }
            if (classFile.getStaticInitializer() != null) {
                dataOutputStream.writeUTF("<clinit>");
                dataOutputStream.writeInt(8);
                dataOutputStream.writeUTF("()V");
            }
            CtConstructor[] ctConstructorArray = ctClass.getDeclaredConstructors();
            Arrays.sort(ctConstructorArray, new Comparator<CtConstructor>(){

                @Override
                public int compare(CtConstructor ctConstructor, CtConstructor ctConstructor2) {
                    return ctConstructor.getMethodInfo2().getDescriptor().compareTo(ctConstructor2.getMethodInfo2().getDescriptor());
                }
            });
            for (n2 = 0; n2 < ctConstructorArray.length; ++n2) {
                CtConstructor ctConstructor = ctConstructorArray[n2];
                n = ctConstructor.getModifiers();
                if ((n & 2) != 0) continue;
                dataOutputStream.writeUTF("<init>");
                dataOutputStream.writeInt(n);
                dataOutputStream.writeUTF(ctConstructor.getMethodInfo2().getDescriptor().replace('/', '.'));
            }
            Arrays.sort(ctMethodArray, new Comparator<CtMethod>(){

                @Override
                public int compare(CtMethod ctMethod, CtMethod ctMethod2) {
                    int n = ctMethod.getName().compareTo(ctMethod2.getName());
                    if (n == 0) {
                        n = ctMethod.getMethodInfo2().getDescriptor().compareTo(ctMethod2.getMethodInfo2().getDescriptor());
                    }
                    return n;
                }
            });
            for (n2 = 0; n2 < ctMethodArray.length; ++n2) {
                CtMethod ctMethod = ctMethodArray[n2];
                n = ctMethod.getModifiers() & 0xD3F;
                if ((n & 2) != 0) continue;
                dataOutputStream.writeUTF(ctMethod.getName());
                dataOutputStream.writeInt(n);
                dataOutputStream.writeUTF(ctMethod.getMethodInfo2().getDescriptor().replace('/', '.'));
            }
            dataOutputStream.flush();
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            byte[] byArray = messageDigest.digest(byteArrayOutputStream.toByteArray());
            long l = 0L;
            for (int i = Math.min(byArray.length, 8) - 1; i >= 0; --i) {
                l = l << 8 | (long)(byArray[i] & 0xFF);
            }
            return l;
        } catch (IOException iOException) {
            throw new CannotCompileException(iOException);
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new CannotCompileException(noSuchAlgorithmException);
        }
    }

    private static String javaName(CtClass ctClass) {
        return Descriptor.toJavaName(Descriptor.toJvmName(ctClass));
    }

    private static String javaName(String string) {
        return Descriptor.toJavaName(Descriptor.toJvmName(string));
    }
}

