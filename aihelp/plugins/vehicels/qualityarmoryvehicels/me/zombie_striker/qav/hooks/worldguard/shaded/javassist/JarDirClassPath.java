/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URL;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPath;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.JarClassPath;

final class JarDirClassPath
implements ClassPath {
    JarClassPath[] jars;

    JarDirClassPath(String string) {
        File[] fileArray = new File(string).listFiles(new FilenameFilter(){

            @Override
            public boolean accept(File file, String string) {
                return (string = string.toLowerCase()).endsWith(".jar") || string.endsWith(".zip");
            }
        });
        if (fileArray != null) {
            this.jars = new JarClassPath[fileArray.length];
            for (int i = 0; i < fileArray.length; ++i) {
                this.jars[i] = new JarClassPath(fileArray[i].getPath());
            }
        }
    }

    @Override
    public InputStream openClassfile(String string) {
        if (this.jars != null) {
            for (int i = 0; i < this.jars.length; ++i) {
                InputStream inputStream = this.jars[i].openClassfile(string);
                if (inputStream == null) continue;
                return inputStream;
            }
        }
        return null;
    }

    @Override
    public URL find(String string) {
        if (this.jars != null) {
            for (int i = 0; i < this.jars.length; ++i) {
                URL uRL = this.jars[i].find(string);
                if (uRL == null) continue;
                return uRL;
            }
        }
        return null;
    }
}

