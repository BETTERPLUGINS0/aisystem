/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPath;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;

final class JarClassPath
implements ClassPath {
    Set<String> jarfileEntries;
    String jarfileURL;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    JarClassPath(String string) {
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(string);
            this.jarfileEntries = new HashSet<String>();
            for (JarEntry jarEntry : Collections.list(jarFile.entries())) {
                if (!jarEntry.getName().endsWith(".class")) continue;
                this.jarfileEntries.add(jarEntry.getName());
            }
            this.jarfileURL = new File(string).getCanonicalFile().toURI().toURL().toString();
            return;
        } catch (IOException iOException) {
        } finally {
            if (null != jarFile) {
                try {
                    jarFile.close();
                } catch (IOException iOException) {}
            }
        }
        throw new NotFoundException(string);
    }

    @Override
    public InputStream openClassfile(String string) {
        URL uRL = this.find(string);
        if (null != uRL) {
            try {
                if (ClassPool.cacheOpenedJarFile) {
                    return uRL.openConnection().getInputStream();
                }
                URLConnection uRLConnection = uRL.openConnection();
                uRLConnection.setUseCaches(false);
                return uRLConnection.getInputStream();
            } catch (IOException iOException) {
                throw new NotFoundException("broken jar file?: " + string);
            }
        }
        return null;
    }

    @Override
    public URL find(String string) {
        String string2 = string.replace('.', '/') + ".class";
        if (this.jarfileEntries.contains(string2)) {
            try {
                return new URL(String.format("jar:%s!/%s", this.jarfileURL, string2));
            } catch (MalformedURLException malformedURLException) {
                // empty catch block
            }
        }
        return null;
    }

    public String toString() {
        return this.jarfileURL == null ? "<null>" : this.jarfileURL;
    }
}

