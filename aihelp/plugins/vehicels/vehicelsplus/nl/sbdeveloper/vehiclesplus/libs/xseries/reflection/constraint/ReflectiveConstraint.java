/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.constraint;

import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.ReflectiveHandle;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface ReflectiveConstraint {
    public String category();

    public String name();

    public boolean appliesTo(ReflectiveHandle<?> var1);
}

