/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.Opcodes
 */
package me.zombie_striker.qav.util.xseries.reflection.asm;

import java.lang.reflect.Field;
import org.objectweb.asm.Opcodes;

final class ASMVersion {
    protected static final int LATEST_ASM_OPCODE_VERSION;
    protected static final int USED_ASM_OPCODE_VERSION;
    protected static final int CURRENT_JAVA_VERSION;
    protected static final int CURRENT_JAVA_FILE_FORMAT;
    protected static final int USED_JAVA_FILE_FORMAT;
    protected static final int LATEST_SUPPORTED_JAVA_CLASS_FILE_FORMAT_VERSION;

    private ASMVersion() {
    }

    protected static int getASMOpcodeVersion(int n) {
        return n << 16 | 0;
    }

    protected static int getJavaVersion() {
        String string = System.getProperty("java.version");
        if (string.startsWith("1.")) {
            string = string.substring(2, 3);
        } else {
            int n = string.indexOf(46);
            if (n != -1) {
                string = string.substring(0, n);
            }
        }
        return Integer.parseInt(string);
    }

    protected static int javaVersionToClassFileFormat(int n) {
        return 0 | 44 + n;
    }

    static {
        CURRENT_JAVA_VERSION = ASMVersion.getJavaVersion();
        CURRENT_JAVA_FILE_FORMAT = ASMVersion.javaVersionToClassFileFormat(CURRENT_JAVA_VERSION);
        int n = 0;
        int n2 = 0;
        try {
            for (Field object : Opcodes.class.getDeclaredFields()) {
                String string = object.getName();
                if (string.contains("EXPERIMENTAL")) continue;
                if (string.startsWith("ASM")) {
                    n = Math.max(n, object.getInt(null));
                }
                if (!string.startsWith("V") || string.length() > 4) continue;
                n2 = Math.max(n2, object.getInt(null));
            }
        } catch (IllegalAccessException illegalAccessException) {
            throw new IllegalStateException(illegalAccessException);
        }
        if (n == 0) {
            n = 393216;
        }
        if (n2 == 0) {
            n2 = 52;
        }
        LATEST_ASM_OPCODE_VERSION = n;
        LATEST_SUPPORTED_JAVA_CLASS_FILE_FORMAT_VERSION = n2;
        int n3 = n;
        int n4 = Math.min(CURRENT_JAVA_FILE_FORMAT, LATEST_SUPPORTED_JAVA_CLASS_FILE_FORMAT_VERSION);
        try {
            String string = System.getProperty("xseries.xreflection.asm.version");
            String string2 = System.getProperty("xseries.xreflection.asm.javaVersion");
            if (string != null) {
                n3 = Integer.parseInt(string);
                System.out.println("[XSeries/XReflection] Using custom ASM version: " + n3);
            }
            if (string2 != null) {
                n4 = Integer.parseInt(string2);
                System.out.println("[XSeries/XReflection] Using custom ASM Java target version: " + n4);
            }
        } catch (SecurityException securityException) {
            // empty catch block
        }
        USED_ASM_OPCODE_VERSION = n3;
        USED_JAVA_FILE_FORMAT = n4;
    }
}

