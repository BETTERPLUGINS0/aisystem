/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPath;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPoolTail;

public class URLClassPath
implements ClassPath {
    protected String hostname;
    protected int port;
    protected String directory;
    protected String packageName;

    public URLClassPath(String string, int n, String string2, String string3) {
        this.hostname = string;
        this.port = n;
        this.directory = string2;
        this.packageName = string3;
    }

    public String toString() {
        return this.hostname + ":" + this.port + this.directory;
    }

    @Override
    public InputStream openClassfile(String string) {
        try {
            URLConnection uRLConnection = this.openClassfile0(string);
            if (uRLConnection != null) {
                return uRLConnection.getInputStream();
            }
        } catch (IOException iOException) {
            // empty catch block
        }
        return null;
    }

    private URLConnection openClassfile0(String string) {
        if (this.packageName == null || string.startsWith(this.packageName)) {
            String string2 = this.directory + string.replace('.', '/') + ".class";
            return URLClassPath.fetchClass0(this.hostname, this.port, string2);
        }
        return null;
    }

    @Override
    public URL find(String string) {
        try {
            URLConnection uRLConnection = this.openClassfile0(string);
            InputStream inputStream = uRLConnection.getInputStream();
            if (inputStream != null) {
                inputStream.close();
                return uRLConnection.getURL();
            }
        } catch (IOException iOException) {
            // empty catch block
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static byte[] fetchClass(String string, int n, String string2, String string3) {
        byte[] byArray;
        URLConnection uRLConnection = URLClassPath.fetchClass0(string, n, string2 + string3.replace('.', '/') + ".class");
        int n2 = uRLConnection.getContentLength();
        try (InputStream inputStream = uRLConnection.getInputStream();){
            if (n2 <= 0) {
                byArray = ClassPoolTail.readStream(inputStream);
            } else {
                int n3;
                byArray = new byte[n2];
                int n4 = 0;
                do {
                    if ((n3 = inputStream.read(byArray, n4, n2 - n4)) >= 0) continue;
                    throw new IOException("the stream was closed: " + string3);
                } while ((n4 += n3) < n2);
            }
        }
        return byArray;
    }

    private static URLConnection fetchClass0(String string, int n, String string2) {
        URL uRL;
        try {
            uRL = new URL("http", string, n, string2);
        } catch (MalformedURLException malformedURLException) {
            throw new IOException("invalid URL?");
        }
        URLConnection uRLConnection = uRL.openConnection();
        uRLConnection.connect();
        return uRLConnection;
    }
}

