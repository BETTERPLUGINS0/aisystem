/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPath;

final class ClassPathList {
    ClassPathList next;
    ClassPath path;

    ClassPathList(ClassPath classPath, ClassPathList classPathList) {
        this.next = classPathList;
        this.path = classPath;
    }
}

