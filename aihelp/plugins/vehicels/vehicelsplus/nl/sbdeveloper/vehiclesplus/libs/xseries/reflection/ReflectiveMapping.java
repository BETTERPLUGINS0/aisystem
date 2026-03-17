/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.reflection;

import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.NamedReflectiveHandle;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface ReflectiveMapping {
    public boolean shouldBeChecked();

    public String category();

    public String name();

    public String process(NamedReflectiveHandle var1, String var2);
}

