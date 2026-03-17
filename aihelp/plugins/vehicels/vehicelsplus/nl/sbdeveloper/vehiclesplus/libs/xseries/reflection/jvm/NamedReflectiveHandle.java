/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm;

import java.util.Set;
import org.jetbrains.annotations.NotNull;

public interface NamedReflectiveHandle {
    @NotNull
    public Set<String> getPossibleNames();
}

