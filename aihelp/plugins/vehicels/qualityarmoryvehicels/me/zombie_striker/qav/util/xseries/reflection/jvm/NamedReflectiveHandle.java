/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.util.xseries.reflection.jvm;

import java.util.Set;
import org.jetbrains.annotations.NotNull;

public interface NamedReflectiveHandle {
    @NotNull
    public Set<String> getPossibleNames();
}

