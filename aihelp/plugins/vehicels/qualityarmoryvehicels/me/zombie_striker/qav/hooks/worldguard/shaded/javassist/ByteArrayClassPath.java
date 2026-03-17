/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPath;

public class ByteArrayClassPath
implements ClassPath {
    protected String classname;
    protected byte[] classfile;

    public ByteArrayClassPath(String string, byte[] byArray) {
        this.classname = string;
        this.classfile = byArray;
    }

    public String toString() {
        return "byte[]:" + this.classname;
    }

    @Override
    public InputStream openClassfile(String string) {
        if (this.classname.equals(string)) {
            return new ByteArrayInputStream(this.classfile);
        }
        return null;
    }

    @Override
    public URL find(String string) {
        if (this.classname.equals(string)) {
            String string2 = string.replace('.', '/') + ".class";
            try {
                return new URL(null, "file:/ByteArrayClassPath/" + string2, new BytecodeURLStreamHandler());
            } catch (MalformedURLException malformedURLException) {
                // empty catch block
            }
        }
        return null;
    }

    private class BytecodeURLStreamHandler
    extends URLStreamHandler {
        private BytecodeURLStreamHandler() {
        }

        @Override
        protected URLConnection openConnection(URL uRL) {
            return new BytecodeURLConnection(uRL);
        }
    }

    private class BytecodeURLConnection
    extends URLConnection {
        protected BytecodeURLConnection(URL uRL) {
            super(uRL);
        }

        @Override
        public void connect() {
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(ByteArrayClassPath.this.classfile);
        }

        @Override
        public int getContentLength() {
            return ByteArrayClassPath.this.classfile.length;
        }
    }
}

