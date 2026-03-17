/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Experimental
 */
package me.zombie_striker.qav.util.xseries.reflection;

import me.zombie_striker.qav.util.xseries.reflection.jvm.NamedReflectiveHandle;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface ReflectiveMapping {
    public boolean shouldBeChecked();

    public String category();

    public String name();

    public String process(NamedReflectiveHandle var1, String var2);
}

