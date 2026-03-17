/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.web;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;

public class Viewer
extends ClassLoader {
    private String server;
    private int port;

    public static void main(String[] stringArray) {
        if (stringArray.length >= 3) {
            Viewer viewer = new Viewer(stringArray[0], Integer.parseInt(stringArray[1]));
            String[] stringArray2 = new String[stringArray.length - 3];
            System.arraycopy(stringArray, 3, stringArray2, 0, stringArray.length - 3);
            viewer.run(stringArray[2], stringArray2);
        } else {
            System.err.println("Usage: java javassist.tools.web.Viewer <host> <port> class [args ...]");
        }
    }

    public Viewer(String string, int n) {
        this.server = string;
        this.port = n;
    }

    public String getServer() {
        return this.server;
    }

    public int getPort() {
        return this.port;
    }

    public void run(String string, String[] stringArray) {
        Class<?> clazz = this.loadClass(string);
        try {
            clazz.getDeclaredMethod("main", String[].class).invoke(null, new Object[]{stringArray});
        } catch (InvocationTargetException invocationTargetException) {
            throw invocationTargetException.getTargetException();
        }
    }

    @Override
    protected synchronized Class<?> loadClass(String string, boolean bl) {
        Class<?> clazz = this.findLoadedClass(string);
        if (clazz == null) {
            clazz = this.findClass(string);
        }
        if (clazz == null) {
            throw new ClassNotFoundException(string);
        }
        if (bl) {
            this.resolveClass(clazz);
        }
        return clazz;
    }

    @Override
    protected Class<?> findClass(String string) {
        Class<?> clazz = null;
        if (string.startsWith("java.") || string.startsWith("javax.") || string.equals("me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.web.Viewer")) {
            clazz = this.findSystemClass(string);
        }
        if (clazz == null) {
            try {
                byte[] byArray = this.fetchClass(string);
                if (byArray != null) {
                    clazz = this.defineClass(string, byArray, 0, byArray.length);
                }
            } catch (Exception exception) {
                // empty catch block
            }
        }
        return clazz;
    }

    protected byte[] fetchClass(String string) {
        byte[] byArray;
        URL uRL = new URL("http", this.server, this.port, "/" + string.replace('.', '/') + ".class");
        URLConnection uRLConnection = uRL.openConnection();
        uRLConnection.connect();
        int n = uRLConnection.getContentLength();
        InputStream inputStream = uRLConnection.getInputStream();
        if (n <= 0) {
            byArray = this.readStream(inputStream);
        } else {
            int n2;
            byArray = new byte[n];
            int n3 = 0;
            do {
                if ((n2 = inputStream.read(byArray, n3, n - n3)) >= 0) continue;
                inputStream.close();
                throw new IOException("the stream was closed: " + string);
            } while ((n3 += n2) < n);
        }
        inputStream.close();
        return byArray;
    }

    private byte[] readStream(InputStream inputStream) {
        byte[] byArray;
        byte[] byArray2 = new byte[4096];
        int n = 0;
        int n2 = 0;
        do {
            if (byArray2.length - (n += n2) > 0) continue;
            byArray = new byte[byArray2.length * 2];
            System.arraycopy(byArray2, 0, byArray, 0, n);
            byArray2 = byArray;
        } while ((n2 = inputStream.read(byArray2, n, byArray2.length - n)) >= 0);
        byArray = new byte[n];
        System.arraycopy(byArray2, 0, byArray, 0, n);
        return byArray;
    }
}

