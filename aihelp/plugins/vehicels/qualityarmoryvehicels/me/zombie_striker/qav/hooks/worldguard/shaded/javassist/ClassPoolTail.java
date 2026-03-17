/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassClassPath;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPath;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPathList;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.DirClassPath;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.JarClassPath;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.JarDirClassPath;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.LoaderClassPath;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;

final class ClassPoolTail {
    protected ClassPathList pathList = null;

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[class path: ");
        ClassPathList classPathList = this.pathList;
        while (classPathList != null) {
            stringBuilder.append(classPathList.path.toString());
            stringBuilder.append(File.pathSeparatorChar);
            classPathList = classPathList.next;
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    public synchronized ClassPath insertClassPath(ClassPath classPath) {
        this.pathList = new ClassPathList(classPath, this.pathList);
        return classPath;
    }

    public synchronized ClassPath appendClassPath(ClassPath classPath) {
        ClassPathList classPathList = new ClassPathList(classPath, null);
        ClassPathList classPathList2 = this.pathList;
        if (classPathList2 == null) {
            this.pathList = classPathList;
        } else {
            while (classPathList2.next != null) {
                classPathList2 = classPathList2.next;
            }
            classPathList2.next = classPathList;
        }
        return classPath;
    }

    public synchronized void removeClassPath(ClassPath classPath) {
        ClassPathList classPathList = this.pathList;
        if (classPathList != null) {
            if (classPathList.path == classPath) {
                this.pathList = classPathList.next;
            } else {
                while (classPathList.next != null) {
                    if (classPathList.next.path == classPath) {
                        classPathList.next = classPathList.next.next;
                        continue;
                    }
                    classPathList = classPathList.next;
                }
            }
        }
    }

    public ClassPath appendSystemPath() {
        if (ClassFile.MAJOR_VERSION < 53) {
            return this.appendClassPath(new ClassClassPath());
        }
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return this.appendClassPath(new LoaderClassPath(classLoader));
    }

    public ClassPath insertClassPath(String string) {
        return this.insertClassPath(ClassPoolTail.makePathObject(string));
    }

    public ClassPath appendClassPath(String string) {
        return this.appendClassPath(ClassPoolTail.makePathObject(string));
    }

    private static ClassPath makePathObject(String string) {
        String string2 = string.toLowerCase();
        if (string2.endsWith(".jar") || string2.endsWith(".zip")) {
            return new JarClassPath(string);
        }
        int n = string.length();
        if (n > 2 && string.charAt(n - 1) == '*' && (string.charAt(n - 2) == '/' || string.charAt(n - 2) == File.separatorChar)) {
            String string3 = string.substring(0, n - 2);
            return new JarDirClassPath(string3);
        }
        return new DirClassPath(string);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void writeClassfile(String string, OutputStream outputStream) {
        InputStream inputStream = this.openClassfile(string);
        if (inputStream == null) {
            throw new NotFoundException(string);
        }
        try {
            ClassPoolTail.copyStream(inputStream, outputStream);
        } finally {
            inputStream.close();
        }
    }

    InputStream openClassfile(String string) {
        ClassPathList classPathList = this.pathList;
        InputStream inputStream = null;
        NotFoundException notFoundException = null;
        while (classPathList != null) {
            block5: {
                try {
                    inputStream = classPathList.path.openClassfile(string);
                } catch (NotFoundException notFoundException2) {
                    if (notFoundException != null) break block5;
                    notFoundException = notFoundException2;
                }
            }
            if (inputStream == null) {
                classPathList = classPathList.next;
                continue;
            }
            return inputStream;
        }
        if (notFoundException != null) {
            throw notFoundException;
        }
        return null;
    }

    public URL find(String string) {
        ClassPathList classPathList = this.pathList;
        URL uRL = null;
        while (classPathList != null) {
            uRL = classPathList.path.find(string);
            if (uRL == null) {
                classPathList = classPathList.next;
                continue;
            }
            return uRL;
        }
        return null;
    }

    public static byte[] readStream(InputStream inputStream) {
        byte[][] byArrayArray = new byte[8][];
        int n = 4096;
        for (int i = 0; i < 8; ++i) {
            byArrayArray[i] = new byte[n];
            int n2 = 0;
            int n3 = 0;
            do {
                if ((n3 = inputStream.read(byArrayArray[i], n2, n - n2)) >= 0) continue;
                byte[] byArray = new byte[n - 4096 + n2];
                int n4 = 0;
                for (int j = 0; j < i; ++j) {
                    System.arraycopy(byArrayArray[j], 0, byArray, n4, n4 + 4096);
                    n4 = n4 + n4 + 4096;
                }
                System.arraycopy(byArrayArray[i], 0, byArray, n4, n2);
                return byArray;
            } while ((n2 += n3) < n);
            n *= 2;
        }
        throw new IOException("too much data");
    }

    public static void copyStream(InputStream inputStream, OutputStream outputStream) {
        int n = 4096;
        byte[] byArray = null;
        for (int i = 0; i < 64; ++i) {
            if (i < 8) {
                byArray = new byte[n *= 2];
            }
            int n2 = 0;
            int n3 = 0;
            do {
                if ((n3 = inputStream.read(byArray, n2, n - n2)) >= 0) continue;
                outputStream.write(byArray, 0, n2);
                return;
            } while ((n2 += n3) < n);
            outputStream.write(byArray);
        }
        throw new IOException("too much data");
    }
}

